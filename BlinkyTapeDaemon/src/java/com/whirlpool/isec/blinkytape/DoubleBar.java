package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPortException;

public class DoubleBar implements Runnable {
  static Logger logger = LoggerFactory.getLogger(DoubleBar.class);

  private Color color = new Color(200, 128, 128);

  private boolean dieFlag = false;

  private Integer delay = 100;

  public void setDelay(Integer delay) {
    this.delay = delay;
  }

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
      int limit = tape.getLength() / 2;

      while (true) {
        long t0 = System.currentTimeMillis();

        if (goingup) {
          if (spot >= limit - 1) {
            goingup = false;
            spot--;
          } else {
            spot++;
          }
        } else {
          if (spot < 0) {
            spot++;
            goingup = true;
          } else {
            spot--;
          }
        }
        logger.debug("spot = {}/{}", spot, limit);
        
      
        for (int i = 0; i < limit; i++) {
          tape.setColor(i, (i <= spot ? color : Color.black));
        }
        for (int i = limit; i < limit + limit; i++) {
          tape.setColor(i, (i <= spot + limit ? color : Color.black));
        }
        long t1 = System.currentTimeMillis();
        tape.updateTape();
        long t2 = System.currentTimeMillis();
        logger.debug("took {} to fiddle pixels, {} to update the tape", t1 - t0, t2 - t1);

        try {
          long delayAlreadyBlown = (System.currentTimeMillis() - t0);
          long delayForThisGo = delay - delayAlreadyBlown;
          if (delayForThisGo < 0) {
            logger.warn("well, it's {}ms too late!", Math.abs(delayForThisGo));
          } else {
            long t10 = System.currentTimeMillis();
            Thread.sleep(delayForThisGo);
            long t11 = System.currentTimeMillis();
            logger.debug("slept for {}", t11 - t10);
          }
        } catch (InterruptedException ex) {
          logger.warn("sleep interrupted");
        }
        long tfinal = System.currentTimeMillis();
        logger.debug("this loop took {}, wanted {}", tfinal - t0, delay);
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

  @Override
  public String toString() {
    return "Cylon [color=" + color + ", delay=" + delay + "]";
  }
}
