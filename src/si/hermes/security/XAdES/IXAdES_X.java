package si.hermes.security.XAdES;

import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES_X extends IXAdES_C
{
  public abstract int addRefsOnlyTimeStamp(ITimestamp paramITimestamp)
    throws ESignDocException;

  public abstract int addSigAndRefsTimeStamp(ITimestamp paramITimestamp)
    throws ESignDocException;

  public abstract int createRefsOnlyTimeStamp()
    throws EXAdESException, ESignDocException;

  public abstract int createSigAndRefsTimeStamp(IHSLSignature paramIHSLSignature)
    throws EXAdESException, ESignDocException;

  public abstract ITimestamps getRefsOnlyTimeStamps();

  public abstract ITimestamps getSigAndRefsTimeStamps();

  public abstract VerifyStatus validateSigAndRefsTimestamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES_X
 * JD-Core Version:    0.6.0
 */