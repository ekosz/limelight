package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;

public class TextBoxPanel extends InputPanel
{
  private TextBox textBox;

  protected Component createComponent()
  {
    return textBox = new TextBox(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new TextBoxTextAccessor(textBox);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "100");
    style.setDefault(Style.HEIGHT, "" + textBox.getPreferredSize().height);
  }

  public TextBox getTextBox()
  {
    return textBox;
  }

  public String getText()
  {
    return textBox.getText();
  }

  public void setText(String text)
  {
    textBox.setText(text);
  }

  private static class TextBoxTextAccessor implements TextAccessor
  {
    private TextBox textBox;

    public TextBoxTextAccessor(TextBox textBox)
    {
      this.textBox = textBox;
    }

    public void setText(String text)
    {
      textBox.setText(text);
    }

    public String getText()
    {
      return textBox.getText();
    }
  }
}