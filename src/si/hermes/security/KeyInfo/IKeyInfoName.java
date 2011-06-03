package si.hermes.security.KeyInfo;

import si.hermes.security.ESignDocException;

public abstract interface IKeyInfoName extends IKeyInfoClause
{
  public abstract String getValue()
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.IKeyInfoName
 * JD-Core Version:    0.6.0
 */