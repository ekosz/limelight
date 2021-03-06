//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import java.util.HashMap;
import java.util.Map;

public class MockStageProxy implements StageProxy
{
  public Map<String, Object> appliedOptions;

  public void applyOptions(Map<String, Object> options)
  {
    appliedOptions = new HashMap<String, Object>(options);
  }
}
