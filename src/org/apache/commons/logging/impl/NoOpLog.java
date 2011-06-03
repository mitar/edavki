package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;

public class NoOpLog
  implements Log, Serializable
{
  public NoOpLog()
  {
  }

  public NoOpLog(String paramString)
  {
  }

  public void trace(Object paramObject)
  {
  }

  public void trace(Object paramObject, Throwable paramThrowable)
  {
  }

  public void debug(Object paramObject)
  {
  }

  public void debug(Object paramObject, Throwable paramThrowable)
  {
  }

  public void info(Object paramObject)
  {
  }

  public void info(Object paramObject, Throwable paramThrowable)
  {
  }

  public void warn(Object paramObject)
  {
  }

  public void warn(Object paramObject, Throwable paramThrowable)
  {
  }

  public void error(Object paramObject)
  {
  }

  public void error(Object paramObject, Throwable paramThrowable)
  {
  }

  public void fatal(Object paramObject)
  {
  }

  public void fatal(Object paramObject, Throwable paramThrowable)
  {
  }

  public final boolean isDebugEnabled()
  {
    return false;
  }

  public final boolean isErrorEnabled()
  {
    return false;
  }

  public final boolean isFatalEnabled()
  {
    return false;
  }

  public final boolean isInfoEnabled()
  {
    return false;
  }

  public final boolean isTraceEnabled()
  {
    return false;
  }

  public final boolean isWarnEnabled()
  {
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.commons.logging.impl.NoOpLog
 * JD-Core Version:    0.6.0
 */