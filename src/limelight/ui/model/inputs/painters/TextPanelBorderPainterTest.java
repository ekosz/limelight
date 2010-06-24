package limelight.ui.model.inputs.painters;

import limelight.Context;
import limelight.KeyboardFocusManager;
import limelight.ui.MockGraphics;
import limelight.ui.Painter;
import limelight.ui.api.MockProp;
import limelight.ui.model.MockDrawable;
import limelight.ui.model.PropPanel;
import limelight.ui.model.inputs.TextBox2Panel;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.MockPainter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;

public class TextPanelBorderPainterTest extends Assert
{
  private MockDrawable normalDrawable;
  private MockDrawable focusDrawable;

  private PropPanel parent;
  private Painter painter;
  private MockGraphics graphics;
  private TextBox2Panel panel;

  @Before
  public void setUp() throws Exception
  {
    parent = new PropPanel(new MockProp());
    panel = new TextBox2Panel();
    parent.add(panel);
    graphics = new MockGraphics();
    Context.instance().keyboardFocusManager = new KeyboardFocusManager();

    TextPanelBorderPainter.normalBorder = normalDrawable = new MockDrawable();
    TextPanelBorderPainter.focusedBorder = focusDrawable = new MockDrawable();

    painter = TextPanelBorderPainter.instance;
    parent.getStyle().setBorderColor("transparent");
  }

  private void assertDrawn(MockDrawable normalDrawable, Graphics expectedGraphics, int expectedX, int expectedY, int expectedWidth, int expectedHeight)
  {
    assertEquals(expectedGraphics, normalDrawable.drawnGraphics2D);
    assertEquals(expectedX, normalDrawable.drawnX);
    assertEquals(expectedY, normalDrawable.drawnY);
    assertEquals(expectedWidth, normalDrawable.drawnWidth);
    assertEquals(expectedHeight, normalDrawable.drawnHeight);
  }

  private void assertNotDrawn(MockDrawable normalDrawable)
  {
    assertEquals(null, normalDrawable.drawnGraphics2D);
    assertEquals(0, normalDrawable.drawnX);
    assertEquals(0, normalDrawable.drawnY);
    assertEquals(0, normalDrawable.drawnWidth);
    assertEquals(0, normalDrawable.drawnHeight);
  }

  @Test
  public void willUseBothBackgroundsWhenFocused()
  {
    Context.instance().keyboardFocusManager.focusPanel(panel);

    painter.paint(graphics, parent);

    assertDrawn(normalDrawable, graphics, 0, 0, parent.getWidth(), parent.getHeight());
    assertDrawn(focusDrawable, graphics, 0, 0, parent.getWidth(), parent.getHeight());
  }

  @Test
  public void willOnlyUseNormalBackgroundIfNotFocused()
  {
    painter.paint(graphics, parent);

    assertDrawn(normalDrawable, graphics, 0, 0, parent.getWidth(), parent.getWidth());
    assertNotDrawn(focusDrawable);
  }

  @Test
  public void shouldDelegateToDefaultBorderPainterIfBorderColorIsSpecified() throws Exception
  {
    MockPainter defaultBorderPainter = new MockPainter();
    BorderPainter.instance = defaultBorderPainter;
    parent.getStyle().setTopBorderColor("blue");

    painter.paint(graphics, parent);

    assertNotDrawn(normalDrawable);
    assertNotDrawn(focusDrawable);
    assertEquals(true, defaultBorderPainter.painted);
  }

}