package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.Collection;

public interface IRenderer {
	public int getStartLED();
	public int getLength();
	public Collection<Color> getColors();

}
