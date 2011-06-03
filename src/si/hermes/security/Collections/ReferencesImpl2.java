package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class ReferencesImpl2 extends ListImpl
  implements IReferences
{
  private static final long serialVersionUID = 7554295142354283554L;

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).LoadXml(paramElement);
    return localReferenceImpl;
  }

  public final IReference getItem(int paramInt)
  {
    return (IReference)super.getItem_(paramInt);
  }

  public final void setItem(IReference paramIReference, int paramInt)
  {
    super.setItem(paramInt, paramIReference);
  }

  public ReferencesImpl2()
  {
    this.fParent = "Manifest";
    this.fParentNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fNodeElem = "Reference";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ReferencesImpl2
 * JD-Core Version:    0.6.0
 */