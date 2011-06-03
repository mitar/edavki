package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class TransformChainHashDataInfoImpl extends ListImpl
  implements ITransformChain
{
  private static final long serialVersionUID = -4180905049815661537L;

  public TransformChainHashDataInfoImpl()
  {
    this.fParent = "Transforms";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "Transform";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    Transform localTransform;
    (localTransform = new Transform()).LoadXml(paramElement);
    return localTransform;
  }

  public final ITransform getItem(int paramInt)
  {
    return (ITransform)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, ITransform paramITransform)
  {
    super.setItem(paramInt, paramITransform);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.TransformChainHashDataInfoImpl
 * JD-Core Version:    0.6.0
 */