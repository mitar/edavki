package si.hermes.security;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public final class KeyStoreCertificateResolver
  implements CertificateResolver
{
  private KeyStore keyStore;

  public KeyStoreCertificateResolver(KeyStore paramKeyStore)
  {
    this.keyStore = paramKeyStore;
  }

  public final X509Certificate resolve(String paramString, BigInteger paramBigInteger)
  {
    Enumeration localEnumeration;
    try
    {
      localEnumeration = this.keyStore.aliases();
    }
    catch (KeyStoreException localKeyStoreException1)
    {
      return null;
    }
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      try
      {
        X509Certificate localX509Certificate;
        if (((localX509Certificate = (X509Certificate)this.keyStore.getCertificate(str)).getIssuerDN().toString().equals(paramString)) && (localX509Certificate.getSerialNumber().equals(paramBigInteger)))
          return localX509Certificate;
      }
      catch (KeyStoreException localKeyStoreException2)
      {
      }
    }
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyStoreCertificateResolver
 * JD-Core Version:    0.6.0
 */