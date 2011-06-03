package si.hermes.security.KeyInfo;

import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class RSAKeyValueImpl extends PersistableImpl
  implements IRSAKeyValue
{
  private static final long serialVersionUID = -1721766082576214792L;
  String fModulus;
  String fExponent;

  public final String getExponent()
  {
    return this.fExponent;
  }

  public final String getModulus()
  {
    return this.fModulus;
  }

  public final void setExponent(String paramString)
  {
    this.fExponent = paramString;
  }

  public final void setModulus(String paramString)
  {
    this.fModulus = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.RSAKeyValueImpl
 * JD-Core Version:    0.6.0
 */