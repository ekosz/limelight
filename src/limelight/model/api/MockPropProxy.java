//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import limelight.util.ResourceLoader;
import limelight.styles.ScreenableStyle;
import limelight.styles.Style;
import limelight.ui.Panel;

import java.util.HashMap;
import java.util.Map;

public class MockPropProxy implements PropProxy
{
  public Style hoverStyle;
  public String text;
  public String name;
  public boolean hooverOn;
  public SceneProxy sceneProxy;
  public Map<String, Object> appliedOptions;

  public MockPropProxy()
  {
  }

  public MockPropProxy(String name)
  {
    this();
    this.name = name;
  }

  public Panel getPanel()
  {
    return null;
  }

  public ScreenableStyle getStyle()
  {
    throw new RuntimeException("getStyle called on MockProp");
  }

  public Style getHoverStyle()
  {
    return hoverStyle;
  }

  public String getName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public SceneProxy getScene()
  {
    return sceneProxy;
  }

  public void applyOptions(Map<String, Object> options)
  {
    appliedOptions = new HashMap<String, Object>(options);
    options.clear();
  }

  public void setText(String value)
  {
    text = value;
  }
}