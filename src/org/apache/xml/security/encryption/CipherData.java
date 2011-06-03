package org.apache.xml.security.encryption;

public abstract interface CipherData
{
  public static final int VALUE_TYPE = 1;
  public static final int REFERENCE_TYPE = 2;

  public abstract int getDataType();

  public abstract CipherValue getCipherValue();

  public abstract void setCipherValue(CipherValue paramCipherValue)
    throws XMLEncryptionException;

  public abstract CipherReference getCipherReference();

  public abstract void setCipherReference(CipherReference paramCipherReference)
    throws XMLEncryptionException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.CipherData
 * JD-Core Version:    0.6.0
 */