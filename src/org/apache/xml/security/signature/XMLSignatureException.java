package org.apache.xml.security.signature;

import org.apache.xml.security.exceptions.XMLSecurityException;

public class XMLSignatureException extends XMLSecurityException
{
  public XMLSignatureException()
  {
  }

  public XMLSignatureException(String paramString)
  {
    super(paramString);
  }

  public XMLSignatureException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString, paramArrayOfObject);
  }

  public XMLSignatureException(String paramString, Exception paramException)
  {
    super(paramString, paramException);
  }

  public XMLSignatureException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(paramString, paramArrayOfObject, paramException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.XMLSignatureException
 * JD-Core Version:    0.6.0
 */