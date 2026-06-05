package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.BaseException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentException extends BaseException {
    public BinaryContentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BinaryContentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 