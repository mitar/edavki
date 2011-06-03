package si.hermes.security.Collections;

import java.util.List;
import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public class HashDataInfosImpl extends ListImpl
  implements IHashDataInfos
{
  private static final long serialVersionUID = -8363899165495813357L;

  public HashDataInfosImpl(String paramString)
  {
    this.fParent = paramString;
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "HashDataInfo";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fAfterElements.add("HashDataInfo");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
  }

  protected IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    HashDataInfoImpl localHashDataInfoImpl;
    (localHashDataInfoImpl = new HashDataInfoImpl()).LoadXml(paramElement);
    return localHashDataInfoImpl;
  }

  public IHashDataInfo getItem(int paramInt)
  {
    return (IHashDataInfo)super.getItem_(paramInt);
  }

  public void setItem(int paramInt, IHashDataInfo paramIHashDataInfo)
  {
    super.setItem(paramInt, paramIHashDataInfo);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.HashDataInfosImpl
 * JD-Core Version:    0.6.0
 */