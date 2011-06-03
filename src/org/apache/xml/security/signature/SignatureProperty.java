package org.apache.xml.security.signature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SignatureProperty extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(SignatureProperty.class.getName());

  public SignatureProperty(Document paramDocument, String paramString)
  {
    this(paramDocument, paramString, null);
  }

  public SignatureProperty(Document paramDocument, String paramString1, String paramString2)
  {
    super(paramDocument);
    setTarget(paramString1);
    setId(paramString2);
  }

  public SignatureProperty(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
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

  public void setTarget(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "Target", paramString);
  }

  public String getTarget()
  {
    return this._constructionElement.getAttributeNS(null, "Target");
  }

  public Node appendChild(Node paramNode)
  {
    return this._constructionElement.appendChild(paramNode);
  }

  public String getBaseLocalName()
  {
    return "SignatureProperty";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.SignatureProperty
 * JD-Core Version:    0.6.0
 */