package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.IReferences;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.VerifyStatus;
import si.hermes.security.paramHelper;

public final class DataObjectFormatImpl extends PersistableImpl
  implements IDataObjectFormat
{
  private static final long serialVersionUID = -3587061988237473999L;
  String fDescription;
  String fMimeType;
  String fEncoding;
  String fObjectReference;
  IObjectIdentifier fObjectIdentifier;

  public final String getDescription()
  {
    return this.fDescription;
  }

  public final String getEncoding()
  {
    return this.fEncoding;
  }

  public final String getMimeType()
  {
    return this.fMimeType;
  }

  public final IObjectIdentifier getObjectIdentifier()
  {
    return this.fObjectIdentifier;
  }

  public final String getObjectReference()
  {
    return this.fObjectReference;
  }

  public final void setDescription(String paramString)
  {
    this.fDescription = paramString;
  }

  public final void setEncoding(String paramString)
  {
    this.fEncoding = paramString;
  }

  public final void setMimeType(String paramString)
  {
    this.fMimeType = paramString;
  }

  public final void setObjectIdentifier(IObjectIdentifier paramIObjectIdentifier)
  {
    this.fObjectIdentifier = paramIObjectIdentifier;
  }

  public final void setObjectReference(String paramString)
  {
    this.fObjectReference = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    Element localElement1 = null;
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "DataObjectFormat", "http://uri.etsi.org/01903/v1.1.1#");
      this.fObjectReference = EnsureAttribute(this.fNode, "ObjectReference", this.fObjectReference, paramBoolean, true);
      paramHelper localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "Description", "http://uri.etsi.org/01903/v1.1.1#", null, this.fDescription, paramBoolean);
      this.fDescription = localparamHelper.str1;
      localElement1 = localparamHelper.elem1;
      Element localElement2 = (localparamHelper = ensureElementAfter(this.fNode, "ObjectIdentifier", "http://uri.etsi.org/01903/v1.1.1#", localElement1, (!paramBoolean) && (this.fObjectIdentifier != null), (!paramBoolean) && (this.fObjectIdentifier == null))).elem1;
      localElement1 = localparamHelper.elem2;
      if (localElement2 != null)
        if (paramBoolean)
        {
          if (this.fObjectIdentifier == null)
            this.fObjectIdentifier = new ObjectIdentifierImpl();
          this.fObjectIdentifier.LoadXml(localElement2);
        }
        else if (this.fObjectIdentifier != null)
        {
          this.fObjectIdentifier.GetXml(localElement2);
        }
      localElement1 = (localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "MimeType", "http://uri.etsi.org/01903/v1.1.1#", localElement1, this.fMimeType, paramBoolean)).elem1;
      this.fMimeType = localparamHelper.str1;
      localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "Encoding", "http://uri.etsi.org/01903/v1.1.1#", localElement1, this.fEncoding, paramBoolean);
      this.fEncoding = localparamHelper.str1;
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final VerifyStatus validate(IHSLSignature paramIHSLSignature)
  {
    int i = 0;
    for (int j = 0; j < paramIHSLSignature.getSignedInfo().getReferences().getCount(); j++)
    {
      if (!this.fObjectReference.equals(paramIHSLSignature.getSignedInfo().getReferences().getItem(j).getUri()))
        continue;
      i = 1;
    }
    if (i == 0)
      return new VerifyStatus("DISPLAY_TRANSFORM_NOT_SIGNED", "");
    return new VerifyStatus("VALID", "");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.DataObjectFormatImpl
 * JD-Core Version:    0.6.0
 */