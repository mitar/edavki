package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.ITransform;
import si.hermes.security.Collections.ITransformChain;
import si.hermes.security.Collections.TransformChain;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;
import si.hermes.security.paramHelper;

public final class SignaturePolicyIdentifierImpl extends PersistableImpl
  implements ISignaturePolicyIdentifier
{
  private static final long serialVersionUID = -6569788528702749701L;
  private String fDescription;
  private String fDigestMethod = "http://www.w3.org/2000/09/xmldsig#sha1";
  private String fDigestValue;
  private IXMLStringList2 fDocumentationReferences;
  private String fIdentifier;
  private boolean fImplied = true;
  private ITransformChain fTransforms;

  public final int addTransform(ITransform paramITransform)
  {
    return getTransforms().Add(paramITransform);
  }

  public final String getDescription()
  {
    return this.fDescription;
  }

  public final String getDigestMethod()
  {
    return this.fDigestMethod;
  }

  public final String getDigestValue()
  {
    return this.fDigestValue;
  }

  public final IXMLStringList2 getDocumentationReferences()
  {
    if (this.fDocumentationReferences == null)
      this.fDocumentationReferences = new XMLStringList2Impl("DocumentationReferences", "http://uri.etsi.org/01903/v1.1.1#", "DocumentationReference", "http://uri.etsi.org/01903/v1.1.1#");
    return this.fDocumentationReferences;
  }

  public final String getIdentifier()
  {
    return this.fIdentifier;
  }

  public final boolean getImplied()
  {
    return this.fImplied;
  }

  public final ITransformChain getTransforms()
  {
    if (this.fTransforms == null)
      this.fTransforms = new TransformChain();
    return this.fTransforms;
  }

  public final void setDescription(String paramString)
  {
    this.fDescription = paramString;
  }

  public final void setDigestMethod(String paramString)
  {
    this.fDigestMethod = paramString;
  }

  public final void setDigestValue(String paramString)
  {
    this.fDigestValue = paramString;
  }

  public final void setIdentifier(String paramString)
  {
    this.fIdentifier = paramString;
  }

  public final void setImplied(boolean paramBoolean)
  {
    this.fImplied = paramBoolean;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "SignaturePolicyIdentifier", "http://uri.etsi.org/01903/v1.1.1#");
      if (((!paramBoolean) && (this.fImplied)) || ((paramBoolean) && (Utility.getNode2(this.fNode, "SignaturePolicyId", "http://uri.etsi.org/01903/v1.1.1#") == null)))
      {
        this.fImplied = true;
        if (!paramBoolean)
          RemoveChildren(this.fNode, "SignaturePolicyId", "http://uri.etsi.org/01903/v1.1.1#");
        this.fNode = EnsureElement(this.fNode, "SignaturePolicyImplied", "http://uri.etsi.org/01903/v1.1.1#", true);
      }
      else
      {
        this.fImplied = false;
        if (!paramBoolean)
          RemoveChildren(this.fNode, "SignaturePolicyImplied", "http://uri.etsi.org/01903/v1.1.1#");
        Element localElement1 = EnsureElement(this.fNode, "SignaturePolicyId", "http://uri.etsi.org/01903/v1.1.1#", true);
        Element localElement2 = EnsureElement(localElement1, "SigPolicyId", "http://uri.etsi.org/01903/v1.1.1#", true);
        Element localElement3 = (localparamHelper = EnsureElementWithValue(localElement2, "Identifier", "http://uri.etsi.org/01903/v1.1.1#", this.fIdentifier, paramBoolean)).elem1;
        this.fIdentifier = localparamHelper.str1;
        paramHelper localparamHelper = ensureOptionalElementAfterWithValue(localElement2, "Description", "http://uri.etsi.org/01903/v1.1.1#", localElement3, this.fDescription, paramBoolean);
        this.fDescription = localparamHelper.str1;
        localElement2 = (localparamHelper = ensureElementAfter(localElement1, "Transforms", "http://uri.etsi.org/01903/v1.1.1#", localElement2, (getTransforms().getCount() != 0) && (!paramBoolean), (getTransforms().getCount() == 0) && (!paramBoolean))).elem1;
        if (paramBoolean)
          getTransforms().LoadXml(localElement2);
        else
          getTransforms().GetXml(localElement2);
        localElement2 = EnsureElement(localElement1, "SigPolicyHash", "http://uri.etsi.org/01903/v1.1.1#", true);
        localElement3 = EnsureElement(localElement2, "DigestMethod", "http://uri.etsi.org/01903/v1.1.1#", true);
        this.fDigestMethod = EnsureAttribute(localElement3, "Algorithm", this.fDigestMethod, paramBoolean, true);
        localElement3 = EnsureElement(localElement2, "DigestValue", "http://uri.etsi.org/01903/v1.1.1#", true);
        this.fDigestValue = EnsureValue(localElement3, this.fDigestValue, paramBoolean);
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.SignaturePolicyIdentifierImpl
 * JD-Core Version:    0.6.0
 */