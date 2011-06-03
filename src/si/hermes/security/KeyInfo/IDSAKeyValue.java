package si.hermes.security.KeyInfo;

public abstract interface IDSAKeyValue extends IKeyInfoClause
{
  public abstract String getP();

  public abstract void setP(String paramString);

  public abstract String getQ();

  public abstract void setQ(String paramString);

  public abstract String getG();

  public abstract void setG(String paramString);

  public abstract String getY();

  public abstract void setY(String paramString);

  public abstract String getJ();

  public abstract void setJ(String paramString);

  public abstract String getSeed();

  public abstract void setSeed(String paramString);

  public abstract String getPgenCounter();

  public abstract void setPgenCounter(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.IDSAKeyValue
 * JD-Core Version:    0.6.0
 */