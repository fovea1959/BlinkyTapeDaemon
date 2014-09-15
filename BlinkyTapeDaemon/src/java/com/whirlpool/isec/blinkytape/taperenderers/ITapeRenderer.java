package com.whirlpool.isec.blinkytape.taperenderers;

import java.awt.Color;

public interface ITapeRenderer {
  public void update (Color[] leds);
  public void close();
}
