package com.whirlpool.isec.blinkytape.renderers;

public class SegmentBarParameters extends SegmentSolidParameters {
  public SegmentBarParameters(SegmentBar segmentBar) {
    super (segmentBar);
  }

  Double value = 0.0;

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