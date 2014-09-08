package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import jssc.SerialPortException;

public class Cylon implements Runnable {
  private Color color = new Color(200, 128, 128);

  private boolean dieFlag = false;

  public void setDieFlag(boolean dieFlag) {
    this.dieFlag = dieFlag;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public void run() {
    BlinkyTape tape = null;
    try {
      tape = new BlinkyTape("COM11");

      boolean goingup = true;
      int spot = 0;

      while (true) {
        tape.setColor(spot, Color.BLACK);
        if (goingup) {
          if (spot >= tape.getLength() - 1) {
            goingup = false;
            spot--;
          } else {
            spot++;
          }
        } else {
          if (spot <= 0) {
            spot++;
            goingup = true;
          } else {
            spot--;
          }
        }
        tape.setColor(spot, color);
        tape.updateTape();

        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {

        }
        if (dieFlag)
          break;
      }
    } catch (SerialPortException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (tape != null)
          tape.close();
      } catch (SerialPortException ex) {
      }
    }

  }
}
