package org.apache.xml.security.utils;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class SignatureElementProxy extends ElementProxy
{
  public SignatureElementProxy(Document paramDocument)
  {
    super(paramDocument);
  }

  public SignatureElementProxy(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public String getBaseNamespace()
  {
    return "http://www.w3.org/2000/09/xmldsig#";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.SignatureElementProxy
 * JD-Core Version:    0.6.0
 */