package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface IRevocationRefs extends IList
{
  public abstract IRevocationRef getItem(int paramInt);

  public abstract void setItem(int paramInt, IRevocationRef paramIRevocationRef);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IRevocationRefs
 * JD-Core Version:    0.6.0
 */