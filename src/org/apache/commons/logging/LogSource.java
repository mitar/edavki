package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Set;
import org.apache.commons.logging.impl.NoOpLog;

public class LogSource
{
  protected static Hashtable logs = new Hashtable();
  protected static boolean log4jIsAvailable = false;
  protected static boolean jdk14IsAvailable = false;
  protected static Constructor logImplctor = null;

  public static void setLogImplementation(String paramString)
    throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException, ClassNotFoundException
  {
    try
    {
      Class localClass = Class.forName(paramString);
      Class[] arrayOfClass;
      (arrayOfClass = new Class[1])[0] = "".getClass();
      logImplctor = localClass.getConstructor(arrayOfClass);
      return;
    }
    catch (Throwable localThrowable)
    {
      logImplctor = null;
    }
  }

  public static void setLogImplementation(Class paramClass)
    throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException
  {
    Class[] arrayOfClass;
    (arrayOfClass = new Class[1])[0] = "".getClass();
    logImplctor = paramClass.getConstructor(arrayOfClass);
  }

  public static Log getInstance(String paramString)
  {
    Log localLog = (Log)logs.get(paramString);
    if (null == localLog)
    {
      localLog = makeNewLogInstance(paramString);
      logs.put(paramString, localLog);
    }
    return localLog;
  }

  public static Log getInstance(Class paramClass)
  {
    return getInstance(paramClass.getName());
  }

  public static Log makeNewLogInstance(String paramString)
  {
    Object localObject = null;
    try
    {
      Object[] arrayOfObject;
      (arrayOfObject = new Object[1])[0] = paramString;
      localObject = (Log)logImplctor.newInstance(arrayOfObject);
    }
    catch (Throwable localThrowable)
    {
      localObject = null;
    }
    if (null == localObject)
      localObject = new NoOpLog(paramString);
    return (Log)localObject;
  }

  public static String[] getLogNames()
  {
    return (String[])logs.keySet().toArray(new String[logs.size()]);
  }

  static
  {
    try
    {
      if (null != Class.forName("org.apache.log4j.Logger"))
        log4jIsAvailable = true;
      else
        log4jIsAvailable = false;
    }
    catch (Throwable localThrowable1)
    {
      log4jIsAvailable = false;
    }
    try
    {
      if ((null != Class.forName("java.util.logging.Logger")) && (null != Class.forName("org.apache.commons.logging.impl.Jdk14Logger")))
        jdk14IsAvailable = true;
      else
        jdk14IsAvailable = false;
    }
    catch (Throwable localThrowable2)
    {
      jdk14IsAvailable = false;
    }
    String str = null;
    try
    {
      if ((str = System.getProperty("org.apache.commons.logging.log")) == null)
        str = System.getProperty("org.apache.commons.logging.Log");
    }
    catch (Throwable localThrowable3)
    {
    }
    if (str != null)
      try
      {
        setLogImplementation(str);
      }
      catch (Throwable localThrowable4)
      {
        try
        {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        }
        catch (Throwable localThrowable5)
        {
        }
      }
    else
      try
      {
        if (log4jIsAvailable)
        {
          setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
        }
        else if (jdk14IsAvailable)
        {
          setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
        }
        else
        {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        }
      }
      catch (Throwable localThrowable6)
      {
        try
        {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
        }
        catch (Throwable localThrowable7)
        {
        }
      }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.commons.logging.LogSource
 * JD-Core Version:    0.6.0
 */