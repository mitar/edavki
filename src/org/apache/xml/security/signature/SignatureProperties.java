package org.apache.xml.security.signature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SignatureProperties extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(SignatureProperties.class.getName());

  public SignatureProperties(Document paramDocument)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public SignatureProperties(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public int getLength()
  {
    Element[] arrayOfElement;
    return (arrayOfElement = XMLUtils.selectDsNodes(this._constructionElement, "SignatureProperty")).length;
  }

  public SignatureProperty item(int paramInt)
    throws XMLSignatureException
  {
    try
    {
      Element localElement;
      if ((localElement = XMLUtils.selectDsNode(this._constructionElement, "SignatureProperty", paramInt)) == null)
        return null;
      return new SignatureProperty(localElement, this._baseURI);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public void setId(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
    {
      this._constructionElement.setAttributeNS(null, "Id", paramString);
      IdResolver.registerElementById(this._constructionElement, paramString);
    }
  }

  public String getId()
  {
    return this._constructionElement.getAttributeNS(null, "Id");
  }

  public void addSignatureProperty(SignatureProperty paramSignatureProperty)
  {
    this._constructionElement.appendChild(paramSignatureProperty.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public String getBaseLocalName()
  {
    return "SignatureProperties";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.SignatureProperties
 * JD-Core Version:    0.6.0
 */