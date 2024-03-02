# REDIS 실무 사용 다양한 방법

## Rate Limit

이벤트 참여 횟수 제한 등의 작업을 할 때는 SET 자료구조를 활용한다.

SET 자료구조는 `key`에 `member`을 입력하며 이미 member가 존재하면 0을 member가 존재하지 않으면 1을 응답한다. 

```redis
-- 이벤트 참여 예시
SADD event:E0001 member_id_1
-- TTL을 줘야하는 경우 (특정 시간 내에 재참여 불가능)
SADD event:E0001 member_id_1 EX 3600
```

SET 자료구조를 사용해 값에 TTL을 적용하려면 NX와 EXPIRE을 사용한다.
값이 존재하지 않으면 1을 이미 존재한다면 nil을 응답한다.

```redis
-- event:E0002 key에 member_id_1를 추가한다.
-- member_id_1 가 없을 때만 값을 추가하며 60분 동안 값이 유지된다. 
SET message-alert:member_id_1 1 NX EX 3600
```

- SADD와 SET 명령어의 차이점
- SADD
  - SET 자료구조에 member을 추가한다.
- SET
  - 특정 키에 string을 저장한다.

## 실시간 랭킹

점수, 상품 판매 수에 순서를 매겨야 한다면 `Sorted Set(ZSET)`을 사용한다.

```redis
ZINCRBY product-sales-count 1 PRODUCT_1
-- 1 응답
ZINCRBY product-sales-count 1 PRODUCT_1
-- 2 응답
ZINCRBY product-sales-count 1 PRODUCT_2
-- 1 응답
ZINCRBY product-sales-count 1 PRODUCT_2
-- 2 응답
ZINCRBY product-sales-count 1 PRODUCT_2
-- 3 응답
ZINCRBY product-sales-count 1 PRODUCT_2
-- 4 응답
ZINCRBY product-sales-count 1 PRODUCT_3
-- 1 응답
ZINCRBY product-sales-count 1 PRODUCT_3
-- 2 응답
ZINCRBY product-sales-count 1 PRODUCT_3
-- 3 응답
```

이렇게 추가된 값들은 `ZREVRANGE` 명령어로 등수를 받을 수 있다.

```redis
ZREVRANGE product-sales-count 0 -1
-- 점수까지 함께 출력
ZREVRANGE product-sales-count 0 -1 WITHSCORES
```

또는 특정 member에 대한 등수를 확인할 때는 `ZREVRANK`를 사용한다.

```redis
ZREVRANK product-sales-count PRODUCT_3
```

## 좋아요

게시물에 대해 사용자가 좋아요를 하거나 좋아요 취소를 하는 경우 SET을 사용해 구현할 수 있다.

```redis
-- MEMBER_1 이 1번 게시물에 좋아요 시도
SADD post:1:like MEMBER_1
-- 1 응답 (성공)

-- MEMBER_1 이 1번 게시물에 좋아요 시도
SADD post:1:like MEMBER_1
-- 0 응답 (실패)

-- MEMBER_2 이 1번 게시물에 좋아요 시도
SADD post:1:like MEMBER_2
-- 1 응답 (성공)

-- MEMBER_1 이 1번 게시물에 좋아요 취소 시도
SREM post:1:like MEMBER_1
-- 1 응답 (성공)
```

이렇게 쌓인 좋아요 데이터는 `SMEMBERS`로 확인할 수 있다.

```redis
SMEMBERS post:1:like
-- 1) "MEMBER_2"
```

만약 좋아요 한 시간순으로 정렬해야 할 경우 `Sorted set(ZSET)`을 사용한다.

```redis
ZADD post:2:like 20240302153700 MEMBER_1
-- (integer) 1
ZADD post:2:like 20240302153705 MEMBER_2
-- (integer) 1
ZADD post:2:like 20240302153720 MEMBER_3
-- (integer) 1
ZADD post:2:like 20240302153730 MEMBER_4
-- (integer) 1
```

이렇게 추가된 좋아요를 시간순으로 출력할 때는 `ZRANGE`를 사용한다.
```redis
ZRANGE post:2:like 0 -1
-- 1) "MEMBER_1"
-- 2) "MEMBER_2"
-- 3) "MEMBER_3"
-- 4) "MEMBER_4"
```

특정 게시물이 받은 좋아요 수를 계산하려면 `Set`의 경우 `SCARD`, `Sorted Set`의 경우 `ZCARD`를 사용한다.
SCARD, ZCARD 명령어는 집합의 수를 확인하는 명령어다.

```redis
SCARD post:1:like
-- (integer) 1
ZCARD post:2:like
-- (integer) 4
```

지금까지 게시물을 좋아요한 멤버을 관리했지만, 특정 멤버가 좋아요한 게시물을 관리하려면 어떻게 해야 할까?

1. 게시물에 멤버가 좋아요 추가
2. 멤버가 좋아요한 게시물 추가

이 작업은 하나의 트랜잭션에서 이루어져야 한다. (한번의 요청에 여러 커맨드)

```redis
MULTI
-- OK
SADD post:3:like MEMBER_1
-- QUEUED
ZADD user:MEMBER_1:post:like 20240302153730 3
-- QUEUED
EXEC
-- 1) (integer) 1
-- 2) (integer) 1
```

```redis
SMEMBERS post:3:like
-- 1) "MEMBER_1"
ZRANGE user:MEMBER_1:post:like 0 -1
-- 1) "3"
```

## 최근에 본 상품 조회

시간과 관련된 최근 본 상품 조회는 `Sorted Set(ZSET)`을 사용한다.

````redis
ZADD last-seen-product:MEMBER_1 20240302153730 product_1
-- (integer) 1
ZADD last-seen-product:MEMBER_1 20240302153750 product_2
-- (integer) 1
ZADD last-seen-product:MEMBER_1 20240302153800 product_4
-- (integer) 1
ZADD last-seen-product:MEMBER_1 20240302153830 product_3
-- (integer) 1
ZREVRANGE last-seen-product:MEMBER_1 0 -1
-- 1) "product_3"
-- 2) "product_4"
-- 3) "product_2"
-- 4) "product_1"
````

만약 최근 본 상품 3개만 필요하다면 아래와 같이 range를 적용한다.

```redis
ZREVRANGE last-seen-product:MEMBER_1 0 2
-- 1) "product_3"
-- 2) "product_4"
-- 3) "product_2"
```

최근 본 상품을 무한정 추가한다면 메모리 사용량이 무한정 늘어나게 된다.
또한 최근 본 상품을 조회할 때마다 모든 버퍼를 확인하게 될 것이다.
이럴 때에는 트랜잭션 안에서 최근 본 상품 등록, 최대 개수만큼 남기고 나머지를 삭제하는 작업을 한다.

`ZREMRANGEBYRANK` 명령어는 특정 키의 특정 범위만큼을 삭제한다.
아래 예시는 최근 본 상품 1개를 제외한 최근 본 상품을 삭제한다.

```redis
ZREVRANGE last-seen-product:MEMBER_1 0 -1
-- 1) "product_3"
-- 2) "product_4"
-- 3) "product_2"
-- 4) "product_1"
MULTI
-- OK
ZADD last-seen-product:MEMBER_1 20240302153850 product_5
-- QUEUED
ZREMRANGEBYRANK last-seen-product:MEMBER_1 0 -2
-- QUEUED
EXEC
-- 1) (integer) 1
-- 2) (integer) 4
ZREVRANGE last-seen-product:MEMBER_1 0 -1
-- 1) "product_5"
```

## Queue

message queue 로 redis를 활용할 수도 있다.
redis에서 Queue를 사용한다면 List를 사용할 수 있다.
값을 추가할 때에는 `LPUSH` 명령어를, 값을 가져올 때는 `BLPOP`을 사용한다.

redis queue 사용 시 명령어
- LPUSH
  - 리스트의 왼쪽에 요소 추가
- RPUSH
  - 리스트의 오른쪽에 요소 추가
- LPOP
  - 리스트의 왼쪽에서 요소를 POP
- RPOP
  - 리스트의 오른쪽에서 요소를 POP
- BLPOP
  - 왼쪽에서 요소를 POP, 리스트가 비어있으면 timeout 시간동안 대기
- BRPOP
  - 오른쪽에서 요소를 POP, 리스트가 비어있으면 timeout 시간동안 대기
- LLEN
  - 리스트의 길이를 반환
  - 큐의 현재 길이를 확인

```redis
LPUSH job:order "{action: create, id: 1}"
-- (integer) 1
LPUSH job:order "{action: wait, id: 1}"
-- (integer) 2
LPUSH job:order "{action: complete, id: 1}"
-- (integer) 3
BRPOP job:order 0
-- 1) "job:order"
-- 2) "{action: create, id: 1}"
BRPOP job:order 0
-- 1) "job:order"
-- 2) "{action: wait, id: 1}"
BRPOP job:order 0
-- 1) "job:order"
-- 2) "{action: complete, id: 1}"
BRPOP job:order 0
-- 대기
```

## 선착순 이벤트

선착순 이벤트의 쿠폰 최대 수량, 쿠폰 발급 처리 작업에 redis를 활용할 수 있다.

1. SET 타입의 자료구조에 이벤트번호를 포함한 key, 사용자 식별자 value 로 데이터를 쌓는다고 가정
2. 트랜잭션 시작
3. SCARD 명령어로 해당 이벤트 참여자의 수를 확인
4. 최대 수량 > 이벤트 참여자 수 일 경우 쿠폰 발급 가능
5. SET 타입의 자료구조에 이벤트번호를 포함한 key, 사용자 식별자 value로 값 추가
6. 만약 해당 사용자가 쿠폰을 발급받지 않았다면 (SADD 성공)
7. queue에 쿠폰 발급 정보 push
8. 트랜잭션 종료
9. Worker 모듈에서 queue BRPOP 작업으로 쿠폰 발급 처리

