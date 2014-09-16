package com.whirlpool.isec.blinkytape.data;

public class DatumWithColorAndNumber extends DatumWithColor implements IHasColorProperty, IHasNumericValueProperty {
  Double value = 0.0;
  public DatumWithColorAndNumber(String name) {
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