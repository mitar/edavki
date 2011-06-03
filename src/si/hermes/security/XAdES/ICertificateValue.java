package si.hermes.security.XAdES;

import si.hermes.security.IPersistable;

public abstract interface ICertificateValue extends IPersistable
{
  public abstract String getValue();

  public abstract void setValue(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ICertificateValue
 * JD-Core Version:    0.6.0
 */