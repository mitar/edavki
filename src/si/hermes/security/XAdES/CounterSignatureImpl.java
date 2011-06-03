package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.SignatureImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class CounterSignatureImpl extends PersistableImpl
  implements ICounterSignature
{
  private static final long serialVersionUID = -7018089821459623550L;
  IHSLSignature fSignature = null;

  public final IHSLSignature getValue()
  {
    if (this.fSignature == null)
      this.fSignature = new SignatureImpl();
    return this.fSignature;
  }

  public final void setValue(IHSLSignature paramIHSLSignature)
  {
    this.fSignature = paramIHSLSignature;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "CounterSignature", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement = EnsureElement(this.fNode, "Signature", "http://www.w3.org/2000/09/xmldsig#", true);
      if (paramBoolean)
      {
        getValue().LoadXml(localElement);
      }
      else
      {
        this.fNode.replaceChild(getValue().GetXml(null), localElement);
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.CounterSignatureImpl
 * JD-Core Version:    0.6.0
 */