package si.hermes.security.XAdES;

import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IHashDataInfo;
import si.hermes.security.Collections.IHashDataInfos;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public class XAdES_XImpl extends XAdES_CImpl
  implements IXAdES_X
{
  private static final long serialVersionUID = 5182347032419873634L;
  private int fHashCount;
  ITimestamps fSigAndRefsTimeStamps;
  ITimestamps fRefsOnlyTimeStamps;

  private final void addHashDataInfoUri(ITimestamp paramITimestamp1, ITimestamp paramITimestamp2, String paramString, boolean paramBoolean)
    throws EXAdESException
  {
    if (paramBoolean)
    {
      paramITimestamp2.addHashDataInfoUri(paramString);
    }
    else
    {
      if (this.fHashCount >= paramITimestamp1.getHashDataInfos().getCount())
        throw new EXAdESException(10, paramString);
      if (!paramString.equals(paramITimestamp1.getHashDataInfos().getItem(this.fHashCount).getUri()))
        throw new EXAdESException(10, paramString + ":" + paramITimestamp1.getHashDataInfos().getItem(this.fHashCount).getUri());
    }
    this.fHashCount += 1;
  }

  public final int addRefsOnlyTimeStamp(ITimestamp paramITimestamp)
  {
    return getRefsOnlyTimeStamps().Add(paramITimestamp);
  }

  public final int addSigAndRefsTimeStamp(ITimestamp paramITimestamp)
  {
    return getSigAndRefsTimeStamps().Add(paramITimestamp);
  }

  private final int CreateOrCheckRefsOnlyTimeStamp(ITimestamp paramITimestamp, boolean paramBoolean)
    throws EXAdESException, ESignDocException
  {
    RefsOnlyTimestampImpl localRefsOnlyTimestampImpl = null;
    this.fHashCount = 0;
    if (paramBoolean)
      (localRefsOnlyTimestampImpl = new RefsOnlyTimestampImpl()).setId("RefsOnlyTimeStamp-" + Utility.generateRandom(30));
    if ((getCompleteCertificateRefs().getCount() == 0) && (getCompleteRevocationRefs().getCount() == 0))
      throw new EXAdESException(5);
    if (IsNullOrEmpty(getCompleteCertificateId()))
      setCompleteCertificateId("CompleteCertificateId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localRefsOnlyTimestampImpl, "#" + getCompleteCertificateId(), paramBoolean);
    if (IsNullOrEmpty(getCompleteRevocationId()))
      setCompleteRevocationId("CompleteRevocationId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localRefsOnlyTimestampImpl, "#" + getCompleteRevocationId(), paramBoolean);
    if (paramBoolean)
      return addRefsOnlyTimeStamp(localRefsOnlyTimestampImpl);
    if (paramITimestamp.getHashDataInfos().getCount() != this.fHashCount)
      throw new EXAdESException(10);
    return -1;
  }

  private final int CreateOrCheckSigAndRefsTimeStamp(ITimestamp paramITimestamp, IHSLSignature paramIHSLSignature, boolean paramBoolean)
    throws EXAdESException, ESignDocException
  {
    SigAndRefsTimestampImpl localSigAndRefsTimestampImpl = null;
    this.fHashCount = 0;
    if (paramBoolean)
      (localSigAndRefsTimestampImpl = new SigAndRefsTimestampImpl()).setId("SigAndRefsTimeStamp-" + Utility.generateRandom(30));
    if ((getCompleteCertificateRefs().getCount() == 0) && (getCompleteRevocationRefs().getCount() == 0))
      throw new EXAdESException(5);
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (getSignatureTimeStamps().getCount() == 0)
      throw new EXAdESException(7);
    if (IsNullOrEmpty(paramIHSLSignature.getSignatureValueId()))
    {
      paramIHSLSignature.setSignatureValueId("SignatureValueId-" + Utility.generateRandom(30));
      paramIHSLSignature.GetXml(null);
    }
    addHashDataInfoUri(paramITimestamp, localSigAndRefsTimestampImpl, "#" + paramIHSLSignature.getSignatureValueId(), paramBoolean);
    for (int i = 0; i < getSignatureTimeStamps().getCount(); i++)
    {
      if (IsNullOrEmpty(getSignatureTimeStamps().getItem(i).getId()))
        getSignatureTimeStamps().getItem(i).setId("SignatureTimeStamp-" + Utility.generateRandom(30));
      addHashDataInfoUri(paramITimestamp, localSigAndRefsTimestampImpl, '#' + getSignatureTimeStamps().getItem(i).getId(), paramBoolean);
    }
    if (IsNullOrEmpty(getCompleteCertificateId()))
      setCompleteCertificateId("CompleteCertificateId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localSigAndRefsTimestampImpl, "#" + getCompleteCertificateId(), paramBoolean);
    if (IsNullOrEmpty(getCompleteRevocationId()))
      setCompleteRevocationId("CompleteRevocationId-" + Utility.generateRandom(30));
    addHashDataInfoUri(paramITimestamp, localSigAndRefsTimestampImpl, "#" + getCompleteRevocationId(), paramBoolean);
    if (paramBoolean)
      return addSigAndRefsTimeStamp(localSigAndRefsTimestampImpl);
    if (paramITimestamp.getHashDataInfos().getCount() != this.fHashCount)
      throw new EXAdESException(10);
    return -1;
  }

  public final int createRefsOnlyTimeStamp()
    throws EXAdESException, ESignDocException
  {
    return CreateOrCheckRefsOnlyTimeStamp(null, true);
  }

  public final int createSigAndRefsTimeStamp(IHSLSignature paramIHSLSignature)
    throws EXAdESException, ESignDocException
  {
    return CreateOrCheckSigAndRefsTimeStamp(null, paramIHSLSignature, true);
  }

  public final ITimestamps getRefsOnlyTimeStamps()
  {
    if (this.fRefsOnlyTimeStamps == null)
      this.fRefsOnlyTimeStamps = new RefsOnlyTimeStampsImpl();
    return this.fRefsOnlyTimeStamps;
  }

  public final ITimestamps getSigAndRefsTimeStamps()
  {
    if (this.fSigAndRefsTimeStamps == null)
      this.fSigAndRefsTimeStamps = new SigAndRefsTimeStampsImpl();
    return this.fSigAndRefsTimeStamps;
  }

  protected boolean isThereAnyUnsignedProperties()
  {
    return (super.isThereAnyUnsignedProperties()) || ((this.fSigAndRefsTimeStamps != null) && (this.fSigAndRefsTimeStamps.getCount() != 0)) || ((this.fRefsOnlyTimeStamps != null) && (this.fRefsOnlyTimeStamps.getCount() != 0));
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      super.Synchronize(paramBoolean);
      if (this.fUnsigedSignatureProperties != null)
      {
        if (paramBoolean)
          getSigAndRefsTimeStamps().LoadXml(this.fUnsigedSignatureProperties);
        else
          getSigAndRefsTimeStamps().GetXml(this.fUnsigedSignatureProperties);
        if (paramBoolean)
          getRefsOnlyTimeStamps().LoadXml(this.fUnsigedSignatureProperties);
        else
          getRefsOnlyTimeStamps().GetXml(this.fUnsigedSignatureProperties);
      }
      else
      {
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateSigAndRefsTimestamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature)
  {
    ITimestamp localITimestamp;
    Object localObject;
    for (int i = 0; i < getSigAndRefsTimeStamps().getCount(); i++)
    {
      localITimestamp = getSigAndRefsTimeStamps().getItem(i);
      try
      {
        CreateOrCheckSigAndRefsTimeStamp(localITimestamp, paramIHSLSignature, false);
        localITimestamp.setProvider(paramITimestampProvider);
        localObject = localITimestamp.validate();
        if (!"VALID".equals(((VerifyStatus)localObject).getStatus()))
          return SetStatus((VerifyStatus)localObject, 3);
      }
      catch (Exception localException1)
      {
        return SetStatus(new VerifyStatus("STRUCTINVALID", localITimestamp.getId()), 3);
      }
    }
    for (i = 0; i < getRefsOnlyTimeStamps().getCount(); i++)
    {
      localITimestamp = getRefsOnlyTimeStamps().getItem(i);
      try
      {
        CreateOrCheckRefsOnlyTimeStamp(localITimestamp, false);
        localITimestamp.setProvider(paramITimestampProvider);
        localObject = localITimestamp.validate();
        if (!"VALID".equals(((VerifyStatus)localObject).getStatus()))
          return SetStatus((VerifyStatus)localObject, 3);
      }
      catch (Exception localException2)
      {
        (localObject = localException2).printStackTrace();
        return SetStatus(new VerifyStatus("STRUCTINVALID", localITimestamp.getId()), 3);
      }
    }
    return (VerifyStatus)SetStatus(new VerifyStatus("VALID", ""), 3);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdES_XImpl
 * JD-Core Version:    0.6.0
 */