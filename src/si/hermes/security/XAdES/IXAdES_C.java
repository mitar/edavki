package si.hermes.security.XAdES;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES_C extends IXAdES_T
{
  public abstract int addCertificateRef(String paramString1, String paramString2)
    throws ESignDocException;

  public abstract int addRevocationRef(String paramString1, String paramString2)
    throws ESignDocException, EXAdESException, Base64DecodingException;

  public abstract void createCompleteRefs(IHSLSignature paramIHSLSignature, String paramString1, String paramString2)
    throws CertificateEncodingException, ESignDocException, EXAdESException, CertificateException, CRLException, IOException, Base64DecodingException;

  public abstract String getCompleteCertificateId();

  public abstract ICertificateList getCompleteCertificateRefs();

  public abstract String getCompleteRevocationId();

  public abstract IRevocationRefs getCompleteRevocationRefs();

  public abstract void setCompleteCertificateId(String paramString);

  public abstract void setCompleteRevocationId(String paramString);

  public abstract VerifyStatus validateRefs(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException, Base64DecodingException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES_C
 * JD-Core Version:    0.6.0
 */