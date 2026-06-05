package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.BaseException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageException extends BaseException {
    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 