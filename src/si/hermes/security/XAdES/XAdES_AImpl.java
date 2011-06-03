package si.hermes.security.XAdES;

import java.io.IOException;
import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IHashDataInfo;
import si.hermes.security.Collections.IHashDataInfos;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.IReferences;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.Collections.ITransform;
import si.hermes.security.Collections.ITransformChain;
import si.hermes.security.Collections.Transform;
import si.hermes.security.ESignDocException;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public final class XAdES_AImpl extends XAdES_X_LImpl
  implements IXAdES_A
{
  private static final long serialVersionUID = -6638041603337812310L;
  ITimestamps fArchiveTimeStamps;
  private int fHashCount;

  public final int addArchiveTimeStamp(ITimestamp paramITimestamp)
  {
    return getArchiveTimeStamps().Add(paramITimestamp);
  }

  private int addHashDataInfoUri(ITimestamp paramITimestamp1, ITimestamp paramITimestamp2, String paramString, boolean paramBoolean)
    throws EXAdESException
  {
    if (paramBoolean)
      return paramITimestamp2.addHashDataInfoUri(paramString);
    if (this.fHashCount >= paramITimestamp1.getHashDataInfos().getCount())
      throw new EXAdESException(10, paramString);
    if (!paramString.equals(paramITimestamp1.getHashDataInfos().getItem(this.fHashCount).getUri()))
      throw new EXAdESException(10, paramString + ":" + paramITimestamp1.getHashDataInfos().getItem(this.fHashCount).getUri());
    this.fHashCount += 1;
    return -1;
  }

  public final int createArchiveTimeStamp(IHSLSignature paramIHSLSignature)
    throws EXAdESException, ESignDocException, IOException
  {
    return createOrCheckArchiveTimeStamp(null, paramIHSLSignature, true, getArchiveTimeStamps().getCount());
  }

  private int createOrCheckArchiveTimeStamp(ITimestamp paramITimestamp, IHSLSignature paramIHSLSignature, boolean paramBoolean, int paramInt)
    throws EXAdESException, ESignDocException, IOException
  {
    this.fHashCount = 0;
    ArchiveTimestampImpl localArchiveTimestampImpl = null;
    if (paramBoolean)
      (localArchiveTimestampImpl = new ArchiveTimestampImpl()).setId("ArchiveTimeStamp-" + Utility.generateRandom(30));
    if ((getCompleteCertificateRefs().getCount() == 0) || (getCompleteRevocationRefs().getCount() == 0))
      throw new EXAdESException(5);
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSignedInfo().getId()))
      throw new EXAdESException(9);
    if (getSignatureTimeStamps().getCount() == 0)
      throw new EXAdESException(7);
    for (int i = 0; i < paramIHSLSignature.getSignedInfo().getReferences().getCount(); i++)
    {
      int j = addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getUri(), paramBoolean);
      int k;
      if (paramBoolean)
      {
        for (k = 0; k < paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getTransformChain().getCount(); k++)
        {
          Transform localTransform;
          (localTransform = new Transform()).LoadXml((Element)paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getTransformChain().getItem(k).GetXml(null).cloneNode(true));
          localArchiveTimestampImpl.getHashDataInfos().getItem(j).addTransform(localTransform);
        }
      }
      else
      {
        if (paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getTransformChain().getCount() != paramITimestamp.getHashDataInfos().getItem(this.fHashCount).getTransforms().getCount())
          throw new EXAdESException(10, paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getUri());
        for (k = 0; k < paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getTransformChain().getCount(); k++)
        {
          if (paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getTransformChain().getItem(k).getToString() == paramITimestamp.getHashDataInfos().getItem(this.fHashCount).getTransforms().getItem(k).getToString())
            continue;
          throw new EXAdESException(10, paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getUri());
        }
      }
    }
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + paramIHSLSignature.getSignedInfo().getId(), paramBoolean);
    if (IsNullOrEmpty(getSignedId()))
      setSignedId("SignedSignaturePropertiesId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getSignedId(), paramBoolean);
    if (getDataObjectFormats().getCount() != 0)
    {
      if (IsNullOrEmpty(getSignedDataId()))
        setSignedDataId("SignedDataObjectPropertiesId-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getSignedDataId(), paramBoolean);
    }
    if (IsNullOrEmpty(paramIHSLSignature.getSignatureValueId()))
    {
      paramIHSLSignature.setSignatureValueId("SignatureValueId-" + Utility.generateRandom(30));
      paramIHSLSignature.GetXml(null);
    }
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + paramIHSLSignature.getSignatureValueId(), paramBoolean);
    for (i = 0; i < getSignatureTimeStamps().getCount(); i++)
    {
      if (IsNullOrEmpty(getSignatureTimeStamps().getItem(i).getId()))
        getSignatureTimeStamps().getItem(i).setId("SignatureTimeStamps-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getSignatureTimeStamps().getItem(i).getId(), paramBoolean);
    }
    if (IsNullOrEmpty(getCompleteCertificateId()))
      setCompleteCertificateId("CompleteCertificateId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getCompleteCertificateId(), paramBoolean);
    if (IsNullOrEmpty(getCompleteRevocationId()))
      setCompleteRevocationId("CompleteRevocationId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getCompleteRevocationId(), paramBoolean);
    if (IsNullOrEmpty(getCertificateValuesId()))
      setCertificateValuesId("CertificateValuesId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getCertificateValuesId(), paramBoolean);
    if (IsNullOrEmpty(getRevocationValuesId()))
      setRevocationValuesId("RevocationValuesId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getRevocationValuesId(), paramBoolean);
    for (i = 0; i < getSigAndRefsTimeStamps().getCount(); i++)
    {
      if (IsNullOrEmpty(getSigAndRefsTimeStamps().getItem(i).getId()))
        getSigAndRefsTimeStamps().getItem(i).setId("SigAndRefsTimeStamps-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getSigAndRefsTimeStamps().getItem(i).getId(), paramBoolean);
    }
    for (i = 0; i < getRefsOnlyTimeStamps().getCount(); i++)
    {
      if (IsNullOrEmpty(getRefsOnlyTimeStamps().getItem(i).getId()))
        getRefsOnlyTimeStamps().getItem(i).setId("RefsOnlyTimeStamps-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getRefsOnlyTimeStamps().getItem(i).getId(), paramBoolean);
    }
    if (paramInt > getArchiveTimeStamps().getCount())
      throw new EXAdESException(2, "num >= getArchiveTimeStamps.Count");
    for (i = 0; i < paramInt; i++)
    {
      if (IsNullOrEmpty(getArchiveTimeStamps().getItem(i).getId()))
        getArchiveTimeStamps().getItem(i).setId("ArchiveTimeStamps-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localArchiveTimestampImpl, "#" + getArchiveTimeStamps().getItem(i).getId(), paramBoolean);
    }
    if (paramBoolean)
      return addArchiveTimeStamp(localArchiveTimestampImpl);
    if (paramITimestamp.getHashDataInfos().getCount() != this.fHashCount)
      throw new EXAdESException(10);
    return -1;
  }

  public final ITimestamps getArchiveTimeStamps()
  {
    if (this.fArchiveTimeStamps == null)
      this.fArchiveTimeStamps = new ArchiveTimestampsImpl();
    return this.fArchiveTimeStamps;
  }

  protected final boolean isThereAnyUnsignedProperties()
  {
    return (super.isThereAnyUnsignedProperties()) || ((this.fArchiveTimeStamps != null) && (this.fArchiveTimeStamps.getCount() != 0));
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    super.Synchronize(paramBoolean);
    if (this.fUnsigedSignatureProperties != null)
    {
      if (paramBoolean)
      {
        getArchiveTimeStamps().LoadXml(this.fUnsigedSignatureProperties);
        return;
      }
      getArchiveTimeStamps().GetXml(this.fUnsigedSignatureProperties);
    }
  }

  public final VerifyStatus validateArchiveTimestamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature)
  {
    for (int i = 0; i < getArchiveTimeStamps().getCount(); i++)
    {
      ITimestamp localITimestamp = getArchiveTimeStamps().getItem(i);
      try
      {
        createOrCheckArchiveTimeStamp(localITimestamp, paramIHSLSignature, false, i);
        localITimestamp.setProvider(paramITimestampProvider);
        localObject = localITimestamp.validate();
        if (!"VALID".equals(((VerifyStatus)localObject).getStatus()))
          return SetStatus((VerifyStatus)localObject, 5);
      }
      catch (Exception localException)
      {
        Object localObject;
        (localObject = localException).printStackTrace();
        return SetStatus(new VerifyStatus("STRUCTINVALID", localITimestamp.getId()), 5);
      }
    }
    return (VerifyStatus)SetStatus(new VerifyStatus("VALID", ""), 5);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdES_AImpl
 * JD-Core Version:    0.6.0
 */