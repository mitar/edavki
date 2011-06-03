package org.apache.xml.security.utils;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class EncryptionElementProxy extends ElementProxy
{
  public EncryptionElementProxy(Document paramDocument)
  {
    super(paramDocument);
  }

  public EncryptionElementProxy(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public final String getBaseNamespace()
  {
    return "http://www.w3.org/2001/04/xmlenc#";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.EncryptionElementProxy
 * JD-Core Version:    0.6.0
 */