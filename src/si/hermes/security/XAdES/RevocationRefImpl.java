package si.hermes.security.XAdES;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.mozilla.jss.asn1.INTEGER;
import org.mozilla.jss.asn1.INTEGER.Template;
import org.mozilla.jss.asn1.OCTET_STRING;
import org.mozilla.jss.asn1.OCTET_STRING.Template;
import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.ISODateFormat;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;
import si.hermes.security.paramHelper;

public final class RevocationRefImpl extends PersistableImpl
  implements IRevocationRef
{
  private static final long serialVersionUID = 1477097914025451411L;
  String fDigestMethod;
  String fDigestValue;
  String fIssuer;
  String fIssueTime;
  String fNumber;
  String fUri;
  String fCRLB64;

  public final void createRevocationRefFromB64(String paramString1, String paramString2)
    throws ESignDocException, EXAdESException, Base64DecodingException
  {
    byte[] arrayOfByte = Base64.decode(paramString1);
    this.fDigestMethod = paramString2;
    this.fDigestValue = Base64.encode(Utility.calculateDigest(arrayOfByte, paramString2));
    X509CRL localX509CRL;
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      CertificateFactory localCertificateFactory;
      localX509CRL = (X509CRL)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCRL(localByteArrayInputStream);
      localByteArrayInputStream.close();
    }
    catch (Exception localException)
    {
      throw new EXAdESException(3, localException);
    }
    this.fCRLB64 = paramString1;
    this.fNumber = GetCRLSerial(localX509CRL);
    this.fIssuer = localX509CRL.getIssuerDN().getName();
    this.fIssueTime = ISODateFormat.DateTime2ISOZ(localX509CRL.getThisUpdate());
  }

  public final String getCRL()
  {
    return this.fCRLB64;
  }

  private String GetCRLSerial(X509CRL paramX509CRL)
    throws ESignDocException
  {
    byte[] arrayOfByte;
    if ((arrayOfByte = paramX509CRL.getExtensionValue("2.5.29.20")) != null)
      try
      {
        OCTET_STRING.Template localTemplate;
        OCTET_STRING localOCTET_STRING = (OCTET_STRING)(localTemplate = new OCTET_STRING.Template()).decode(new ByteArrayInputStream(arrayOfByte));
        INTEGER.Template localTemplate1;
        INTEGER localINTEGER;
        return (localINTEGER = (INTEGER)(localTemplate1 = new INTEGER.Template()).decode(new ByteArrayInputStream(localOCTET_STRING.toByteArray()))).toString();
      }
      catch (Exception localException)
      {
        throw new ESignDocException(35, "Cannot decode CRL Serial Number", localException);
      }
    return "";
  }

  public final String getDigestMethod()
  {
    return this.fDigestMethod;
  }

  public final String getDigestValue()
  {
    return this.fDigestValue;
  }

  public final String getIssuer()
  {
    return this.fIssuer;
  }

  public final String getIssueTime()
  {
    return this.fIssueTime;
  }

  public final String getNumber()
  {
    return this.fNumber;
  }

  public final String getUri()
  {
    return this.fUri;
  }

  public final void setCRL(String paramString)
  {
    this.fCRLB64 = paramString;
  }

  public final void setDigestMethod(String paramString)
  {
    this.fDigestMethod = paramString;
  }

  public final void setDigestValue(String paramString)
  {
    this.fDigestValue = paramString;
  }

  public final void setIssuer(String paramString)
  {
    this.fIssuer = paramString;
  }

  public final void setIssueTime(String paramString)
  {
    this.fIssueTime = paramString;
  }

  public final void setNumber(String paramString)
  {
    this.fNumber = paramString;
  }

  public final void setUri(String paramString)
  {
    this.fUri = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "CRLRef", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement1 = EnsureElement(this.fNode, "DigestAlgAndValue", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement2 = EnsureElement(localElement1, "DigestMethod", "http://uri.etsi.org/01903/v1.1.1#");
      this.fDigestMethod = EnsureAttribute(localElement2, "Algorithm", this.fDigestMethod, paramBoolean);
      localElement2 = EnsureElement(localElement1, "DigestValue", "http://uri.etsi.org/01903/v1.1.1#");
      this.fDigestValue = EnsureValue(localElement2, this.fDigestValue, paramBoolean);
      localElement1 = EnsureElement(this.fNode, "CRLIdentifier", "http://uri.etsi.org/01903/v1.1.1#");
      this.fUri = EnsureAttribute(localElement1, "URI", this.fUri, paramBoolean, false);
      localElement2 = EnsureElement(localElement1, "Issuer", "http://uri.etsi.org/01903/v1.1.1#");
      this.fIssuer = EnsureValue(localElement2, this.fIssuer, paramBoolean);
      localElement2 = EnsureElement(localElement1, "IssueTime", "http://uri.etsi.org/01903/v1.1.1#");
      this.fIssueTime = EnsureValue(localElement2, this.fIssueTime, paramBoolean);
      this.fNumber = ensureOptionalElementWithValue(localElement1, "Number", "http://uri.etsi.org/01903/v1.1.1#", this.fNumber, paramBoolean).str1;
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateRevocationRef(String paramString)
    throws EXAdESException, ESignDocException, Base64DecodingException
  {
    if ((IsNullOrEmpty(this.fDigestValue)) || (IsNullOrEmpty(this.fIssuer)) || (IsNullOrEmpty(this.fIssueTime)))
      return new VerifyStatus("REVOCATION_LIST_NOT_FOUND", "");
    byte[] arrayOfByte = Base64.decode(paramString);
    X509CRL localX509CRL;
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      CertificateFactory localCertificateFactory;
      localX509CRL = (X509CRL)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCRL(localByteArrayInputStream);
      localByteArrayInputStream.close();
    }
    catch (Exception localException)
    {
      throw new EXAdESException(3, localException);
    }
    if ((!Utility.normalizeDN(localX509CRL.getIssuerDN().getName()).equals(Utility.normalizeDN(this.fIssuer))) || (!GetCRLSerial(localX509CRL).equals(this.fNumber)))
      return new VerifyStatus("REVOCATION_LIST_INVALID", "");
    if (IsNullOrEmpty(this.fDigestMethod))
      this.fDigestMethod = "http://www.w3.org/2000/09/xmldsig#sha1";
    if (!Base64.encode(Utility.calculateDigest(arrayOfByte, this.fDigestMethod)).equals(this.fDigestValue))
      return new VerifyStatus("REVOCATION_LIST_DIGEST_INVALID", "");
    return new VerifyStatus("REVOCATION_LIST_VALID", "");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.RevocationRefImpl
 * JD-Core Version:    0.6.0
 */