package org.apache.xml.security.encryption;

import java.util.Iterator;

public abstract interface EncryptionProperties
{
  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract Iterator getEncryptionProperties();

  public abstract void addEncryptionProperty(EncryptionProperty paramEncryptionProperty);

  public abstract void removeEncryptionProperty(EncryptionProperty paramEncryptionProperty);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.EncryptionProperties
 * JD-Core Version:    0.6.0
 */