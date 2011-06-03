package si.hermes.security.Collections;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.paramHelper;

public class HashDataInfoImpl extends PersistableImpl
  implements IHashDataInfo
{
  private static final long serialVersionUID = 795725085466854525L;
  String fUri;
  ITransformChain fTransforms;

  public String getUri()
  {
    return this.fUri;
  }

  public void setUri(String paramString)
  {
    this.fUri = paramString;
  }

  public ITransformChain getTransforms()
  {
    if (this.fTransforms == null)
      this.fTransforms = new TransformChainHashDataInfoImpl();
    return this.fTransforms;
  }

  public int addTransform(ITransform paramITransform)
  {
    return getTransforms().Add(paramITransform);
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "HashDataInfo", "http://uri.etsi.org/01903/v1.1.1#");
      this.fUri = EnsureAttribute(this.fNode, "uri", this.fUri, paramBoolean);
      Object localObject = null;
      paramHelper localparamHelper;
      Element localElement;
      if ((localElement = (localparamHelper = ensureElementAfter(this.fNode, "Transforms", "http://uri.etsi.org/01903/v1.1.1#", null, (!paramBoolean) && (getTransforms().getCount() != 0), (!paramBoolean) && (getTransforms().getCount() == 0))).elem1) != null)
      {
        if (paramBoolean)
          getTransforms().LoadXml(localElement);
        else
          getTransforms().GetXml(localElement);
      }
      else
        return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.HashDataInfoImpl
 * JD-Core Version:    0.6.0
 */