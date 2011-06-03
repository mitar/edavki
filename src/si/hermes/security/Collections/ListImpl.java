package si.hermes.security.Collections;

import java.util.List;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.PersistableImpl;

public abstract class ListImpl extends PersistableImpl
  implements IList
{
  private static final long serialVersionUID = -7146617789757636200L;
  protected String fParent = "List";
  protected String fParentNS = "";
  protected String fNodeElem = "";
  protected String fNodeElemNS = "";
  protected boolean fCheckIfParentExists = true;
  protected boolean fCheckIfElementMatches = true;
  protected List docList = new Vector();
  protected List deletedList = new Vector();
  protected List fAfterElements = new Vector();
  int fNext = 0;

  public int Add(IPersistable paramIPersistable)
  {
    this.docList.add(new ListItemXml(paramIPersistable, this.fNode, null, 1));
    return this.docList.size() - 1;
  }

  public int Add(IPersistable paramIPersistable, Element paramElement, boolean paramBoolean)
    throws ESignDocException
  {
    ListItemXml localListItemXml = new ListItemXml(paramIPersistable, paramElement, null, 1);
    this.docList.add(localListItemXml);
    if (paramBoolean)
      if (paramElement == null)
      {
        localListItemXml.getSynhronizedNode();
      }
      else
      {
        if (localListItemXml.getParent() == null)
          localListItemXml.setParent(this.fNode);
        createAfter(localListItemXml.getParent(), localListItemXml.getSynhronizedNode());
      }
    return this.docList.size() - 1;
  }

  public void Clear()
    throws ESignDocException
  {
    while (this.docList.size() > 0)
      Remove(0);
    this.docList.clear();
  }

  public int getCount()
  {
    return this.docList.size();
  }

  public IPersistable getItem_(int paramInt)
  {
    return ((ListItemXml)this.docList.get(paramInt)).getItem();
  }

  public void Remove(int paramInt)
    throws ESignDocException
  {
    this.deletedList.add(this.docList.get(paramInt));
    this.docList.remove(paramInt);
  }

  public void setItem(int paramInt, IPersistable paramIPersistable)
  {
    ((ListItemXml)this.docList.get(paramInt)).setItem(paramIPersistable);
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      if ((this.fCheckIfParentExists) || (this.fNode == null))
        this.fNode = EnsureBaseElement(this.fNode, this.fParent, this.fParentNS);
      if (paramBoolean)
      {
        this.docList.clear();
        this.deletedList.clear();
        NodeList localNodeList = selectItems();
        for (int j = 0; j < localNodeList.getLength(); j++)
        {
          IPersistable localIPersistable;
          if ((localNodeList.item(j).getNodeType() != 1) || ((this.fCheckIfElementMatches) && ((!this.fNodeElem.equals(localNodeList.item(j).getLocalName())) || (!this.fNodeElemNS.equals(localNodeList.item(j).getNamespaceURI())))) || ((localIPersistable = CreateItem((Element)localNodeList.item(j))) == null))
            continue;
          this.docList.add(new ListItemXml(localIPersistable, localNodeList.item(j).getParentNode(), (Element)localNodeList.item(j), 0));
        }
      }
      else
      {
        for (int i = 0; i < this.deletedList.size(); i++)
          ((ListItemXml)this.deletedList.get(i)).removeItem();
        this.deletedList.clear();
        for (i = 0; i < this.docList.size(); i++)
        {
          ListItemXml localListItemXml;
          switch ((localListItemXml = (ListItemXml)this.docList.get(i)).getStatus())
          {
          case 0:
            localListItemXml.getSynhronizedNode();
            break;
          case 1:
            if (localListItemXml.getParent() == null)
              localListItemXml.setParent(this.fNode);
            createAfter(localListItemXml.getParent(), localListItemXml.getSynhronizedNode());
            break;
          case 2:
            localListItemXml.getParent().replaceChild(localListItemXml.getSynhronizedNode(), localListItemXml.getOldItem());
          }
        }
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  private void createAfter(Node paramNode1, Node paramNode2)
  {
    Node localNode = null;
    if (this.fAfterElements.size() != 0)
      localNode = findAfter(this.fAfterElements, paramNode1);
    if (localNode == null)
    {
      paramNode1.appendChild(paramNode2);
      return;
    }
    paramNode1.insertBefore(paramNode2, localNode);
  }

  protected NodeList selectItems()
    throws TransformerException
  {
    return this.fNode.getChildNodes();
  }

  protected abstract IPersistable CreateItem(Element paramElement)
    throws ESignDocException;

  public IPersistable getItem_(boolean paramBoolean, String paramString)
  {
    this.fNext += 1;
    if (paramBoolean)
      this.fNext = 0;
    for (int i = this.fNext; i < getCount(); i++)
    {
      IPersistable localIPersistable;
      if ((localIPersistable = getItem_(i)).getClass().getName().equals(paramString))
        return localIPersistable;
    }
    return null;
  }

  private class ListItemXml
  {
    Element fOldItem;
    Element fNode;
    Node fParent_;
    int fStatus;
    IPersistable fItem;

    public ListItemXml(IPersistable paramNode, Node paramElement, Element paramInt, int arg5)
    {
      this.fItem = paramNode;
      this.fParent_ = paramElement;
      this.fNode = paramInt;
      int i;
      this.fStatus = i;
      this.fOldItem = null;
    }

    public IPersistable getItem()
    {
      return this.fItem;
    }

    public Element getOldItem()
    {
      return this.fOldItem;
    }

    public Node getParent()
    {
      return this.fParent_;
    }

    public int getStatus()
    {
      if ((this.fStatus == 2) && (this.fOldItem == null))
        return 1;
      return this.fStatus;
    }

    public Element getSynhronizedNode()
      throws ESignDocException
    {
      this.fNode = this.fItem.GetXml();
      if ((this.fParent_ != null) && (this.fParent_.getOwnerDocument() != this.fNode.getOwnerDocument()))
      {
        this.fNode = ((Element)this.fParent_.getOwnerDocument().importNode(this.fNode, true));
        this.fItem.LoadXml(this.fNode);
      }
      this.fStatus = 0;
      return this.fNode;
    }

    public void removeItem()
    {
      if ((this.fNode != null) && (this.fParent_ != null))
        this.fParent_.removeChild(this.fNode);
    }

    public void setItem(IPersistable paramIPersistable)
    {
      if (this.fOldItem == null)
        this.fOldItem = this.fNode;
      this.fItem = paramIPersistable;
      this.fStatus = 2;
    }

    public void setParent(Element paramElement)
    {
      this.fParent_ = paramElement;
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ListImpl
 * JD-Core Version:    0.6.0
 */