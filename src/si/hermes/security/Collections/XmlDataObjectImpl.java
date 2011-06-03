package si.hermes.security.Collections;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class XmlDataObjectImpl extends PersistableImpl
  implements IXmlDataObject
{
  protected String fId;
  protected String fMimeType;
  protected String fEncoding;
  protected NodeList fObjectData;
  protected boolean fObjectDataPreserve;
  private static final long serialVersionUID = -8710465532167609788L;

  public final NodeList getData()
  {
    return this.fObjectData;
  }

  public final String getEncoding()
  {
    return this.fEncoding;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final String getMimeType()
  {
    return this.fMimeType;
  }

  public final void setData(NodeList paramNodeList)
  {
    this.fObjectData = paramNodeList;
  }

  public final void setEncoding(String paramString)
  {
    this.fEncoding = paramString;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final void setMimeType(String paramString)
  {
    this.fMimeType = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "Object", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      this.fMimeType = EnsureAttribute(this.fNode, "MimeType", this.fMimeType, paramBoolean, false);
      this.fEncoding = EnsureAttribute(this.fNode, "Encoding", this.fEncoding, paramBoolean, false);
      if ((!paramBoolean) && (!this.fObjectDataPreserve) && (this.fObjectData != null))
      {
        RemoveAllChildren(this.fNode);
        for (int i = 0; i < this.fObjectData.getLength(); i++)
        {
          Node localNode = this.fObjectData.item(i);
          if (this.fNode.getOwnerDocument() != localNode.getOwnerDocument())
            localNode = this.fNode.getOwnerDocument().importNode(localNode, true);
          this.fNode.appendChild(localNode);
        }
      }
      this.fObjectData = this.fNode.getChildNodes();
      this.fObjectDataPreserve = true;
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final Node getDataNode()
  {
    if (this.fObjectData.getLength() > 0)
      return this.fObjectData.item(0);
    return null;
  }

  public final void setDataNode(Node paramNode)
    throws TransformerException
  {
    XObject localXObject = XPathAPI.eval(paramNode, ".");
    this.fObjectData = localXObject.nodelist();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.XmlDataObjectImpl
 * JD-Core Version:    0.6.0
 */