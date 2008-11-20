package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.util.Aligner;

public class VerticalAlignmentAttributeCompilerTest extends TestCase
{
    private VerticalAlignmentAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new VerticalAlignmentAttributeCompiler();
    compiler.setName("Vertical alignment");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(Aligner.TOP, ((SimpleVerticalAlignmentAttribute) compiler.compile("top")).getAlignment());
    assertEquals(Aligner.BOTTOM, ((SimpleVerticalAlignmentAttribute) compiler.compile("bottom")).getAlignment());
    assertEquals(Aligner.VERTICAL_CENTER, ((SimpleVerticalAlignmentAttribute) compiler.compile("center")).getAlignment());
    assertEquals(Aligner.VERTICAL_CENTER, ((SimpleVerticalAlignmentAttribute) compiler.compile("middle")).getAlignment());
  }

  public void testInvalidValue() throws Exception
  {
    checkForError("blah");
  }

  private void checkForError(String value)
  {
    try
    {
      compiler.compile(value);
      fail("should have throw error");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value '" + value + "' for Vertical alignment style attribute.", e.getMessage());
    }
  }
}
