package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.BaseException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends BaseException {
    public ChannelException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChannelException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 