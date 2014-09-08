package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BlinkyTape {
  static Logger logger = LoggerFactory.getLogger(BlinkyTape.class);
  private SerialPort serialPort;

  private final static int length = 60;

  Color[] leds = new Color[length];

  public BlinkyTape(String portName) throws SerialPortException {
    serialPort = new SerialPort(portName);
    serialPort.openPort();
    serialPort.setParams(115200, 8, 1, 0);
    triple255();
  }

  public void setColor(int i, Color c) {
    leds[i] = c;
  }

  public void updateTape() throws SerialPortException {
    long fiddleTime = 0;
    long sendTime = 0;
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
      long t1  = System.currentTimeMillis();

      serialPort.writeByte(bc(r));
      serialPort.writeByte(bc(g));
      serialPort.writeByte(bc(b));
      
      long t2 = System.currentTimeMillis();
      
      fiddleTime += (t1 - t0);
      sendTime += (t2 - t0);
    }
    long t0 = System.currentTimeMillis();
    triple255();
    long t1 = System.currentTimeMillis();
    long triple255Time = t1 - t0;
    logger.debug ("update breakdown: fiddling={}, sending={}, t255={}", fiddleTime, sendTime, triple255Time);
  }

  public void close() throws SerialPortException {
    serialPort.closePort();
  }

  public int getLength() {
    return length;
  }

  void triple255() throws SerialPortException {
    serialPort.writeByte((byte) 255);
    serialPort.writeByte((byte) 255);
    serialPort.writeByte((byte) 255);
  }

  static byte bc(int b) {
    int rv = b;
    if (rv > 254)
      rv = 254;
    return (byte) rv;
  }

}
