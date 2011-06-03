package si.hermes.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public final class CertificateHelper
{
  public PrivateKey privatekey;
  public X509Certificate certificate;

  public CertificateHelper(PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
  {
    this.privatekey = paramPrivateKey;
    this.certificate = paramX509Certificate;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CertificateHelper
 * JD-Core Version:    0.6.0
 */