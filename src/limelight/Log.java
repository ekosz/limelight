//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.*;

public class Log
{
  public static final Level DEBUG = new CustomLogLevel("DEBUG", Level.CONFIG.intValue() - 50);
  public static final Level defaultLevel = Level.WARNING;
  public static final Handler stderrHandler = new ConsoleHandler();
  public static FileHandler fileHandler;
  public static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getParent();

  static {
    stderrHandler.setFormatter(new LimelightFormatter());
    silence();
    setLevel(defaultLevel);
    stderrOn();
  }

  private static class LimelightFormatter extends Formatter
  {
    @Override
    public String format(LogRecord logRecord)
    {
      return String.format("%1$7s %2$tH:%2$tM:%2$tS:%2$tL %3$s: %4$s\n",
        logRecord.getLevel(),
        logRecord.getMillis(),
        logRecord.getLoggerName(),
        logRecord.getMessage());
    }
  }

  public static void silence()
  {
    for(Handler h : logger.getHandlers())
      logger.removeHandler(h);
  }

  public static void setLogFile(String filePath)
  {
    try
    {
      fileHandler = new FileHandler(filePath);
      fileHandler.setFormatter(new LimelightFormatter());
      fileHandler.setLevel(logger.getLevel());
      logger.addHandler(fileHandler);
      logger.removeHandler(stderrHandler);
      Log.info("Logging to file: " + filePath);
    }
    catch(IOException e)
    {
      throw new LimelightException("Failed to set log file: " + filePath, e);
    }
  }

  public static void stderrOn()
  {
    stderrHandler.setLevel(logger.getLevel());
    logger.addHandler(stderrHandler);
  }

  public static void setLevel(Level level)
  {
    logger.setLevel(level);
    for(Handler handler : logger.getHandlers())
      handler.setLevel(level);
    info("Log - level set to: " + level.getName());
  }

  public static void setLevel(String levelName)
  {
    if(levelName == null)
      setLevel(Level.OFF);
    else if("DEBUG".equals(levelName.toUpperCase()))
      setLevel(DEBUG);
    else
      setLevel(Level.parse(levelName.toUpperCase()));
  }

  public static void off()
  {
    setLevel(Level.OFF);
  }

  public static void infoOn()
  {
    setLevel(Level.INFO);
  }

  public static void warnOn()
  {
    setLevel(Level.WARNING);
  }

  public static void debugOn()
  {
    setLevel(DEBUG);
  }

  public static String getLevelName()
  {
    return logger.getLevel().getName();
  }

  public static void severe(String message)
  {
    logger.severe(message);
  }

  public static void severe(String message, Throwable e)
  {
    logger.log(Level.SEVERE, message, e);
    logError(Level.SEVERE, e);
  }

  public static void warn(String message)
  {
    logger.warning(message);
  }

  public static void warn(String message, Throwable e)
  {
    logger.log(Level.WARNING, message);
    logError(Level.WARNING, e);
  }

  private static void logError(Level level, Throwable e)
  {
    final ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
    final PrintStream buffer = new PrintStream(byteBuffer);
    e.printStackTrace(buffer);
    buffer.close();
    logger.log(level, byteBuffer.toString());
  }

  public static void info(String message)
  {
    logger.info(message);
  }

  public static void config(String message)
  {
    logger.config(message);
  }

  public static void debug(String message)
  {
    logger.log(DEBUG, message);
  }

  public static void debug(String message, Throwable e)
  {
    logger.log(DEBUG, message);
    logError(DEBUG, e);
  }

  public static void fine(String message)
  {
    logger.fine(message);
  }

  public static void finer(String message)
  {
    logger.finer(message);
  }

  public static void finest(String message)
  {
    logger.finest(message);
  }

  private static class CustomLogLevel extends Level
  {
    public CustomLogLevel(String name, int value)
    {
      super(name, value);
    }
  }
}
