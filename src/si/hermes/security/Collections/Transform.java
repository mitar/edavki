package si.hermes.security.Collections;

import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class Transform extends PersistableImpl
  implements ITransform
{
  private static final long serialVersionUID = -1326027201903875576L;
  private String fAlgorithm = "";

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "Transform", "http://www.w3.org/2000/09/xmldsig#");
      this.fAlgorithm = EnsureAttribute(this.fNode, "Algorithm", this.fAlgorithm, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final String getAlgorithm()
  {
    return this.fAlgorithm;
  }

  public final void setAlgorithm(String paramString)
  {
    this.fAlgorithm = paramString;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.Transform
 * JD-Core Version:    0.6.0
 */