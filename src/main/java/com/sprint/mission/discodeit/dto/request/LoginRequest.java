package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "사용자 이름은 2글자 이상, 20글자 미만 이여아 합니다.")
    String username,

    @NotBlank(message = "패스워드는 필수 입니다")
    @Size(min = 4, max = 20, message = "패스워드는 4글자 이상, 20글자 미만 이여아 합니다.")
    String password
) {

}
