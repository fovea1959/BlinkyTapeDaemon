package com.whirlpool.isec.blinkytape.segmentrenderers;

import java.awt.Color;
import java.util.*;

@SuppressWarnings("rawtypes")
public class Marker extends Segment<SegmentParameters> {
  boolean wasOn = false;
  
  private long period = 2000;
  private double duty = 0.1;

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
      markChanged();
    wasOn = shouldBeOn;
  }

  boolean wellAmILit() {
    long t = System.currentTimeMillis();
    return ((t % period) < (duty * period));
  }

  @Override
  public List<Color> getLeds() {
    logger.debug("length = {}", getLength());
    List<Color> rv = new ArrayList<Color>(getLength());
    boolean lit = wellAmILit();
    for (int i = 0; i < getLength(); i++) {
      rv.add(lit ? colorOn : colorOff);
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

  public long getPeriod() {
    return period;
  }

  public void setPeriod(long period) {
    this.period = period;
  }

  public double getDuty() {
    return duty;
  }

  public void setDuty(double duty) {
    this.duty = duty;
  }
}
