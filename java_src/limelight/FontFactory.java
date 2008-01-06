package limelight;

import java.awt.*;

public class FontFactory
{
  public static FontFactory instance;

  static
  {
    instance = new FontFactory();
  }

  public Font createFont(Style style)
	{
		int fontStyle = 0;
		if(style.getFontStyle() != null && style.getFontStyle().indexOf("bold") != -1)
			fontStyle |= Font.BOLD;
		if(style.getFontStyle() != null && style.getFontStyle().indexOf("italic") != -1)
			fontStyle |= Font.ITALIC;

		String face = style.getFontFace() == null ? "Arial" : style.getFontFace();
		int size = style.asInt(style.getFontSize()) <= 0 ? 10 : style.asInt(style.getFontSize());

		return new Font(face, fontStyle, size);
	}
}