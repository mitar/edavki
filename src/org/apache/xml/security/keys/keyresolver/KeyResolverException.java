package org.apache.xml.security.keys.keyresolver;

import org.apache.xml.security.exceptions.XMLSecurityException;

public class KeyResolverException extends XMLSecurityException
{
  public KeyResolverException()
  {
  }

  public KeyResolverException(String paramString)
  {
    super(paramString);
  }

  public KeyResolverException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString, paramArrayOfObject);
  }

  public KeyResolverException(String paramString, Exception paramException)
  {
    super(paramString, paramException);
  }

  public KeyResolverException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(paramString, paramArrayOfObject, paramException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.KeyResolverException
 * JD-Core Version:    0.6.0
 */