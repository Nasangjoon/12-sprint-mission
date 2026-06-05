package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Map<String, Object> details;
  private final Instant timestamp;

  public BaseException(ErrorCode errorCode){
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = new LinkedHashMap<>();
  }

  public BaseException(ErrorCode errorCode, Throwable cause){
    super(errorCode.getMessage(), cause);
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = new LinkedHashMap<>();
  }

  public void addDetail(String key, Object value){
    this.getDetails().put(key, value);
  }

}
