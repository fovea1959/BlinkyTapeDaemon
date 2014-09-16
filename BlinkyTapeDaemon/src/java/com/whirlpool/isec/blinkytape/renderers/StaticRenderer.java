package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

import com.whirlpool.isec.blinkytape.segments.Segment;

public class StaticRenderer extends AbstractRenderer {
  long staticWasLastChanged = 0;
  static private Random random = new Random();
  
  public StaticRenderer() {
    super();
    setName(this.toString());
    super.setLength(1);
  }

  @Override
  public Segment createParametersInstance() {
    return new Segment(getName());
  }

  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    long now = System.currentTimeMillis();
    if (now - staticWasLastChanged > 200) {
      markChanged(now);
    }
  }
  
  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      int b = random.nextInt(16);
      rv.add(new Color(b, b, b));
    }
    return rv;
  }

}
