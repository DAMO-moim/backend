package com.springboot.auth.dto;

import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class LoginDto {
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    private String username;

    @Schema(description = "사용자 비밀번호", example = "zizonhuzzang123!@")
    private String password;
}
