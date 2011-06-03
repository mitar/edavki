package si.hermes.security.XAdES;

import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class CertificateValueImpl extends PersistableImpl
  implements ICertificateValue
{
  private static final long serialVersionUID = -3998377948022990132L;
  String fCertificateValue;

  public final String getValue()
  {
    return this.fCertificateValue;
  }

  public final void setValue(String paramString)
  {
    this.fCertificateValue = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "EncapsulatedX509Certificate", "http://uri.etsi.org/01903/v1.1.1#");
      this.fCertificateValue = EnsureValue(this.fNode, this.fCertificateValue, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.CertificateValueImpl
 * JD-Core Version:    0.6.0
 */