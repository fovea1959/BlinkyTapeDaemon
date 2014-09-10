package com.whirlpool.isec.blinkytape.test;

import java.awt.Color;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BlinkytapeTest {

  public static void main(String[] args) {
    SerialPort serialPort = new SerialPort("COM10");
    try {
      System.out.println("Port opened: " + serialPort.openPort());
      System.out.println("Params setted: " + serialPort.setParams(115200, 8, 1, 0));
      serialPort.writeByte((byte) 255);

      float phase = 0;
      int sent = 0;

      while (phase < Float.MAX_VALUE) {
        for (int i = 0; i < 60; i++) {
          double budge = .3;
          double rr = (Math.sin(phase * .9 + i * budge) + 1.0) * 127.0;
          double gg = (Math.sin(phase + Math.PI / 2 + i * budge) + 1) * 127;
          double bb = (Math.sin(phase * 1.1 + Math.PI + i * budge) + 1) * 127;
          // System.out.printf("position %d = %.0f, %.0f, %.0f\n", i, rr, gg,
          // bb);
          Color c = new Color((int) rr, (int) gg, (int) bb);

          int r, g, b;
          r = c.getRed();
          g = c.getGreen();
          b = c.getBlue();
          // System.out.printf("position %d = %d, %d, %d\n", i, r, g, b);

          // Send the color for the current LED to the strip,
          // being careful not to send 255 (because that would
          // cause the strip to display the pixels
          serialPort.writeByte(bc(r));
          serialPort.writeByte(bc(g));
          serialPort.writeByte(bc(b));
          sent = delayIfNecessary(sent, 3);
        }
        serialPort.writeByte((byte) 255);
        sent = delayIfNecessary(sent, 1);
        phase += .2;
        System.out.println("updated");
      }
      System.out.println("Port closed: " + serialPort.closePort());
    } catch (SerialPortException ex) {
      System.out.println(ex);
    }
  }

  static byte bc(int b) {
    byte rv = (byte) b;
    if (rv > 254)
      rv = (byte) 254;
    return rv;
  }

  static int delayIfNecessary(int count, int more) {
    count += more;
    if (count > 30) {
      System.out.println("waiting");
      delay();
      count = 0;
    }
    return count;
  }

  static void delay() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException ex) {
    }
  }

}