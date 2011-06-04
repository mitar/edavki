package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory
{
  public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
  public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
  public static final String FACTORY_PROPERTIES = "commons-logging.properties";
  protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
  protected static Hashtable factories = new Hashtable();

  public abstract Object getAttribute(String paramString);

  public abstract String[] getAttributeNames();

  public abstract Log getInstance(Class paramClass)
    throws LogConfigurationException;

  public abstract Log getInstance(String paramString)
    throws LogConfigurationException;

  public abstract void release();

  public abstract void removeAttribute(String paramString);

  public abstract void setAttribute(String paramString, Object paramObject);

  public static LogFactory getFactory()
    throws LogConfigurationException
  {
    ClassLoader localClassLoader;
    LogFactory localLogFactory;
    if ((localLogFactory = getCachedFactory(localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run()
      {
        return LogFactory.getContextClassLoader();
      }
    }))) != null)
      return localLogFactory;
    Properties localProperties = null;
    Object localObject1;
    try
    {
      if ((localObject1 = getResourceAsStream(localClassLoader, "commons-logging.properties")) != null)
      {
        (localProperties = new Properties()).load((InputStream)localObject1);
        ((InputStream)localObject1).close();
      }
    }
    catch (IOException localIOException)
    {
    }
    catch (SecurityException localSecurityException1)
    {
    }
    try
    {
      if ((localObject1 = System.getProperty("org.apache.commons.logging.LogFactory")) != null)
        localLogFactory = newFactory((String)localObject1, localClassLoader);
    }
    catch (SecurityException localSecurityException2)
    {
    }
    Object localObject2;
    String str;
    if (localLogFactory == null)
      try
      {
        if ((localObject1 = getResourceAsStream(localClassLoader, "META-INF/services/org.apache.commons.logging.LogFactory")) != null)
        {
          try
          {
            localObject2 = new BufferedReader(new InputStreamReader((InputStream)localObject1, "UTF-8"));
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException)
          {
            localObject2 = new BufferedReader(new InputStreamReader((InputStream)localObject1));
          }
          str = ((BufferedReader)localObject2).readLine();
          ((BufferedReader)localObject2).close();
          if ((str != null) && (!"".equals(str)))
            localLogFactory = newFactory(str, localClassLoader);
        }
      }
      catch (Exception localException)
      {
      }
    if ((localLogFactory == null) && (localProperties != null) && ((localObject1 = localProperties.getProperty("org.apache.commons.logging.LogFactory")) != null))
      localLogFactory = newFactory((String)localObject1, localClassLoader);
    if (localLogFactory == null)
      localLogFactory = newFactory("org.apache.commons.logging.impl.LogFactoryImpl", LogFactory.class.getClassLoader());
    if (localLogFactory != null)
    {
      cacheFactory(localClassLoader, localLogFactory);
      if (localProperties != null)
      {
        localObject1 = localProperties.propertyNames();
        while (((Enumeration)localObject1).hasMoreElements())
        {
          localObject2 = (String)((Enumeration)localObject1).nextElement();
          str = localProperties.getProperty((String)localObject2);
          localLogFactory.setAttribute((String)localObject2, str);
        }
      }
    }
    return (LogFactory)(LogFactory)localLogFactory;
  }

  public static Log getLog(Class paramClass)
    throws LogConfigurationException
  {
    return getFactory().getInstance(paramClass);
  }

  public static Log getLog(String paramString)
    throws LogConfigurationException
  {
    return getFactory().getInstance(paramString);
  }

  public static void release(ClassLoader paramClassLoader)
  {
    synchronized (factories)
    {
      LogFactory localLogFactory;
      if ((localLogFactory = (LogFactory)factories.get(paramClassLoader)) != null)
      {
        localLogFactory.release();
        factories.remove(paramClassLoader);
      }
      return;
    }
  }

  public static void releaseAll()
  {
    synchronized (factories)
    {
      Enumeration localEnumeration = factories.elements();
      while (localEnumeration.hasMoreElements())
      {
        LogFactory localLogFactory;
        (localLogFactory = (LogFactory)localEnumeration.nextElement()).release();
      }
      factories.clear();
      return;
    }
  }

  protected static ClassLoader getContextClassLoader()
    throws LogConfigurationException
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
        throw new LogConfigurationException("Unexpected IllegalAccessException", localIllegalAccessException);
      }
      catch (InvocationTargetException localInvocationTargetException2)
      {
        if (!((localInvocationTargetException1 = localInvocationTargetException2).getTargetException() instanceof SecurityException))
        	throw new LogConfigurationException("Unexpected InvocationTargetException", localInvocationTargetException1.getTargetException());
      }
      return localClassLoader;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      localClassLoader = LogFactory.class.getClassLoader();
    }
    return localClassLoader;
  }

  private static LogFactory getCachedFactory(ClassLoader paramClassLoader)
  {
    LogFactory localLogFactory = null;
    if (paramClassLoader != null)
      localLogFactory = (LogFactory)factories.get(paramClassLoader);
    return localLogFactory;
  }

  private static void cacheFactory(ClassLoader paramClassLoader, LogFactory paramLogFactory)
  {
    if ((paramClassLoader != null) && (paramLogFactory != null))
      factories.put(paramClassLoader, paramLogFactory);
  }

  protected static LogFactory newFactory(String paramString, ClassLoader paramClassLoader)
    throws LogConfigurationException
  {
    Object localObject;
    if (((localObject = AccessController.doPrivileged(new PrivilegedAction(paramClassLoader, paramString)
    {
      private final ClassLoader val$classLoader;
      private final String val$factoryClass;

      public Object run()
      {
        Class localClass = null;
        try
        {
          if (this.val$classLoader != null)
            try
            {
              return (LogFactory)(localClass = this.val$classLoader.loadClass(this.val$factoryClass)).newInstance();
            }
            catch (ClassNotFoundException localClassNotFoundException)
            {
              if (this.val$classLoader == LogFactory.class.getClassLoader())
                throw localClassNotFoundException;
            }
            catch (NoClassDefFoundError localNoClassDefFoundError)
            {
              if (this.val$classLoader == LogFactory.class.getClassLoader())
                throw localNoClassDefFoundError;
            }
            catch (ClassCastException localClassCastException)
            {
              if (this.val$classLoader == LogFactory.class.getClassLoader())
                throw localClassCastException;
            }
          return (LogFactory)(localClass = Class.forName(this.val$factoryClass)).newInstance();
        }
        catch (Exception localException)
        {
          if ((localClass != null) && (!LogFactory.class.isAssignableFrom(localClass)))
            return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", localException);
        }
        return new LogConfigurationException(localException);
      }
    })) instanceof LogConfigurationException))
      throw ((LogConfigurationException)localObject);
    return (LogFactory)localObject;
  }

  private static InputStream getResourceAsStream(ClassLoader paramClassLoader, String paramString)
  {
    return (InputStream)AccessController.doPrivileged(new PrivilegedAction(paramClassLoader, paramString)
    {
      private final ClassLoader val$loader;
      private final String val$name;

      public Object run()
      {
        if (this.val$loader != null)
          return this.val$loader.getResourceAsStream(this.val$name);
        return ClassLoader.getSystemResourceAsStream(this.val$name);
      }
    });
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.commons.logging.LogFactory
 * JD-Core Version:    0.6.0
 */