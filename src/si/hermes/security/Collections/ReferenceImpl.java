package si.hermes.security.Collections;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;

public final class ReferenceImpl extends PersistableImpl
  implements IReference
{
  private static final long serialVersionUID = 5369712672290024348L;
  private String fDigestMethod = "";
  private String fDigestValue = "";
  private String fId = "";
  private String fType = "";
  private String fUri = "";
  private ITransformChain fTransformChain;

  public final int AddTransform(ITransform paramITransform)
    throws ParserConfigurationException
  {
    return getTransformChain().Add(paramITransform);
  }

  public final String getDigestMethod()
  {
    return this.fDigestMethod;
  }

  public final String getDigestValue()
  {
    return this.fDigestValue;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final ITransformChain getTransformChain()
  {
    if (this.fTransformChain == null)
      this.fTransformChain = new TransformChain();
    return this.fTransformChain;
  }

  public final String getType()
  {
    return this.fType;
  }

  public final String getUri()
  {
    return this.fUri;
  }

  public final void setDigestMethod(String paramString)
  {
    this.fDigestMethod = paramString;
  }

  public final void setDigestValue(String paramString)
  {
    this.fDigestValue = paramString;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final void setType(String paramString)
  {
    this.fType = paramString;
  }

  public final void setUri(String paramString)
  {
    this.fUri = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "Reference", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      this.fUri = EnsureAttribute(this.fNode, "URI", this.fUri, paramBoolean);
      this.fType = EnsureAttribute(this.fNode, "Type", this.fType, paramBoolean, false);
      if (paramBoolean)
      {
        if ((localElement = EnsureElement(this.fNode, "Transforms", "http://www.w3.org/2000/09/xmldsig#", false)) != null)
          getTransformChain().LoadXml(localElement);
      }
      else if (getTransformChain().getCount() > 0)
      {
        localElement = EnsureElement(this.fNode, "Transforms", "http://www.w3.org/2000/09/xmldsig#");
        getTransformChain().GetXml(localElement);
      }
      Element localElement = EnsureElement(this.fNode, "DigestMethod", "http://www.w3.org/2000/09/xmldsig#");
      this.fDigestMethod = EnsureAttribute(localElement, "Algorithm", this.fDigestMethod, paramBoolean);
      localElement = EnsureElement(this.fNode, "DigestValue", "http://www.w3.org/2000/09/xmldsig#");
      this.fDigestValue = EnsureValue(localElement, this.fDigestValue, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public final int AddTransformAlgorithm(int paramInt)
    throws ParserConfigurationException
  {
    Transform localTransform = new Transform();
    switch (paramInt)
    {
    case 1:
      break;
    case 2:
      break;
    case 3:
      break;
    case 4:
      break;
    case 5:
      break;
    case 6:
      localTransform.setAlgorithm("http://www.w3.org/2000/09/xmldsig#base64");
    }
    return AddTransform(localTransform);
  }

  public final int AddTransformXPath(String paramString)
    throws ESignDocException, ParserConfigurationException
  {
    int i = AddTransformAlgorithm(3);
    Element localElement1;
    Element localElement2 = Utility.CreateElement((localElement1 = getTransformChain().getItem(i).GetXml()).getOwnerDocument(), "XPath", "http://www.w3.org/2000/09/xmldsig#");
    localElement1.appendChild(localElement2);
    Utility.setTextNodeValue(localElement2, paramString);
    return i;
  }

  public final int AddTransformXslt(String paramString)
    throws DOMException, Base64DecodingException, ESignDocException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
  {
    return AddTransformXsltDom(Utility.createDomDocumentFromB64String(paramString));
  }

  public final int AddTransformXsltDom(Document paramDocument)
    throws DOMException, ESignDocException, ParserConfigurationException
  {
    int i = AddTransformAlgorithm(5);
    Element localElement;
    (localElement = getTransformChain().getItem(i).GetXml()).appendChild(localElement.getOwnerDocument().importNode(paramDocument.getDocumentElement(), true));
    return i;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ReferenceImpl
 * JD-Core Version:    0.6.0
 */