package si.hermes.security.SignedInfo;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.Collections.IManifest;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.IReferences;
import si.hermes.security.Collections.ITransform;
import si.hermes.security.Collections.ReferenceImpl;
import si.hermes.security.Collections.ReferencesImpl;
import si.hermes.security.Collections.Transform;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class SignedInfoImpl extends PersistableImpl
  implements ISignedInfo
{
  private static final long serialVersionUID = 789866539734621888L;
  private String fId = "";
  private String fCanonizationMethod = "";
  private String fSignatureMethod = "";
  private ReferencesImpl fReferences = null;
  Document Doc = null;

  public final int getCount()
  {
    return getReferences().getCount();
  }

  public final int AddReference(IReference paramIReference)
  {
    return getReferences().Add(paramIReference);
  }

  public final int AddReferenceUri(String paramString)
  {
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri(paramString);
    return AddReference(localReferenceImpl);
  }

  public final String getCanonizationMethod()
  {
    return this.fCanonizationMethod;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final IReferences getReferences()
  {
    if (this.fReferences == null)
      this.fReferences = new ReferencesImpl();
    return this.fReferences;
  }

  public final String getSignatureMethod()
  {
    return this.fSignatureMethod;
  }

  public final void setCanonizationMethod(String paramString)
  {
    this.fCanonizationMethod = paramString;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final void setSignatureMethod(String paramString)
  {
    this.fSignatureMethod = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "SignedInfo", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      Element localElement = EnsureElement(this.fNode, "CanonicalizationMethod", "http://www.w3.org/2000/09/xmldsig#");
      this.fCanonizationMethod = EnsureAttribute(localElement, "Algorithm", this.fCanonizationMethod, paramBoolean);
      localElement = EnsureElement(this.fNode, "SignatureMethod", "http://www.w3.org/2000/09/xmldsig#");
      this.fSignatureMethod = EnsureAttribute(localElement, "Algorithm", this.fSignatureMethod, paramBoolean);
      if (paramBoolean)
      {
        getReferences().LoadXml(this.fNode);
      }
      else
      {
        if (getReferences().getCount() == 0)
        {
          ReferenceImpl localReferenceImpl = new ReferenceImpl();
          Transform localTransform;
          (localTransform = new Transform()).setAlgorithm("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
          localReferenceImpl.AddTransform(localTransform);
          AddReference(localReferenceImpl);
        }
        getReferences().GetXml(this.fNode);
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final int AddManifestReference(IManifest paramIManifest)
    throws ESignDocException
  {
    if ((paramIManifest.getId() == null) || ("".equals(paramIManifest.getId())))
      throw new ESignDocException(27);
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri("#" + paramIManifest.getId());
    return getReferences().Add(localReferenceImpl);
  }

  public final int AddReferenceTransform(String paramString, int paramInt)
    throws ParserConfigurationException
  {
    int i = AddReferenceUri(paramString);
    getReferences().getItem(i).AddTransformAlgorithm(paramInt);
    return i;
  }

  public final int AddReferenceXPath(String paramString1, String paramString2)
    throws ESignDocException, ParserConfigurationException
  {
    int i = AddReferenceUri(paramString1);
    getReferences().getItem(i).AddTransformXPath(paramString2);
    return i;
  }

  public final int AddReferenceXslt(String paramString1, String paramString2)
    throws DOMException, Base64DecodingException, ESignDocException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
  {
    int i = AddReferenceUri(paramString1);
    getReferences().getItem(i).AddTransformXslt(paramString2);
    return i;
  }

  public final int AddReferenceXsltDom(String paramString, Document paramDocument)
    throws DOMException, ESignDocException, ParserConfigurationException
  {
    int i = AddReferenceUri(paramString);
    getReferences().getItem(i).AddTransformXsltDom(paramDocument);
    return i;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.SignedInfo.SignedInfoImpl
 * JD-Core Version:    0.6.0
 */