package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BinaryContentCreateRequest(

    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    String contentType,
    @NotBlank(message = "파일 데이터는 필수입니다.")
    byte[] bytes
) {

}
