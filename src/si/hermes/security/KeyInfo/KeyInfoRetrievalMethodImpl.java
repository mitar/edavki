package si.hermes.security.KeyInfo;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class KeyInfoRetrievalMethodImpl extends PersistableImpl
  implements IKeyInfoRetrievalMethod
{
  private static final long serialVersionUID = 5912407362098925608L;

  public final String getUri()
  {
    String str = "";
    if ((this.fNode != null) && ("RetrievalMethod".equals(this.fNode.getLocalName())) && ("http://www.w3.org/2000/09/xmldsig#".equals(this.fNode.getNamespaceURI())))
      str = this.fNode.getAttribute("URI");
    return str;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.KeyInfoRetrievalMethodImpl
 * JD-Core Version:    0.6.0
 */