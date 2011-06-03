package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory
{
  public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
  protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
  private static final String LOG_INTERFACE = "org.apache.commons.logging.Log";
  protected Hashtable attributes = new Hashtable();
  protected Hashtable instances = new Hashtable();
  private String logClassName;
  protected Constructor logConstructor = null;
  protected Class[] logConstructorSignature = { String.class };
  protected Method logMethod = null;
  protected Class[] logMethodSignature = { LogFactory.class };

  public Object getAttribute(String paramString)
  {
    return this.attributes.get(paramString);
  }

  public String[] getAttributeNames()
  {
    Vector localVector = new Vector();
    Enumeration localEnumeration = this.attributes.keys();
    while (localEnumeration.hasMoreElements())
      localVector.addElement((String)localEnumeration.nextElement());
    String[] arrayOfString = new String[localVector.size()];
    for (int i = 0; i < arrayOfString.length; i++)
      arrayOfString[i] = ((String)localVector.elementAt(i));
    return arrayOfString;
  }

  public Log getInstance(Class paramClass)
    throws LogConfigurationException
  {
    return getInstance(paramClass.getName());
  }

  public Log getInstance(String paramString)
    throws LogConfigurationException
  {
    Log localLog;
    if ((localLog = (Log)this.instances.get(paramString)) == null)
    {
      localLog = newInstance(paramString);
      this.instances.put(paramString, localLog);
    }
    return localLog;
  }

  public void release()
  {
    this.instances.clear();
  }

  public void removeAttribute(String paramString)
  {
    this.attributes.remove(paramString);
  }

  public void setAttribute(String paramString, Object paramObject)
  {
    if (paramObject == null)
    {
      this.attributes.remove(paramString);
      return;
    }
    this.attributes.put(paramString, paramObject);
  }

  protected String getLogClassName()
  {
    if (this.logClassName != null)
      return this.logClassName;
    this.logClassName = ((String)getAttribute("org.apache.commons.logging.Log"));
    if (this.logClassName == null)
      this.logClassName = ((String)getAttribute("org.apache.commons.logging.log"));
    if (this.logClassName == null)
      try
      {
        this.logClassName = System.getProperty("org.apache.commons.logging.Log");
      }
      catch (SecurityException localSecurityException1)
      {
      }
    if (this.logClassName == null)
      try
      {
        this.logClassName = System.getProperty("org.apache.commons.logging.log");
      }
      catch (SecurityException localSecurityException2)
      {
      }
    if ((this.logClassName == null) && (isLog4JAvailable()))
      this.logClassName = "org.apache.commons.logging.impl.Log4JLogger";
    if ((this.logClassName == null) && (isJdk14Available()))
      this.logClassName = "org.apache.commons.logging.impl.Jdk14Logger";
    if ((this.logClassName == null) && (isJdk13LumberjackAvailable()))
      this.logClassName = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
    if (this.logClassName == null)
      this.logClassName = "org.apache.commons.logging.impl.SimpleLog";
    return this.logClassName;
  }

  protected Constructor getLogConstructor()
    throws LogConfigurationException
  {
    if (this.logConstructor != null)
      return this.logConstructor;
    String str = getLogClassName();
    Class localClass1 = null;
    Class localClass2 = null;
    try
    {
      localClass2 = getClass().getClassLoader().loadClass("org.apache.commons.logging.Log");
      if ((localClass1 = loadClass(str)) == null)
        throw new LogConfigurationException("No suitable Log implementation for " + str);
      if (!localClass2.isAssignableFrom(localClass1))
      {
        Class[] arrayOfClass = localClass1.getInterfaces();
        for (int i = 0; i < arrayOfClass.length; i++)
        {
          if (!"org.apache.commons.logging.Log".equals(arrayOfClass[i].getName()))
            continue;
          throw new LogConfigurationException("Invalid class loader hierarchy.  You have more than one version of 'org.apache.commons.logging.Log' visible, which is not allowed.");
        }
        throw new LogConfigurationException("Class " + str + " does not implement '" + "org.apache.commons.logging.Log" + "'.");
      }
    }
    catch (Throwable localThrowable1)
    {
      throw new LogConfigurationException(localThrowable1);
    }
    try
    {
      this.logMethod = localClass1.getMethod("setLogFactory", this.logMethodSignature);
    }
    catch (Throwable localThrowable3)
    {
      this.logMethod = null;
    }
    try
    {
      this.logConstructor = localClass1.getConstructor(this.logConstructorSignature);
      return this.logConstructor;
    }
    catch (Throwable localThrowable2)
    {
    }
    throw new LogConfigurationException("No suitable Log constructor " + this.logConstructorSignature + " for " + str, localThrowable2);
  }

  private static Class loadClass(String paramString)
    throws ClassNotFoundException
  {
    Object localObject;
    if (((localObject = AccessController.doPrivileged(new PrivilegedAction(paramString)
    {
      private final String val$name;

      // ERROR //
      public Object run()
      {
        // Byte code:
        //   0: invokestatic 10	org/apache/commons/logging/impl/LogFactoryImpl:access$000	()Ljava/lang/ClassLoader;
        //   3: dup
        //   4: astore_1
        //   5: ifnull +13 -> 18
        //   8: aload_1
        //   9: aload_0
        //   10: getfield 8	org/apache/commons/logging/impl/LogFactoryImpl$1:val$name	Ljava/lang/String;
        //   13: invokevirtual 12	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
        //   16: areturn
        //   17: pop
        //   18: aload_0
        //   19: getfield 8	org/apache/commons/logging/impl/LogFactoryImpl$1:val$name	Ljava/lang/String;
        //   22: invokestatic 11	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
        //   25: areturn
        //   26: dup
        //   27: astore_2
        //   28: areturn
        //
        // Exception table:
        //   from	to	target	type
        //   8	16	17	java/lang/ClassNotFoundException
        //   18	25	26	java/lang/ClassNotFoundException
      }
    })) instanceof Class))
      return (Class)localObject;
    throw ((ClassNotFoundException)localObject);
  }

  protected boolean isJdk13LumberjackAvailable()
  {
    try
    {
      loadClass("java.util.logging.Logger");
      loadClass("org.apache.commons.logging.impl.Jdk13LumberjackLogger");
      return true;
    }
    catch (Throwable localThrowable)
    {
    }
    return false;
  }

  protected boolean isJdk14Available()
  {
    try
    {
      loadClass("java.util.logging.Logger");
      loadClass("org.apache.commons.logging.impl.Jdk14Logger");
      Class localClass;
      return (localClass = loadClass("java.lang.Throwable")).getDeclaredMethod("getStackTrace", null) != null;
    }
    catch (Throwable localThrowable)
    {
    }
    return false;
  }

  protected boolean isLog4JAvailable()
  {
    try
    {
      loadClass("org.apache.log4j.Logger");
      loadClass("org.apache.commons.logging.impl.Log4JLogger");
      return true;
    }
    catch (Throwable localThrowable)
    {
    }
    return false;
  }

  protected Log newInstance(String paramString)
    throws LogConfigurationException
  {
    Log localLog = null;
    try
    {
      (localObject = new Object[1])[0] = paramString;
      localLog = (Log)getLogConstructor().newInstance(localObject);
      if (this.logMethod != null)
      {
        localObject[0] = this;
        this.logMethod.invoke(localLog, localObject);
      }
      return localLog;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      Object localObject;
      Throwable localThrowable2;
      if ((localThrowable2 = (localObject = localInvocationTargetException).getTargetException()) != null)
        throw new LogConfigurationException(localThrowable2);
      throw new LogConfigurationException((Throwable)localObject);
    }
    catch (Throwable localThrowable1)
    {
    }
    throw new LogConfigurationException(localThrowable1);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.commons.logging.impl.LogFactoryImpl
 * JD-Core Version:    0.6.0
 */