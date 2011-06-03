package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IHashDataInfo;
import si.hermes.security.Collections.IHashDataInfos;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.Collections.TimestampImpl;
import si.hermes.security.Collections.TimestampsImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;

public class XAdES_TImpl extends XAdESImpl
  implements IXAdES_T
{
  private static final long serialVersionUID = -8038439122096168304L;
  private int fHashCount;
  ITimestamps fTimestamps;

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

  public final int addTimestamp(ITimestamp paramITimestamp)
    throws ESignDocException
  {
    int i = getSignatureTimeStamps().Add(paramITimestamp);
    if (this.fNode != null)
      paramITimestamp.GetXml(Utility.CreateElement(this.fNode.getOwnerDocument(), "SignatureTimeStamp", "http://uri.etsi.org/01903/v1.1.1#"));
    return i;
  }

  private final int createOrCheckTimestamp(ITimestamp paramITimestamp, IHSLSignature paramIHSLSignature, boolean paramBoolean)
    throws ESignDocException, EXAdESException
  {
    TimestampImpl localTimestampImpl = null;
    this.fHashCount = 0;
    if (paramBoolean)
      (localTimestampImpl = new TimestampImpl()).setId("SignatureTimeStamp-" + Utility.generateRandom(30));
    if (paramIHSLSignature == null)
      throw new EXAdESException(6);
    if (IsNullOrEmpty(paramIHSLSignature.getSignatureValueId()))
    {
      paramIHSLSignature.setSignatureValueId("SignatureValueId-" + Utility.generateRandom(30));
      paramIHSLSignature.GetXml(null);
    }
    addHashDataInfoUri(paramITimestamp, localTimestampImpl, "#" + paramIHSLSignature.getSignatureValueId(), paramBoolean);
    if (paramBoolean)
      return addTimestamp(localTimestampImpl);
    if (paramITimestamp.getHashDataInfos().getCount() != this.fHashCount)
      throw new EXAdESException(10);
    return -1;
  }

  public final int createTimestamp(IHSLSignature paramIHSLSignature)
    throws ESignDocException, EXAdESException
  {
    return createOrCheckTimestamp(null, paramIHSLSignature, true);
  }

  public final ITimestamps getSignatureTimeStamps()
  {
    if (this.fTimestamps == null)
      this.fTimestamps = new TimestampsImpl();
    return this.fTimestamps;
  }

  protected boolean isThereAnyUnsignedProperties()
  {
    return (super.isThereAnyUnsignedProperties()) || ((this.fTimestamps != null) && (this.fTimestamps.getCount() != 0));
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
          getSignatureTimeStamps().LoadXml(this.fUnsigedSignatureProperties);
        else
          getSignatureTimeStamps().GetXml(this.fUnsigedSignatureProperties);
      }
      else
        return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validateTimeStamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature)
  {
    for (int i = 0; i < getSignatureTimeStamps().getCount(); i++)
    {
      ITimestamp localITimestamp = getSignatureTimeStamps().getItem(i);
      try
      {
        createOrCheckTimestamp(localITimestamp, paramIHSLSignature, false);
        localITimestamp.setProvider(paramITimestampProvider);
        localObject = localITimestamp.validate();
        if (!"VALID".equals(((VerifyStatus)localObject).getStatus()))
          return SetStatus((VerifyStatus)localObject, 1);
      }
      catch (Exception localException)
      {
        Object localObject;
        (localObject = localException).printStackTrace();
        return SetStatus(new VerifyStatus("STRUCTINVALID", localITimestamp.getId()), 1);
      }
    }
    return (VerifyStatus)SetStatus(new VerifyStatus("VALID", ""), 1);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.XAdES_TImpl
 * JD-Core Version:    0.6.0
 */