//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.Stage;
import limelight.model.api.StageProxy;
import limelight.ui.model.FramedStage;

import java.util.Map;

public class JavaStage implements StageProxy
{
  private FramedStage peer;

  JavaStage(String name, Map<String, Object> options)
  {
    peer = new FramedStage(name, this);
    peer.applyOptions(options);
  }

  public Stage getPeer()
  {
    return peer;
  }

  public void applyOptions(Map<String, Object> options)
  {
  }

}
