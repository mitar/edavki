package si.hermes.security.Collections;

import java.util.List;
import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public class TimestampsImpl extends ListImpl
  implements ITimestamps
{
  private static final long serialVersionUID = -5978718266829326983L;

  public TimestampsImpl()
  {
    this.fParent = "UnsignedSignatureProperties";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "SignatureTimeStamp";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fAfterElements.add("CounterSignature");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
    this.fAfterElements.add("SignatureTimeStamp");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
  }

  protected IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    TimestampImpl localTimestampImpl;
    (localTimestampImpl = new TimestampImpl()).LoadXml(paramElement);
    return localTimestampImpl;
  }

  public ITimestamp getItem(int paramInt)
  {
    return (ITimestamp)super.getItem_(paramInt);
  }

  public void setItem(int paramInt, ITimestamp paramITimestamp)
  {
    super.setItem(paramInt, paramITimestamp);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.TimestampsImpl
 * JD-Core Version:    0.6.0
 */