package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface ICertificateValues extends IList
{
  public abstract ICertificateValue getItem(int paramInt);

  public abstract void setItem(int paramInt, ICertificateValue paramICertificateValue);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ICertificateValues
 * JD-Core Version:    0.6.0
 */