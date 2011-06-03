package si.hermes.security.XAdES;

import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public final class SigningCertificateImpl extends PersistableImpl
  implements ICertificate
{
  private static final long serialVersionUID = -6099305685975722193L;
  private String fDigestMethod;
  private String fDigestValue;
  private String fIssuerName;
  private String fSerialNumber;

  public final void createCertificateFromB64(String paramString1, String paramString2)
    throws ESignDocException
  {
    X509Certificate localX509Certificate;
    try
    {
      localX509Certificate = Utility.createCertificateFromString(paramString1);
    }
    catch (Exception localException)
    {
      throw new ESignDocException(30, localException);
    }
    this.fDigestMethod = paramString2;
    try
    {
      this.fDigestValue = Base64.encode(Utility.calculateDigest(localX509Certificate.getEncoded(), paramString2));
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
      throw new ESignDocException(30, localCertificateEncodingException);
    }
    this.fIssuerName = localX509Certificate.getIssuerDN().getName();
    this.fSerialNumber = localX509Certificate.getSerialNumber().toString();
  }

  public final String getDigestMethod()
  {
    return this.fDigestMethod;
  }

  public final String getDigestValue()
  {
    return this.fDigestValue;
  }

  public final String getIssuerName()
  {
    return this.fIssuerName;
  }

  public final String getSerialNumber()
  {
    return this.fSerialNumber;
  }

  public final void setDigestMethod(String paramString)
  {
    this.fDigestMethod = paramString;
  }

  public final void setDigestValue(String paramString)
  {
    this.fDigestValue = paramString;
  }

  public final void setIssuerName(String paramString)
  {
    this.fIssuerName = paramString;
  }

  public final void setSerialNumber(String paramString)
  {
    this.fSerialNumber = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "Cert", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement1 = EnsureElement(this.fNode, "CertDigest", "http://uri.etsi.org/01903/v1.1.1#", true);
      Element localElement2 = EnsureElement(localElement1, "DigestMethod", "http://uri.etsi.org/01903/v1.1.1#", true);
      this.fDigestMethod = EnsureAttribute(localElement2, "Algorithm", this.fDigestMethod, paramBoolean, true);
      localElement2 = EnsureElement(localElement1, "DigestValue", "http://uri.etsi.org/01903/v1.1.1#", true);
      this.fDigestValue = EnsureValue(localElement2, this.fDigestValue, paramBoolean);
      localElement1 = EnsureElement(this.fNode, "IssuerSerial", "http://uri.etsi.org/01903/v1.1.1#", true);
      localElement2 = EnsureElement(localElement1, "X509IssuerName", "http://www.w3.org/2000/09/xmldsig#", true);
      this.fIssuerName = EnsureValue(localElement2, this.fIssuerName, paramBoolean);
      localElement2 = EnsureElement(localElement1, "X509SerialNumber", "http://www.w3.org/2000/09/xmldsig#", true);
      this.fSerialNumber = EnsureValue(localElement2, this.fSerialNumber, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateCertificate(String paramString)
    throws ESignDocException
  {
    if ((IsNullOrEmpty(this.fDigestValue)) || (IsNullOrEmpty(this.fSerialNumber)))
      return new VerifyStatus("CREDENTIALS NOT FOUND", "");
    X509Certificate localX509Certificate;
    try
    {
      localX509Certificate = Utility.createCertificateFromString(paramString);
    }
    catch (Exception localException)
    {
      throw new ESignDocException(30, localException);
    }
    String str1 = localX509Certificate.getIssuerDN().getName();
    String str2 = localX509Certificate.getSerialNumber().toString();
    if (!Utility.normalizeDN(this.fIssuerName).equals(Utility.normalizeDN(str1)))
      return new VerifyStatus("CREDENTIALS ARE INVALID", Utility.normalizeDN(this.fIssuerName) + " ni enak " + Utility.normalizeDN(str1));
    if (!this.fSerialNumber.equals(str2))
      return new VerifyStatus("CREDENTIALS ARE INVALID", this.fSerialNumber + " ni enak " + str2);
    if (IsNullOrEmpty(this.fDigestMethod))
      this.fDigestMethod = "http://www.w3.org/2000/09/xmldsig#sha1";
    String str3;
    try
    {
      str3 = Base64.encode(Utility.calculateDigest(localX509Certificate.getEncoded(), this.fDigestMethod));
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
      throw new ESignDocException(30, localCertificateEncodingException);
    }
    if (!this.fDigestValue.equals(str3))
      return new VerifyStatus("CREDENTIALS DIGEST IS INVALID", "");
    return new VerifyStatus("VALID CREDENTIALS", "");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.SigningCertificateImpl
 * JD-Core Version:    0.6.0
 */