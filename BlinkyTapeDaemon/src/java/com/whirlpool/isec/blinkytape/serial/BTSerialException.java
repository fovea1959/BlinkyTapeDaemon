package com.whirlpool.isec.blinkytape.serial;

@SuppressWarnings("serial")
public class BTSerialException extends RuntimeException {

  public BTSerialException() {
    super();
  }

  public BTSerialException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public BTSerialException(String message, Throwable cause) {
    super(message, cause);
  }

  public BTSerialException(String message) {
    super(message);
  }

  public BTSerialException(Throwable cause) {
    super(cause);
  }

}
