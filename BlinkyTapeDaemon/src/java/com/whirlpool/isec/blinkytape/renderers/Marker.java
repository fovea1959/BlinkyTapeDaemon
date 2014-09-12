package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

@SuppressWarnings("rawtypes")
public class Marker extends Segment<SegmentParameters> {
  boolean wasOn = false;

  Color colorOn = Color.DARK_GRAY;

  Color colorOff = Color.BLACK;

  public Marker() {
    super();
    setName(this.toString());
    super.setLength(1);
  }

  @Override
  MarkerParameters createParametersInstance() {
    return new MarkerParameters(this);
  }

  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    boolean shouldBeOn = wellAmILit();
    if (shouldBeOn != wasOn)
      blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning();
    wasOn = shouldBeOn;
  }

  boolean wellAmILit() {
    long t = System.currentTimeMillis();
    return (t % 5000 < 100);
  }

  @Override
  public List<Color> getLedsForInformation() {
    logger.debug("length = {}", getLength());
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      rv.add(wellAmILit() ? colorOn : colorOff);
    }
    logger.debug("marker {} colors = {}", this.toString(), rv.toString());
    return rv;
  }

  public void setColorOn(Color colorOn) {
    this.colorOn = colorOn;
  }

  public void setColorOff(Color colorOff) {
    this.colorOff = colorOff;
  }

  @Override
  public void setLength(Integer length) {
    throw new RuntimeException("no no no; I am a marker");
  }
}
