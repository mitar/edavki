package si.hermes.security.XAdES;

import si.hermes.security.Collections.IList;

public abstract interface IDataObjectFormats extends IList
{
  public abstract IDataObjectFormat getItem(int paramInt);

  public abstract void setItem(int paramInt, IDataObjectFormat paramIDataObjectFormat);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IDataObjectFormats
 * JD-Core Version:    0.6.0
 */