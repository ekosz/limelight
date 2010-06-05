//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import limelight.ui.TextLayoutImpl;
import limelight.ui.TypedLayout;
import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TextModel implements ClipboardOwner
{
  public static final int SIDE_TEXT_MARGIN = 6;
  public static final int SIDE_DETECTION_MARGIN = SIDE_TEXT_MARGIN + 1;
  public static final int TOP_MARGIN = 7;
  public static final int BOTTOM_MARGIN = 7;

  protected TextInputPanel myPanel;
  protected ArrayList<TypedLayout> textLayouts;
  protected int yOffset;
  protected int xOffset;

  private StringBuffer text = new StringBuffer();
  private int cursorX;
  private boolean selectionOn;
  private int selectionStartX;
  private int cursorIndex;
  private int selectionIndex;
  private String lastLayedOutText;
  private int lastCursorIndex;
  private int spaceWidth;
  private int YOffset;

  public abstract void shiftOffset(int index);

  protected abstract int getXPosFromText(String toIndexString);

  public abstract boolean isBoxFull();

  public abstract boolean isMoveUpEvent(int keyCode);

  public abstract boolean isMoveDownEvent(int keyCode);

  public abstract int getTopOfStartPositionForCursor();

  public abstract int getBottomPositionForCursor();

  public abstract int getIndexOfLastCharInLine(int line);

  public abstract void calculateLeftShiftingOffset();

  public abstract int calculateYOffset();

  public abstract boolean isCursorAtCriticalEdge(int cursorX);

  public TextModel(TextInputPanel panel)
  {
    this.myPanel = panel;
    cursorX = SIDE_TEXT_MARGIN;
    selectionOn = false;
    selectionStartX = 0;
    cursorIndex = 0;
    selectionIndex = 0;
    xOffset = 0;
  }

  public void setCursorAndSelectionStartX()
  {
    cursorX = getXPosFromIndex(cursorIndex);
    selectionStartX = getXPosFromIndex(selectionIndex);
  }

  public Font getFont()
  {
    Style style = myPanel.getStyle();
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
  }

  public int getXPosFromIndex(int index)
  {
    if(text == null || text.length() == 0)
      return SIDE_TEXT_MARGIN;
    int lineNumber = getLineNumberOfIndex(index);
    int startOfLineIndex = 0;
    for(int i = 0; i < lineNumber; i++)
      startOfLineIndex += textLayouts.get(i).getText().length();
    if(index <= 0 || startOfLineIndex == index)
      return SIDE_TEXT_MARGIN;
    String toIndexString = text.substring(startOfLineIndex, index);
    int xPos = getXPosFromText(toIndexString) + getTerminatingSpaceWidth(toIndexString);
    if(xPos < SIDE_TEXT_MARGIN)
      return SIDE_TEXT_MARGIN;
    return xPos;
  }

  public void clearLayouts()
  {
    textLayouts = null;
  }

  public int getYPosFromIndex(int index)
  {
    int yPos = TOP_MARGIN;
    if(text == null || text.length() == 0)
      return yPos;
    int lineNumber = getLineNumberOfIndex(index);
    int lineHeight = getTotalHeightOfLineWithLeadingMargin(0);
    for(int layoutIndex = 0; layoutIndex < lineNumber; layoutIndex++)
      yPos += lineHeight;
    return yPos;
  }

  public int getTerminatingSpaceWidth(String string)
  {
    int trailingSpaces = 0;
    for(int i = string.length() - 1; i > 0 && string.charAt(i) == ' '; i--)
      trailingSpaces++;

    return trailingSpaces * spaceWidth();
  }

  public int spaceWidth()
  {
    Font font = getFont();
    if(spaceWidth == 0)
    {
      TypedLayout layout1 = new TextLayoutImpl("a a", font, TextPanel.getRenderContext());
      TypedLayout layout2 = new TextLayoutImpl("aa", font, TextPanel.getRenderContext());
      spaceWidth = getWidthDimension(layout1) - getWidthDimension(layout2);
    }
    return spaceWidth;
  }

  public abstract Dimension calculateTextDimensions();

  public float getHeightDimension(TypedLayout layout)
  {
    return (layout.getAscent() + layout.getDescent());

  }

  public int getTotalHeightOfLineWithLeadingMargin(int layoutIndex)
  {
    textLayouts = getTextLayouts();
    TypedLayout typedLayout = textLayouts.get(layoutIndex);
    return (int) (getHeightDimension(typedLayout) + typedLayout.getLeading() + 0.5);
  }

  public int getHeightOfCurrentLine()
  {
    int lineNumber = getLineNumberOfIndex(cursorIndex);
    TypedLayout layoutForLine = textLayouts.get(lineNumber);
    return (int) getHeightDimension(layoutForLine);
  }

  public int getWidthDimension(TypedLayout layout)
  {
    return (int) (layout.getBounds().getWidth() + layout.getBounds().getX() + 0.5);
  }

  public int getXOffset()
  {
    return xOffset;
  }

  public abstract ArrayList<TypedLayout> getTextLayouts();

  public Point getPanelAbsoluteLocation()
  {
    return myPanel.getAbsoluteLocation();
  }

  public int getPanelWidth()
  {
    return myPanel.getWidth();
  }

  public int getPanelHeight()
  {
    return myPanel.getHeight();
  }

  public SimpleHorizontalAlignmentValue getHorizontalAlignment()
  {
    return myPanel.horizontalTextAlignment;
  }

  public SimpleVerticalAlignmentValue getVerticalAlignment()
  {
    return myPanel.verticalTextAlignment;
  }

  public boolean isCursorOn()
  {
    return myPanel.isCursorOn();
  }

  public boolean isFocused()
  {
    return myPanel.isFocused();
  }

  public void copyText(String clipboard)
  {
    StringSelection stringSelection = new StringSelection(clipboard);
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    systemClipboard.setContents(stringSelection, this);
  }

  public String getClipboardContents()
  {
    String clipboardString = "";
    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = systemClipboard.getContents(null);
    boolean hasText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if(hasText)
    {
      try
      {
        clipboardString = (String) contents.getTransferData(DataFlavor.stringFlavor);
      }
      catch(UnsupportedFlavorException e)
      {
        e.printStackTrace();
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
    return clipboardString;
  }

  public void copySelection()
  {
    String clipboard;
    if(selectionIndex < cursorIndex)
      clipboard = text.substring(selectionIndex, cursorIndex);
    else
      clipboard = text.substring(cursorIndex, selectionIndex);
    copyText(clipboard);
  }

  public void pasteClipboard()
  {
    String clipboard = getClipboardContents();
    if(clipboard != null && clipboard.length() > 0)
    {
      text.insert(cursorIndex, clipboard);
      setCursorIndex(cursorIndex + clipboard.length());
    }
  }

  public void cutSelection()
  {
    copySelection();
    deleteSelection();
  }

  public void deleteSelection()
  {
    if(selectionIndex < cursorIndex)
      deleteEnclosedText(selectionIndex, cursorIndex);
    else
      deleteEnclosedText(cursorIndex, selectionIndex);
  }

  public void deleteEnclosedText(int first, int second)
  {
    text.delete(first, second);
    setCursorIndex(first);
    setSelectionIndex(0);
  }

  public abstract ArrayList<Rectangle> getSelectionRegions();

  public int findWordsRightEdge(int index)
  {
    for(int i = index; i <= text.length() - 1; i++)
    {
      if(i == 0)
        i = 1;
      if(isAtEndOfWord(i))
        return i;
    }
    return text.length();
  }

  public boolean isAtEndOfWord(int i)
  {
    return text.charAt(i - 1) != ' ' && text.charAt(i - 1) != '\n' && (text.charAt(i) == ' ' || text.charAt(i) == '\n');
  }

  public int findWordsLeftEdge(int index)
  {
    for(int i = index; i > 1; i--)
    {
      if(isAtStartOfWord(i))
        return i;
    }
    return 0;
  }

  public boolean isAtStartOfWord(int i)
  {
    return (text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') && (text.charAt(i) != ' ' && text.charAt(i) != '\n');
  }

  public int getCursorIndex()
  {
    return cursorIndex;
  }

  public void setCursorIndex(int cursorIndex)
  {
    lastCursorIndex = this.cursorIndex;
    this.cursorIndex = cursorIndex;
  }

  public int getSelectionIndex()
  {
    return selectionIndex;
  }

  public void setSelectionIndex(int selectionIndex)
  {
    this.selectionIndex = selectionIndex;
  }

  public void setText(String text)
  {
    if(text == null)
      this.text = null;
    else
    {
      this.text = new StringBuffer(text);
      setCursorIndex(text.length());
    }
  }

  public String getText()
  {
    if(text == null)
      return null;
    return text.toString();
  }

  public void setLastLayedOutText(String lastLayedOutText)
  {
    this.lastLayedOutText = lastLayedOutText;
  }

  public String getLastLayedOutText()
  {
    return lastLayedOutText;
  }

  public int getLastKeyPressed()
  {
    return myPanel.getLastKeyPressed();
  }

  public void setLastKeyPressed(int keyCode)
  {
    myPanel.setLastKeyPressed(keyCode);
  }

  public int getLastCursorIndex()
  {
    return lastCursorIndex;
  }

  public void setLastCursorIndex(int index)
  {
    lastCursorIndex = index;
  }

  public boolean isThereSomeDifferentText()
  {
    return !text.toString().equals(lastLayedOutText);
  }


  public int getLineNumberOfIndex(int index)
  {
    getTextLayouts();
    int lineNumber = 0;
    for(; lineNumber < textLayouts.size() - 1; lineNumber++)
    {
      index -= textLayouts.get(lineNumber).getText().length();
      if(index < 0)
        break;
    }
    return lineNumber;
  }


  public void insertCharIntoTextBox(char c)
  {
    if(c == KeyEvent.CHAR_UNDEFINED)
      return;
    text.insert(getCursorIndex(), c);
    setCursorIndex(getCursorIndex() + 1);
  }

  public boolean isMoveRightEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_RIGHT && getCursorIndex() < getText().length();
  }

  public boolean isMoveLeftEvent(int keyCode)
  {
    return keyCode == KeyEvent.VK_LEFT && getCursorIndex() > 0;
  }

  public void initSelection()
  {
    selectionOn = true;
    setSelectionIndex(getCursorIndex());
  }

  public int findNearestWordToTheLeft()
  {
    return findWordsLeftEdge(getCursorIndex() - 1);
  }

  public int findNearestWordToTheRight()
  {
    return findNextWordSkippingSpacesOrNewLines(findWordsRightEdge(cursorIndex));
  }

  protected int findNextWordSkippingSpacesOrNewLines(int startIndex)
  {
    for(int i = startIndex; i <= getText().length() - 1; i++)
    {
      if(isAtStartOfWord(i))
        return i;
    }
    return getText().length();
  }


  public void selectAll()
  {
    selectionOn = true;
    setCursorIndex(getText().length());
    setSelectionIndex(0);
  }

  public void moveCursorUpALine()
  {
    if(getLastKeyPressed() == KeyEvent.VK_DOWN)
    {
      setCursorIndex(getLastCursorIndex());
      return;
    }
    int previousLine = getLineNumberOfIndex(cursorIndex) - 1;
    setCursorIndex(getNewCursorPositionAfterMovingByALine(previousLine));
  }

  public void moveCursorDownALine()
  {
    if(getLastKeyPressed() == KeyEvent.VK_UP)
    {
      setCursorIndex(getLastCursorIndex());
      return;
    }
    int nextLine = getLineNumberOfIndex(cursorIndex) + 1;
    int newCursorIndex = getNewCursorPositionAfterMovingByALine(nextLine);
    setCursorIndex(newCursorIndex);
  }

  private int getNewCursorPositionAfterMovingByALine(int nextLine)
  {
    int charCount = 0;
    for(int i = 0; i < nextLine; i++)
      charCount += textLayouts.get(i).getText().length();
    int xPos = getXPosFromIndex(cursorIndex);
    String lineText = textLayouts.get(nextLine).getText();
    int lineLength = lineText.length();
    int newCursorIndex = charCount + lineLength - 1;
    if(nextLine == textLayouts.size() - 1)
      newCursorIndex++;
    if(getXPosFromIndex(newCursorIndex) < xPos)
    {
      return newCursorIndex;
    }
    else
    {
      TextHitInfo hitInfo = textLayouts.get(nextLine).hitTestChar(xPos - SIDE_TEXT_MARGIN, 5);
      newCursorIndex = hitInfo.getInsertionIndex();
      if(newCursorIndex == lineLength && lineText.endsWith("\n"))
        newCursorIndex--;
      return newCursorIndex + charCount;
    }
  }

  public void sendCursorToStartOfLine()
  {
    int currentLine = getLineNumberOfIndex(cursorIndex);

    if(currentLine == 0)
    {
      setCursorIndex(0);
    }
    else
    {
      setCursorIndex(getIndexOfLastCharInLine(currentLine - 1) + 1);
    }
  }

  public void sendCursorToEndOfLine()
  {
    int currentLine = getLineNumberOfIndex(cursorIndex);
    setCursorIndex(getIndexOfLastCharInLine(currentLine));
  }

  public void setSelectionOn(boolean value)
  {
    selectionOn = value;
  }

  public boolean isSelectionOn()
  {
    return selectionOn;
  }

  public int getYOffset()
  {
    return YOffset;
  }

  public void setYOffset(int yOffset)
  {
    this.yOffset = yOffset;
  }

  public void setCursorX(int x)
  {
    cursorX = x;
  }

  public int getCursorX()
  {
    return cursorX;
  }

  public int getSelectionStartX()
  {
    return selectionStartX;
  }

  public TextInputPanel getPanel()
  {
    return myPanel;
  }
}
