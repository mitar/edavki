package si.hermes.security.KeyInfo;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class KeyInfoNodeImpl extends PersistableImpl
  implements IKeyInfoNode
{
  private static final long serialVersionUID = 4322680548388298343L;

  public final Element getValue()
  {
    return this.fNode;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.KeyInfoNodeImpl
 * JD-Core Version:    0.6.0
 */