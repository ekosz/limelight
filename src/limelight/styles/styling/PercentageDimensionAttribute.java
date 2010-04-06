//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

public class PercentageDimensionAttribute extends SimplePercentageAttribute implements DimensionAttribute
{
  public PercentageDimensionAttribute(double percentValue)
  {
    super(percentValue);
  }

  public boolean isAuto()
  {
    return false;
  }

  public boolean isDynamic()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max, int greediness)
  {
    int calculatedSize = (int) ((getPercentage() * 0.01) * (double) consumableSize);

    if(!max.isNone())
      calculatedSize = Math.min(calculatedSize, max.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0));
    if(!min.isNone())
      calculatedSize = Math.max(calculatedSize, min.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE, 0));

    return calculatedSize;
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    return currentSize;
  }
}
