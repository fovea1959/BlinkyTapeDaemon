package com.whirlpool.isec.blinkytape.serial;

abstract public class BTSerial {
  abstract public void writeBytes(byte[] bytes);
  abstract public void writeByte(byte b);
  abstract public void reset();
  abstract public void close();
}
