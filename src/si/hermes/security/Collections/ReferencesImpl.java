package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class ReferencesImpl extends ListImpl
  implements IReferences
{
  private static final long serialVersionUID = -7447039247053191206L;

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).LoadXml(paramElement);
    return localReferenceImpl;
  }

  public ReferencesImpl()
  {
    this.fParent = "SignedInfo";
    this.fParentNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fNodeElem = "Reference";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
  }

  public final IReference getItem(int paramInt)
  {
    return (IReference)super.getItem_(paramInt);
  }

  public final void setItem(IReference paramIReference, int paramInt)
  {
    super.setItem(paramInt, paramIReference);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ReferencesImpl
 * JD-Core Version:    0.6.0
 */