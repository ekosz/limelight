package limelight.task;

import junit.framework.TestCase;

public class RecurringTaskTest extends TestCase
{
  private TestableRecurringTask task;

  private class TestableRecurringTask extends RecurringTask
  {
    public boolean performed;
    public int performances;

    public TestableRecurringTask(int delayInMillis)
    {
      super(delayInMillis);
    }

    protected void doPerform()
    {
      performed = true;
      performances++;
    }
  }

  public void setUp() throws Exception
  {
    task = new TestableRecurringTask(100);
  }
  
  public void testDelay() throws Exception
  {
    assertEquals(10000000, task.getDelayNano());

    task.setPerformancesPerSecond(10);

    assertEquals(100000000, task.getDelayNano());
  }
  
  public void testIsReady() throws Exception
  {
    assertEquals(false, task.isReady());

    Thread.sleep(20);

    assertEquals(true, task.isReady());

    task.perform();

    assertEquals(false, task.isReady());
  }
  
  public void testLastExecutionTime() throws Exception
  {
    task.perform();

    assertEquals("too long: " + task.nanosSinceLastPerformance(), true, task.nanosSinceLastPerformance() < 100000000);
  }

  public void testCallsDoPerform() throws Exception
  {
    task.perform();

    assertEquals(true, task.performed);
  }
  
  public void testNonStrictDoesntMakeUpMissedPerformances() throws Exception
  {
    task.perform();

    Thread.sleep(100);

    task.perform();

    assertEquals(2, task.performances);
  }
  
  public void testStrictIsOffByDefault() throws Exception
  {
    assertEquals(false, task.isStrict());
  }
  
  public void testMissedPerformancesAreMadeupWithStrictness() throws Exception
  {
    task.setStrict(true);
    task.perform();

    Thread.sleep(25);
    task.perform();

    assertEquals(3, task.performances);
  }
  
  public void testMaximumMakeupPerformancesIs5() throws Exception
  {
    task.setStrict(true);
    task.perform();

    Thread.sleep(100);
    task.perform();

    assertEquals(7, task.performances);
  }
}
