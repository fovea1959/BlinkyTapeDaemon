package com.whirlpool.isec.blinkytape.serial;

abstract public class BTSerial {
  abstract public void writeBytes(byte[] bytes) throws BTSerialException;
  abstract public void writeByte(byte b) throws BTSerialException;
  abstract public byte[] readAvailableBytes() throws BTSerialException;
  abstract public void reset() throws BTSerialException;
  abstract public void close() throws BTSerialException;
  abstract public String getPortName();
}
