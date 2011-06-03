package si.hermes.security.XAdES;

import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class RevocationValueImpl extends PersistableImpl
  implements IRevocationValue
{
  private static final long serialVersionUID = 4259632765333018368L;
  String fRevocationValue;

  public final String getValue()
  {
    return this.fRevocationValue;
  }

  public final void setValue(String paramString)
  {
    this.fRevocationValue = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "EncapsulatedCRLValue", "http://uri.etsi.org/01903/v1.1.1#");
      this.fRevocationValue = EnsureValue(this.fNode, this.fRevocationValue, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.RevocationValueImpl
 * JD-Core Version:    0.6.0
 */