package com.whirlpool.isec.blinkytape.serial;

import org.slf4j.*;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

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
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeBytes(byte[] bytes) {
    logger.info("writing {} bytes", bytes.length);
    try {
      serialPort.writeBytes(bytes);
    } catch (SerialPortException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeByte(byte b) {
    logger.info("writing byte {}", (int) b & 0xFF);
    try {
      serialPort.writeByte(b);
    } catch (SerialPortException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    try {
      serialPort.closePort();
    } catch (SerialPortException e) {
      throw new RuntimeException(e);
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
      throw new RuntimeException(e);
    }
  }

}
