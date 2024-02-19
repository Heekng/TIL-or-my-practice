package com.heekng.springwithkafka.dto;

public record FirstDto(Long id, String name, Integer age, FirstInnerDto firstInnerDto) {

    public record FirstInnerDto(String innerName, Long length) {}

}

