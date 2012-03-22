//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.FakePlayerRecruiter;
import limelight.model.api.FakePropProxy;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.painting.Border;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PropPanelLayoutTest
{
  private PropPanel parent;
  private PropPanelLayout layout;
  private int scrollGirth = ScrollBarPanel.GIRTH;
  private ScenePanel root;

  @Before
  public void setUp() throws Exception
  {
    root = new ScenePanel(new FakePropProxy(), new FakePlayerRecruiter()); // TODO - Should be able to use FakeScene here but tests fail. Figuring out why is non-trivial.
    MockStage frame = new MockStage();
    root.setStage(frame);
    parent = new PropPanel(new FakePropProxy());
    root.add(parent);
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");


    layout = PropPanelLayout.instance;
//    scrollGirth = new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().width;
  }

  @Test
  public void layoutWithOneFullSizedChild() throws Exception
  {
    PropPanel child = addChildWithSize(parent, "100", "100");

    layout.doLayout(parent);

    assertEquals(true, child.isLaidOut());
    assertEquals(new Point(0, 0), child.getLocation());
  }

  @Test
  public void layoutWithTwoChildrenSameRow() throws Exception
  {
    PropPanel child1 = addChildWithSize(parent, "50", "50");
    PropPanel child2 = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(true, child1.isLaidOut());
    assertEquals(true, child2.isLaidOut());
    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
  }

  @Test
  public void layoutWrappingToNewRow() throws Exception
  {
    PropPanel child1 = addChildWithSize(parent, "50", "50");
    PropPanel child2 = addChildWithSize(parent, "50", "50");
    PropPanel child3 = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  @Test
  public void layoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getStyle().setVerticalAlignment("bottom");
    parent.getStyle().setHorizontalAlignment("right");
    PropPanel child = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(true, child.isLaidOut());
    assertEquals(new Point(50, 50), child.getLocation());
  }

  @Test
  public void layoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getStyle().setVerticalAlignment("center");
    parent.getStyle().setHorizontalAlignment("center");
    PropPanel child1 = addChildWithSize(parent, "25", "50");
    PropPanel child2 = addChildWithSize(parent, "25", "50");

    layout.doLayout(parent);

    assertEquals(new Point(25, 25), child1.getLocation());
    assertEquals(new Point(50, 25), child2.getLocation());
  }

  @Test
  public void autoSize() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  @Test
  public void autoWidthOnly() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "100");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(100, panel.getHeight());
  }

  @Test
  public void autoHeightOnly() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "70%", "auto");
    addChildWithSize(panel, "50", "50");

    layout.doLayout(parent);

    assertEquals(70, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  @Test
  public void autoSizeIncludesMarginPaddingAndBorder() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setTopMargin("1");
    panel.getStyle().setTopBorderWidth("2");
    panel.getStyle().setTopPadding("3");
    panel.getStyle().setRightMargin("4");
    panel.getStyle().setRightBorderWidth("5");
    panel.getStyle().setRightPadding("6");
    panel.getStyle().setBottomMargin("7");
    panel.getStyle().setBottomBorderWidth("8");
    panel.getStyle().setBottomPadding("9");
    panel.getStyle().setLeftMargin("10");
    panel.getStyle().setLeftBorderWidth("11");
    panel.getStyle().setLeftPadding("12");
    MockProp child = new MockProp();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(98, panel.getWidth());
    assertEquals(80, panel.getHeight());
  }

  @Test
  public void autoSizeWithChildrenCentered() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setHorizontalAlignment("center");
    panel.getStyle().setVerticalAlignment("center");
    panel.getStyle().setMargin("10");
    MockProp child = new MockProp();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.doLayout(parent);

    assertEquals(10, child.getX());
    assertEquals(10, child.getY());
    assertEquals(70, panel.getWidth());
    assertEquals(70, panel.getHeight());
  }

  @Test
  public void autoSizingWithNoChildren() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");

    layout.doLayout(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  @Test
  public void autoSizeWithMinDimensions() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setMinWidth("20");
    panel.getStyle().setMinHeight("15");
    addChildWithSize(panel, "5", "5");

    layout.doLayout(parent);

    assertEquals(20, panel.getWidth());
    assertEquals(15, panel.getHeight());
  }

  @Test
  public void autoDimensionsWithMaxHeightAndWidth() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    panel.getStyle().setMaxWidth("75");
    panel.getStyle().setMaxHeight("86");
    addChildWithSize(panel, "90", "90");

    layout.doLayout(parent);

    assertEquals(75, panel.getWidth());
    assertEquals(86, panel.getHeight());
  }

  @Test
  public void floatersAreNoConsideredInDeterminingScrollMode() throws Exception
  {
    PropPanel child = addChildWithSize(parent, "50", "50");
    PropPanel floater = addChildWithSize(parent, "500", "500");
    floater.getStyle().setFloat("on");

    layout.doLayout(parent);

    assertEquals(2, parent.getChildren().size());
    assertSame(child, parent.getChildren().get(0));
    assertSame(floater, parent.getChildren().get(1));
  }

  @Test
  public void floatersDoNotInfluenceAutoSize() throws Exception
  {

    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    addChildWithSize(panel, "50", "50");
    PropPanel floater = addChildWithSize(panel, "500", "500");
    floater.getStyle().setFloat("on");

    layout.doLayout(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  @Test
  public void floatersAreLaidOut() throws Exception
  {
    PropPanel floater = addChildWithSize(parent, "50", "50");
    floater.getStyle().setFloat(true);
    PropPanel child = addChildWithSize(parent, "50", "50");

    layout.doLayout(parent);

    assertEquals(true, floater.isLaidOut());
    assertEquals(true, child.isLaidOut());
    assertEquals(new Point(0, 0), child.getLocation());
  }

  @Test
  public void addingScrollBars() throws Exception
  {
    assertEquals(0, parent.getChildren().size());
    assertEquals(null, parent.getVerticalScrollbar());
    assertEquals(null, parent.getHorizontalScrollbar());
    parent.getStyle().setScrollbars("on");

    layout.establishScrollBars(parent);

    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollbar());
    assertNotNull(parent.getHorizontalScrollbar());
  }

  @Test
  public void removingScrollBars() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.establishScrollBars(parent);
    assertEquals(2, parent.getChildren().size());
    assertNotNull(parent.getVerticalScrollbar());
    assertNotNull(parent.getHorizontalScrollbar());

    parent.getStyle().setScrollbars("off");
    layout.establishScrollBars(parent);
    assertEquals(0, parent.getChildren().size());
    assertNull(parent.getVerticalScrollbar());
    assertNull(parent.getHorizontalScrollbar());
  }

  @Test
  public void scrollBarLayout() throws Exception
  {
    parent.getStyle().setScrollbars("on");

    layout.doLayout(parent);

    ScrollBarPanel verticalScrollBar = parent.getVerticalScrollbar();
    assertEquals(100 - scrollGirth, verticalScrollBar.getX());
    assertEquals(0, verticalScrollBar.getY());

    ScrollBarPanel horizontalScrollBar = parent.getHorizontalScrollbar();
    assertEquals(0, horizontalScrollBar.getX());
    assertEquals(100 - scrollGirth, horizontalScrollBar.getY());
  }

  @Test
  public void autoSizingWithNoChildrenAndScrollBars() throws Exception
  {
    PropPanel panel = new PropPanel(new FakePropProxy());
    parent.add(panel);
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setScrollbars("on");

    layout.doLayout(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  @Test
  public void scrollBarsDontGetLayoutLikeOtherProps() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.doLayout(parent);

    PropPanel panel = addChildWithSize(parent, "100%", "100%");
    layout.doLayout(parent);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  @Test
  public void scrollAdjusting() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    layout.doLayout(parent);

    addChildWithSize(parent, "200", "300");
    layout.doLayout(parent);

    assertEquals(200, parent.getHorizontalScrollbar().getAvailableAmount());
    assertEquals(100 - scrollGirth, parent.getHorizontalScrollbar().getVisibleAmount());
  }

  @Test
  public void layoutRowsWithScrollOffsets() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    PropPanel panel = addChildWithSize(parent, "200", "300");
    layout.doLayout(parent);

    parent.getVerticalScrollbar().setValue(100);
    parent.getHorizontalScrollbar().setValue(50);
    layout.doLayout(parent);

    assertEquals(-50, panel.getX());
    assertEquals(-100, panel.getY());
  }

  @Test
  public void borderGetUpdatedOnLayout() throws Exception
  {
    parent.setSize(100, 100);
    Border border = parent.getBorderShaper();

    parent.getStyle().setBorderWidth("21");
    layout.doLayout(parent);

    assertEquals(21, border.getTopWidth());
  }

  @Test
  public void panelKnownWhenItsLaidOut() throws Exception
  {
    assertEquals(false, parent.isLaidOut());

    layout.doLayout(parent);

    assertEquals(true, parent.isLaidOut());
  }

  @Test
  public void panelMarkedAsDirty() throws Exception
  {
    root.getAndClearDirtyRegions(new ArrayList<Rectangle>());

    layout.doLayout(parent);

    assertEquals(true, root.dirtyRegionsContains(parent.getAbsoluteBounds()));
  }

  @Test
  public void shouldOverideEverything() throws Exception
  {
    assertEquals(true, layout.overides(null));
  }

  @Test
  public void layoutChildrenWithPercentageDimensionsWhenSizeChanges() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "100%", "100%");
    layout.doLayout(parent);

    parent.getStyle().setWidth(200);
    parent.getStyle().setHeight(300);
    layout.doLayout(parent);

    assertEquals(200, panel.getWidth());
    assertEquals(300, panel.getHeight());
  }

  @Test
  public void layoutChildrenWithAutoDimensionsWhenSizeChanges() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "auto", "auto");
    PropPanel child = addChildWithSize(panel, "100%", "100%");
    layout.doLayout(parent);

    parent.getStyle().setWidth(200);
    parent.getStyle().setHeight(300);
    layout.doLayout(parent);

    assertEquals(200, panel.getWidth());
    assertEquals(300, panel.getHeight());
    assertEquals(200, child.getWidth());
    assertEquals(300, child.getHeight());
  }

  private PropPanel addChildWithSize(ParentPanelBase parent, String width, String height)
  {
    PropPanel panel = new PropPanel(new FakePropProxy());
    panel.getStyle().setWidth(width);
    panel.getStyle().setHeight(height);
    parent.add(panel);
    return panel;
  }

  @Test
  public void shouldVerticallyAlignChildrenWithinARow() throws Exception
  {
    parent.getStyle().setVerticalAlignment("bottom");
    PropPanel panel1 = addChildWithSize(parent, "50%", "100%");
    PropPanel panel2 = addChildWithSize(parent, "50%", "50%");

    layout.doLayout(parent);

    assertEquals(0, panel1.getY());
    assertEquals(50, panel2.getY());
  }

  @Test
  public void rowWithOneGreedyProp() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "20", "100");
    PropPanel panel2 = addChildWithSize(parent, "greedy", "100");

    layout.doLayout(parent);

    assertEquals(20, panel1.getWidth());
    assertEquals(80, panel2.getWidth());
  }

  @Test
  public void rowWithTwoGreedyProp() throws Exception
  {
    PropPanel inert = addChildWithSize(parent, "20", "100");
    PropPanel greedy1 = addChildWithSize(parent, "greedy", "100");
    PropPanel greedy2 = addChildWithSize(parent, "greedy", "100");

    layout.doLayout(parent);

    assertEquals(20, inert.getWidth());
    assertEquals(40, greedy1.getWidth());
    assertEquals(40, greedy2.getWidth());
  }

  @Test
  public void onePropWithGreedyHeight() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "100", "20");
    PropPanel panel2 = addChildWithSize(parent, "100", "greedy");

    layout.doLayout(parent);

    assertEquals(20, panel1.getHeight());
    assertEquals(80, panel2.getHeight());
  }

  // MDM - An example where greedy doesn't work... but ought to.

//  @Test
//  public void onePropWithGreedyHeightWithAutoSibling() throws Exception
//  {
//    PropPanel panel1 = addChildWithSize(parent, "100", "auto");
//    PropPanel panel2 = addChildWithSize(parent, "100", "greedy");
//
//    layout.doLayout(parent);
//
//    assertEquals(0, panel1.getHeight());
//    assertEquals(100, panel2.getHeight());
//  }

  @Test
  public void greedyAdjacentToLargeNonGreedy() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "50", "100%");
    PropPanel panel2 = addChildWithSize(parent, "50", "greedy");

    layout.doLayout(parent);

    assertEquals(100, panel1.getHeight());
    assertEquals(100, panel2.getHeight());
  }

  @Test
  public void twoPropsWithGreedyHeightInDifferentRows() throws Exception
  {
    PropPanel panel1 = addChildWithSize(parent, "100", "20");
    PropPanel greedy1 = addChildWithSize(parent, "100", "greedy");
    PropPanel greedy2 = addChildWithSize(parent, "100", "greedy");

    layout.doLayout(parent);

    assertEquals(20, panel1.getHeight());
    assertEquals(40, greedy1.getHeight());
    assertEquals(40, greedy2.getHeight());
  }

  @Test
  public void greedyWidthWithAutoHeight() throws Exception
  {
    PropPanel greedy1 = addChildWithSize(parent, "greedy", "auto");
    PropPanel child = addChildWithSize(greedy1, "100%", "100%");

    layout.doLayout(parent);

    assertEquals(100, child.getWidth());
    assertEquals(100, child.getHeight());
  }

  @Test
  public void autoWidthWithGreedyHeight() throws Exception
  {
    addChildWithSize(parent, "auto", "greedy");

    try
    {
      layout.doLayout(parent);
      fail("should throw exception");
    }
    catch(Exception e)
    {
      assertEquals("A greedy height is not allowed with auto width.", e.getMessage());
    }
  }

  @Test
  public void autoHeightIsCollapsedWhenContainingChildren() throws Exception
  {
    PropPanel panel = addChildWithSize(parent, "100%", "auto");
    PropPanel child = addChildWithSize(panel, "100%", "auto");
    addChildWithSize(child, "100%", "14");

    layout.doLayout(parent);

    assertEquals(100, panel.getWidth());
    assertEquals(14, panel.getHeight());
  }

  @Test
  public void preferredSize() throws Exception
  {
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("200");

    layout.snapToSize(parent, false);

    assertEquals(100, parent.getWidth());
    assertEquals(200, parent.getHeight());
  }

  @Test
  public void sizeUsingAutoWidthAndHeight() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent, false);
    assertEquals(100, parent.getWidth());
    assertEquals(100, parent.getHeight());

    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("50");
    layout.snapToSize(parent, false);
    assertEquals(100, parent.getWidth());
    assertEquals(50, parent.getHeight());

    parent.getStyle().setWidth("42%");
    parent.getStyle().setHeight("auto");
    layout.snapToSize(parent, false);
    assertEquals(42, parent.getWidth());
    assertEquals(100, parent.getHeight());
  }

  @Test
  public void snapToSizeWithMaxSizeAgainstAutoSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("auto");
    parent.getStyle().setHeight("auto");
    parent.getStyle().setMaxWidth("75");
    parent.getStyle().setMaxHeight("82");

    layout.snapToSize(parent, false);

    assertEquals(75, parent.getWidth());
    assertEquals(82, parent.getHeight());
  }

  @Test
  public void snapToSizeWithMaxSizeAgainstPercentageSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("90%");
    parent.getStyle().setHeight("90%");
    parent.getStyle().setMaxWidth("75");
    parent.getStyle().setMaxHeight("82");

    layout.snapToSize(parent, false);

    assertEquals(75, parent.getWidth());
    assertEquals(82, parent.getHeight());
  }

  @Test
  public void snapToSizeWithMinSizeAgainstPercentageSizing() throws Exception
  {
    root.setSize(100, 100);
    parent.getStyle().setWidth("20%");
    parent.getStyle().setHeight("20%");
    parent.getStyle().setMinWidth("42");
    parent.getStyle().setMinHeight("51");

    layout.snapToSize(parent, false);

    assertEquals(42, parent.getWidth());
    assertEquals(51, parent.getHeight());
  }

  @Test
  public void scrollContentThatIsntAlignedTopLeft() throws Exception
  {
    parent.getStyle().setScrollbars("on");
    parent.getStyle().setAlignment("center");
    PropPanel panel = addChildWithSize(parent, "200", "200");

    layout.doLayout(parent);

    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }
}
