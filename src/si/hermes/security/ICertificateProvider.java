package si.hermes.security;

import java.security.Key;
import java.security.cert.X509Certificate;
import org.apache.xml.security.signature.XMLSignatureException;

public abstract interface ICertificateProvider
{
  public abstract String getProviderName();

  public abstract String getShortProviderName();

  public abstract Key convertToPublicKey(Key paramKey)
    throws XMLSignatureException;

  public abstract X509Certificate[] getAllCertificatesWithPrivateKey(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException;

  public abstract void initProfile(String paramString1, String paramString2)
    throws ESignDocException;

  public abstract CertificateHelper findCertificate(String paramString1, String paramString2, String paramString3, char[] paramArrayOfChar)
    throws ESignDocException;

  public abstract X509Certificate[] buildCertificates(String paramString, boolean paramBoolean)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ICertificateProvider
 * JD-Core Version:    0.6.0
 */