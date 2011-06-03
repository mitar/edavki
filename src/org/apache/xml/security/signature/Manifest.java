package org.apache.xml.security.signature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Manifest extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(Manifest.class.getName());
  Vector _references;
  Element[] _referencesEl;
  private boolean[] verificationResults = null;
  Vector _signedContents = new Vector();
  HashMap _resolverProperties = new HashMap(10);
  Vector _perManifestResolvers = new Vector();

  public Manifest(Document paramDocument)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._references = new Vector();
  }

  public Manifest(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    this._referencesEl = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Reference");
    int i;
    if ((i = this._referencesEl.length) == 0)
    {
      Object[] arrayOfObject = { "Reference", "Manifest" };
      throw new DOMException(4, I18n.translate("xml.WrongContent", arrayOfObject));
    }
    this._references = new Vector(i);
    for (int j = 0; j < i; j++)
      this._references.add(null);
  }

  public void addDocument(String paramString1, String paramString2, Transforms paramTransforms, String paramString3, String paramString4, String paramString5)
    throws XMLSignatureException
  {
    if (this._state == 0)
    {
      Reference localReference = new Reference(this._doc, paramString1, paramString2, this, paramTransforms, paramString3);
      if (paramString4 != null)
        localReference.setId(paramString4);
      if (paramString5 != null)
        localReference.setType(paramString5);
      this._references.add(localReference);
      this._constructionElement.appendChild(localReference.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void generateDigestValues()
    throws XMLSignatureException, ReferenceNotInitializedException
  {
    if (this._state == 0)
      for (int i = 0; i < getLength(); i++)
      {
        Reference localReference;
        (localReference = (Reference)this._references.elementAt(i)).generateDigestValue();
      }
  }

  public int getLength()
  {
    return this._references.size();
  }

  public Reference item(int paramInt)
    throws XMLSecurityException
  {
    if (this._state == 0)
      return (Reference)this._references.elementAt(paramInt);
    if (this._references.elementAt(paramInt) == null)
    {
      Reference localReference = new Reference(this._referencesEl[paramInt], this._baseURI, this);
      this._references.set(paramInt, localReference);
    }
    return (Reference)this._references.elementAt(paramInt);
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

  public boolean verifyReferences()
    throws MissingResourceFailureException, XMLSecurityException
  {
    return verifyReferences(false);
  }

  public boolean verifyReferences(boolean paramBoolean)
    throws MissingResourceFailureException, XMLSecurityException
  {
    if (this._referencesEl == null)
      this._referencesEl = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Reference");
    if (log.isDebugEnabled())
    {
      log.debug("verify " + this._referencesEl.length + " References");
      log.debug("I am " + (paramBoolean ? "" : "not") + " requested to follow nested Manifests");
    }
    int i = 1;
    if (this._referencesEl.length == 0)
      throw new XMLSecurityException("empty");
    this.verificationResults = new boolean[this._referencesEl.length];
    for (int j = 0; j < this._referencesEl.length; j++)
    {
      Reference localReference = new Reference(this._referencesEl[j], this._baseURI, this);
      this._references.set(j, localReference);
      try
      {
        boolean bool1 = localReference.verify();
        setVerificationResult(j, bool1);
        if (!bool1)
          i = 0;
        if (log.isDebugEnabled())
          log.debug("The Reference has Type " + localReference.getType());
        if ((i != 0) && (paramBoolean) && (localReference.typeIsReferenceToManifest()))
        {
          log.debug("We have to follow a nested Manifest");
          try
          {
            XMLSignatureInput localXMLSignatureInput;
            Set localSet = (localXMLSignatureInput = localReference.dereferenceURIandPerformTransforms(null)).getNodeSet();
            Manifest localManifest = null;
            Iterator localIterator = localSet.iterator();
            while (localIterator.hasNext())
            {
              Node localNode;
              if (((localNode = (Node)localIterator.next()).getNodeType() == 1) && (((Element)localNode).getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) && (((Element)localNode).getLocalName().equals("Manifest")))
                try
                {
                  localManifest = new Manifest((Element)localNode, localXMLSignatureInput.getSourceURI());
                }
                catch (XMLSecurityException localXMLSecurityException)
                {
                }
            }
            if (localManifest == null)
              throw new MissingResourceFailureException("empty", localReference);
            localManifest._perManifestResolvers = this._perManifestResolvers;
            localManifest._resolverProperties = this._resolverProperties;
            boolean bool2;
            if (!(bool2 = localManifest.verifyReferences(paramBoolean)))
            {
              i = 0;
              log.warn("The nested Manifest was invalid (bad)");
            }
            else
            {
              log.debug("The nested Manifest was valid (good)");
            }
          }
          catch (IOException localIOException)
          {
            throw new ReferenceNotInitializedException("empty", localIOException);
          }
          catch (ParserConfigurationException localParserConfigurationException)
          {
            throw new ReferenceNotInitializedException("empty", localParserConfigurationException);
          }
          catch (SAXException localSAXException)
          {
            throw new ReferenceNotInitializedException("empty", localSAXException);
          }
        }
      }
      catch (ReferenceNotInitializedException localReferenceNotInitializedException)
      {
        Object[] arrayOfObject = { localReference.getURI() };
        throw new MissingResourceFailureException("signature.Verification.Reference.NoInput", arrayOfObject, localReferenceNotInitializedException, localReference);
      }
    }
    return i;
  }

  private void setVerificationResult(int paramInt, boolean paramBoolean)
  {
    if (this.verificationResults == null)
      this.verificationResults = new boolean[getLength()];
    this.verificationResults[paramInt] = paramBoolean;
  }

  public boolean getVerificationResult(int paramInt)
    throws XMLSecurityException
  {
    if ((paramInt < 0) || (paramInt > getLength() - 1))
    {
      Object[] arrayOfObject = { Integer.toString(paramInt), Integer.toString(getLength()) };
      IndexOutOfBoundsException localIndexOutOfBoundsException = new IndexOutOfBoundsException(I18n.translate("signature.Verification.IndexOutOfBounds", arrayOfObject));
      throw new XMLSecurityException("generic.EmptyMessage", localIndexOutOfBoundsException);
    }
    if (this.verificationResults == null)
      try
      {
        verifyReferences();
      }
      catch (Exception localException)
      {
        throw new XMLSecurityException("generic.EmptyMessage", localException);
      }
    return this.verificationResults[paramInt];
  }

  public void addResourceResolver(ResourceResolver paramResourceResolver)
  {
    if (paramResourceResolver != null)
      this._perManifestResolvers.add(paramResourceResolver);
  }

  public void addResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
  {
    if (paramResourceResolverSpi != null)
      this._perManifestResolvers.add(new ResourceResolver(paramResourceResolverSpi));
  }

  public void setResolverProperty(String paramString1, String paramString2)
  {
    this._resolverProperties.put(paramString1, paramString2);
  }

  public String getResolverProperty(String paramString)
  {
    return (String)this._resolverProperties.get(paramString);
  }

  public byte[] getSignedContentItem(int paramInt)
    throws XMLSignatureException
  {
    try
    {
      return getReferencedContentAfterTransformsItem(paramInt).getBytes();
    }
    catch (IOException localIOException)
    {
      throw new XMLSignatureException("empty", localIOException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new XMLSignatureException("empty", localCanonicalizationException);
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public XMLSignatureInput getReferencedContentBeforeTransformsItem(int paramInt)
    throws XMLSecurityException
  {
    return item(paramInt).getContentsBeforeTransformation();
  }

  public XMLSignatureInput getReferencedContentAfterTransformsItem(int paramInt)
    throws XMLSecurityException
  {
    return item(paramInt).getContentsAfterTransformation();
  }

  public int getSignedContentLength()
  {
    return getLength();
  }

  public String getBaseLocalName()
  {
    return "Manifest";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.Manifest
 * JD-Core Version:    0.6.0
 */