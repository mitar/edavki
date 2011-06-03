package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.PersistableImpl;

public final class XMLStringList2Impl extends ListImpl
  implements IXMLStringList2
{
  private static final long serialVersionUID = -9003321026259059759L;

  public XMLStringList2Impl(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.fParent = paramString1;
    this.fParentNS = paramString2;
    this.fNodeElem = paramString3;
    this.fNodeElemNS = paramString4;
  }

  public final int addValue(String paramString)
  {
    StringItem localStringItem;
    (localStringItem = new StringItem(this.fNodeElem, this.fNodeElemNS)).setValue(paramString);
    return Add(localStringItem);
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    StringItem localStringItem;
    (localStringItem = new StringItem(this.fNodeElem, this.fNodeElemNS)).LoadXml(paramElement);
    return localStringItem;
  }

  public final String getItem(int paramInt)
  {
    return ((StringItem)super.getItem_(paramInt)).getValue();
  }

  public final void setItem(String paramString, int paramInt)
  {
    ((StringItem)super.getItem_(paramInt)).setValue(paramString);
  }

  private class StringItem extends PersistableImpl
    implements IPersistable
  {
    private static final long serialVersionUID = -5934425030719980439L;
    private String fBaseNode;
    private String fBaseNS;
    private String fValue = null;

    public StringItem(String paramString1, String arg3)
    {
      this.fBaseNode = paramString1;
      Object localObject;
      this.fBaseNS = localObject;
    }

    public String getValue()
    {
      return this.fValue;
    }

    public void setValue(String paramString)
    {
      this.fValue = paramString;
    }

    protected void Synchronize(boolean paramBoolean)
      throws ESignDocException
    {
      try
      {
        this.fNode = EnsureBaseElement(this.fNode, this.fBaseNode, this.fBaseNS);
        this.fValue = EnsureValue(this.fNode, this.fValue, paramBoolean);
        return;
      }
      catch (Exception localException2)
      {
        Exception localException1;
        (localException1 = localException2).printStackTrace();
      }
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XMLStringList2Impl
 * JD-Core Version:    0.6.0
 */