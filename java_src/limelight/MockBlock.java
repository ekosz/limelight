package limelight;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MockBlock implements Block
{
  public FlatStyle style;
  public String text;
  public String name;
  public Object pressedKey;
  public Object releasedKey;
  public Object typedKey;
  public Object clickedMouse;
  public Object enteredMouse;
  public boolean hooverOn;
  public Object exitedMouse;
  public Object releasedMouse;
  public Object pressedMouse;
  public Object draggedMouse;
  public Object movedMouse;

  public MockBlock()
  {
    style = new FlatStyle();
  }

  public Panel getPanel()
  {
    return null;
  }

  public FlatStyle getStyle()
  {
    return style;
  }

  public String getClassName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public Page getPage()
  {
    return null;
  }

  public void setText(String value)
  {
    text = value;
  }

  public void mouse_clicked(MouseEvent e)
  {
    clickedMouse = e;
  }

  public void hover_on()
  {
    hooverOn = true;
  }

  public void mouse_entered(MouseEvent e)
  {
    enteredMouse = e;
  }

  public void mouse_exited(MouseEvent e)
  {
    exitedMouse = e;
  }

  public void mouse_pressed(MouseEvent e)
  {
    pressedMouse = e;
  }

  public void mouse_released(MouseEvent e)
  {
    releasedMouse = e;
  }

  public void mouse_dragged(MouseEvent e)
  {
    draggedMouse = e;
  }

  public void mouse_moved(MouseEvent e)
  {
    movedMouse = e;
  }

  public void hover_off()
  {
    hooverOn = false;
  }

  public void key_typed(KeyEvent e)
  {
    typedKey = e;
  }

  public void key_pressed(KeyEvent e)
  {
    pressedKey = e;
  }

  public void key_released(KeyEvent e)
  {
    releasedKey = e;
  }
}
