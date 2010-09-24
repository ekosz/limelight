//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.model.Production;
import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.api.PropProxy;
import limelight.util.ResourceLoader;

import java.awt.*;
import java.util.*;

public class Scene extends Prop implements RootPanel
{
  private final AbstractList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private Stage stage;
  private final Map<String, RichStyle> styles;
  private HashMap<String, Prop> index = new HashMap<String, Prop>();
  private Production production;
  private boolean shouldAllowClose = true;

  public Scene(PropProxy propProxy)
  {
    super(propProxy);
    styles = Collections.synchronizedMap(new HashMap<String, RichStyle>());
  }

  public void setStage(Stage newFrame)
  {   
    if(stage != null && newFrame != stage)
      delluminate();

    stage = newFrame;

    if(stage != null)
    {
      illuminate();
      addPanelNeedingLayout(this);
    }
  }

  @Override
  public Layout getDefaultLayout()
  {
    return SceneLayout.instance;
  }

  @Override
  public RootPanel getRoot()
  {
    return this;
  }

  @Override
  public Graphics2D getGraphics()
  {
    return (Graphics2D) stage.getGraphics();
  }

  @Override
  public boolean isDescendantOf(Panel panel)
  {
    return panel == this;
  }

  @Override
  public Panel getClosestCommonAncestor(Panel panel)
  {
    return this;
  }

  @Override
  public void setCursor(Cursor cursor)
  {
    if(stage.getCursor() != cursor)
      stage.setCursor(cursor);
  }

  public Cursor getCursor()
  {
    return stage.getCursor();
  }

  public void addPanelNeedingLayout(Panel child)
  {
    synchronized(panelsNeedingLayout)
    {
      boolean shouldAdd = true;
      for(Iterator<Panel> iterator = panelsNeedingLayout.iterator(); iterator.hasNext();)
      {
        Panel panel = iterator.next();
        if(child == panel)
        {
          shouldAdd = false;
          break;
        }
        else if(child.isDescendantOf(panel) && child.getParent().needsLayout())
        {
          shouldAdd = false;
          break;
        }
        else if(panel.isDescendantOf(child) && panel.getParent().needsLayout())
        {
          iterator.remove();
        }
      }
      if(shouldAdd)
      {
        panelsNeedingLayout.add(child);
      }
    }
    Context.kickPainter();
  }

  public boolean hasPanelsNeedingLayout()
  {
    synchronized(panelsNeedingLayout)
    {
      return panelsNeedingLayout.size() > 0;
    }
  }

  public void getAndClearPanelsNeedingLayout(Collection<Panel> buffer)
  {
    synchronized(panelsNeedingLayout)
    {
      buffer.addAll(panelsNeedingLayout);
      panelsNeedingLayout.clear();
    }
  }

  public void addDirtyRegion(Rectangle region)
  {
    synchronized(dirtyRegions)
    {
      boolean shouldAdd = true;
      if(region.width <= 0 || region.height <= 0)
        shouldAdd = false;
      else
      {
        for(Iterator<Rectangle> iterator = dirtyRegions.iterator(); iterator.hasNext();)
        {
          Rectangle dirtyRegion = iterator.next();
          if(dirtyRegion.contains(region))
          {
            shouldAdd = false;
            break;
          }
          else if(region.intersects(dirtyRegion))
          {
            iterator.remove();
            region = region.union(dirtyRegion);
          }
        }
      }
      if(shouldAdd)
      {
        dirtyRegions.add(region);
      }
    }
    Context.kickPainter();
  }

  public boolean hasDirtyRegions()
  {
    synchronized(dirtyRegions)
    {
      return dirtyRegions.size() > 0;
    }
  }

  public void getAndClearDirtyRegions(Collection<Rectangle> buffer)
  {
    synchronized(dirtyRegions)
    {
      buffer.addAll(dirtyRegions);
      dirtyRegions.clear();
    }
  }

  public boolean dirtyRegionsContains(Rectangle region)
  {
    synchronized(dirtyRegions)
    {
      return dirtyRegions.contains(region);
    }
  }

  public ImageCache getImageCache()
  {
    if(imageCache == null)
    {
      PropProxy propProxy = getProp();
      ResourceLoader loader = propProxy.getLoader();
      imageCache = new ImageCache(loader);
    }
    return imageCache;
  }

  @Override
  public Stage getStage()
  {
    return stage;
  }

  public Map<String, RichStyle> getStylesStore()
  {
    return styles;
  }

  public void addToIndex(Prop prop)
  {
    Prop value = index.get(prop.getId());
    if(value != null && value != prop)
      throw new LimelightException("Duplicate id: " + prop.getId());
    index.put(prop.getId(), prop);
  }

  public void removeFromIndex(Prop prop)
  {
    index.remove(prop.getId());
  }

  public Prop find(String id)
  {
    return index.get(id);
  }

  public void setProduction(Production production)
  {
    this.production = production;
  }

  public Production getProduction()
  {
    return production;
  }

  public void setShouldAllowClose(boolean value)
  {
    shouldAllowClose = value;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  private static class SceneLayout implements Layout
  {
    public static Layout instance = new SceneLayout();

    public void doLayout(Panel panel)
    {
      Scene scene = (Scene)panel;
      Style style = scene.getStyle();
      final Stage stage = scene.stage;
      Insets insets = stage.getInsets();

      panel.setLocation(insets.left, insets.top);

      final int consumableWidth = stage.getWidth() - insets.left - insets.right;
      final int consumableHeight = stage.getHeight() - insets.top - insets.bottom;
      final int width = style.getCompiledWidth().calculateDimension(consumableWidth, style.getCompiledMinWidth(), style.getCompiledMaxWidth(), 0);
      final int height = style.getCompiledHeight().calculateDimension(consumableHeight, style.getCompiledMinHeight(), style.getCompiledMaxHeight(), 0);
      scene.setSize(width, height);

      PropPanelLayout.instance.doLayout(scene);
    }

    public boolean overides(Layout other)
    {
      return true;
    }

    public void doLayout(Panel panel, boolean topLevel)
    {
      doLayout(panel);
    }
  }
}