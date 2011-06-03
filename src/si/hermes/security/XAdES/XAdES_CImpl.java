package si.hermes.security.XAdES;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Vector;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.Element;
import si.hermes.security.CertificateChain;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public class XAdES_CImpl extends XAdES_TImpl
  implements IXAdES_C
{
  private static final long serialVersionUID = 8220714873818310020L;
  String fCompleteCertificateId;
  String fCompleteRevocationId;
  ICertificateList fCompleteCertificateRefs;
  IRevocationRefs fCompleteRevocationRefs;

  public final int addCertificateRef(String paramString1, String paramString2)
    throws ESignDocException
  {
    SigningCertificateImpl localSigningCertificateImpl;
    (localSigningCertificateImpl = new SigningCertificateImpl()).createCertificateFromB64(paramString1, paramString2);
    return getCompleteCertificateRefs().Add(localSigningCertificateImpl);
  }

  public final int addRevocationRef(String paramString1, String paramString2)
    throws ESignDocException, EXAdESException, Base64DecodingException
  {
    RevocationRefImpl localRevocationRefImpl;
    (localRevocationRefImpl = new RevocationRefImpl()).createRevocationRefFromB64(paramString1, paramString2);
    return getCompleteRevocationRefs().Add(localRevocationRefImpl);
  }

  public final void createCompleteRefs(IHSLSignature paramIHSLSignature, String paramString1, String paramString2)
    throws ESignDocException, EXAdESException, CertificateException, CRLException, IOException, Base64DecodingException
  {
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSigningCertificateB64()))
      throw new ESignDocException(32);
    createCompleteRefsFromB64(paramIHSLSignature.getSigningCertificateB64(), paramString1, paramString2);
  }

  private final void createCompleteRefsFromB64(String paramString1, String paramString2, String paramString3)
    throws ESignDocException, EXAdESException, CertificateEncodingException, Base64DecodingException
  {
    getCompleteCertificateRefs().Clear();
    getCompleteRevocationRefs().Clear();
    Date localDate = getDateFromTimestamp();
    CertificateChain localCertificateChain;
    (localCertificateChain = new CertificateChain()).init(paramString1, localDate, true, paramString3);
    if (localCertificateChain.getCertStatus(0) != CertificateChain.CERT_VALID)
      throw new ESignDocException(33);
    if (IsNullOrEmpty(getCompleteCertificateId()))
      setCompleteCertificateId("CompleteCertificateRefs-" + Utility.generateRandom(30));
    if (IsNullOrEmpty(getCompleteRevocationId()))
      setCompleteRevocationId("CompleteRevocationRefs-" + Utility.generateRandom(30));
    for (int i = 0; i < localCertificateChain.getCertCount(); i++)
    {
      addCertificateRef(localCertificateChain.getCertB64(i), paramString2);
      String str = localCertificateChain.getCRLB64(i);
      if (IsNullOrEmpty(str))
        continue;
      addRevocationRef(str, paramString2);
    }
  }

  public final String getCompleteCertificateId()
  {
    return this.fCompleteCertificateId;
  }

  public final ICertificateList getCompleteCertificateRefs()
  {
    if (this.fCompleteCertificateRefs == null)
      this.fCompleteCertificateRefs = new CertificateRefsImpl();
    return this.fCompleteCertificateRefs;
  }

  public final String getCompleteRevocationId()
  {
    return this.fCompleteRevocationId;
  }

  public final IRevocationRefs getCompleteRevocationRefs()
  {
    if (this.fCompleteRevocationRefs == null)
      this.fCompleteRevocationRefs = new RevocationRefsImpl();
    return this.fCompleteRevocationRefs;
  }

  protected final Date getDateFromTimestamp()
    throws ESignDocException
  {
    if (getSignatureTimeStamps().getCount() == 0)
      return new Date();
    Object localObject = new Date();
    for (int i = 0; i < getSignatureTimeStamps().getCount(); i++)
    {
      Date localDate = getSignatureTimeStamps().getItem(i).getTimestampDate();
      if (!((Date)localObject).after(localDate))
        continue;
      localObject = localDate;
    }
    return (Date)localObject;
  }

  protected boolean isThereAnyUnsignedProperties()
  {
    return (super.isThereAnyUnsignedProperties()) || ((this.fCompleteCertificateRefs != null) && (this.fCompleteCertificateRefs.getCount() != 0)) || ((this.fCompleteRevocationRefs != null) && (this.fCompleteRevocationRefs.getCount() != 0));
  }

  public final void setCompleteCertificateId(String paramString)
  {
    this.fCompleteCertificateId = paramString;
  }

  public final void setCompleteRevocationId(String paramString)
  {
    this.fCompleteRevocationId = paramString;
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      super.Synchronize(paramBoolean);
      if (this.fUnsigedSignatureProperties != null)
      {
        Vector localVector;
        (localVector = new Vector()).add("CounterSignature");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        localVector.add("SignatureTimeStamp");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        localVector.add("CompleteCertificateRefs");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        Element localElement;
        if ((localElement = EnsureElementBefore(this.fUnsigedSignatureProperties, "CompleteCertificateRefs", "http://uri.etsi.org/01903/v1.1.1#", findAfter(localVector, this.fUnsigedSignatureProperties), (!paramBoolean) && (getCompleteCertificateRefs().getCount() != 0), (!paramBoolean) && (getCompleteCertificateRefs().getCount() == 0))) != null)
          this.fCompleteCertificateId = EnsureAttribute(localElement, "Id", this.fCompleteCertificateId, paramBoolean, false);
        if (localElement != null)
          localElement = EnsureElement(localElement, "CertRefs", "http://uri.etsi.org/01903/v1.1.1#");
        if (localElement != null)
          if (paramBoolean)
            getCompleteCertificateRefs().LoadXml(localElement);
          else
            getCompleteCertificateRefs().GetXml(localElement);
        localVector.add("CompleteRevocationRefs");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        if ((localElement = EnsureElementBefore(this.fUnsigedSignatureProperties, "CompleteRevocationRefs", "http://uri.etsi.org/01903/v1.1.1#", findAfter(localVector, this.fUnsigedSignatureProperties), (!paramBoolean) && (getCompleteRevocationRefs().getCount() != 0), (!paramBoolean) && (getCompleteRevocationRefs().getCount() == 0))) != null)
          this.fCompleteRevocationId = EnsureAttribute(localElement, "Id", this.fCompleteRevocationId, paramBoolean, false);
        if (localElement != null)
          localElement = EnsureElement(localElement, "CRLRefs", "http://uri.etsi.org/01903/v1.1.1#");
        if (localElement != null)
        {
          if (paramBoolean)
          {
            getCompleteRevocationRefs().LoadXml(localElement);
            return;
          }
          getCompleteRevocationRefs().GetXml(localElement);
        }
      }
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateRefs(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException, Base64DecodingException
  {
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSigningCertificateB64()))
      throw new ESignDocException(32);
    return SetStatus(validateRefsFromB64(paramIHSLSignature.getSigningCertificateB64(), paramString), 2);
  }

  private final VerifyStatus validateRefsFromB64(String paramString1, String paramString2)
    throws ESignDocException, CertificateEncodingException, EXAdESException, Base64DecodingException
  {
    if (getCompleteCertificateRefs().getCount() == 0)
      return new VerifyStatus("CERTIFICATE_REFERENCES_EMPTY", "");
    Date localDate = getDateFromTimestamp();
    CertificateChain localCertificateChain;
    (localCertificateChain = new CertificateChain()).init(paramString1, localDate, false, paramString2);
    if (getCompleteCertificateRefs().getCount() != localCertificateChain.getCertCount())
      return new VerifyStatus("REFERENCES_NOT_VALID", "Number of certificates in chain does not match.");
    if (getCompleteRevocationRefs().getCount() < getCompleteCertificateRefs().getCount() - 1)
      return new VerifyStatus("REFERENCES_NOT_VALID", "Number of revocation references in chain is not correct.");
    for (int i = 0; i < localCertificateChain.getCertCount(); i++)
    {
      int j = 0;
      Object localObject;
      for (int k = 0; k < getCompleteCertificateRefs().getCount(); k++)
      {
        if ((!localCertificateChain.getCertSerialAsNumber(i).equalsIgnoreCase(getCompleteCertificateRefs().getItem(k).getSerialNumber())) || (!Utility.normalizeDN(localCertificateChain.getCertIssuerDN(i)).equals(Utility.normalizeDN(getCompleteCertificateRefs().getItem(k).getIssuerName()))))
          continue;
        localObject = getCompleteCertificateRefs().getItem(i).validateCertificate(localCertificateChain.getCertB64(i));
        if (!"VALID CREDENTIALS".equals(((VerifyStatus)localObject).getStatus()))
          return localObject;
        if (localCertificateChain.getCertStatus(i) != CertificateChain.CERT_VALID)
          return new VerifyStatus("CREDENTIALS ARE INVALID", "Certificate verification status: " + localCertificateChain.getCertStatus(i));
        j = 1;
        break;
      }
      if (j == 0)
        return new VerifyStatus("REFERENCES_NOT_VALID", "Certificate in chain not found.");
      if (i >= localCertificateChain.getCertCount() - 1)
        continue;
      j = 0;
      for (k = 0; k < getCompleteRevocationRefs().getCount(); k++)
      {
        if (!Utility.normalizeDN(localCertificateChain.getCertIssuerDN(i)).equals(Utility.normalizeDN(getCompleteRevocationRefs().getItem(k).getIssuer())))
          continue;
        if (((localObject = getRevocationValue(getCompleteRevocationRefs().getItem(k).getUri(), localCertificateChain, i)) == null) || (localObject == ""))
          return new VerifyStatus("REVOCATION_LIST_NOT_FOUND", "CRL not found.");
        int m = 0;
        for (int n = 0; n < localCertificateChain.getCertCount(); n++)
        {
          if (!Utility.normalizeDN(localCertificateChain.getCertSubjectDN(n)).equals(Utility.normalizeDN(getCompleteRevocationRefs().getItem(k).getIssuer())))
            continue;
          m = 1;
          break;
        }
        if (m == 0)
          return new VerifyStatus("REFERENCES_NOT_VALID", "Certificate of revocation list issuer not found.");
        VerifyStatus localVerifyStatus = getCompleteRevocationRefs().getItem(k).validateRevocationRef((String)localObject);
        if (!"REVOCATION_LIST_VALID".equals(localVerifyStatus.getStatus()))
          return localVerifyStatus;
        if (!Utility.UtilCheckCertRevocation(localCertificateChain.getCertB64(i), localCertificateChain.getCertB64(n), localCertificateChain.getCRLB64(i), new Date()))
          return new VerifyStatus("REVOCATION_LIST_INVALID", "Certificate was revoked.");
        j = 1;
        break;
      }
      if (j == 0)
        return new VerifyStatus("REFERENCES_NOT_VALID", "Revocation list in chain not found.");
    }
    return (VerifyStatus)new VerifyStatus("VALID", "");
  }

  protected String getRevocationValue(String paramString, CertificateChain paramCertificateChain, int paramInt)
    throws ESignDocException, Base64DecodingException, EXAdESException
  {
    return paramCertificateChain.getCRLB64(paramInt);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdES_CImpl
 * JD-Core Version:    0.6.0
 */