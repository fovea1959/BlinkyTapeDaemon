package com.whirlpool.isec.blinkytape.strips;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.config.TapeConfig;

abstract public class AbstractTape {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  private final static int length = 60;
  
  private Color[] leds = new Color[length];
  
  private TapeConfig tapeConfig;

  public AbstractTape(TapeConfig tapeConfig) {
    this.tapeConfig = tapeConfig;
  }

  public void setColor(int i, Color c) {
    leds[i] = c;
  }
  
  public void clear() {
    for (int i = 0; i < length; i++) leds[i] = Color.black;
  }
  
  Color getColorAccountingForReverse (int i) {
    if (tapeConfig.isReverse()) i = (length - 1) - i; 
    return leds[i];
  }

  abstract public void updateTape();
  
  public void close() {
  }

  public int getLength() {
    return length;
  }

  public TapeConfig getTapeConfig() {
    return tapeConfig;
  }

}
