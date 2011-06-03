package si.hermes.security.Collections;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import si.hermes.security.DigSigLoader;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class SignaturesImpl extends ListImpl
  implements ISignatures
{
  private static final long serialVersionUID = 570101637293423094L;

  public SignaturesImpl()
  {
    this.fParent = "Envelope";
    this.fParentNS = "";
    this.fNodeElem = "Signature";
    this.fNodeElemNS = "http://www.w3.org/2000/09/xmldsig#";
    this.fCheckIfParentExists = false;
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    if ((paramElement.getParentNode() != null) && (paramElement.getParentNode().getNodeType() != 9) && ("TimeStampToken".equals(paramElement.getAttribute("Id"))))
      return null;
    if ((paramElement.getParentNode() != null) && ("XMLTimeStamp".equals(paramElement.getParentNode().getLocalName())) && ("http://uri.etsi.org/01903/v1.1.1#".equals(paramElement.getParentNode().getNamespaceURI())))
      return null;
    SignatureImpl localSignatureImpl;
    (localSignatureImpl = new SignatureImpl()).LoadXml(paramElement);
    return localSignatureImpl;
  }

  protected final NodeList selectItems()
    throws TransformerException
  {
    return DigSigLoader.selectNodes(this.fNode, "//dsig:Signature");
  }

  public final IHSLSignature getItem(int paramInt)
  {
    return (IHSLSignature)super.getItem_(paramInt);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.SignaturesImpl
 * JD-Core Version:    0.6.0
 */