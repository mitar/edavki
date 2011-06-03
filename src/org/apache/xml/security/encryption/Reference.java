package org.apache.xml.security.encryption;

import java.util.Iterator;
import org.w3c.dom.Element;

public abstract interface Reference
{
  public abstract String getURI();

  public abstract void setURI(String paramString);

  public abstract Iterator getElementRetrievalInformation();

  public abstract void addElementRetrievalInformation(Element paramElement);

  public abstract void removeElementRetrievalInformation(Element paramElement);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.Reference
 * JD-Core Version:    0.6.0
 */