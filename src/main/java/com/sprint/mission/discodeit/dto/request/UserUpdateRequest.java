package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(min = 3, max = 20, message = "사용자 이름은 3자 이상 20자 이하로 입력해야 합니다.")
    String newUsername,

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    String newEmail,

    @Size(min=8, max = 30, message = "패스워드는 8글자 이상, 30글자 미만 이여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$",
        message = "비밀번호는 숫자, 문자, 일부 특수문자만 포함해야 합니다."
    )
    String newPassword
) {

}
