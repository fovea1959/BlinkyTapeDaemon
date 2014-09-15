package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.config.TapeConfig;
import com.whirlpool.isec.blinkytape.segmentrenderers.Segment;
import com.whirlpool.isec.blinkytape.serial.BTSerialException;
import com.whirlpool.isec.blinkytape.taperenderers.ITapeRenderer;

public class Tape implements Runnable {
  static Logger logger = LoggerFactory.getLogger(Tape.class);

  private final int length = 60;

  private String name = null;

  private TapeConfig tapeConfig;
  
  Color[] leds = new Color[length];

  public Tape(TapeConfig tapeConfig) {
    super();
    this.tapeConfig = tapeConfig;
    // Util.setupConverters();
  }

  @Override
  public void run() {
    try {
      long timeOfLastTapeUpdate = 0;
      while (true) {
        long t0 = System.currentTimeMillis();

        // TODO: optimize this into updating a local representation,
        // then blasting it out
        clear();
        boolean needToUpdateTheTape = false;
        int i = 0;

        for (Segment<?> segment : getTapeConfig().getSegments()) {
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
            setColor(i++, c);
          }
        }
        // force an update every 2 seconds regardless
        if (t0 - timeOfLastTapeUpdate > 2000) {
          logger.debug("forcing an update");
          needToUpdateTheTape = true;
        }
        if (needToUpdateTheTape) {
          for (ITapeRenderer iTapeRenderer : tapeConfig.getTapeRenderers()) {
            iTapeRenderer.update(leds);
          }
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
            logger.debug("sleeping for {}", delayForThisGo);
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
  
  public void close() {
    for (ITapeRenderer iTapeRenderer : tapeConfig.getTapeRenderers()) {
      iTapeRenderer.close();
    }
  }

  private boolean dieFlag = false;

  private Integer delay = 100;

  public void setDieFlag(boolean dieFlag) {
    this.dieFlag = dieFlag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColor(int i, Color c) {
    leds[i] = c;
  }
  
  public void clear() {
    for (int i = 0; i < length; i++) leds[i] = Color.black;
  }
  
  public int getLength() {
    return length;
  }

  public TapeConfig getTapeConfig() {
    return tapeConfig;
  }

}
