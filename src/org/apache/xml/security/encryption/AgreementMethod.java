package org.apache.xml.security.encryption;

import java.util.Iterator;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Element;

public abstract interface AgreementMethod
{
  public abstract byte[] getKANonce();

  public abstract void setKANonce(byte[] paramArrayOfByte);

  public abstract Iterator getAgreementMethodInformation();

  public abstract void addAgreementMethodInformation(Element paramElement);

  public abstract void revoveAgreementMethodInformation(Element paramElement);

  public abstract KeyInfo getOriginatorKeyInfo();

  public abstract void setOriginatorKeyInfo(KeyInfo paramKeyInfo);

  public abstract KeyInfo getRecipientKeyInfo();

  public abstract void setRecipientKeyInfo(KeyInfo paramKeyInfo);

  public abstract String getAlgorithm();
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.AgreementMethod
 * JD-Core Version:    0.6.0
 */