package si.hermes.security.XAdES;

import java.util.List;
import org.w3c.dom.Element;
import si.hermes.security.Collections.ListImpl;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public final class DataObjectFormatsImpl extends ListImpl
  implements IDataObjectFormats
{
  private static final long serialVersionUID = -9160147432509484656L;

  public DataObjectFormatsImpl()
  {
    this.fParent = "SignedDataObjectProperties";
    this.fParentNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fNodeElem = "DataObjectFormat";
    this.fNodeElemNS = "http://uri.etsi.org/01903/v1.1.1#";
    this.fAfterElements.add("DataObjectFormat");
    this.fAfterElements.add("http://uri.etsi.org/01903/v1.1.1#");
  }

  protected final IPersistable CreateItem(Element paramElement)
    throws ESignDocException
  {
    DataObjectFormatImpl localDataObjectFormatImpl;
    (localDataObjectFormatImpl = new DataObjectFormatImpl()).LoadXml(paramElement);
    return localDataObjectFormatImpl;
  }

  public final IDataObjectFormat getItem(int paramInt)
  {
    return (IDataObjectFormat)super.getItem_(paramInt);
  }

  public final void setItem(int paramInt, IDataObjectFormat paramIDataObjectFormat)
  {
    super.setItem(paramInt, paramIDataObjectFormat);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.DataObjectFormatsImpl
 * JD-Core Version:    0.6.0
 */