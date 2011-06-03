package si.hermes.security.Collections;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import si.hermes.security.DigSigLoader;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class ManifestsImpl extends ListImpl
  implements IManifests
{
  private static final long serialVersionUID = 1627367488465055223L;

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    ManifestImpl localManifestImpl;
    (localManifestImpl = new ManifestImpl()).LoadXml(paramElement);
    return localManifestImpl;
  }

  public final IManifest getItem(int paramInt)
  {
    return (IManifest)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, IManifest paramIManifest)
  {
    super.setItem(paramInt, paramIManifest);
  }

  public ManifestsImpl()
  {
    this.fParent = "Envelope";
    this.fParentNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fNodeElem = "Manifest";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fCheckIfParentExists = false;
  }

  protected final NodeList selectItems()
    throws TransformerException
  {
    return DigSigLoader.selectNodes(this.fNode, "//dsig:Manifest");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ManifestsImpl
 * JD-Core Version:    0.6.0
 */