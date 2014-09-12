package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BlinkyTape {
  static Logger logger = LoggerFactory.getLogger(BlinkyTape.class);
  static Logger loggerSerial = LoggerFactory.getLogger(BlinkyTape.class.getName() + ".serial");

  private SerialPort serialPort;
  private String portName;

  private final static int length = 60;

  Color[] leds = new Color[length];

  public BlinkyTape(String portName) throws SerialPortException {
    logger.info("opening up tape on {}", portName);
    this.portName = portName;
    serialPort = new SerialPort(portName);
    serialPort.openPort();
    serialPort.setParams(115200, 8, 1, 0);
    send255();
  }

  public void setColor(int i, Color c) {
    leds[i] = c;
  }
  
  public void clear() {
    for (int i = 0; i < length; i++) leds[i] = Color.black;
  }

  public void updateTape() throws SerialPortException {
    long fiddleTime = 0;
    byte[] bytes = new byte[length*3];
    int bindex = 0;
    for (int i = 0; i < length; i++) {
      long t0  = System.currentTimeMillis();
      Color c = leds[i];
      if (c == null)
        c = Color.BLACK;
      int r, g, b;
      r = c.getRed();
      g = c.getGreen();
      b = c.getBlue();
      // System.out.printf("position %d = %d, %d, %d\n", i, r, g, b);

      // Send the color for the current LED to the strip,
      // being careful not to send 255 (because that would
      // cause the strip to display the pixels
      
      bytes[bindex++] = bc(r);
      bytes[bindex++] = bc(g);
      bytes[bindex++] = bc(b);

      long t1  = System.currentTimeMillis();
      fiddleTime += (t1 - t0);
    }
    long t0 = System.currentTimeMillis();
    
    int offset = 0;
    while (offset < bytes.length) {
      int length = Math.min(64, bytes.length - offset);
      byte[] batch = new byte[length];
      System.arraycopy(bytes, offset, batch, 0, length);
      offset += length;
      serialPort.writeBytes(batch);
      loggerSerial.debug("sent {} bytes", batch.length);
      delay();
    }
    send255();
    long t1 = System.currentTimeMillis();
    long sendTime = t1 - t0;
    logger.debug ("update breakdown: fiddling={}, sending={}", fiddleTime, sendTime);
  }

  public void close() throws SerialPortException {
    serialPort.closePort();
  }

  public int getLength() {
    return length;
  }

  void send255() throws SerialPortException {
    serialPort.writeByte((byte) 255);
    loggerSerial.debug("sent 1 byte (the 255)");
    delay();
  }

  static byte bc(int b) {
    int rv = b;
    if (rv > 254)
      rv = 254;
    return (byte) rv;
  }
  
  static void delay() {
    try {
      Thread.sleep(5);
    } catch (InterruptedException ex) {
    }
  }
  

  public void reset() throws SerialPortException {
    logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!! resetting the port");
    serialPort.closePort();
    SerialPort resetSerialPort = new SerialPort(portName);
    resetSerialPort.openPort();
    resetSerialPort.setParams(1200, 8, 0, 0);
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) { }
    resetSerialPort.closePort();
    serialPort.openPort();
  }


}
