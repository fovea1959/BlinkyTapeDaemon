package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.renderers.Segment;
import com.whirlpool.isec.blinkytape.serial.BTSerialException;
import com.whirlpool.isec.blinkytape.strips.AbstractTape;

public class TapeRenderer implements Runnable {
  static Logger logger = LoggerFactory.getLogger(TapeRenderer.class);

  private List<AbstractTape> tapes = null;

  public TapeRenderer() {
    super();
    tapes = new ArrayList<AbstractTape>();
    // Util.setupConverters();
  }

  private boolean dieFlag = false;

  private Integer delay = 100;

  public void setDieFlag(boolean dieFlag) {
    this.dieFlag = dieFlag;
  }

  @Override
  public void run() {
    try {
      long timeOfLastTapeUpdate = 0;
      while (true) {
        long t0 = System.currentTimeMillis();

        // TODO: optimize this into updating a local representation,
        // then blasting it out
        for (AbstractTape tape : tapes) {
          tape.clear();
          boolean needToUpdateTheTape = false;
          int i = 0;

          for (Segment<?> segment : tape.getTapeConfig().getSegments()) {
            boolean thisSegmentIsDirty = false;
            long timeOfLastSegmentUpdate = segment.getLastTimeUpdated();
            if (timeOfLastSegmentUpdate > timeOfLastTapeUpdate) {
              thisSegmentIsDirty = true;
              needToUpdateTheTape = true;
            }
            Collection<Color> cs = segment.getLeds();
            if (thisSegmentIsDirty) {
              logger.debug("{} thought we should update, and presents colors {}",
                  segment.getName(), cs);
            }
            for (Color c : cs) {
              tape.setColor(i++, c);
            }
          }
          // force an update every 2 seconds regardless
          if (t0 - timeOfLastTapeUpdate > 2000) {
            logger.debug("forcing an update");
            needToUpdateTheTape = true;
          }
          if (needToUpdateTheTape) {
            tape.updateTape();
          }

          // need to handle serial port errors inside updateTape
          try {
            long delayAlreadyBlown = (System.currentTimeMillis() - t0);
            long delayForThisGo = delay - delayAlreadyBlown;
            if (delayForThisGo < 0) {
              logger.warn("well, it's {}ms too late!", Math.abs(delayForThisGo));
              Thread.sleep(3); // wait a few anyway to let the tape catch up
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
      }
    } catch (BTSerialException ex) {
      ex.printStackTrace();
    } finally {
    }

  }
  
  public void addTape (AbstractTape tape) {
    tapes.add(tape);
  }
}
