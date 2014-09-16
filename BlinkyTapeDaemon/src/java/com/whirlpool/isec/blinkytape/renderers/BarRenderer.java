package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.data.IHasColorProperty;
import com.whirlpool.isec.blinkytape.data.IHasNumericValueProperty;
import com.whirlpool.isec.blinkytape.data.Datum;
import com.whirlpool.isec.blinkytape.data.DatumWithColorAndNumber;

public class BarRenderer extends SolidRenderer {
  Double min = 0.0;

  Double max = 1.0;

  boolean round = true;

  BarColorMapper barColorMapper = null;

  @Override
  public DatumWithColorAndNumber createDatumInstance() {
    DatumWithColorAndNumber rv = new DatumWithColorAndNumber(this.getName());
    rv.setColor(this.getColor());
    return rv;
  }

  Color[] templateColors = null;

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());

    Datum datum = Config.getInstance().getDatum(getName());

    Double value = null;
    if (datum instanceof IHasNumericValueProperty) {
      value = ((IHasNumericValueProperty) datum).getValue();
    } else {
      logger.error("datum {} doesn't contain a numeric value");
    }

    Color datumColor = null;
    if (datum instanceof IHasColorProperty) {
      datumColor = ((IHasColorProperty) datum).getColor();
    } else {
      logger.error("datum {} doesn't contain a color");
    }

    value = calculateLed(value);

    if (barColorMapper != null) {
      if (!barColorMapper.isZebra()) {
        Color c = barColorMapper.getColorForValue(value);
        if (c != null)
          datumColor = c;
      }
    }

    boolean over = false;
    for (int i = 1; i <= getLength(); i++) {
      Color c1 = Color.black;
      if (i <= value) {
        logger.debug("** {} <= {}", i, value);
        if (templateColors != null) {
          c1 = templateColors[i];
        } else {
          c1 = datumColor;
        }
      } else {
        if (!over)
          logger.debug("{} !<= {}", i, value);
        over = true;
      }
      rv.add(c1);
    }
    return rv;
  }

  @Override
  public void postConfig() {
    super.postConfig();
    if (barColorMapper != null) {
      if (barColorMapper.isZebra()) {
        templateColors = new Color[getLength()];
        for (int led = 0; led < getLength(); led++) {
          double d = calculateV(led);
          templateColors[led] = barColorMapper.getColorForValue(d);
        }
      }
    }
  }

  double calculateLed(double v) {
    double rv = ((v - getMin()) / (getMax() - getMin())) * getLength();
    if (isRound())
      rv = Math.round(rv);
    return rv;
  }

  double calculateV(int led) {
    return getMin() + ((led * 1.0 * (getMax() - getMin()) / getLength()));
  }

  public void setBarColorMapper(BarColorMapper b) {
    barColorMapper = b;
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  public boolean isRound() {
    return round;
  }

  public void setRound(boolean round) {
    this.round = round;
  }

}
