package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public abstract interface ISignatures extends IList
{
  public abstract IHSLSignature getItem(int paramInt);

  public abstract int Add(IPersistable paramIPersistable, Element paramElement, boolean paramBoolean)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ISignatures
 * JD-Core Version:    0.6.0
 */