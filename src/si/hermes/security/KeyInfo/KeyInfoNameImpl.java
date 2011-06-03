package si.hermes.security.KeyInfo;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;

public final class KeyInfoNameImpl extends PersistableImpl
  implements IKeyInfoName
{
  private static final long serialVersionUID = 5329041765375224505L;
  private String KeyName = "KeyName";

  public final String getValue()
  {
    String str = "";
    if ((this.fNode != null) && (this.KeyName.equals(this.fNode.getLocalName())) && ("http://www.w3.org/2000/09/xmldsig#".equals(this.fNode.getNamespaceURI())))
      try
      {
        str = Utility.getTextNodeValue(this.fNode);
      }
      catch (ESignDocException localESignDocException)
      {
      }
    return str;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.KeyInfoNameImpl
 * JD-Core Version:    0.6.0
 */