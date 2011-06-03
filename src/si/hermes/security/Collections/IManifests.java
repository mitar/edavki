package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public abstract interface IManifests extends IList
{
  public abstract IManifest getItem(int paramInt);

  public abstract int Add(IPersistable paramIPersistable, Element paramElement, boolean paramBoolean)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IManifests
 * JD-Core Version:    0.6.0
 */