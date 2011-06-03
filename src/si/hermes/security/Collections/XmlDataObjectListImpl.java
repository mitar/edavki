package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class XmlDataObjectListImpl extends ListImpl
  implements IXmlDataObjectList
{
  private static final long serialVersionUID = -2023415303758994013L;

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    XmlDataObjectImpl localXmlDataObjectImpl;
    (localXmlDataObjectImpl = new XmlDataObjectImpl()).LoadXml(paramElement);
    return localXmlDataObjectImpl;
  }

  public XmlDataObjectListImpl()
  {
    this.fParent = "Signature";
    this.fParentNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fNodeElem = "Object";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
  }

  public final XmlDataObjectImpl getItem(int paramInt)
  {
    return (XmlDataObjectImpl)super.getItem_(paramInt);
  }

  public final void setItem(XmlDataObjectImpl paramXmlDataObjectImpl, int paramInt)
  {
    super.setItem(paramInt, paramXmlDataObjectImpl);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.XmlDataObjectListImpl
 * JD-Core Version:    0.6.0
 */