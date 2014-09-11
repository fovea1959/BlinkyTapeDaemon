package com.whirlpool.isec.blinkytape.renderers;

public class SegmentBarParameters extends SegmentSolidParameters {
  public SegmentBarParameters(SegmentBar segmentBar) {
    super (segmentBar);
  }

  Integer value = 0;

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("%s [value=%s]", super.toString(), value);
  }

}