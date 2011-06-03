package org.apache.xml.security.keys.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RetrievalMethod extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(RetrievalMethod.class.getName());
  public static final String TYPE_DSA = "http://www.w3.org/2000/09/xmldsig#DSAKeyValue";
  public static final String TYPE_RSA = "http://www.w3.org/2000/09/xmldsig#RSAKeyValue";
  public static final String TYPE_PGP = "http://www.w3.org/2000/09/xmldsig#PGPData";
  public static final String TYPE_SPKI = "http://www.w3.org/2000/09/xmldsig#SPKIData";
  public static final String TYPE_MGMT = "http://www.w3.org/2000/09/xmldsig#MgmtData";
  public static final String TYPE_X509 = "http://www.w3.org/2000/09/xmldsig#X509Data";
  public static final String TYPE_RAWX509 = "http://www.w3.org/2000/09/xmldsig#rawX509Certificate";

  public RetrievalMethod(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public RetrievalMethod(Document paramDocument, String paramString1, Transforms paramTransforms, String paramString2)
  {
    super(paramDocument);
    this._constructionElement.setAttributeNS(null, "URI", paramString1);
    if (paramString2 != null)
      this._constructionElement.setAttributeNS(null, "Type", paramString2);
    if (paramTransforms != null)
    {
      this._constructionElement.appendChild(paramTransforms.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public Attr getURIAttr()
  {
    return this._constructionElement.getAttributeNodeNS(null, "URI");
  }

  public String getURI()
  {
    return getURIAttr().getNodeValue();
  }

  public String getType()
  {
    return this._constructionElement.getAttributeNS(null, "Type");
  }

  public Transforms getTransforms()
    throws XMLSecurityException
  {
    try
    {
      Element localElement;
      if ((localElement = XMLUtils.selectDsNode(this._constructionElement, "Transforms", 0)) != null)
        return new Transforms(localElement, this._baseURI);
      return null;
    }
    catch (XMLSignatureException localXMLSignatureException)
    {
    }
    throw new XMLSecurityException("empty", localXMLSignatureException);
  }

  public String getBaseLocalName()
  {
    return "RetrievalMethod";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.RetrievalMethod
 * JD-Core Version:    0.6.0
 */