package limelight.ui.model.inputs.keyProcessors;


import org.junit.Before;
import org.junit.Test;

import java.awt.event.KeyEvent;

public class ShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = ShiftCmdKeyProcessor.instance;
    modifier = 5;
  }

  @Test
  public void canProcessRightArrowAndSelectToTheRightEdge()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_RIGHT);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), 1, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToTheLeftEdge()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_LEFT);

    processor.processKey(mockEvent, model);

    assertSelection(0, 1, true);
  }

  @Test
  public void canProcessUpArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_UP);

    processor.processKey(mockEvent, model);

    assertSelection(0, 1, true);
  }

  @Test
  public void canProcessDownArrow()
  {
    mockEvent = new MockKeyEvent(modifier, KeyEvent.VK_DOWN);

    processor.processKey(mockEvent, model);

    assertSelection(model.getText().length(), 1, true);
  }

}
