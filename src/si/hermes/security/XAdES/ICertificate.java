package si.hermes.security.XAdES;

import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.VerifyStatus;

public abstract interface ICertificate extends IPersistable
{
  public abstract void createCertificateFromB64(String paramString1, String paramString2)
    throws ESignDocException;

  public abstract String getDigestMethod();

  public abstract String getDigestValue();

  public abstract String getIssuerName();

  public abstract String getSerialNumber();

  public abstract void setDigestMethod(String paramString);

  public abstract void setDigestValue(String paramString);

  public abstract void setIssuerName(String paramString);

  public abstract void setSerialNumber(String paramString);

  public abstract VerifyStatus validateCertificate(String paramString)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ICertificate
 * JD-Core Version:    0.6.0
 */