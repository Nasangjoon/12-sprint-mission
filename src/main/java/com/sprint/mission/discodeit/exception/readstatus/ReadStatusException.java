package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.BaseException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ReadStatusException extends BaseException {
    public ReadStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReadStatusException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 