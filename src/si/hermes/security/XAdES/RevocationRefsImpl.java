package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class RevocationRefsImpl extends ListImpl
  implements IRevocationRefs
{
  private static final long serialVersionUID = -6665955576058282884L;

  public RevocationRefsImpl()
  {
    this.fParent = "CRLRefs";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "CRLRef";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    RevocationRefImpl localRevocationRefImpl;
    (localRevocationRefImpl = new RevocationRefImpl()).LoadXml(paramElement);
    return localRevocationRefImpl;
  }

  public final IRevocationRef getItem(int paramInt)
  {
    return (IRevocationRef)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, IRevocationRef paramIRevocationRef)
  {
    super.setItem(paramInt, paramIRevocationRef);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.RevocationRefsImpl
 * JD-Core Version:    0.6.0
 */