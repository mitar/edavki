package si.hermes.security.XAdES;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Element;
import si.hermes.security.CertificateChain;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public class XAdES_X_LImpl extends XAdES_XImpl
  implements IXAdES_X_L
{
  private static final long serialVersionUID = 8548996803443484572L;
  ICertificateValues fCertificateValues;
  IRevocationValues fRevocationValues;
  String fRevocationValuesId;
  String fCertificateValuesId;

  public final int addCertificateValue(ICertificateValue paramICertificateValue)
  {
    return getCertificateValues().Add(paramICertificateValue);
  }

  public final int addRevocationValue(IRevocationValue paramIRevocationValue)
  {
    return getRevocationValues().Add(paramIRevocationValue);
  }

  public final void createCertAndRevocationValues(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException
  {
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSigningCertificateB64()))
      throw new ESignDocException(32);
    createCertAndRevocationValuesFromB64(paramIHSLSignature.getSigningCertificateB64(), paramString);
  }

  private final void createCertAndRevocationValuesFromB64(String paramString1, String paramString2)
    throws ESignDocException, CertificateEncodingException
  {
    getCertificateValues().Clear();
    getRevocationValues().Clear();
    Date localDate = getDateFromTimestamp();
    CertificateChain localCertificateChain;
    (localCertificateChain = new CertificateChain()).init(paramString1, localDate, true, paramString2);
    if (localCertificateChain.getCertStatus(0) != CertificateChain.CERT_VALID)
      throw new ESignDocException(33);
    if (IsNullOrEmpty(getCertificateValuesId()))
      setCertificateValuesId("CertificatesValues-" + Utility.generateRandom(30));
    if (IsNullOrEmpty(getRevocationValuesId()))
      setRevocationValuesId("RevocationValues-" + Utility.generateRandom(30));
    for (int i = 0; i < localCertificateChain.getCertCount(); i++)
    {
      CertificateValueImpl localCertificateValueImpl;
      (localCertificateValueImpl = new CertificateValueImpl()).setValue(localCertificateChain.getCertB64(i));
      addCertificateValue(localCertificateValueImpl);
      String str = localCertificateChain.getCRLB64(i);
      if (IsNullOrEmpty(str))
        continue;
      RevocationValueImpl localRevocationValueImpl;
      (localRevocationValueImpl = new RevocationValueImpl()).setValue(str);
      addRevocationValue(localRevocationValueImpl);
    }
  }

  public final ICertificateValues getCertificateValues()
  {
    if (this.fCertificateValues == null)
      this.fCertificateValues = new CertificateValuesImpl();
    return this.fCertificateValues;
  }

  public final String getCertificateValuesId()
  {
    return this.fCertificateValuesId;
  }

  public final IRevocationValues getRevocationValues()
  {
    if (this.fRevocationValues == null)
      this.fRevocationValues = new RevocationValuesImpl();
    return this.fRevocationValues;
  }

  public final String getRevocationValuesId()
  {
    return this.fRevocationValuesId;
  }

  protected boolean isThereAnyUnsignedProperties()
  {
    return (super.isThereAnyUnsignedProperties()) || ((this.fCertificateValues != null) && (this.fCertificateValues.getCount() != 0)) || ((this.fRevocationValues != null) && (this.fRevocationValues.getCount() != 0));
  }

  public final void setCertificateValuesId(String paramString)
  {
    this.fCertificateValuesId = paramString;
  }

  public final void setRevocationValuesId(String paramString)
  {
    this.fRevocationValuesId = paramString;
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
        localVector.add("CompleteRevocationRefs");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        localVector.add("RefsOnlyTimeStamp");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        localVector.add("SigAndRefsTimeStamp");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        Element localElement;
        if ((localElement = EnsureElementBefore(this.fUnsigedSignatureProperties, "CertificateValues", "http://uri.etsi.org/01903/v1.1.1#", findAfter(localVector, this.fUnsigedSignatureProperties), (!paramBoolean) && (getCertificateValues().getCount() != 0), (!paramBoolean) && (getCertificateValues().getCount() == 0))) != null)
          this.fCertificateValuesId = EnsureAttribute(localElement, "Id", this.fCertificateValuesId, paramBoolean, false);
        if (localElement != null)
          if (paramBoolean)
            getCertificateValues().LoadXml(localElement);
          else
            getCertificateValues().GetXml(localElement);
        localVector.add("CertificateValues");
        localVector.add("http://uri.etsi.org/01903/v1.1.1#");
        if ((localElement = EnsureElementBefore(this.fUnsigedSignatureProperties, "RevocationValues", "http://uri.etsi.org/01903/v1.1.1#", findAfter(localVector, this.fUnsigedSignatureProperties), (!paramBoolean) && (getRevocationValues().getCount() != 0), (!paramBoolean) && (getRevocationValues().getCount() == 0))) != null)
          this.fRevocationValuesId = EnsureAttribute(localElement, "Id", this.fRevocationValuesId, paramBoolean, false);
        if (localElement != null)
          localElement = EnsureElement(localElement, "CRLValues", "http://uri.etsi.org/01903/v1.1.1#");
        if (localElement != null)
        {
          if (paramBoolean)
          {
            getRevocationValues().LoadXml(localElement);
            return;
          }
          getRevocationValues().GetXml(localElement);
        }
      }
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateCertAndRevocationValues(IHSLSignature paramIHSLSignature, String paramString)
    throws EXAdESException, ESignDocException, CertificateEncodingException, Base64DecodingException
  {
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSigningCertificateB64()))
      throw new ESignDocException(32);
    return SetStatus(validateCertAndRevocationValuesFromB64(paramIHSLSignature.getSigningCertificateB64(), paramString), 4);
  }

  private final VerifyStatus validateCertAndRevocationValuesFromB64(String paramString1, String paramString2)
    throws ESignDocException, CertificateEncodingException, EXAdESException, Base64DecodingException
  {
    if (getCertificateValues().getCount() == 0)
      return new VerifyStatus("CERTIFICATE_VALUES_EMPTY", "");
    Date localDate = getDateFromTimestamp();
    CertificateChain localCertificateChain;
    (localCertificateChain = new CertificateChain()).init(paramString1, localDate, false, paramString2);
    if (getCertificateValues().getCount() != localCertificateChain.getCertCount())
      return new VerifyStatus("NUMBER_OF_CERTIFICATES_INCORRECT", "");
    if (getRevocationValues().getCount() < getCertificateValues().getCount() - 1)
      return new VerifyStatus("NUBER_OF_REVOCATION_LIST_INCORRECT", "");
    RevocationRefImpl localRevocationRefImpl = new RevocationRefImpl();
    for (int j = 0; j < localCertificateChain.getCertCount(); j++)
    {
      int i = 0;
      for (int k = 0; k < getCertificateValues().getCount(); k++)
        try
        {
          byte[] arrayOfByte1 = Utility.calculateDigest(Base64.decode(getCertificateValues().getItem(k).getValue()), "http://www.w3.org/2000/09/xmldsig#sha1");
          byte[] arrayOfByte2 = Utility.calculateDigest(Base64.decode(localCertificateChain.getCertB64(j)), "http://www.w3.org/2000/09/xmldsig#sha1");
          if (Arrays.equals(arrayOfByte1, arrayOfByte2))
          {
            if (localCertificateChain.getCertStatus(j) != CertificateChain.CERT_VALID)
              return new VerifyStatus("CREDENTIALS ARE INVALID", "Certificate verification status:" + localCertificateChain.getCertStatus(j));
            i = 1;
            break;
          }
        }
        catch (Exception localException)
        {
          return new VerifyStatus("CREDENTIALS ARE INVALID", "Certificate verification status:" + localCertificateChain.getCertStatus(j));
        }
      if (i == 0)
        return new VerifyStatus("REFERENCES_NOT_VALID", "CREDENTIALS NOT FOUND");
      if (j >= localCertificateChain.getCertCount() - 1)
        continue;
      i = 0;
      for (k = 0; j < getRevocationValues().getCount(); k++)
      {
        localRevocationRefImpl.createRevocationRefFromB64(getRevocationValues().getItem(k).getValue(), "http://www.w3.org/2000/09/xmldsig#sha1");
        if (!localCertificateChain.getCertIssuerDN(j).equals(localRevocationRefImpl.getIssuer()))
          continue;
        int m = 0;
        for (int n = 0; n < localCertificateChain.getCertCount(); n++)
        {
          if (!localCertificateChain.getCertSubjectDN(n).equals(localRevocationRefImpl.getIssuer()))
            continue;
          m = 1;
          break;
        }
        if (m == 0)
          return new VerifyStatus("REFERENCES_NOT_VALID", "Certificate of revocation list issuer not found.");
        if (!Utility.UtilCheckCertRevocation(localCertificateChain.getCertB64(j), localCertificateChain.getCertB64(n), localRevocationRefImpl.getCRL(), new Date()))
          return new VerifyStatus("REVOCATION_LIST_INVALID", "Certificate was revoked.");
        i = 1;
        break;
      }
      if (i == 0)
        return new VerifyStatus("REFERENCES_NOT_VALID", "Revocation list in chain not found.");
    }
    return new VerifyStatus("VALID", "");
  }

  protected final String getRevocationValue(String paramString, CertificateChain paramCertificateChain, int paramInt)
    throws ESignDocException, Base64DecodingException, EXAdESException
  {
    for (int i = 0; i < getRevocationValues().getCount(); i++)
    {
      byte[] arrayOfByte = Base64.decode(getRevocationValues().getItem(i).getValue());
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
      String str = localX509CRL.getIssuerDN().getName();
      if (Utility.normalizeDN(paramCertificateChain.getCertIssuerDN(paramInt)).equals(Utility.normalizeDN(str)))
        return getRevocationValues().getItem(i).getValue();
    }
    return super.getRevocationValue(paramString, paramCertificateChain, paramInt);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdES_X_LImpl
 * JD-Core Version:    0.6.0
 */