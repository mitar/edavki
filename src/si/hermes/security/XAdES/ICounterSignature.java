package si.hermes.security.XAdES;

import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.IPersistable;

public abstract interface ICounterSignature extends IPersistable
{
  public abstract IHSLSignature getValue();

  public abstract void setValue(IHSLSignature paramIHSLSignature);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ICounterSignature
 * JD-Core Version:    0.6.0
 */