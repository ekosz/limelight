//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.ui.MockGraphics;
import limelight.util.Box;

import java.awt.*;

public class ScaleFillStrategyAttributeTest extends FillStrategyAttributeTest
{
  private ScaleFillStrategyAttribute attribute;
  private MockGraphics graphics;
  private Box clip;

  public void setUp() throws Exception
  {
    attribute = new ScaleFillStrategyAttribute();
    graphics = new MockGraphics();
    clip = new Box(0, 0, 60, 60);
    graphics.clip = clip;
  }

  public void testName() throws Exception
  {
    assertEquals("scale", attribute.toString());
  }

  public void testFill() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(0), new StaticYCoordinateAttribute(0));

    assertEquals(1, graphics.drawnImages.size());
    checkScaledImage(image, graphics.drawnImages.get(0), new Rectangle(0, 0, 20, 20), new Rectangle(0, 0, 60, 60));
  }

  public void testFillNonZero() throws Exception
  {
    Image image = new MockImage(20, 20);
    attribute.fill(graphics, image, new StaticXCoordinateAttribute(20), new StaticYCoordinateAttribute(20));

    assertEquals(1, graphics.drawnImages.size());
    checkScaledImage(image, graphics.drawnImages.get(0), new Rectangle(0, 0, 20, 20), new Rectangle(20, 20, 60, 60));
  }
}
