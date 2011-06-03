package si.hermes.security.Collections;

import java.util.Vector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;

public final class XmlStringListImpl extends PersistableImpl
  implements IXmlStringList
{
  private static final long serialVersionUID = -1761602944591899616L;
  private Vector fNodeList = new Vector();
  private String fNodeName;
  private String fNS;
  private String fXPath;
  private boolean fUseXPath;
  private PrefixResolver fPrefixResolver;

  public final void SetFilterName(String paramString1, String paramString2)
  {
    this.fNodeName = paramString1;
    this.fNS = paramString2;
    this.fUseXPath = false;
  }

  public final void SetXPath(String paramString, PrefixResolver paramPrefixResolver)
  {
    this.fXPath = paramString;
    this.fPrefixResolver = paramPrefixResolver;
    this.fUseXPath = true;
  }

  public final void Remove(int paramInt)
  {
    if ((paramInt < this.fNodeList.size()) && (paramInt >= 0))
    {
      Node localNode;
      (localNode = (Node)this.fNodeList.get(paramInt)).getParentNode().removeChild(localNode);
      this.fNodeList.remove(paramInt);
      return;
    }
    throw new ArrayIndexOutOfBoundsException();
  }

  public final int getCount()
  {
    return this.fNodeList.size();
  }

  public final String getItem(int paramInt)
    throws ESignDocException
  {
    Node localNode;
    return Utility.getTextNodeValue((Element)(localNode = (Node)this.fNodeList.get(paramInt)));
  }

  public final void setItem(int paramInt, String paramString)
    throws ESignDocException
  {
    Node localNode;
    Utility.setTextNodeValue((Element)(localNode = (Node)this.fNodeList.get(paramInt)), paramString);
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    if (this.fNode == null)
      throw new ESignDocException(19);
    try
    {
      if (paramBoolean)
      {
        Object localObject;
        NodeList localNodeList = this.fUseXPath ? (localObject = XPathAPI.eval(this.fNode, this.fXPath, this.fPrefixResolver)).nodelist() : this.fNode.getChildNodes();
        for (int i = 0; i < localNodeList.getLength(); i++)
        {
          localObject = localNodeList.item(i);
          if ((!this.fUseXPath) && (!new String("").equals(this.fNodeName)) && ((!this.fNodeName.equals(((Node)localObject).getLocalName())) || (!this.fNS.equals(((Node)localObject).getNamespaceURI()))))
            continue;
          this.fNodeList.add(localNodeList.item(i));
        }
      }
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.XmlStringListImpl
 * JD-Core Version:    0.6.0
 */