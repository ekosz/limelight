package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.util.Aligner;

public class HorizontalAlignmentAttributeCompilerTest extends TestCase
{
    private HorizontalAlignmentAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new HorizontalAlignmentAttributeCompiler();
    compiler.setName("horizontal alignment");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(Aligner.LEFT, ((SimpleHorizontalAlignmentAttribute) compiler.compile("left")).getAlignment());
    assertEquals(Aligner.RIGHT, ((SimpleHorizontalAlignmentAttribute) compiler.compile("right")).getAlignment());
    assertEquals(Aligner.HORIZONTAL_CENTER, ((SimpleHorizontalAlignmentAttribute) compiler.compile("center")).getAlignment());
    assertEquals(Aligner.HORIZONTAL_CENTER, ((SimpleHorizontalAlignmentAttribute) compiler.compile("middle")).getAlignment());
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
      assertEquals("Invalid value '" + value + "' for horizontal alignment style attribute.", e.getMessage());
    }
  }
}
