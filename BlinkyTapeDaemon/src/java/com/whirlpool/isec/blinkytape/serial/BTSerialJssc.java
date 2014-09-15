package com.whirlpool.isec.blinkytape.serial;

import org.slf4j.*;

import jssc.*;

public class BTSerialJssc extends BTSerial {
  static Logger logger = LoggerFactory.getLogger(BTSerialJssc.class);

  SerialPort serialPort;

  String portName;

  public BTSerialJssc() {
    super();
    String[] portNames = SerialPortList.getPortNames();
    for (String name : portNames) {
      logger.info("considering {}", name);
      if (name.matches("^COM1\\d$")) {
        portName = name;
      }
    }
    if (portName == null)
      portName = "/dev/blinky";

    logger.info("using {}", portName);
    serialPort = new SerialPort(portName);
    try {
      serialPort.openPort();
      serialPort.setParams(115200, 8, 1, 0);
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }

  @Override
  public void writeBytes(byte[] bytes) {
    logger.debug("writing {} bytes", bytes.length);
    try {
      boolean ok = serialPort.writeBytes(bytes);
      if (!ok)
        throw new RuntimeException("writeBytes() failed");
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }

  @Override
  public void writeByte(byte b) {
    logger.debug("writing byte {}", (int) b & 0xFF);
    try {
      boolean ok = serialPort.writeByte(b);
      if (!ok)
        throw new RuntimeException("writeBytes() failed");
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }

  public byte[] readAvailableBytes() throws BTSerialException {
    try {
      byte[] rv = new byte[0];
      int i = serialPort.getInputBufferBytesCount();
      if (i > 0) rv = serialPort.readBytes(i);
      return rv;
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }

  @Override
  public void close() {
    try {
      serialPort.closePort();
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }

  @Override
  public void reset() {
    logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!! resetting the port");
    try {
      serialPort.closePort();
      SerialPort resetSerialPort = new SerialPort(portName);
      resetSerialPort.openPort();
      resetSerialPort.setParams(1200, 8, 0, 0);
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
      resetSerialPort.closePort();
      serialPort.openPort();
    } catch (SerialPortException e) {
      throw new BTSerialException(e);
    }
  }
  
  @Override
  public String getPortName() {
    return serialPort.getPortName();
  }

}
