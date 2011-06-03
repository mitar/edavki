package org.apache.xml.security.signature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ObjectContainer extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(ObjectContainer.class.getName());

  public ObjectContainer(Document paramDocument)
  {
    super(paramDocument);
  }

  public ObjectContainer(Element paramElement, String paramString)
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

  public void setMimeType(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "MimeType", paramString);
  }

  public String getMimeType()
  {
    return this._constructionElement.getAttributeNS(null, "MimeType");
  }

  public void setEncoding(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "Encoding", paramString);
  }

  public String getEncoding()
  {
    return this._constructionElement.getAttributeNS(null, "Encoding");
  }

  public Node appendChild(Node paramNode)
  {
    Node localNode = null;
    if (this._state == 0)
      localNode = this._constructionElement.appendChild(paramNode);
    return localNode;
  }

  public String getBaseLocalName()
  {
    return "Object";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.ObjectContainer
 * JD-Core Version:    0.6.0
 */