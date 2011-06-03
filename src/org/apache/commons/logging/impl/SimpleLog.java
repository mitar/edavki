package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

public class SimpleLog
  implements Log, Serializable
{
  protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
  protected static final Properties simpleLogProps = new Properties();
  protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
  protected static boolean showLogName = false;
  protected static boolean showShortName = true;
  protected static boolean showDateTime = false;
  protected static String dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
  protected static DateFormat dateFormatter = null;
  public static final int LOG_LEVEL_TRACE = 1;
  public static final int LOG_LEVEL_DEBUG = 2;
  public static final int LOG_LEVEL_INFO = 3;
  public static final int LOG_LEVEL_WARN = 4;
  public static final int LOG_LEVEL_ERROR = 5;
  public static final int LOG_LEVEL_FATAL = 6;
  public static final int LOG_LEVEL_ALL = 0;
  public static final int LOG_LEVEL_OFF = 7;
  protected String logName = null;
  protected int currentLogLevel;
  private String shortLogName = null;

  private static String getStringProperty(String paramString)
  {
    String str = null;
    try
    {
      str = System.getProperty(paramString);
    }
    catch (SecurityException localSecurityException)
    {
    }
    if (str == null)
      return simpleLogProps.getProperty(paramString);
    return str;
  }

  private static String getStringProperty(String paramString1, String paramString2)
  {
    String str;
    if ((str = getStringProperty(paramString1)) == null)
      return paramString2;
    return str;
  }

  private static boolean getBooleanProperty(String paramString, boolean paramBoolean)
  {
    String str;
    if ((str = getStringProperty(paramString)) == null)
      return paramBoolean;
    return "true".equalsIgnoreCase(str);
  }

  public SimpleLog(String paramString)
  {
    this.logName = paramString;
    setLevel(3);
    String str = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
    int i = String.valueOf(paramString).lastIndexOf(".");
    paramString = paramString.substring(0, i);
    new StringBuffer();
    if (null == str)
      str = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
    if ("off".equalsIgnoreCase(str))
      setLevel("fatal".equalsIgnoreCase(str) ? 6 : "error".equalsIgnoreCase(str) ? 5 : "warn".equalsIgnoreCase(str) ? 4 : "info".equalsIgnoreCase(str) ? 3 : "debug".equalsIgnoreCase(str) ? 2 : "trace".equalsIgnoreCase(str) ? 1 : "all".equalsIgnoreCase(str) ? 0 : 7);
  }

  public void setLevel(int paramInt)
  {
    this.currentLogLevel = paramInt;
  }

  public int getLevel()
  {
    return this.currentLogLevel;
  }

  protected void log(int paramInt, Object paramObject, Throwable paramThrowable)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (showDateTime)
    {
      localStringBuffer.append(dateFormatter.format(new Date()));
      localStringBuffer.append(" ");
    }
    switch (paramInt)
    {
    case 1:
      localStringBuffer.append("[TRACE] ");
      break;
    case 2:
      localStringBuffer.append("[DEBUG] ");
      break;
    case 3:
      localStringBuffer.append("[INFO] ");
      break;
    case 4:
      localStringBuffer.append("[WARN] ");
      break;
    case 5:
      localStringBuffer.append("[ERROR] ");
      break;
    case 6:
      localStringBuffer.append("[FATAL] ");
    }
    if (showShortName)
    {
      if (this.shortLogName == null)
      {
        this.shortLogName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
        this.shortLogName = this.shortLogName.substring(this.shortLogName.lastIndexOf("/") + 1);
      }
      localStringBuffer.append(String.valueOf(this.shortLogName)).append(" - ");
    }
    else if (showLogName)
    {
      localStringBuffer.append(String.valueOf(this.logName)).append(" - ");
    }
    localStringBuffer.append(String.valueOf(paramObject));
    if (paramThrowable != null)
    {
      localStringBuffer.append(" <");
      localStringBuffer.append(paramThrowable.toString());
      localStringBuffer.append(">");
      StringWriter localStringWriter = new StringWriter(1024);
      PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
      paramThrowable.printStackTrace(localPrintWriter);
      localPrintWriter.close();
      localStringBuffer.append(localStringWriter.toString());
    }
    write(localStringBuffer);
  }

  protected void write(StringBuffer paramStringBuffer)
  {
    System.err.println(paramStringBuffer.toString());
  }

  protected boolean isLevelEnabled(int paramInt)
  {
    return paramInt >= this.currentLogLevel;
  }

  public final void debug(Object paramObject)
  {
    if (isLevelEnabled(2))
      log(2, paramObject, null);
  }

  public final void debug(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(2))
      log(2, paramObject, paramThrowable);
  }

  public final void trace(Object paramObject)
  {
    if (isLevelEnabled(1))
      log(1, paramObject, null);
  }

  public final void trace(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(1))
      log(1, paramObject, paramThrowable);
  }

  public final void info(Object paramObject)
  {
    if (isLevelEnabled(3))
      log(3, paramObject, null);
  }

  public final void info(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(3))
      log(3, paramObject, paramThrowable);
  }

  public final void warn(Object paramObject)
  {
    if (isLevelEnabled(4))
      log(4, paramObject, null);
  }

  public final void warn(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(4))
      log(4, paramObject, paramThrowable);
  }

  public final void error(Object paramObject)
  {
    if (isLevelEnabled(5))
      log(5, paramObject, null);
  }

  public final void error(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(5))
      log(5, paramObject, paramThrowable);
  }

  public final void fatal(Object paramObject)
  {
    if (isLevelEnabled(6))
      log(6, paramObject, null);
  }

  public final void fatal(Object paramObject, Throwable paramThrowable)
  {
    if (isLevelEnabled(6))
      log(6, paramObject, paramThrowable);
  }

  public final boolean isDebugEnabled()
  {
    return isLevelEnabled(2);
  }

  public final boolean isErrorEnabled()
  {
    return isLevelEnabled(5);
  }

  public final boolean isFatalEnabled()
  {
    return isLevelEnabled(6);
  }

  public final boolean isInfoEnabled()
  {
    return isLevelEnabled(3);
  }

  public final boolean isTraceEnabled()
  {
    return isLevelEnabled(1);
  }

  public final boolean isWarnEnabled()
  {
    return isLevelEnabled(4);
  }

  private static ClassLoader getContextClassLoader()
  {
    ClassLoader localClassLoader = null;
    try
    {
      Method localMethod = Thread.class.getMethod("getContextClassLoader", null);
      InvocationTargetException localInvocationTargetException1;
      try
      {
        localClassLoader = (ClassLoader)localMethod.invoke(Thread.currentThread(), null);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
      }
      catch (InvocationTargetException localInvocationTargetException2)
      {
        if (!((localInvocationTargetException1 = localInvocationTargetException2).getTargetException() instanceof SecurityException))
          break label63;
      }
      break label81;
      label63: throw new LogConfigurationException("Unexpected InvocationTargetException", localInvocationTargetException1.getTargetException());
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
    }
    label81: if (localClassLoader == null)
      localClassLoader = SimpleLog.class.getClassLoader();
    return localClassLoader;
  }

  private static InputStream getResourceAsStream(String paramString)
  {
    return (InputStream)AccessController.doPrivileged(new PrivilegedAction(paramString)
    {
      private final String val$name;

      public Object run()
      {
        ClassLoader localClassLoader;
        if ((localClassLoader = SimpleLog.access$000()) != null)
          return localClassLoader.getResourceAsStream(this.val$name);
        return ClassLoader.getSystemResourceAsStream(this.val$name);
      }
    });
  }

  static
  {
    InputStream localInputStream = getResourceAsStream("simplelog.properties");
    if (null != localInputStream)
      try
      {
        simpleLogProps.load(localInputStream);
        localInputStream.close();
      }
      catch (IOException localIOException)
      {
      }
    showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
    showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
    showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
    if (showDateTime)
    {
      dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
      try
      {
        dateFormatter = new SimpleDateFormat(dateTimeFormat);
        return;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
        dateFormatter = new SimpleDateFormat(dateTimeFormat);
      }
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.commons.logging.impl.SimpleLog
 * JD-Core Version:    0.6.0
 */