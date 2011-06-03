package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface IXMLStringList2 extends IList
{
  public abstract int addValue(String paramString);

  public abstract String getItem(int paramInt);

  public abstract void setItem(String paramString, int paramInt);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXMLStringList2
 * JD-Core Version:    0.6.0
 */