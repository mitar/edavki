package org.apache.xml.security.encryption;

import java.util.Iterator;
import org.w3c.dom.Element;

public abstract interface EncryptionMethod
{
  public abstract String getAlgorithm();

  public abstract int getKeySize();

  public abstract void setKeySize(int paramInt);

  public abstract byte[] getOAEPparams();

  public abstract void setOAEPparams(byte[] paramArrayOfByte);

  public abstract Iterator getEncryptionMethodInformation();

  public abstract void addEncryptionMethodInformation(Element paramElement);

  public abstract void removeEncryptionMethodInformation(Element paramElement);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.EncryptionMethod
 * JD-Core Version:    0.6.0
 */