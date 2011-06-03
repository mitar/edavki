package org.apache.xml.security.keys.content.x509;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.RFC2253Parser;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLX509IssuerSerial extends SignatureElementProxy
  implements XMLX509DataContent
{
  static Log log = LogFactory.getLog(XMLX509IssuerSerial.class.getName());

  public XMLX509IssuerSerial(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public XMLX509IssuerSerial(Document paramDocument, String paramString, BigInteger paramBigInteger)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    addTextElement(paramString, "X509IssuerName");
    XMLUtils.addReturnToElement(this._constructionElement);
    addTextElement(paramBigInteger.toString(), "X509SerialNumber");
  }

  public XMLX509IssuerSerial(Document paramDocument, String paramString1, String paramString2)
  {
    this(paramDocument, paramString1, new BigInteger(paramString2));
  }

  public XMLX509IssuerSerial(Document paramDocument, String paramString, int paramInt)
  {
    this(paramDocument, paramString, new BigInteger(Integer.toString(paramInt)));
  }

  public XMLX509IssuerSerial(Document paramDocument, X509Certificate paramX509Certificate)
  {
    this(paramDocument, RFC2253Parser.normalize(paramX509Certificate.getIssuerDN().getName()), paramX509Certificate.getSerialNumber());
  }

  public BigInteger getSerialNumber()
  {
    String str = getTextFromChildElement("X509SerialNumber", "http://www.w3.org/2000/09/xmldsig#");
    if (log.isDebugEnabled())
      log.debug("In dem X509SerialNumber wurde gefunden: " + str);
    return new BigInteger(str);
  }

  public int getSerialNumberInteger()
  {
    return getSerialNumber().intValue();
  }

  public String getIssuerName()
  {
    return RFC2253Parser.normalize(getTextFromChildElement("X509IssuerName", "http://www.w3.org/2000/09/xmldsig#"));
  }

  public boolean equals(Object paramObject)
  {
    if (!paramObject.getClass().getName().equals(getClass().getName()))
      return false;
    XMLX509IssuerSerial localXMLX509IssuerSerial;
    return ((localXMLX509IssuerSerial = (XMLX509IssuerSerial)paramObject).getSerialNumber().equals(getSerialNumber())) && (localXMLX509IssuerSerial.getIssuerName().equals(getIssuerName()));
  }

  public String getBaseLocalName()
  {
    return "X509IssuerSerial";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.x509.XMLX509IssuerSerial
 * JD-Core Version:    0.6.0
 */