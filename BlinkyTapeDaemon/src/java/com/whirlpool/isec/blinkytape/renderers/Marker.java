package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.List;

public class Marker extends SegmentSolid  {

  public Marker() {
    super();
    setLength(1);
  }


  @Override
  SegmentSolidParameters createParametersInstance() {
    // TODO Auto-generated method stub
    return new MarkerParameters(this);
  }


}
