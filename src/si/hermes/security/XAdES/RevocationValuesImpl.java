package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class RevocationValuesImpl extends ListImpl
  implements IRevocationValues
{
  private static final long serialVersionUID = 1726257562458567106L;

  public RevocationValuesImpl()
  {
    this.fParent = "CRLValues";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "EncapsulatedCRLValue";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    RevocationValueImpl localRevocationValueImpl;
    (localRevocationValueImpl = new RevocationValueImpl()).LoadXml(paramElement);
    return localRevocationValueImpl;
  }

  public final IRevocationValue getItem(int paramInt)
  {
    return (IRevocationValue)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, IRevocationValue paramIRevocationValue)
  {
    super.setItem(paramInt, paramIRevocationValue);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.RevocationValuesImpl
 * JD-Core Version:    0.6.0
 */