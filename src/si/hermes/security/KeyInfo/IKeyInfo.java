package si.hermes.security.KeyInfo;

import si.hermes.security.IPersistable;

public abstract interface IKeyInfo extends IPersistable
{
  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract int AddClause(IKeyInfoClause paramIKeyInfoClause);

  public abstract void RemoveClause(String paramString, int paramInt);

  public abstract IKeyInfoClause getClause(String paramString, boolean paramBoolean);

  public abstract int getCount();

  public abstract IKeyInfoName getKeyName(boolean paramBoolean);

  public abstract IKeyInfoRetrievalMethod getRetrievalMethod(boolean paramBoolean);

  public abstract IKeyInfoX509Data getX509Data(boolean paramBoolean);

  public abstract IDSAKeyValue getDSAKeyValue(boolean paramBoolean);

  public abstract IRSAKeyValue getRSAKeyValue(boolean paramBoolean);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.IKeyInfo
 * JD-Core Version:    0.6.0
 */