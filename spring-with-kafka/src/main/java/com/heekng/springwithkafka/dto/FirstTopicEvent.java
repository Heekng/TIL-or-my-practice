package com.heekng.springwithkafka.dto;

public record FirstTopicEvent(Long id, String name, Integer age, FirstInnerEvent firstInnerEvent) {

    public record FirstInnerEvent(String innerName, Long length) {}

}

