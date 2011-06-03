package si.hermes.security.Collections;

import si.hermes.security.IPersistable;

public abstract interface ITransform extends IPersistable
{
  public abstract String getAlgorithm();

  public abstract void setAlgorithm(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ITransform
 * JD-Core Version:    0.6.0
 */