package org.apache.xml.security.signature;

import B;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.InvalidTransformException;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.transforms.params.InclusiveNamespaces;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.DigesterOutputStream;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.ReferenceResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.UnsyncBufferedOutputStream;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class Reference extends SignatureElementProxy
{
  public static boolean CacheSignedNodes = false;
  static Log log = LogFactory.getLog(Reference.class.getName());
  public static final String OBJECT_URI = "http://www.w3.org/2000/09/xmldsig#Object";
  public static final String MANIFEST_URI = "http://www.w3.org/2000/09/xmldsig#Manifest";
  Manifest _manifest = null;
  XMLSignatureInput _transformsOutput;

  protected Reference(Document paramDocument, String paramString1, String paramString2, Manifest paramManifest, Transforms paramTransforms, String paramString3)
    throws XMLSignatureException
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._baseURI = paramString1;
    this._manifest = paramManifest;
    setURI(paramString2);
    if (paramTransforms != null)
    {
      this._constructionElement.appendChild(paramTransforms.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
    Object localObject = MessageDigestAlgorithm.getInstance(this._doc, paramString3);
    this._constructionElement.appendChild(((MessageDigestAlgorithm)localObject).getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
    localObject = XMLUtils.createElementInSignatureSpace(this._doc, "DigestValue");
    this._constructionElement.appendChild((Node)localObject);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  protected Reference(Element paramElement, String paramString, Manifest paramManifest)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    this._manifest = paramManifest;
  }

  public MessageDigestAlgorithm getMessageDigestAlgorithm()
    throws XMLSignatureException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "DigestMethod", 0)) == null)
      return null;
    String str;
    if ((str = localElement.getAttributeNS(null, "Algorithm")) == null)
      return null;
    return MessageDigestAlgorithm.getInstance(this._doc, str);
  }

  public void setURI(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "URI", paramString);
  }

  public String getURI()
  {
    return this._constructionElement.getAttributeNS(null, "URI");
  }

  public void setId(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
    {
      this._constructionElement.setAttributeNS(null, "Id", paramString);
      IdResolver.registerElementById(this._constructionElement, paramString);
    }
  }

  public String getId()
  {
    return this._constructionElement.getAttributeNS(null, "Id");
  }

  public void setType(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
      this._constructionElement.setAttributeNS(null, "Type", paramString);
  }

  public String getType()
  {
    return this._constructionElement.getAttributeNS(null, "Type");
  }

  public boolean typeIsReferenceToObject()
  {
    return (getType() != null) && (getType().equals("http://www.w3.org/2000/09/xmldsig#Object"));
  }

  public boolean typeIsReferenceToManifest()
  {
    return (getType() != null) && (getType().equals("http://www.w3.org/2000/09/xmldsig#Manifest"));
  }

  private void setDigestValueElement(byte[] paramArrayOfByte)
  {
    if (this._state == 0)
    {
      Element localElement;
      Node localNode;
      localElement.removeChild(localNode);
      String str = Base64.encode(paramArrayOfByte);
      Text localText = this._doc.createTextNode(str);
      localElement.appendChild(localText);
    }
  }

  public void generateDigestValue()
    throws XMLSignatureException, ReferenceNotInitializedException
  {
    if (this._state == 0)
      setDigestValueElement(calculateDigest());
  }

  public XMLSignatureInput getContentsBeforeTransformation()
    throws ReferenceNotInitializedException
  {
    try
    {
      Attr localAttr;
      String str = (localAttr = this._constructionElement.getAttributeNodeNS(null, "URI")) == null ? null : localAttr.getNodeValue();
      ResourceResolver localResourceResolver;
      Object localObject;
      if ((localResourceResolver = ResourceResolver.getInstance(localAttr, this._baseURI, this._manifest._perManifestResolvers)) == null)
      {
        localObject = new Object[] { str };
        throw new ReferenceNotInitializedException("signature.Verification.Reference.NoInput", localObject);
      }
      localResourceResolver.addProperties(this._manifest._resolverProperties);
      return localObject = localResourceResolver.resolve(localAttr, this._baseURI);
    }
    catch (ResourceResolverException localResourceResolverException)
    {
      throw new ReferenceNotInitializedException("empty", localResourceResolverException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
  }

  public XMLSignatureInput getTransformsInput()
    throws ReferenceNotInitializedException
  {
    XMLSignatureInput localXMLSignatureInput1 = getContentsBeforeTransformation();
    XMLSignatureInput localXMLSignatureInput2;
    try
    {
      localXMLSignatureInput2 = new XMLSignatureInput(localXMLSignatureInput1.getBytes());
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new ReferenceNotInitializedException("empty", localCanonicalizationException);
    }
    catch (IOException localIOException)
    {
      throw new ReferenceNotInitializedException("empty", localIOException);
    }
    localXMLSignatureInput2.setSourceURI(localXMLSignatureInput1.getSourceURI());
    return localXMLSignatureInput2;
  }

  private XMLSignatureInput getContentsAfterTransformation(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws XMLSignatureException
  {
    try
    {
      Transforms localTransforms = getTransforms();
      XMLSignatureInput localXMLSignatureInput = null;
      if (localTransforms != null)
      {
        localXMLSignatureInput = localTransforms.performTransforms(paramXMLSignatureInput, paramOutputStream);
        this._transformsOutput = localXMLSignatureInput;
      }
      else
      {
        localXMLSignatureInput = paramXMLSignatureInput;
      }
      return localXMLSignatureInput;
    }
    catch (ResourceResolverException localResourceResolverException)
    {
      throw new XMLSignatureException("empty", localResourceResolverException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new XMLSignatureException("empty", localCanonicalizationException);
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
    }
    catch (TransformationException localTransformationException)
    {
      throw new XMLSignatureException("empty", localTransformationException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public XMLSignatureInput getContentsAfterTransformation()
    throws XMLSignatureException
  {
    XMLSignatureInput localXMLSignatureInput = getContentsBeforeTransformation();
    return getContentsAfterTransformation(localXMLSignatureInput, null);
  }

  public XMLSignatureInput getNodesetBeforeFirstCanonicalization()
    throws XMLSignatureException
  {
    try
    {
      XMLSignatureInput localXMLSignatureInput1;
      XMLSignatureInput localXMLSignatureInput2 = localXMLSignatureInput1 = getContentsBeforeTransformation();
      Transforms localTransforms;
      if ((localTransforms = getTransforms()) != null)
      {
        Transform localTransform;
        String str;
        for (int i = 0; (i < localTransforms.getLength()) && (!(str = (localTransform = localTransforms.item(i)).getURI()).equals("http://www.w3.org/2001/10/xml-exc-c14n#")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) && (!str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) && (!str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")); i++)
          localXMLSignatureInput2 = localTransform.performTransform(localXMLSignatureInput2, null);
        localXMLSignatureInput2.setSourceURI(localXMLSignatureInput1.getSourceURI());
      }
      return localXMLSignatureInput2;
    }
    catch (IOException localIOException)
    {
      throw new XMLSignatureException("empty", localIOException);
    }
    catch (ResourceResolverException localResourceResolverException)
    {
      throw new XMLSignatureException("empty", localResourceResolverException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new XMLSignatureException("empty", localCanonicalizationException);
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
    }
    catch (TransformationException localTransformationException)
    {
      throw new XMLSignatureException("empty", localTransformationException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public String getHTMLRepresentation()
    throws XMLSignatureException
  {
    try
    {
      XMLSignatureInput localXMLSignatureInput = getNodesetBeforeFirstCanonicalization();
      Object localObject1 = new HashSet();
      Transforms localTransforms = getTransforms();
      Object localObject2 = null;
      if (localTransforms != null)
        for (int i = 0; i < localTransforms.getLength(); i++)
        {
          Transform localTransform;
          String str;
          if ((!(str = (localTransform = localTransforms.item(i)).getURI()).equals("http://www.w3.org/2001/10/xml-exc-c14n#")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")))
            continue;
          localObject2 = localTransform;
          break;
        }
      if ((localObject2 != null) && (localObject2.length("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces") == 1))
      {
        InclusiveNamespaces localInclusiveNamespaces;
        localObject1 = InclusiveNamespaces.prefixStr2Set((localInclusiveNamespaces = new InclusiveNamespaces(XMLUtils.selectNode(localObject2.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", 0), getBaseURI())).getInclusiveNamespaces());
      }
      return localXMLSignatureInput.getHTMLRepresentation((Set)localObject1);
    }
    catch (TransformationException localTransformationException)
    {
      throw new XMLSignatureException("empty", localTransformationException);
    }
    catch (InvalidTransformException localInvalidTransformException)
    {
      throw new XMLSignatureException("empty", localInvalidTransformException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public XMLSignatureInput getTransformsOutput()
  {
    return this._transformsOutput;
  }

  protected XMLSignatureInput dereferenceURIandPerformTransforms(OutputStream paramOutputStream)
    throws XMLSignatureException
  {
    try
    {
      XMLSignatureInput localXMLSignatureInput1 = getContentsBeforeTransformation();
      XMLSignatureInput localXMLSignatureInput2 = getContentsAfterTransformation(localXMLSignatureInput1, paramOutputStream);
      if (!CacheSignedNodes)
        this._transformsOutput = localXMLSignatureInput2;
      return localXMLSignatureInput2;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
  }

  public Transforms getTransforms()
    throws XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "Transforms", 0)) != null)
    {
      Transforms localTransforms;
      return localTransforms = new Transforms(localElement, this._baseURI);
    }
    return null;
  }

  public byte[] getReferencedBytes()
    throws ReferenceNotInitializedException, XMLSignatureException
  {
    try
    {
      Object localObject;
      if (((getTransforms() == null) || (getTransforms().getLength() == 0)) && ((localObject = ReferenceResolver.getReferenceDigestById(this._constructionElement.getOwnerDocument(), getURI())) != null))
        return localObject;
      byte[] arrayOfByte = (localObject = dereferenceURIandPerformTransforms(null)).getBytes();
      if ((getTransforms() == null) || (getTransforms().getLength() == 0))
        ReferenceResolver.registerReferenceDigestById(((XMLSignatureInput)localObject).getBytes(), this._constructionElement.getOwnerDocument(), getURI());
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new ReferenceNotInitializedException("empty", localIOException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
  }

  private byte[] calculateDigest()
    throws ReferenceNotInitializedException, XMLSignatureException
  {
    try
    {
      MessageDigestAlgorithm localMessageDigestAlgorithm;
      (localMessageDigestAlgorithm = getMessageDigestAlgorithm()).reset();
      DigesterOutputStream localDigesterOutputStream = new DigesterOutputStream(localMessageDigestAlgorithm);
      UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localDigesterOutputStream);
      byte[] arrayOfByte = null;
      if ((getTransforms() == null) || (getTransforms().getLength() == 0))
        arrayOfByte = ReferenceResolver.getReferenceDigestById(this._constructionElement.getOwnerDocument(), getURI());
      if (arrayOfByte == null)
      {
        XMLSignatureInput localXMLSignatureInput;
        (localXMLSignatureInput = dereferenceURIandPerformTransforms(localUnsyncBufferedOutputStream)).updateOutputStream(localUnsyncBufferedOutputStream);
        if ((getTransforms() == null) || (getTransforms().getLength() == 0))
          ReferenceResolver.registerReferenceDigestById(localXMLSignatureInput.getBytes(), this._constructionElement.getOwnerDocument(), getURI());
      }
      else
      {
        localUnsyncBufferedOutputStream.write(arrayOfByte);
      }
      localUnsyncBufferedOutputStream.flush();
      return localDigesterOutputStream.getDigestValue();
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
    }
    catch (IOException localIOException)
    {
    }
    throw new ReferenceNotInitializedException("empty", localIOException);
  }

  public byte[] getDigestValue()
    throws Base64DecodingException, XMLSecurityException
  {
    Element localElement;
    Object localObject;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "DigestValue", 0)) == null)
    {
      localObject = new Object[] { "DigestValue", "http://www.w3.org/2000/09/xmldsig#" };
      throw new XMLSecurityException("signature.Verification.NoSignatureElement", localObject);
    }
    return (B)(localObject = Base64.decode(localElement));
  }

  public boolean verify()
    throws ReferenceNotInitializedException, XMLSecurityException
  {
    byte[] arrayOfByte1 = getDigestValue();
    byte[] arrayOfByte2 = calculateDigest();
    boolean bool;
    if (!(bool = MessageDigestAlgorithm.isEqual(arrayOfByte1, arrayOfByte2)))
      log.warn("Verification failed for URI \"" + getURI() + "\"");
    else
      log.info("Verification successful for URI \"" + getURI() + "\"");
    return bool;
  }

  public String getBaseLocalName()
  {
    return "Reference";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.Reference
 * JD-Core Version:    0.6.0
 */