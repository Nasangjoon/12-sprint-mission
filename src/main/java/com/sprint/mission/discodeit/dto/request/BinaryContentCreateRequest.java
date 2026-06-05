package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(

    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    String contentType,
    @NotNull(message = "파일 데이터는 필수입니다.")
    @Size(min = 1, message = "파일 데이터는 최소 1바이트 이상이어야 합니다.")
    byte[] bytes
) {

}
