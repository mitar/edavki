package org.apache.xml.security.encryption;

import org.apache.xml.security.exceptions.XMLSecurityException;

public class XMLEncryptionException extends XMLSecurityException
{
  public XMLEncryptionException()
  {
  }

  public XMLEncryptionException(String paramString)
  {
    super(paramString);
  }

  public XMLEncryptionException(String paramString, Object[] paramArrayOfObject)
  {
    super(paramString, paramArrayOfObject);
  }

  public XMLEncryptionException(String paramString, Exception paramException)
  {
    super(paramString, paramException);
  }

  public XMLEncryptionException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(paramString, paramArrayOfObject, paramException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.XMLEncryptionException
 * JD-Core Version:    0.6.0
 */