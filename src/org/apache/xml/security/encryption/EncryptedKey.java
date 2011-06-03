package org.apache.xml.security.encryption;

public abstract interface EncryptedKey extends EncryptedType
{
  public abstract String getRecipient();

  public abstract void setRecipient(String paramString);

  public abstract ReferenceList getReferenceList();

  public abstract void setReferenceList(ReferenceList paramReferenceList);

  public abstract String getCarriedName();

  public abstract void setCarriedName(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.EncryptedKey
 * JD-Core Version:    0.6.0
 */