package si.hermes.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.LDAPCertStoreParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import java.util.Collection;
import java.util.Date;
import sun.security.x509.X509CRLImpl;

public final class CRLResolverImpl
  implements ICRLResolver
{
  private static int chunkSize = 4096;

  public final X509CRL ResolveLDAP(URI paramURI)
    throws CertStoreException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException
  {
    LDAPCertStoreParameters localLDAPCertStoreParameters = new LDAPCertStoreParameters(paramURI.getHost(), paramURI.getPort() == -1 ? 389 : paramURI.getPort());
    CertStore localCertStore = CertStore.getInstance("LDAP", localLDAPCertStoreParameters);
    X509CRLSelector localX509CRLSelector;
    (localX509CRLSelector = new X509CRLSelector()).setDateAndTime(new Date());
    localX509CRLSelector.addIssuerName(paramURI.getPath().substring(1));
    Collection localCollection;
    if ((localCollection = localCertStore.getCRLs(localX509CRLSelector)).size() != 1)
      throw new CertStoreException("Return number of crls is not 1");
    return (X509CRLImpl)localCollection.toArray()[0];
  }

  public final byte[] readEverything(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[chunkSize];
    int i;
    while ((i = paramInputStream.read(arrayOfByte, 0, chunkSize)) >= 0)
    {
      if (i == 0)
      {
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException)
        {
          Thread.currentThread().interrupt();
        }
        continue;
      }
      localByteArrayOutputStream.write(arrayOfByte, 0, i);
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public final X509CRL ResolveURL(URI paramURI)
    throws CertificateException, CRLException, IOException
  {
    URL localURL;
    URLConnection localURLConnection;
    (localURLConnection = (localURL = paramURI.toURL()).openConnection()).connect();
    InputStream localInputStream = localURLConnection.getInputStream();
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(readEverything(localURLConnection.getInputStream()));
    localInputStream.close();
    CertificateFactory localCertificateFactory;
    X509CRL localX509CRL;
    return localX509CRL = (X509CRL)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCRL(localByteArrayInputStream);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CRLResolverImpl
 * JD-Core Version:    0.6.0
 */