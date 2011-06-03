package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.paramHelper;

public final class ObjectIdentifierImpl extends PersistableImpl
  implements IObjectIdentifier
{
  private static final long serialVersionUID = -8090644184687054630L;
  XMLStringList2Impl fDocumentationReferences;
  String fQualifier;
  String fIdentifier;
  String fDescription;

  public final int addDocumentationReference(String paramString)
  {
    return getDocumentationReferences().addValue(paramString);
  }

  public final String getDescription()
  {
    return this.fDescription;
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

  public final String getQualifier()
  {
    return this.fQualifier;
  }

  public final void setDescription(String paramString)
  {
    this.fDescription = paramString;
  }

  public final void setIdentifier(String paramString)
  {
    this.fIdentifier = paramString;
  }

  public final void setQualifier(String paramString)
  {
    this.fQualifier = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "ObjectIdentifier", "http://uri.etsi.org/01903/v1.1.1#");
      paramHelper localparamHelper;
      Element localElement1 = (localparamHelper = EnsureElementWithValue(this.fNode, "Identifier", "http://uri.etsi.org/01903/v1.1.1#", this.fIdentifier, paramBoolean)).elem1;
      this.fIdentifier = localparamHelper.str1;
      this.fQualifier = EnsureAttribute(localElement1, "Qualifier", this.fQualifier, paramBoolean, false);
      localElement1 = (localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "Description", "http://uri.etsi.org/01903/v1.1.1#", localElement1, this.fDescription, paramBoolean)).elem1;
      this.fDescription = localparamHelper.str1;
      Element localElement2 = (localparamHelper = ensureElementAfter(this.fNode, "DocumentationReferences", "http://uri.etsi.org/01903/v1.1.1#", localElement1, (!paramBoolean) && (getDocumentationReferences().getCount() != 0), (!paramBoolean) && (getDocumentationReferences().getCount() == 0))).elem1;
      if (paramBoolean)
      {
        getDocumentationReferences().LoadXml(localElement2);
      }
      else
      {
        if (localElement2 != null)
          getDocumentationReferences().GetXml(localElement2);
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
 * Qualified Name:     si.hermes.security.XAdES.ObjectIdentifierImpl
 * JD-Core Version:    0.6.0
 */