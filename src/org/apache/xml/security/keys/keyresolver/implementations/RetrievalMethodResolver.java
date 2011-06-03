package org.apache.xml.security.keys.keyresolver.implementations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.RetrievalMethod;
import org.apache.xml.security.keys.keyresolver.KeyResolver;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class RetrievalMethodResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(RetrievalMethodResolver.class.getName());

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return XMLUtils.elementIsInSignatureSpace(paramElement, "RetrievalMethod");
  }

  public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    try
    {
      RetrievalMethod localRetrievalMethod;
      Attr localAttr = (localRetrievalMethod = new RetrievalMethod(paramElement, paramString)).getURIAttr();
      String str = localRetrievalMethod.getType();
      Transforms localTransforms = localRetrievalMethod.getTransforms();
      ResourceResolver localResourceResolver;
      if ((localResourceResolver = ResourceResolver.getInstance(localAttr, paramString)) != null)
      {
        XMLSignatureInput localXMLSignatureInput = localResourceResolver.resolve(localAttr, paramString);
        if (log.isDebugEnabled())
          log.debug("Before applying Transforms, resource has " + localXMLSignatureInput.getBytes().length + "bytes");
        if (localTransforms != null)
        {
          log.debug("We have Transforms");
          localXMLSignatureInput = localTransforms.performTransforms(localXMLSignatureInput);
        }
        if (log.isDebugEnabled())
        {
          log.debug("After applying Transforms, resource has " + localXMLSignatureInput.getBytes().length + "bytes");
          log.debug("Resolved to resource " + localXMLSignatureInput.getSourceURI());
        }
        byte[] arrayOfByte = localXMLSignatureInput.getBytes();
        Object localObject2;
        if ((str != null) && (str.equals("http://www.w3.org/2000/09/xmldsig#rawX509Certificate")))
        {
          if ((localObject2 = (X509Certificate)(localObject1 = CertificateFactory.getInstance("X.509")).generateCertificate(new ByteArrayInputStream(arrayOfByte))) != null)
            return ((X509Certificate)localObject2).getPublicKey();
          break label452;
        }
        if (log.isDebugEnabled())
          log.debug("we have to parse " + arrayOfByte.length + " bytes");
        Object localObject1 = getDocFromBytes(arrayOfByte);
        if (log.isDebugEnabled())
          log.debug("Now we have a {" + ((Element)localObject1).getNamespaceURI() + "}" + ((Element)localObject1).getLocalName() + " Element");
        if ((localObject1 != null) && ((localObject2 = KeyResolver.getInstance(getFirstElementChild((Element)localObject1), paramString, paramStorageResolver)) != null))
          return ((KeyResolver)localObject2).resolvePublicKey(getFirstElementChild((Element)localObject1), paramString, paramStorageResolver);
      }
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      tmpTernaryOp = "XMLSecurityException";
    }
    catch (CertificateException localCertificateException)
    {
      tmpTernaryOp = "CertificateException";
    }
    catch (IOException localIOException)
    {
    }
    log.debug("IOException", localIOException);
    label452: return (PublicKey)(PublicKey)null;
  }

  public X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    try
    {
      RetrievalMethod localRetrievalMethod;
      Attr localAttr = (localRetrievalMethod = new RetrievalMethod(paramElement, paramString)).getURIAttr();
      Transforms localTransforms = localRetrievalMethod.getTransforms();
      if (log.isDebugEnabled())
        log.debug("Asked to resolve URI " + localAttr);
      ResourceResolver localResourceResolver;
      if ((localResourceResolver = ResourceResolver.getInstance(localAttr, paramString)) != null)
      {
        XMLSignatureInput localXMLSignatureInput = localResourceResolver.resolve(localAttr, paramString);
        if (log.isDebugEnabled())
          log.debug("Before applying Transforms, resource has " + localXMLSignatureInput.getBytes().length + "bytes");
        if (localTransforms != null)
        {
          log.debug("We have Transforms");
          localXMLSignatureInput = localTransforms.performTransforms(localXMLSignatureInput);
        }
        if (log.isDebugEnabled())
        {
          log.debug("After applying Transforms, resource has " + localXMLSignatureInput.getBytes().length + "bytes");
          log.debug("Resolved to resource " + localXMLSignatureInput.getSourceURI());
        }
        byte[] arrayOfByte = localXMLSignatureInput.getBytes();
        Object localObject2;
        if ((localRetrievalMethod.getType() != null) && (localRetrievalMethod.getType().equals("http://www.w3.org/2000/09/xmldsig#rawX509Certificate")))
        {
          if ((localObject2 = (X509Certificate)(localObject1 = CertificateFactory.getInstance("X.509")).generateCertificate(new ByteArrayInputStream(arrayOfByte))) != null)
            return localObject2;
          break label487;
        }
        if (log.isDebugEnabled())
          log.debug("we have to parse " + arrayOfByte.length + " bytes");
        Object localObject1 = getDocFromBytes(arrayOfByte);
        if (log.isDebugEnabled())
          log.debug("Now we have a {" + ((Element)localObject1).getNamespaceURI() + "}" + ((Element)localObject1).getLocalName() + " Element");
        if ((localObject1 != null) && ((localObject2 = KeyResolver.getInstance(getFirstElementChild((Element)localObject1), paramString, paramStorageResolver)) != null))
          return ((KeyResolver)localObject2).resolveX509Certificate(getFirstElementChild((Element)localObject1), paramString, paramStorageResolver);
      }
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      tmpTernaryOp = "XMLSecurityException";
    }
    catch (CertificateException localCertificateException)
    {
      tmpTernaryOp = "CertificateException";
    }
    catch (IOException localIOException)
    {
    }
    log.debug("IOException", localIOException);
    label487: return (X509Certificate)(X509Certificate)null;
  }

  Element getDocFromBytes(byte[] paramArrayOfByte)
    throws KeyResolverException
  {
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory;
      (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setNamespaceAware(true);
      DocumentBuilder localDocumentBuilder;
      Document localDocument;
      return (localDocument = (localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder()).parse(new ByteArrayInputStream(paramArrayOfByte))).getDocumentElement();
    }
    catch (SAXException localSAXException)
    {
      throw new KeyResolverException("empty", localSAXException);
    }
    catch (IOException localIOException)
    {
      throw new KeyResolverException("empty", localIOException);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
    }
    throw new KeyResolverException("empty", localParserConfigurationException);
  }

  public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }

  static Element getFirstElementChild(Element paramElement)
  {
    Node localNode;
    return (Element)localNode;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.RetrievalMethodResolver
 * JD-Core Version:    0.6.0
 */