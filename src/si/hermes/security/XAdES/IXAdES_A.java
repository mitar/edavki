package si.hermes.security.XAdES;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestampProvider;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES_A extends IXAdES_X_L
{
  public abstract int addArchiveTimeStamp(ITimestamp paramITimestamp)
    throws ParserConfigurationException;

  public abstract int createArchiveTimeStamp(IHSLSignature paramIHSLSignature)
    throws EXAdESException, ESignDocException, IOException;

  public abstract ITimestamps getArchiveTimeStamps();

  public abstract VerifyStatus validateArchiveTimestamps(ITimestampProvider paramITimestampProvider, IHSLSignature paramIHSLSignature);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES_A
 * JD-Core Version:    0.6.0
 */