package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.renderers.Segment;
import com.whirlpool.isec.blinkytape.serial.BTSerialException;
import com.whirlpool.isec.blinkytape.strips.AbstractStrip;

public class TapeRenderer implements Runnable {
  public TapeRenderer(AbstractStrip tape) {
    super();
    Util.setupConverters();
    this.tape = tape;
  }
  static Logger logger = LoggerFactory.getLogger(TapeRenderer.class);
  
  private AbstractStrip tape = null;

  private boolean dieFlag = false;

  private Integer delay = 100;

  public void setDieFlag(boolean dieFlag) {
    this.dieFlag = dieFlag;
  }

  @Override
  public void run() {
    try {
      while (true) {
        long t0 = System.currentTimeMillis();
        //logger.info("top of loop");
        int i = 0;
        tape.clear();
        boolean needToActuallyUpdate = false;
        for (Segment<?> segment: tape.getTapeConfig().getSegments()) {
          boolean thisSegmentIsDirty = false;
          if (!segment.isBlinkyTapeVersionCurrent()) {
            thisSegmentIsDirty = true;
            needToActuallyUpdate = true;
          }
          Collection<Color> cs = segment.getLedsForBlinkyTape();
          if (thisSegmentIsDirty) {
            logger.debug("{} thought we should update, and presents colors {}", segment.getName(), cs);
          }
          for (Color c : cs) {
            tape.setColor(i++, c);
          }
        }
        if (needToActuallyUpdate) {
          logger.debug("updating tape");
          tape.updateTape();
        }

        // need to handle serial port errors inside updateTape
        try {
          long delayAlreadyBlown = (System.currentTimeMillis() - t0);
          long delayForThisGo = delay - delayAlreadyBlown;
          if (delayForThisGo < 0) {
            logger.warn("well, it's {}ms too late!", Math.abs(delayForThisGo));
            // tape.reset();
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
    } catch (BTSerialException ex) {
      ex.printStackTrace();
    } finally {
    }

  }
}
