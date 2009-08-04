package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.YCoordinateAttribute;
import limelight.styles.VerticalAlignment;

public class YCoordinateAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      YCoordinateAttribute attribute;
      attribute = attemptAlignedAttribute(value);
      if(attribute == null)
        attribute = attemptPercentageAttribute(value);
      if(attribute == null)
        attribute = attemptStaticAttribute(value);

      if(attribute != null)
        return attribute;
      else
        throw makeError(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }

  private YCoordinateAttribute attemptStaticAttribute(String value)
  {
    int intValue = IntegerAttributeCompiler.convertToInt(value);

    if(intValue >= 0)
      return new StaticYCoordinateAttribute(intValue);
    else
      return null;
  }

  private YCoordinateAttribute attemptAlignedAttribute(String value)
  {
    VerticalAlignment alignment = VerticalAlignmentAttributeCompiler.parse(value);
    if(alignment != null)
      return new AlignedYCoordinateAttribute(alignment);
    else
      return null;
  }

  private YCoordinateAttribute attemptPercentageAttribute(String value)
  {
    if(PercentageAttributeCompiler.isPercentage(value))
    {
      double percentValue = PercentageAttributeCompiler.convertToDouble(value);
      if(percentValue >= 0)
        return new PercentageYCoordinateAttribute(percentValue);
    }
    return null;
  }
}
