//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.MockTypedLayout;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.text.TextLocation;
import limelight.util.Box;
import limelight.util.Colors;
import limelight.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class TextPanelTextPainterTest
{
  public TextPanelPainter painter;
  public limelight.ui.model.text.TextModel model;
  public limelight.ui.model.text.MockTextContainer container;
  public MockGraphics graphics;

  @Before
  public void setUp()
  {
    assumeTrue(TestUtil.notHeadless());
    container = new limelight.ui.model.text.MockTextContainer();
    container.bounds = new Box(0, 0, 150, 50);

    model = new limelight.ui.model.text.MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("Some Text");
    model.setCaretLocation(TextLocation.at(0, 4));

    graphics = new MockGraphics();
    container.cursorOn = true;
    painter = TextPanelTextPainter.instance;
  }

  private MockTypedLayout layout(int index)
  {
    return (MockTypedLayout)model.getLines().get(index);
  }

  @Test
  public void willNotPaintTextIfTextIsNull()
  {
    model.setText(null);

    painter.paint(graphics, model);

    assertEquals(false, layout(0).hasDrawn);
  }

  @Test
  public void willDrawTextToLayout()
  {
    painter.paint(graphics, model);

    assertEquals(true, layout(0).hasDrawn);
    assertEquals(model.getText(), layout(0).getText());
  }

  @Test
  public void willDrawTextToCorrectLocation()
  {
    painter.paint(graphics, model);

    assertEquals(0, layout(0).drawnX);
    assertEquals(7, layout(0).drawnY);
  }

  @Test
  public void willDrawTextWithTheRightColor()
  {
    painter.paint(graphics, model);

    assertEquals(Color.black, graphics.color);
  }

  @Test
  public void shouldColorOfText() throws Exception
  {
    container.style.setTextColor("red");

    painter.paint(graphics, model);

    assertEquals(Colors.resolve("red"), graphics.color);
  }

  @Test
  public void shouldHorizontallyAlignEachLine() throws Exception
  {
    container.style.setHorizontalAlignment("right");
    model.setText("Line 1\nLine Two");

    painter.paint(graphics, model);

    assertEquals(90, layout(0).drawnX);
    assertEquals(70, layout(1).drawnX);
  }
}
