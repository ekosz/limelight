package limelight.ui.model.inputs;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;

public class MockTextContainer implements TextContainer
{
  public RichStyle style;
  public Box bounds;

  public MockTextContainer()
  {
    style = new RichStyle();
  }

  public Style getStyle()
  {
    return style;
  }

  public Point getAbsoluteLocation()
  {
    return bounds.getLocation();
  }

  public int getWidth()
  {
    return bounds.width;
  }

  public int getHeight()
  {
    return bounds.height;
  }

  public Box getBoundingBox()
  {
    return bounds;
  }

  public boolean isCursorOn()
  {
    return false;
  }

  public boolean isFocused()
  {
    return false;
  }

  public int getLastKeyPressed()
  {
    return 0;
  }

  public void setLastKeyPressed(int keyCode)
  {
  }
}
