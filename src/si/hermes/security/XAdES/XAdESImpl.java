package si.hermes.security.XAdES;

import java.io.IOException;
import java.util.Date;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.IReferences;
import si.hermes.security.Collections.ISignatures;
import si.hermes.security.Collections.IXmlStringList;
import si.hermes.security.ESignDocException;
import si.hermes.security.ISODateFormat;
import si.hermes.security.KeyInfo.IKeyInfo;
import si.hermes.security.KeyInfo.IKeyInfoX509Data;
import si.hermes.security.PersistableImpl;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;
import si.hermes.security.paramHelper;

public class XAdESImpl extends PersistableImpl
  implements IXAdES
{
  private static final long serialVersionUID = 1766598472984379948L;
  ISignatures fCounterSignatures;
  IDataObjectFormats fDataObjectFormats;
  String fId;
  String fSignedDataId;
  String fSignedId;
  String fTarget;
  String fSigningTime = ISODateFormat.DateTime2ISOZ(new Date());
  ISignaturePolicyIdentifier fSignaturePolicyIdentifier;
  ISignatureProductionPlace fSignatureProductionPlace;
  ISignerRole fSignerRole;
  ICertificateList fSigningCertificates;
  Element fUnsigedSignatureProperties;
  VerifyStatus[] fStatus = new VerifyStatus[6];

  protected final VerifyStatus SetStatus(VerifyStatus paramVerifyStatus, int paramInt)
  {
    this.fStatus[paramInt] = paramVerifyStatus;
    return paramVerifyStatus;
  }

  public VerifyStatus status(int paramInt)
    throws ESignDocException
  {
    if ((paramInt < 0) || (paramInt > 5))
      throw new ESignDocException(3);
    VerifyStatus localVerifyStatus;
    if ((localVerifyStatus = this.fStatus[paramInt]) == null)
      throw new ESignDocException(28);
    return localVerifyStatus;
  }

  public final int addCounterSignature(IHSLSignature paramIHSLSignature)
  {
    CounterSignatureImpl localCounterSignatureImpl;
    (localCounterSignatureImpl = new CounterSignatureImpl()).setValue(paramIHSLSignature);
    return getCounterSignatures().Add(localCounterSignatureImpl);
  }

  public final int addDataObjectFormat(IDataObjectFormat paramIDataObjectFormat)
  {
    return getDataObjectFormats().Add(paramIDataObjectFormat);
  }

  public final int addSigningCertificate(ICertificate paramICertificate)
  {
    return getSigningCertificates().Add(paramICertificate);
  }

  public final void createSignedData(IHSLSignature paramIHSLSignature, String paramString1, String paramString2, Date paramDate)
    throws ESignDocException, EXAdESException
  {
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(getSignedId()))
      setSignedId("SignedId-" + Utility.generateRandom(30));
    if (IsNullOrEmpty(paramIHSLSignature.getId()))
      paramIHSLSignature.setId("Signature-" + Utility.generateRandom(30));
    setTarget("#" + paramIHSLSignature.getId());
    if (!FindReferenceUri(paramIHSLSignature, getSignedId()))
    {
      int i = paramIHSLSignature.getSignedInfo().AddReferenceUri("#" + getSignedId());
      paramIHSLSignature.getSignedInfo().getReferences().getItem(i).setType("http://uri.etsi.org/01903/v1.1.1#SignedProperties");
    }
    SigningCertificateImpl localSigningCertificateImpl;
    (localSigningCertificateImpl = new SigningCertificateImpl()).createCertificateFromB64(paramString1, paramString2);
    addSigningCertificate(localSigningCertificateImpl);
    setSigningTime(paramDate);
  }

  private final boolean FindReferenceUri(IHSLSignature paramIHSLSignature, String paramString)
  {
    if (paramString == null)
      return false;
    for (int i = 0; i < paramIHSLSignature.getSignedInfo().getReferences().getCount(); i++)
      if (paramString.equals(paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getUri()))
        return true;
    return false;
  }

  public final ISignatures getCounterSignatures()
  {
    if (this.fCounterSignatures == null)
      this.fCounterSignatures = new CounterSignaturesImpl();
    return this.fCounterSignatures;
  }

  public final IDataObjectFormats getDataObjectFormats()
  {
    if (this.fDataObjectFormats == null)
      this.fDataObjectFormats = new DataObjectFormatsImpl();
    return this.fDataObjectFormats;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final ISignaturePolicyIdentifier getSignaturePolicyIdentifier()
  {
    if (this.fSignaturePolicyIdentifier == null)
      this.fSignaturePolicyIdentifier = new SignaturePolicyIdentifierImpl();
    return this.fSignaturePolicyIdentifier;
  }

  public final ISignatureProductionPlace getSignatureProductionPlace()
  {
    return this.fSignatureProductionPlace;
  }

  public final String getSignedDataId()
  {
    return this.fSignedDataId;
  }

  public final String getSignedId()
  {
    return this.fSignedId;
  }

  public final ISignerRole getSignerRole()
  {
    return this.fSignerRole;
  }

  public final ICertificateList getSigningCertificates()
  {
    if (this.fSigningCertificates == null)
      this.fSigningCertificates = new SigningCertificatesImpl();
    return this.fSigningCertificates;
  }

  public final Date getSigningTime()
    throws ESignDocException
  {
    return ISODateFormat.ISO2DateTime(this.fSigningTime);
  }

  public final String getTarget()
  {
    return this.fTarget;
  }

  protected boolean isThereAnyUnsignedProperties()
  {
    return (this.fCounterSignatures != null) && (this.fCounterSignatures.getCount() != 0);
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final void setSignaturePolicyIdentifier(ISignaturePolicyIdentifier paramISignaturePolicyIdentifier)
  {
    this.fSignaturePolicyIdentifier = paramISignaturePolicyIdentifier;
  }

  public final void setSignatureProductionPlace(ISignatureProductionPlace paramISignatureProductionPlace)
  {
    this.fSignatureProductionPlace = paramISignatureProductionPlace;
  }

  public final void setSignedDataId(String paramString)
  {
    this.fSignedDataId = paramString;
  }

  public final void setSignedId(String paramString)
  {
    this.fSignedId = paramString;
  }

  public final void setSignerRole(ISignerRole paramISignerRole)
  {
    this.fSignerRole = paramISignerRole;
  }

  public final void setSigningTime(Date paramDate)
  {
    this.fSigningTime = ISODateFormat.DateTime2ISOZ(paramDate);
  }

  public final void setTarget(String paramString)
  {
    this.fTarget = paramString;
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "QualifyingProperties", "http://uri.etsi.org/01903/v1.1.1#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      this.fTarget = EnsureAttribute(this.fNode, "Target", this.fTarget, paramBoolean, true);
      Element localElement1 = EnsureElement(this.fNode, "SignedProperties", "http://uri.etsi.org/01903/v1.1.1#");
      this.fSignedId = EnsureAttribute(localElement1, "Id", this.fSignedId, paramBoolean, false);
      Element localElement2 = EnsureElement(localElement1, "SignedSignatureProperties", "http://uri.etsi.org/01903/v1.1.1#", true);
      Element localElement3 = EnsureElement(localElement2, "SigningTime", "http://uri.etsi.org/01903/v1.1.1#", true);
      this.fSigningTime = EnsureValue(localElement3, this.fSigningTime, paramBoolean);
      localElement3 = EnsureElement(localElement2, "SigningCertificate", "http://uri.etsi.org/01903/v1.1.1#", true);
      if (paramBoolean)
        getSigningCertificates().LoadXml(localElement3);
      else
        getSigningCertificates().GetXml(localElement3);
      localElement3 = EnsureElement(localElement2, "SignaturePolicyIdentifier", "http://uri.etsi.org/01903/v1.1.1#", true);
      if (paramBoolean)
        getSignaturePolicyIdentifier().LoadXml(localElement3);
      else
        getSignaturePolicyIdentifier().GetXml(localElement3);
      paramHelper localparamHelper;
      Element localElement4;
      if (paramBoolean)
      {
        localElement3 = localElement4 = (localparamHelper = ensureElementAfter(localElement2, "SignatureProductionPlace", "http://uri.etsi.org/01903/v1.1.1#", localElement3, false, false)).elem1;
        if (localElement4 != null)
        {
          this.fSignatureProductionPlace = new SignatureProductionPlaceImpl();
          this.fSignatureProductionPlace.LoadXml(localElement4);
        }
      }
      else
      {
        localElement3 = localElement4 = (localparamHelper = ensureElementAfter(localElement2, "SignatureProductionPlace", "http://uri.etsi.org/01903/v1.1.1#", localElement3, this.fSignatureProductionPlace != null, this.fSignatureProductionPlace == null)).elem1;
        if (this.fSignatureProductionPlace != null)
          this.fSignatureProductionPlace.GetXml(localElement4);
      }
      if (paramBoolean)
      {
        if ((localElement4 = (localparamHelper = ensureElementAfter(localElement2, "SignerRole", "http://uri.etsi.org/01903/v1.1.1#", localElement3, false, false)).elem1) != null)
        {
          this.fSignerRole = new SignerRoleImpl();
          this.fSignerRole.LoadXml(localElement4);
        }
      }
      else
      {
        localElement4 = (localparamHelper = ensureElementAfter(localElement2, "SignerRole", "http://uri.etsi.org/01903/v1.1.1#", localElement3, this.fSignerRole != null, this.fSignerRole == null)).elem1;
        if (this.fSignerRole != null)
          this.fSignerRole.GetXml(localElement4);
      }
      if ((localElement2 = (localparamHelper = ensureElementAfter(localElement1, "SignedDataObjectProperties", "http://uri.etsi.org/01903/v1.1.1#", localElement2, (!paramBoolean) && (getDataObjectFormats().getCount() != 0), (!paramBoolean) && (getDataObjectFormats().getCount() == 0))).elem1) != null)
      {
        this.fSignedDataId = EnsureAttribute(localElement2, "Id", this.fSignedDataId, paramBoolean, false);
        if (paramBoolean)
          getDataObjectFormats().LoadXml(localElement2);
        else
          getDataObjectFormats().GetXml(localElement2);
      }
      if ((localElement1 = (localparamHelper = ensureElementAfter(this.fNode, "UnsignedProperties", "http://uri.etsi.org/01903/v1.1.1#", localElement1, (!paramBoolean) && (isThereAnyUnsignedProperties()), (!paramBoolean) && (!isThereAnyUnsignedProperties()))).elem1) != null)
      {
        this.fUnsigedSignatureProperties = EnsureElement(localElement1, "UnsignedSignatureProperties", "http://uri.etsi.org/01903/v1.1.1#", true);
        if (paramBoolean)
          getCounterSignatures().LoadXml(this.fUnsigedSignatureProperties);
        else
          getCounterSignatures().GetXml(this.fUnsigedSignatureProperties);
      }
      else
      {
        this.fUnsigedSignatureProperties = null;
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateSignedData(IHSLSignature paramIHSLSignature)
    throws ESignDocException, XMLSignatureException, IOException, XMLSecurityException, TransformerException
  {
    IKeyInfoX509Data localIKeyInfoX509Data;
    if ((localIKeyInfoX509Data = paramIHSLSignature.getKeyInfo().getX509Data(false)) == null)
      throw new ESignDocException(0, "TXAdES.ValidateSignedData.1");
    if (getSigningCertificates().getCount() == 0)
      return SetStatus(new VerifyStatus("CREDENTIALS NOT FOUND", ""), 0);
    if (localIKeyInfoX509Data.getCertificate().getCount() != getSigningCertificates().getCount())
      throw new ESignDocException(0, "TXAdES.ValidateSignedData.2");
    if (!FindReferenceUri(paramIHSLSignature, "#" + getSignedId()))
      throw new ESignDocException(31, getSignedId());
    VerifyStatus localVerifyStatus;
    for (int i = 0; i < localIKeyInfoX509Data.getCertificate().getCount(); i++)
    {
      localVerifyStatus = getSigningCertificates().getItem(i).validateCertificate(localIKeyInfoX509Data.getCertificate().getItem(i));
      if (!"VALID CREDENTIALS".equals(localVerifyStatus.getStatus()))
        return SetStatus(localVerifyStatus, 0);
    }
    for (i = 0; i < getCounterSignatures().getCount(); i++)
    {
      localVerifyStatus = getCounterSignatures().getItem(i).Validate();
      if ("VALID".equals(localVerifyStatus.getStatus()))
        return SetStatus(localVerifyStatus, 0);
    }
    for (i = 0; i < getDataObjectFormats().getCount(); i++)
    {
      localVerifyStatus = getDataObjectFormats().getItem(i).validate(paramIHSLSignature);
      if ("VALID".equals(localVerifyStatus.getStatus()))
        return SetStatus(localVerifyStatus, 0);
    }
    return SetStatus(new VerifyStatus("VALID", ""), 0);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdESImpl
 * JD-Core Version:    0.6.0
 */