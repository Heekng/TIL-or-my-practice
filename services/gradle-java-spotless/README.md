# Java Spotless lint

## build.gradle 에서 설정할 것

```Gradle
plugins {
    ...
    id("com.diffplug.spotless") version "6.25.0"
    ...
}

spotless {
    java {
        googleJavaFormat()

        removeUnusedImports()
        trimTrailingWhitespace()
        indentWithTabs(2)
        indentWithSpaces(4)
        endWithNewline()
    }
}
```

- spotless 사용을 위한 플러그인 추가
- spotless 린트 설정을 위한 설정 추가

## 린트 자동화를 위해 설정할 것

**.githooks/pre-commit**

```shell
#!/bin/bash

targetFiles=$(git diff --staged --name-only)

./gradlew spotlessApply
./gradlew spotlessCheck

for file in $targetFiles; do
  if test -f "$file"; then
    git add $file
  fi
done
```

- pre-commit 이란 git에서 제공하는 hook을 이용할 때, 커밋 이전에 자동으로 실행할 스크립트를 뜻한다.
1. 커밋 대상 파일을 미리 추출한다.
2. spotlessApply task 를 실행해 전역 파일 린트를 실행한다.
3. 린트로 인해 변경된 파일을 다시 stage 상태로 추가해 커밋 대상에 포함되게 한다.

## pre-commit 실행권한 부여
```shell
chmod +x ./.githooks/pre-commit
```

## git 의 hook 경로를 변경 
```shell
git config core.hookspath .githooks
```

## 추가) gitKraken을 사용하는 경우

gitKraken의 경우 자체 git을 사용하기 때문에 gitHook 이 정상적으로 실행되지 않는 문제가 있다.

```shell
env SHELL=/bin/zsh open -a "GitKraken"
```

위와 같이 SHELL 실행 환경을 지정해 GitKraken을 실행하면 정상적으로 작동한다.

