package com.spring_ai.learn_spring_ai.dto;

public record Joke (
        String text,
        String category,
        Double laughRate,
        Boolean isNSFW
){
}
