package org.apache.xml.security.keys.content.x509;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLX509Certificate extends SignatureElementProxy
  implements XMLX509DataContent
{
  static Log log = LogFactory.getLog(XMLX509Certificate.class.getName());
  public static final String JCA_CERT_ID = "X.509";

  public XMLX509Certificate(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public XMLX509Certificate(Document paramDocument, byte[] paramArrayOfByte)
  {
    super(paramDocument);
    addBase64Text(paramArrayOfByte);
  }

  public XMLX509Certificate(Document paramDocument, X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    super(paramDocument);
    try
    {
      addBase64Text(paramX509Certificate.getEncoded());
      return;
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
      throw new XMLSecurityException("empty", localCertificateEncodingException);
    }
  }

  public byte[] getCertificateBytes()
    throws XMLSecurityException
  {
    return getBytesFromTextChild();
  }

  public X509Certificate getX509Certificate()
    throws XMLSecurityException
  {
    try
    {
      byte[] arrayOfByte = getCertificateBytes();
      CertificateFactory localCertificateFactory;
      X509Certificate localX509Certificate;
      if ((localX509Certificate = (X509Certificate)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCertificate(new ByteArrayInputStream(arrayOfByte))) != null)
        return localX509Certificate;
      return null;
    }
    catch (CertificateException localCertificateException)
    {
    }
    throw new XMLSecurityException("empty", localCertificateException);
  }

  public PublicKey getPublicKey()
    throws XMLSecurityException
  {
    X509Certificate localX509Certificate;
    if ((localX509Certificate = getX509Certificate()) != null)
      return localX509Certificate.getPublicKey();
    return null;
  }

  public boolean equals(Object paramObject)
  {
    try
    {
      if (!paramObject.getClass().getName().equals(getClass().getName()))
        return false;
      XMLX509Certificate localXMLX509Certificate;
      return JavaUtils.binaryCompare((localXMLX509Certificate = (XMLX509Certificate)paramObject).getCertificateBytes(), getCertificateBytes());
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    return false;
  }

  public String getBaseLocalName()
  {
    return "X509Certificate";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.x509.XMLX509Certificate
 * JD-Core Version:    0.6.0
 */