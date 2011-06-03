package org.apache.xml.security.signature;

public class MissingResourceFailureException extends XMLSignatureException
{
  Reference uninitializedReference = null;

  public MissingResourceFailureException(String paramString, Reference paramReference)
  {
    super(paramString);
    this.uninitializedReference = paramReference;
  }

  public MissingResourceFailureException(String paramString, Object[] paramArrayOfObject, Reference paramReference)
  {
    super(paramString, paramArrayOfObject);
    this.uninitializedReference = paramReference;
  }

  public MissingResourceFailureException(String paramString, Exception paramException, Reference paramReference)
  {
    super(paramString, paramException);
    this.uninitializedReference = paramReference;
  }

  public MissingResourceFailureException(String paramString, Object[] paramArrayOfObject, Exception paramException, Reference paramReference)
  {
    super(paramString, paramArrayOfObject, paramException);
    this.uninitializedReference = paramReference;
  }

  public void setReference(Reference paramReference)
  {
    this.uninitializedReference = paramReference;
  }

  public Reference getReference()
  {
    return this.uninitializedReference;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.MissingResourceFailureException
 * JD-Core Version:    0.6.0
 */