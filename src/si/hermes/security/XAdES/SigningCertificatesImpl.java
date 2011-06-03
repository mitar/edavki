package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class SigningCertificatesImpl extends ListImpl
  implements ICertificateList
{
  private static final long serialVersionUID = 5227682236616155823L;

  public SigningCertificatesImpl()
  {
    this.fParent = "SigningCertificate";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "Cert";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    SigningCertificateImpl localSigningCertificateImpl;
    (localSigningCertificateImpl = new SigningCertificateImpl()).LoadXml(paramElement);
    return localSigningCertificateImpl;
  }

  public final ICertificate getItem(int paramInt)
  {
    return (ICertificate)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, ICertificate paramICertificate)
  {
    super.setItem(paramInt, paramICertificate);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.SigningCertificatesImpl
 * JD-Core Version:    0.6.0
 */