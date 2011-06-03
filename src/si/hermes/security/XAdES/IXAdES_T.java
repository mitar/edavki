package si.hermes.security.XAdES;

import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES_T extends IXAdES
{
  public abstract int addTimestamp(ITimestamp paramITimestamp)
    throws ESignDocException;

  public abstract int createTimestamp(IHSLSignature paramIHSLSignature)
    throws ESignDocException, EXAdESException;

  public abstract ITimestamps getSignatureTimeStamps();

  public abstract VerifyStatus validateTimeStamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES_T
 * JD-Core Version:    0.6.0
 */