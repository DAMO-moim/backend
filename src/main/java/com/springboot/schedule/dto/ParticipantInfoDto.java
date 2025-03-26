package com.springboot.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantInfoDto {
    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "회원 이름", example = "권택현")
    private String name;

    @Schema(description = "회원 프로필 이미지 URL", example = "/images/members/1/profile.png")
    private String image;
}
