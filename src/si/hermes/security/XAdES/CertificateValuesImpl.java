package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class CertificateValuesImpl extends ListImpl
  implements ICertificateValues
{
  private static final long serialVersionUID = 5167638733410220358L;

  public CertificateValuesImpl()
  {
    this.fParent = "CertificateValues";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "EncapsulatedX509Certificate";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    CertificateValueImpl localCertificateValueImpl;
    (localCertificateValueImpl = new CertificateValueImpl()).LoadXml(paramElement);
    return localCertificateValueImpl;
  }

  public final ICertificateValue getItem(int paramInt)
  {
    return (ICertificateValue)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, ICertificateValue paramICertificateValue)
  {
    super.setItem(paramInt, paramICertificateValue);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.CertificateValuesImpl
 * JD-Core Version:    0.6.0
 */