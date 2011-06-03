package si.hermes.security.XAdES;

import java.util.List;
import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ISignatures;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class CounterSignaturesImpl extends ListImpl
  implements ISignatures
{
  private static final long serialVersionUID = -4216227226930048942L;

  public CounterSignaturesImpl()
  {
    this.fParent = "UnsignedSignatureProperties";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "CounterSignature";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fAfterElements.add("CounterSignature");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    CounterSignatureImpl localCounterSignatureImpl;
    (localCounterSignatureImpl = new CounterSignatureImpl()).LoadXml(paramElement);
    return localCounterSignatureImpl;
  }

  public final IHSLSignature getItem(int paramInt)
  {
    return (IHSLSignature)super.getItem_(paramInt);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.CounterSignaturesImpl
 * JD-Core Version:    0.6.0
 */