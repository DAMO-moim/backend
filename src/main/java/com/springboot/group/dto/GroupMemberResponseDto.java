package com.springboot.group.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberResponseDto {
    private Long memberId;
    private String name;
    private String image;
}

