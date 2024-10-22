package com.eureka.mindbloom.member.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
public class SignUpRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 8~20자리이며, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;

    @Builder
    public SignUpRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
