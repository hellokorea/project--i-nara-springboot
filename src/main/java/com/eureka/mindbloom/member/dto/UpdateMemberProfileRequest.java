package com.eureka.mindbloom.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateMemberProfileRequest(
        String phone,
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
                message = "비밀번호는 8~20자리이며, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.") String password,
        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.") String name
){
}
