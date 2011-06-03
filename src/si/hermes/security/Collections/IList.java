package si.hermes.security.Collections;

import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public abstract interface IList extends IPersistable
{
  public abstract int getCount();

  public abstract int Add(IPersistable paramIPersistable);

  public abstract IPersistable getItem_(int paramInt);

  public abstract void setItem(int paramInt, IPersistable paramIPersistable);

  public abstract void Clear()
    throws ESignDocException;

  public abstract void Remove(int paramInt)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IList
 * JD-Core Version:    0.6.0
 */