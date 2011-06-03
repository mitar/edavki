package si.hermes.security;

import java.io.IOException;
import java.io.Serializable;
import org.w3c.dom.Element;

public abstract interface IPersistable extends Serializable
{
  public abstract void LoadXml(Element paramElement)
    throws ESignDocException;

  public abstract Element GetXml(Element paramElement)
    throws ESignDocException;

  public abstract Element GetXml()
    throws ESignDocException;

  public abstract String getToString()
    throws IOException, ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.IPersistable
 * JD-Core Version:    0.6.0
 */