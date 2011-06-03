package org.apache.xml.security.transforms;

import org.apache.xml.security.exceptions.XMLSecurityException;

public class InvalidTransformException extends XMLSecurityException
{
  public InvalidTransformException()
  {
  }

  public InvalidTransformException(String paramString)
  {
    super(paramString);
  }

  public InvalidTransformException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString, paramArrayOfObject);
  }

  public InvalidTransformException(String paramString, Exception paramException)
  {
    super(paramString, paramException);
  }

  public InvalidTransformException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(paramString, paramArrayOfObject, paramException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.InvalidTransformException
 * JD-Core Version:    0.6.0
 */