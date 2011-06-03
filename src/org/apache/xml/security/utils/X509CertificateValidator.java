package org.apache.xml.security.utils;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class X509CertificateValidator
{
  public static void validate(X509Certificate paramX509Certificate)
    throws CertificateNotYetValidException, CertificateExpiredException
  {
    paramX509Certificate.checkValidity();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.X509CertificateValidator
 * JD-Core Version:    0.6.0
 */