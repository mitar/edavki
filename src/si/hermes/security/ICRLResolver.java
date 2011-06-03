package si.hermes.security;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;

public abstract interface ICRLResolver
{
  public abstract X509CRL ResolveLDAP(URI paramURI)
    throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertStoreException, CRLException;

  public abstract X509CRL ResolveURL(URI paramURI)
    throws CertificateException, CRLException, IOException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ICRLResolver
 * JD-Core Version:    0.6.0
 */