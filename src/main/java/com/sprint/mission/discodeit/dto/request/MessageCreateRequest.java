package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageCreateRequest(

    @NotBlank(message = "메시지 내용은 필수입니다.")
    String content,

    @NotBlank(message = "채널 ID는 필수입니다.")
    UUID channelId,

    @NotBlank(message = "작성자 ID는 필수입니다.")
    UUID authorId
) {

}
