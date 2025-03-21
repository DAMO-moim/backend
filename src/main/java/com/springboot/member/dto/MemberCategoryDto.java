package com.springboot.member.dto;

import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;

public class MemberCategoryDto {
    @Getter
    public static class Post {
        @Size(min = 1, max = 3)
        private List<String> categoryNames;
    }
}
