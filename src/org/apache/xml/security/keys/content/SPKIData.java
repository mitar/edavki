package org.apache.xml.security.keys.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Element;

public class SPKIData extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(SPKIData.class.getName());

  public SPKIData(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public String getBaseLocalName()
  {
    return "SPKIData";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.SPKIData
 * JD-Core Version:    0.6.0
 */