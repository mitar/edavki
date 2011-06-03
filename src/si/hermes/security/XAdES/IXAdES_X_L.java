package si.hermes.security.XAdES;

import java.security.cert.CertificateEncodingException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES_X_L extends IXAdES_X
{
  public abstract int addCertificateValue(ICertificateValue paramICertificateValue);

  public abstract int addRevocationValue(IRevocationValue paramIRevocationValue);

  public abstract void createCertAndRevocationValues(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException;

  public abstract ICertificateValues getCertificateValues();

  public abstract String getCertificateValuesId();

  public abstract IRevocationValues getRevocationValues();

  public abstract String getRevocationValuesId();

  public abstract void setCertificateValuesId(String paramString);

  public abstract void setRevocationValuesId(String paramString);

  public abstract VerifyStatus validateCertAndRevocationValues(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException, Base64DecodingException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES_X_L
 * JD-Core Version:    0.6.0
 */