package si.hermes.security.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import si.hermes.security.CertificateChain;
import si.hermes.security.ESignDocException;
import si.hermes.security.XAdES.IXAdES_A;

public abstract interface IHSLSignature extends ISignature
{
  public abstract String getCertificateIssuerDN()
    throws ESignDocException;

  public abstract String getCertificateSerialNumber()
    throws ESignDocException;

  public abstract String getCertificateSerialNumberHex()
    throws ESignDocException;

  public abstract String getCertificateSubjectDN()
    throws ESignDocException;

  public abstract boolean getDefaultDisplayTransform();

  public abstract String getDisplayTransformName();

  public abstract String getDisplayTransformStream()
    throws ESignDocException;

  public abstract String getFriendlyName();

  public abstract String getSigningCertificateB64()
    throws ESignDocException;

  public abstract CertificateChain getSigningCertificateChain(boolean paramBoolean, String paramString)
    throws ESignDocException;

  public abstract ITimestamp getTimestamp();

  public abstract void setDefaultDisplayTransform(boolean paramBoolean);

  public abstract void setDisplayTransformName(String paramString);

  public abstract void setFriendlyName(String paramString);

  public abstract void setTimestamp(ITimestamp paramITimestamp)
    throws ESignDocException, ParserConfigurationException;

  public abstract IXAdES_A getXAdES()
    throws ESignDocException;

  public abstract void setXAdES(IXAdES_A paramIXAdES_A)
    throws ESignDocException;

  public abstract void addAttachment(String paramString1, String paramString2, String paramString3, String paramString4)
    throws ESignDocException, TransformerException, ParserConfigurationException;

  public abstract void addAttachmentUri(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IHSLSignature
 * JD-Core Version:    0.6.0
 */