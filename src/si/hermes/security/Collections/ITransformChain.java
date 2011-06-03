package si.hermes.security.Collections;

public abstract interface ITransformChain extends IList
{
  public abstract ITransform getItem(int paramInt);

  public abstract void setItem(int paramInt, ITransform paramITransform);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ITransformChain
 * JD-Core Version:    0.6.0
 */