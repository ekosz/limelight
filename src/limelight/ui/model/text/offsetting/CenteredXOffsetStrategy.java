//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.text.offsetting;

import limelight.util.Box;

public class CenteredXOffsetStrategy implements XOffsetStrategy
{
  public int calculateXOffset(limelight.ui.model.text.TextModel model)
  {
    int xOffset = model.getXOffset();
    Box boundingBox = model.getContainer().getConsumableBounds();
    int absoluteCaretX = model.getAbsoluteX(model.getCaretLocation());
    int relativeCaretX = absoluteCaretX + xOffset;

    if(relativeCaretX >= boundingBox.width || relativeCaretX < 0)
    {
      xOffset = (absoluteCaretX - boundingBox.width / 2) * -1;

      int maxOffset = boundingBox.width - model.getTextDimensions().width - model.getCaretWidth();

      if(xOffset < maxOffset)
        xOffset = maxOffset;
      else if(xOffset > 0)
        xOffset = 0;

      relativeCaretX = absoluteCaretX + xOffset;
      if(relativeCaretX == boundingBox.width)
        xOffset -= model.getCaretWidth();
    }
    return xOffset;
  }
}
