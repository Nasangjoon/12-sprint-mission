package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.BaseException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends BaseException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
