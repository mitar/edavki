package si.hermes.security.Collections;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.IPersistable;

public abstract interface IXmlDataObject extends IPersistable
{
  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract String getMimeType();

  public abstract void setMimeType(String paramString);

  public abstract String getEncoding();

  public abstract void setEncoding(String paramString);

  public abstract NodeList getData();

  public abstract void setData(NodeList paramNodeList);

  public abstract Node getDataNode();

  public abstract void setDataNode(Node paramNode)
    throws TransformerException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IXmlDataObject
 * JD-Core Version:    0.6.0
 */