package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface ICertificateList extends IList
{
  public abstract ICertificate getItem(int paramInt);

  public abstract void setItem(int paramInt, ICertificate paramICertificate);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ICertificateList
 * JD-Core Version:    0.6.0
 */