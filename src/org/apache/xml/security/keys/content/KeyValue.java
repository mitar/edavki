package org.apache.xml.security.keys.content;

import java.security.PublicKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.keyvalues.DSAKeyValue;
import org.apache.xml.security.keys.content.keyvalues.RSAKeyValue;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeyValue extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(KeyValue.class.getName());

  public KeyValue(Document paramDocument, DSAKeyValue paramDSAKeyValue)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._constructionElement.appendChild(paramDSAKeyValue.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public KeyValue(Document paramDocument, RSAKeyValue paramRSAKeyValue)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._constructionElement.appendChild(paramRSAKeyValue.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public KeyValue(Document paramDocument, Element paramElement)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._constructionElement.appendChild(paramElement);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public KeyValue(Document paramDocument, PublicKey paramPublicKey)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    Object localObject;
    if (JavaUtils.implementsInterface(paramPublicKey, "java.security.interfaces.DSAPublicKey"))
    {
      localObject = new DSAKeyValue(this._doc, paramPublicKey);
      this._constructionElement.appendChild(((DSAKeyValue)localObject).getElement());
    }
    else
    {
      if (!JavaUtils.implementsInterface(paramPublicKey, "java.security.interfaces.RSAPublicKey"))
        return;
      localObject = new RSAKeyValue(this._doc, paramPublicKey);
      this._constructionElement.appendChild(((RSAKeyValue)localObject).getElement());
    }
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public KeyValue(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public PublicKey getPublicKey()
    throws XMLSecurityException
  {
    Element localElement;
    Object localObject;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement, "RSAKeyValue", 0)) != null)
      return (localObject = new RSAKeyValue(localElement, this._baseURI)).getPublicKey();
    if ((localObject = XMLUtils.selectDsNode(this._constructionElement, "DSAKeyValue", 0)) != null)
    {
      DSAKeyValue localDSAKeyValue;
      return (localDSAKeyValue = new DSAKeyValue((Element)localObject, this._baseURI)).getPublicKey();
    }
    return (PublicKey)null;
  }

  public String getBaseLocalName()
  {
    return "KeyValue";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.KeyValue
 * JD-Core Version:    0.6.0
 */