package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

import com.whirlpool.isec.blinkytape.data.Datum;

public class StaticRenderer extends AbstractRenderer {
  long staticWasLastChanged = 0;
  static private Random random = new Random();
  
  private Color color = new Color (16, 16, 16);
  
  public StaticRenderer() {
    super();
    setName(this.toString());
    super.setLength(1);
  }

  @Override
  public Datum createDatumInstance() {
    return new Datum(getName());
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
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      float brightness = random.nextFloat();
      rv.add(new Color(Math.round (brightness * r), Math.round (brightness * g), Math.round(brightness * b)));
    }
    return rv;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
