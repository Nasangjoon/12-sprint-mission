package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "사용자 이름은 2글자 이상, 20글자 미만 이여아 합니다.")
    String username,

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Size(max = 200, message = "이메일은 100자 이하여야 합니다.")
    String email,

    @NotBlank(message = "패스워드는 필수입니다.")
    @Size(min=8, max = 30, message = "패스워드는 8글자 이상, 30글자 미만 이여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$",
        message = "비밀번호는 숫자, 문자, 일부 특수문자만 포함해야 합니다."
    )
    String password
) {

}
