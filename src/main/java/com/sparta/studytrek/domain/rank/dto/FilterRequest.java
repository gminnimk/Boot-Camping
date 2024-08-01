package com.sparta.studytrek.domain.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    private String track;
    private String environment;
    private String cost;
}