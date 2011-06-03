package org.apache.xml.security.keys.content.x509;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLX509CRL extends SignatureElementProxy
  implements XMLX509DataContent
{
  static Log log = LogFactory.getLog(XMLX509CRL.class.getName());

  public XMLX509CRL(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public XMLX509CRL(Document paramDocument, byte[] paramArrayOfByte)
  {
    super(paramDocument);
    addBase64Text(paramArrayOfByte);
  }

  public byte[] getCRLBytes()
    throws XMLSecurityException
  {
    return getBytesFromTextChild();
  }

  public String getBaseLocalName()
  {
    return "X509CRL";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.x509.XMLX509CRL
 * JD-Core Version:    0.6.0
 */