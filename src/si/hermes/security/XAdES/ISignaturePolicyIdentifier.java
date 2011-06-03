package si.hermes.security.XAdES;

import si.hermes.security.Collections.ITransform;
import si.hermes.security.Collections.ITransformChain;
import si.hermes.security.IPersistable;

public abstract interface ISignaturePolicyIdentifier extends IPersistable
{
  public abstract int addTransform(ITransform paramITransform);

  public abstract String getDescription();

  public abstract String getDigestMethod();

  public abstract String getDigestValue();

  public abstract IXMLStringList2 getDocumentationReferences();

  public abstract String getIdentifier();

  public abstract boolean getImplied();

  public abstract ITransformChain getTransforms();

  public abstract void setDescription(String paramString);

  public abstract void setDigestMethod(String paramString);

  public abstract void setDigestValue(String paramString);

  public abstract void setIdentifier(String paramString);

  public abstract void setImplied(boolean paramBoolean);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ISignaturePolicyIdentifier
 * JD-Core Version:    0.6.0
 */