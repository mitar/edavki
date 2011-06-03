package si.hermes.security.Collections;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class ManifestImpl extends PersistableImpl
  implements IManifest
{
  private static final long serialVersionUID = -7554954550977438236L;
  private String fId;
  private IReferences fReferences;

  public final String getId()
  {
    return this.fId;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final IReferences getReferences()
  {
    if (this.fReferences == null)
      this.fReferences = new ReferencesImpl2();
    return this.fReferences;
  }

  public final int AddManifestReference(IManifest paramIManifest)
    throws ParserConfigurationException
  {
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri("#" + paramIManifest.getId());
    return getReferences().Add(localReferenceImpl);
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

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "Manifest", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      if (paramBoolean)
      {
        getReferences().LoadXml(this.fNode);
      }
      else
      {
        getReferences().GetXml(this.fNode);
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
 * Qualified Name:     si.hermes.security.Collections.ManifestImpl
 * JD-Core Version:    0.6.0
 */