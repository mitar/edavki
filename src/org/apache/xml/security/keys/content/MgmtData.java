package org.apache.xml.security.keys.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MgmtData extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(MgmtData.class.getName());

  public MgmtData(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public MgmtData(Document paramDocument, String paramString)
  {
    super(paramDocument);
    addText(paramString);
  }

  public String getMgmtData()
  {
    return getTextFromTextChild();
  }

  public String getBaseLocalName()
  {
    return "MgmtData";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.MgmtData
 * JD-Core Version:    0.6.0
 */