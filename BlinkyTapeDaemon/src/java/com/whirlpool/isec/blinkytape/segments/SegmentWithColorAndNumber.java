package com.whirlpool.isec.blinkytape.segments;

public class SegmentWithColorAndNumber extends SegmentWithColor implements IHasColorProperty, IHasNumericValueProperty {
  Double value = 0.0;
  public SegmentWithColorAndNumber(String name) {
    super (name);
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("%s [value=%s]", super.toString(), value);
  }

}