package org.apache.xml.security.keys.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeyName extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(KeyName.class.getName());

  public KeyName(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public KeyName(Document paramDocument, String paramString)
  {
    super(paramDocument);
    addText(paramString);
  }

  public String getKeyName()
  {
    return getTextFromTextChild();
  }

  public String getBaseLocalName()
  {
    return "KeyName";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.KeyName
 * JD-Core Version:    0.6.0
 */