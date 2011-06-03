package org.apache.xml.security.algorithms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Algorithm extends ElementProxy
{
  static Log log = LogFactory.getLog(Algorithm.class.getName());

  public Algorithm(Document paramDocument, String paramString)
  {
    super(paramDocument);
    setAlgorithmURI(paramString);
  }

  public Algorithm(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public String getAlgorithmURI()
  {
    return this._constructionElement.getAttributeNS(null, "Algorithm");
  }

  protected void setAlgorithmURI(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "Algorithm", paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.Algorithm
 * JD-Core Version:    0.6.0
 */