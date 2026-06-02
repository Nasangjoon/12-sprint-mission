package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(

    @NotBlank(message = "채널 이름은 필수입니다.")
    @Size(min = 2, max = 100, message = "채널 이름은 2글자 이상, 100글자 미만 이여아 합니다.")
    String newName,
    String newDescription
) {

}
