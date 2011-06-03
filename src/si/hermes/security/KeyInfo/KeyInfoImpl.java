package si.hermes.security.KeyInfo;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.PersistableImpl;

public final class KeyInfoImpl extends PersistableImpl
  implements IKeyInfo
{
  private static final long serialVersionUID = -9071458083127597675L;
  private Vector fClauses = new Vector();
  private String fId;
  private int idx;
  private String prevtype;
  private Vector fDeletedList = new Vector();

  public final int AddClause(IKeyInfoClause paramIKeyInfoClause)
  {
    this.fClauses.add(paramIKeyInfoClause);
    return this.fClauses.size() - 1;
  }

  public final int getCount()
  {
    return this.fClauses.size();
  }

  public final IKeyInfoClause getClause(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      this.idx = (paramString != this.prevtype ? 0 : this.idx + 1);
    this.prevtype = paramString;
    int i = this.idx;
    this.idx = i;
    IKeyInfoClause localIKeyInfoClause = (IKeyInfoClause)this.fClauses.get(i);
    if (paramString == "")
      return localIKeyInfoClause;
    if (("KeyName".equals(paramString)) && ((localIKeyInfoClause instanceof KeyInfoNameImpl)))
      return localIKeyInfoClause;
    if (("RetrievalMethod".equals(paramString)) && ((localIKeyInfoClause instanceof KeyInfoRetrievalMethodImpl)))
      return localIKeyInfoClause;
    if (("X509Data".equals(paramString)) && ((localIKeyInfoClause instanceof KeyInfoX509DataImpl)))
      return localIKeyInfoClause;
    if (("DSAKeyValue".equals(paramString)) && ((localIKeyInfoClause instanceof DSAKeyValueImpl)))
      return localIKeyInfoClause;
    if (("RSAKeyValue".equals(paramString)) && ((localIKeyInfoClause instanceof RSAKeyValueImpl)))
      return (IKeyInfoClause)this.fClauses.get(i);
    if ((localIKeyInfoClause instanceof KeyInfoNodeImpl))
      return localIKeyInfoClause;
    i++;
    return null;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final void RemoveClause(String paramString, int paramInt)
  {
    IKeyInfoClause localIKeyInfoClause = null;
    int j = this.idx;
    String str = this.prevtype;
    this.idx = 0;
    for (int i = 0; (i <= paramInt) && ((localIKeyInfoClause = getClause(paramString, true)) != null); i++);
    if (localIKeyInfoClause != null)
    {
      this.fDeletedList.add(this.fClauses.get(this.idx));
      this.fClauses.remove(this.idx);
    }
    this.idx = j;
    this.prevtype = str;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "KeyInfo", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      Object localObject1;
      Object localObject3;
      if (paramBoolean)
      {
        this.fClauses.clear();
        this.fDeletedList.clear();
        for (i = 0; i < this.fNode.getChildNodes().getLength(); i++)
        {
          localObject1 = this.fNode.getChildNodes().item(i);
          Object localObject2 = null;
          if ("http://www.w3.org/2000/09/xmldsig#".equals(((Node)localObject1).getNamespaceURI()))
            if ("KeyValue".equals(((Node)localObject1).getLocalName()))
            {
              localObject3 = ((Node)localObject1).getFirstChild();
              int k = 0;
              while (localObject3 != null)
              {
                k = 1;
                if (((Node)localObject3).getNodeType() != 3)
                {
                  if ("DSAKeyValue".equals(((Node)localObject3).getLocalName()))
                    localObject2 = new DSAKeyValueImpl();
                  else if ("RSAKeyValue".equals(((Node)localObject3).getLocalName()))
                    localObject2 = new RSAKeyValueImpl();
                  else
                    throw new ESignDocException(17);
                  localObject3 = null;
                  continue;
                }
                localObject3 = ((Node)localObject3).getNextSibling();
              }
              if (k == 0)
                throw new ESignDocException(14);
            }
            else
            {
              localObject2 = "X509Data".equals(((Node)localObject1).getLocalName()) ? new KeyInfoX509DataImpl() : "RetrievalMethod".equals(((Node)localObject1).getLocalName()) ? new KeyInfoRetrievalMethodImpl() : "KeyName".equals(((Node)localObject1).getLocalName()) ? new KeyInfoNameImpl() : new KeyInfoNodeImpl();
            }
          if (localObject2 == null)
            continue;
          ((IKeyInfoClause)localObject2).LoadXml((Element)localObject1);
          AddClause((IKeyInfoClause)localObject2);
        }
      }
      int j = 0;
      int i = 0;
      for (i = 0; i < this.fDeletedList.size(); i++)
      {
        if ((localObject3 = ((IPersistable)this.fDeletedList.get(i)).GetXml(null)).getParentNode() == null)
          continue;
        ((Node)localObject3).getParentNode().removeChild((Node)localObject3);
      }
      this.fDeletedList.clear();
      IPersistable localIPersistable;
      for (i = 0; i < this.fNode.getChildNodes().getLength(); i++)
      {
        localObject3 = this.fNode.getChildNodes().item(i);
        if (!"http://www.w3.org/2000/09/xmldsig#".equals(((Node)localObject3).getNamespaceURI()))
          continue;
        localObject1 = (localIPersistable = (IPersistable)this.fClauses.get(j)).GetXml(null);
        if (this.fNode.getOwnerDocument() != ((Node)localObject1).getOwnerDocument())
        {
          localObject1 = this.fNode.getOwnerDocument().importNode((Node)localObject1, true);
          localIPersistable.LoadXml((Element)localObject1);
        }
        this.fNode.replaceChild((Node)localObject1, (Node)localObject3);
        j++;
      }
      while (j < getCount())
      {
        localObject1 = (localIPersistable = (IPersistable)this.fClauses.get(j)).GetXml(null);
        if (this.fNode.getOwnerDocument() != ((Node)localObject1).getOwnerDocument())
        {
          localObject1 = this.fNode.getOwnerDocument().importNode((Node)localObject1, true);
          localIPersistable.LoadXml((Element)localObject1);
        }
        this.fNode.appendChild((Node)localObject1);
        j++;
      }
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final IDSAKeyValue getDSAKeyValue(boolean paramBoolean)
  {
    return (IDSAKeyValue)getClause("DSAKeyValue", paramBoolean);
  }

  public final IKeyInfoName getKeyName(boolean paramBoolean)
  {
    return (IKeyInfoName)getClause("KeyName", paramBoolean);
  }

  public final IKeyInfoRetrievalMethod getRetrievalMethod(boolean paramBoolean)
  {
    return (IKeyInfoRetrievalMethod)getClause("RetrievalMethod", paramBoolean);
  }

  public final IRSAKeyValue getRSAKeyValue(boolean paramBoolean)
  {
    return (IRSAKeyValue)getClause("RSAKeyValue", paramBoolean);
  }

  public final IKeyInfoX509Data getX509Data(boolean paramBoolean)
  {
    return (IKeyInfoX509Data)getClause("X509Data", paramBoolean);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.KeyInfoImpl
 * JD-Core Version:    0.6.0
 */