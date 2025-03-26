package com.springboot.schedule.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantInfoDto {
    private Long memberId;
    private String name;
    private String image;
}
