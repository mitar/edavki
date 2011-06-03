package si.hermes.security.Collections;

import si.hermes.security.IPersistable;

public abstract interface IHashDataInfo extends IPersistable
{
  public abstract String getUri();

  public abstract void setUri(String paramString);

  public abstract ITransformChain getTransforms();

  public abstract int addTransform(ITransform paramITransform);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IHashDataInfo
 * JD-Core Version:    0.6.0
 */