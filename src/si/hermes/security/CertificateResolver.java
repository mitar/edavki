package si.hermes.security;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

public abstract interface CertificateResolver
{
  public abstract X509Certificate resolve(String paramString, BigInteger paramBigInteger);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CertificateResolver
 * JD-Core Version:    0.6.0
 */