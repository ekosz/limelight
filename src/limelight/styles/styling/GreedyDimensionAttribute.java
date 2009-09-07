package limelight.styles.styling;

import limelight.styles.abstrstyling.DimensionAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;

public class GreedyDimensionAttribute implements DimensionAttribute
{

  public boolean isAuto()
  {
    return false;
  }

  public boolean isDynamic()
  {
    return true;
  }

  public int calculateDimension(int consumableSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    if(min.isNone())
      return 0;
    else
      return min.getAttribute().calculateDimension(consumableSize, DIMENSION_NONE, DIMENSION_NONE);
  }

  public int collapseExcess(int currentSize, int consumedSize, NoneableAttribute<DimensionAttribute> min, NoneableAttribute<DimensionAttribute> max)
  {
    return currentSize;
  }
}