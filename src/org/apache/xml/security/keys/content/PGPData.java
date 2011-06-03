package org.apache.xml.security.keys.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Element;

public class PGPData extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(PGPData.class.getName());

  public PGPData(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public String getBaseLocalName()
  {
    return "PGPData";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.PGPData
 * JD-Core Version:    0.6.0
 */