package si.hermes.security.KeyInfo;

public abstract interface IRSAKeyValue extends IKeyInfoClause
{
  public abstract String getModulus();

  public abstract String getExponent();

  public abstract void setModulus(String paramString);

  public abstract void setExponent(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.IRSAKeyValue
 * JD-Core Version:    0.6.0
 */