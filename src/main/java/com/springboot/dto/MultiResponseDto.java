package com.springboot.dto;

import lombok.Getter;

@Getter
public class MultiResponseDto {
    private List<T> data;
    private PageInfo pageInfo;
}
