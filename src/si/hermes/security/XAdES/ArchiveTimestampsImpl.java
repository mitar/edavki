package si.hermes.security.XAdES;

import java.util.List;
import org.w3c.dom.Element;
import si.hermes.security.Collections.ITimestamp;
import si.hermes.security.Collections.ITimestamps;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class ArchiveTimestampsImpl extends ListImpl
  implements ITimestamps
{
  private static final long serialVersionUID = -7181337096016446854L;

  public ArchiveTimestampsImpl()
  {
    this.fParent = "UnsignedSignatureProperties";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "ArchiveTimeStamp";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fAfterElements.add("CounterSignature");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("SignatureTimeStamp");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("CompleteCertificateRefs");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("CompleteRevocationRefs");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("SigAndRefsTimeStamp");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("RefsOnlyTimeStamp");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("CertificateValues");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("RevocationValues");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("ArchiveTimeStamp");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    ArchiveTimestampImpl localArchiveTimestampImpl;
    (localArchiveTimestampImpl = new ArchiveTimestampImpl()).LoadXml(paramElement);
    return localArchiveTimestampImpl;
  }

  public final ITimestamp getItem(int paramInt)
  {
    return (ITimestamp)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, ITimestamp paramITimestamp)
  {
    super.setItem(paramInt, paramITimestamp);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ArchiveTimestampsImpl
 * JD-Core Version:    0.6.0
 */