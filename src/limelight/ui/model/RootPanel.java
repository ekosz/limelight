//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RootPanel implements Panel
{
  private Panel panel;
  private Container contentPane;
  private EventListener listener;
  private boolean alive;
  private Frame frame;
  private HashSet<Panel> changedPanels = new HashSet<Panel>();
  private ArrayList<Panel> panelsToUpdateBuffer = new ArrayList<Panel>(50);
  private ArrayList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private ArrayList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);

  public RootPanel(Frame frame)
  {
    this.frame = frame;
    contentPane = frame.getContentPane();
  }

  public Container getContentPane()
  {
    return contentPane;
  }

  public Box getChildConsumableArea()
  {
    return new Box(getX(), getY(), contentPane.getWidth(), contentPane.getHeight());
  }

  public Box getBoxInsidePadding()
  {
    return getChildConsumableArea();
  }

  public void doLayout()
  {
    panel.doLayout();  
  }

  public int getWidth()
  {
    return contentPane.getWidth();
  }

  public int getHeight()
  {
    return contentPane.getHeight();
  }

  public void setLocation(int x, int y)
  {
    contentPane.setLocation(x, y);
  }

  public Point getLocation()
  {
    return contentPane.getLocation();
  }

  public void setSize(int width, int height)
  {
    contentPane.setSize(width, height);
  }

  public Box getAbsoluteBounds()
  {
    Rectangle bounds = contentPane.getBounds();
    return new Box(0, 0, bounds.width, bounds.height);
  }

  public Point getAbsoluteLocation()
  {
    return new Point(0, 0);
  }

  public int getX()
  {
    return getAbsoluteLocation().x;
  }

  public int getY()
  {
    return getAbsoluteLocation().y;
  }

  public void setPanel(Panel child)
  {
    panel = child;
    panel.setParent(this);

    listener = new EventListener(panel);
    contentPane.addMouseListener(listener);
    contentPane.addMouseMotionListener(listener);
    contentPane.addMouseWheelListener(listener);
    contentPane.addKeyListener(listener);
    alive = true;
  }

  public void destroy()
  {
    removeKeyboardFocus();
    contentPane.removeMouseListener(listener);
    contentPane.removeMouseMotionListener(listener);
    contentPane.removeMouseWheelListener(listener);
    contentPane.removeKeyListener(listener);
    listener = null;
    panel.setParent(null);
    changedPanels.clear();
    alive = false;
  }

  private void removeKeyboardFocus()
  {
    Panel focuedPanel = Context.instance().keyboardFocusManager.getFocusedPanel();
    if (focuedPanel != null && focuedPanel.getRoot() == this)
      Context.instance().keyboardFocusManager.unfocusCurrentlyFocusedComponent();
  }

  public RootPanel getRoot()
  {
    return this;
  }

  public Graphics2D getGraphics()
  {
    return (Graphics2D) contentPane.getGraphics();
  }

  public boolean isAncestor(Panel panel)
  {
    return panel == this;
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    return this;
  }

  public void repaint()
  {
    doLayout();
    PaintJob job = new PaintJob(getAbsoluteBounds());
    job.paint(panel);
    job.applyTo(getGraphics());
  }

  public void setCursor(Cursor cursor)
  {
    contentPane.setCursor(cursor);
  }

  public Panel getPanel()
  {
    return panel;
  }

  public boolean isAlive()
  {
    return alive;
  }

  public EventListener getListener()
  {
    return listener;
  }

  public Iterator<Panel> iterator()
  {
    return panel.iterator();
  }

  public synchronized void addPanelNeedingLayout(Panel child)
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
      else if(child.isAncestor(panel))
      {
        shouldAdd = false;
        break;
      }
      else if(panel.isAncestor(child))
      {
        iterator.remove();
      }
    }
    if(shouldAdd)
      panelsNeedingLayout.add(child);
  }

  public synchronized void getAndClearPanelsNeedingLayout(ArrayList<Panel> buffer)
  {
    buffer.addAll(panelsNeedingLayout);
    panelsNeedingLayout.clear();
  }

  public boolean panelsNeedingUpdateContains(Panel panel)
  {
    return panelsNeedingLayout.contains(panel);
  }

  public synchronized void addDirtyRegion(Rectangle region)
  {
    boolean shouldAdd = true;
    for(Iterator<Rectangle> iterator = dirtyRegions.iterator(); iterator.hasNext();)
    {
      Rectangle dirtyRegion = iterator.next();
      if(dirtyRegion.contains(region))
      {
        shouldAdd = false;
        break;
      }
      else if(region.contains(dirtyRegion))
        iterator.remove();
    }
    if(shouldAdd)
      dirtyRegions.add(region);
  }

  public synchronized void getAndClearDirtyRegions(ArrayList<Rectangle> buffer)
  {
    buffer.addAll(dirtyRegions);
    dirtyRegions.clear();
  }

  public boolean dirtyRegionsContains(Rectangle region)
  {
    return dirtyRegions.contains(region);
  }

  /////////////////////////////////////////////
  /// NOT NEEDED
  /////////////////////////////////////////////

  public void paintOn(Graphics2D graphics)
  {
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public boolean hasChildren()
  {
    return true;
  }

  public java.util.List<Panel> getChildren()
  {
    LinkedList<Panel> panels = new LinkedList<Panel>();
    panels.add(panel);
    return panels;
  }

  public void replace(Panel child, Panel newChild)
  {
  }

  public boolean remove(Panel child)
  {
    return false;
  }

  public void removeAll()
  {
  }

  public boolean containsAbsolutePoint(Point point)
  {
    return false;
  }

  public void setParent(Panel panel)
  {
  }

  public void sterilize()
  {
  }

  public boolean isSterilized()
  {
    return false;
  }

  public Panel getParent()
  {
    return null;
  }

  public Style getStyle()
  {
    throw new RuntimeException("RootPanel.getStyle()");
  }

  public boolean isFloater()
  {
    return false;
  }

  public boolean containsRelativePoint(Point point)
  {
    return true;
  }

  public Panel getOwnerOfPoint(Point point)
  {
//    System.err.println("RootPanel.getOwnerOfPoint()");
//    throw new RuntimeException("RootPanel.getOwnerOfPoint()");
    point = new Point(point.x - getX(), point.y - getY());
    if (panel.containsRelativePoint(point))
      return panel.getOwnerOfPoint(point);
    return this;
  }

  public void clearCache()
  {
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseMoved(MouseEvent e)
  {
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
  }

  public void focusGained(FocusEvent e)
  {
  }

  public void focusLost(FocusEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void keyPressed(KeyEvent e)
  {
  }

  public void keyReleased(KeyEvent e)
  {
  }

  public void buttonPressed(ActionEvent e)
  {
  }

  public void valueChanged(Object e)
  {
  }

  public boolean needsLayout()
  {
    return false;
  }

  public void setNeedsLayout()
  {
  }

  public void add(Panel child)
  {
  }

  public Frame getFrame()
  {
    return frame;
  }
}
