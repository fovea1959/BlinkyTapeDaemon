package com.whirlpool.isec.blinkytape.tapes;

import java.awt.Color;

public interface ITape {
  public void update (Color[] leds);
  public void close();
}
