package si.hermes.security.XAdES;

import si.hermes.security.IPersistable;

public abstract interface ISignatureProductionPlace extends IPersistable
{
  public abstract String getCity();

  public abstract String getCountryName();

  public abstract String getPostalCode();

  public abstract String getStateOrProvince();

  public abstract void setCity(String paramString);

  public abstract void setCountryName(String paramString);

  public abstract void setPostalCode(String paramString);

  public abstract void setStateOrProvince(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ISignatureProductionPlace
 * JD-Core Version:    0.6.0
 */