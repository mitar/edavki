package org.apache.xml.security.keys.keyresolver;

import org.apache.xml.security.exceptions.XMLSecurityException;

public class InvalidKeyResolverException extends XMLSecurityException
{
  public InvalidKeyResolverException()
  {
  }

  public InvalidKeyResolverException(String paramString)
  {
    super(paramString);
  }

  public InvalidKeyResolverException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString, paramArrayOfObject);
  }

  public InvalidKeyResolverException(String paramString, Exception paramException)
  {
    super(paramString, paramException);
  }

  public InvalidKeyResolverException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(paramString, paramArrayOfObject, paramException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.InvalidKeyResolverException
 * JD-Core Version:    0.6.0
 */