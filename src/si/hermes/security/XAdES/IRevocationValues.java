package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface IRevocationValues extends IList
{
  public abstract IRevocationValue getItem(int paramInt);

  public abstract void setItem(int paramInt, IRevocationValue paramIRevocationValue);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IRevocationValues
 * JD-Core Version:    0.6.0
 */