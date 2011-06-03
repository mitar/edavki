package org.apache.xml.security.keys;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.KeyName;
import org.apache.xml.security.keys.content.KeyValue;
import org.apache.xml.security.keys.content.MgmtData;
import org.apache.xml.security.keys.content.PGPData;
import org.apache.xml.security.keys.content.RetrievalMethod;
import org.apache.xml.security.keys.content.SPKIData;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.keys.content.keyvalues.DSAKeyValue;
import org.apache.xml.security.keys.content.keyvalues.RSAKeyValue;
import org.apache.xml.security.keys.keyresolver.KeyResolver;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KeyInfo extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(KeyInfo.class.getName());
  List _internalKeyResolvers = new ArrayList();
  List _storageResolvers = new ArrayList();
  static boolean _alreadyInitialized = false;

  public KeyInfo(Document paramDocument)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public KeyInfo(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
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

  public void addKeyName(String paramString)
  {
    add(new KeyName(this._doc, paramString));
  }

  public void add(KeyName paramKeyName)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramKeyName.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addKeyValue(PublicKey paramPublicKey)
  {
    add(new KeyValue(this._doc, paramPublicKey));
  }

  public void addKeyValue(Element paramElement)
  {
    add(new KeyValue(this._doc, paramElement));
  }

  public void add(DSAKeyValue paramDSAKeyValue)
  {
    add(new KeyValue(this._doc, paramDSAKeyValue));
  }

  public void add(RSAKeyValue paramRSAKeyValue)
  {
    add(new KeyValue(this._doc, paramRSAKeyValue));
  }

  public void add(PublicKey paramPublicKey)
  {
    add(new KeyValue(this._doc, paramPublicKey));
  }

  public void add(KeyValue paramKeyValue)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramKeyValue.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addMgmtData(String paramString)
  {
    add(new MgmtData(this._doc, paramString));
  }

  public void add(MgmtData paramMgmtData)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramMgmtData.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void add(PGPData paramPGPData)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramPGPData.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addRetrievalMethod(String paramString1, Transforms paramTransforms, String paramString2)
  {
    add(new RetrievalMethod(this._doc, paramString1, paramTransforms, paramString2));
  }

  public void add(RetrievalMethod paramRetrievalMethod)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramRetrievalMethod.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void add(SPKIData paramSPKIData)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramSPKIData.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void add(X509Data paramX509Data)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramX509Data.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void add(EncryptedKey paramEncryptedKey)
    throws XMLEncryptionException
  {
    if (this._state == 0)
    {
      XMLCipher localXMLCipher = XMLCipher.getInstance();
      this._constructionElement.appendChild(localXMLCipher.martial(paramEncryptedKey));
    }
  }

  public void addUnknownElement(Element paramElement)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramElement);
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public int lengthKeyName()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "KeyName");
  }

  public int lengthKeyValue()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
  }

  public int lengthMgmtData()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
  }

  public int lengthPGPData()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "PGPData");
  }

  public int lengthRetrievalMethod()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
  }

  public int lengthSPKIData()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
  }

  public int lengthX509Data()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509Data");
  }

  public int lengthUnknownElement()
  {
    int i = 0;
    NodeList localNodeList = this._constructionElement.getChildNodes();
    for (int j = 0; j < localNodeList.getLength(); j++)
    {
      Node localNode;
      if (((localNode = localNodeList.item(j)).getNodeType() != 1) || (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
        continue;
      i++;
    }
    return i;
  }

  public KeyName itemKeyName(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "KeyName", paramInt)) != null)
      return new KeyName(localElement, this._baseURI);
    return null;
  }

  public KeyValue itemKeyValue(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "KeyValue", paramInt)) != null)
      return new KeyValue(localElement, this._baseURI);
    return null;
  }

  public MgmtData itemMgmtData(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "MgmtData", paramInt)) != null)
      return new MgmtData(localElement, this._baseURI);
    return null;
  }

  public PGPData itemPGPData(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "PGPData", paramInt)) != null)
      return new PGPData(localElement, this._baseURI);
    return null;
  }

  public RetrievalMethod itemRetrievalMethod(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "RetrievalMethod", paramInt)) != null)
      return new RetrievalMethod(localElement, this._baseURI);
    return null;
  }

  public SPKIData itemSPKIData(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SPKIData", paramInt)) != null)
      return new SPKIData(localElement, this._baseURI);
    return null;
  }

  public X509Data itemX509Data(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509Data", paramInt)) != null)
      return new X509Data(localElement, this._baseURI);
    return null;
  }

  public EncryptedKey itemEncryptedKey(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectXencNode(this._constructionElement.getFirstChild(), "EncryptedKey", paramInt)) != null)
    {
      XMLCipher localXMLCipher;
      (localXMLCipher = XMLCipher.getInstance()).init(4, null);
      return localXMLCipher.loadEncryptedKey(localElement);
    }
    return null;
  }

  public Element itemUnknownElement(int paramInt)
  {
    NodeList localNodeList = this._constructionElement.getChildNodes();
    int i = 0;
    for (int j = 0; j < localNodeList.getLength(); j++)
    {
      Node localNode;
      if (((localNode = localNodeList.item(j)).getNodeType() != 1) || (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
        continue;
      i++;
      if (i == paramInt)
        return (Element)localNode;
    }
    return null;
  }

  public boolean isEmpty()
  {
    return this._constructionElement.getFirstChild() == null;
  }

  public boolean containsKeyName()
  {
    return lengthKeyName() > 0;
  }

  public boolean containsKeyValue()
  {
    return lengthKeyValue() > 0;
  }

  public boolean containsMgmtData()
  {
    return lengthMgmtData() > 0;
  }

  public boolean containsPGPData()
  {
    return lengthPGPData() > 0;
  }

  public boolean containsRetrievalMethod()
  {
    return lengthRetrievalMethod() > 0;
  }

  public boolean containsSPKIData()
  {
    return lengthSPKIData() > 0;
  }

  public boolean containsUnknownElement()
  {
    return lengthUnknownElement() > 0;
  }

  public boolean containsX509Data()
  {
    return lengthX509Data() > 0;
  }

  public PublicKey getPublicKey()
    throws KeyResolverException
  {
    PublicKey localPublicKey;
    if ((localPublicKey = getPublicKeyFromInternalResolvers()) != null)
    {
      log.debug("I could find a key using the per-KeyInfo key resolvers");
      return localPublicKey;
    }
    log.debug("I couldn't find a key using the per-KeyInfo key resolvers");
    if ((localPublicKey = getPublicKeyFromStaticResolvers()) != null)
    {
      log.debug("I could find a key using the system-wide key resolvers");
      return localPublicKey;
    }
    log.debug("I couldn't find a key using the system-wide key resolvers");
    return null;
  }

  PublicKey getPublicKeyFromStaticResolvers()
    throws KeyResolverException
  {
    Node localNode;
    for (int i = 0; i < KeyResolver.length(); i++)
    {
      KeyResolver localKeyResolver = KeyResolver.item(i);
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolver.resolvePublicKey((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          PublicKey localPublicKey;
          if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localPublicKey = localKeyResolver.resolvePublicKey((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localPublicKey;
        }
    }
    return (PublicKey)null;
  }

  PublicKey getPublicKeyFromInternalResolvers()
    throws KeyResolverException
  {
    Node localNode;
    for (int i = 0; i < lengthInternalKeyResolver(); i++)
    {
      KeyResolverSpi localKeyResolverSpi = itemInternalKeyResolver(i);
      if (log.isDebugEnabled())
        log.debug("Try " + localKeyResolverSpi.getClass().getName());
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolverSpi.engineResolvePublicKey((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          PublicKey localPublicKey;
          if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localPublicKey = localKeyResolverSpi.engineResolvePublicKey((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localPublicKey;
        }
    }
    return (PublicKey)null;
  }

  public X509Certificate getX509Certificate()
    throws KeyResolverException
  {
    X509Certificate localX509Certificate;
    if ((localX509Certificate = getX509CertificateFromInternalResolvers()) != null)
    {
      log.debug("I could find a X509Certificate using the per-KeyInfo key resolvers");
      return localX509Certificate;
    }
    log.debug("I couldn't find a X509Certificate using the per-KeyInfo key resolvers");
    if ((localX509Certificate = getX509CertificateFromStaticResolvers()) != null)
    {
      log.debug("I could find a X509Certificate using the system-wide key resolvers");
      return localX509Certificate;
    }
    log.debug("I couldn't find a X509Certificate using the system-wide key resolvers");
    return null;
  }

  X509Certificate getX509CertificateFromStaticResolvers()
    throws KeyResolverException
  {
    if (log.isDebugEnabled())
      log.debug("Start getX509CertificateFromStaticResolvers() with " + KeyResolver.length() + " resolvers");
    Node localNode;
    for (int i = 0; i < KeyResolver.length(); i++)
    {
      KeyResolver localKeyResolver = KeyResolver.item(i);
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolver.resolveX509Certificate((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          X509Certificate localX509Certificate;
          if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localX509Certificate = localKeyResolver.resolveX509Certificate((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localX509Certificate;
        }
    }
    return (X509Certificate)null;
  }

  X509Certificate getX509CertificateFromInternalResolvers()
    throws KeyResolverException
  {
    if (log.isDebugEnabled())
      log.debug("Start getX509CertificateFromInternalResolvers() with " + lengthInternalKeyResolver() + " resolvers");
    Node localNode;
    for (int i = 0; i < lengthInternalKeyResolver(); i++)
    {
      KeyResolverSpi localKeyResolverSpi = itemInternalKeyResolver(i);
      if (log.isDebugEnabled())
        log.debug("Try " + localKeyResolverSpi.getClass().getName());
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolverSpi.engineResolveX509Certificate((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          X509Certificate localX509Certificate;
          if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localX509Certificate = localKeyResolverSpi.engineResolveX509Certificate((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localX509Certificate;
        }
    }
    return (X509Certificate)null;
  }

  public SecretKey getSecretKey()
    throws KeyResolverException
  {
    SecretKey localSecretKey;
    if ((localSecretKey = getSecretKeyFromInternalResolvers()) != null)
    {
      log.debug("I could find a secret key using the per-KeyInfo key resolvers");
      return localSecretKey;
    }
    log.debug("I couldn't find a secret key using the per-KeyInfo key resolvers");
    if ((localSecretKey = getSecretKeyFromStaticResolvers()) != null)
    {
      log.debug("I could find a secret key using the system-wide key resolvers");
      return localSecretKey;
    }
    log.debug("I couldn't find a secret key using the system-wide key resolvers");
    return null;
  }

  SecretKey getSecretKeyFromStaticResolvers()
    throws KeyResolverException
  {
    Node localNode;
    for (int i = 0; i < KeyResolver.length(); i++)
    {
      KeyResolver localKeyResolver = KeyResolver.item(i);
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolver.resolveSecretKey((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          SecretKey localSecretKey;
          if ((localKeyResolver.canResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localSecretKey = localKeyResolver.resolveSecretKey((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localSecretKey;
        }
    }
    return (SecretKey)null;
  }

  SecretKey getSecretKeyFromInternalResolvers()
    throws KeyResolverException
  {
    Node localNode;
    for (int i = 0; i < lengthInternalKeyResolver(); i++)
    {
      KeyResolverSpi localKeyResolverSpi = itemInternalKeyResolver(i);
      if (log.isDebugEnabled())
        log.debug("Try " + localKeyResolverSpi.getClass().getName());
      if (localNode.getNodeType() != 1)
        continue;
      Object localObject;
      if (this._storageResolvers.size() == 0)
      {
        if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), null)) && ((localObject = localKeyResolverSpi.engineResolveSecretKey((Element)localNode, getBaseURI(), null)) != null))
          return localObject;
      }
      else
        for (int j = 0; j < this._storageResolvers.size(); j++)
        {
          localObject = (StorageResolver)this._storageResolvers.get(j);
          SecretKey localSecretKey;
          if ((localKeyResolverSpi.engineCanResolve((Element)localNode, getBaseURI(), (StorageResolver)localObject)) && ((localSecretKey = localKeyResolverSpi.engineResolveSecretKey((Element)localNode, getBaseURI(), (StorageResolver)localObject)) != null))
            return localSecretKey;
        }
    }
    return (SecretKey)null;
  }

  public void registerInternalKeyResolver(KeyResolverSpi paramKeyResolverSpi)
  {
    this._internalKeyResolvers.add(paramKeyResolverSpi);
  }

  int lengthInternalKeyResolver()
  {
    return this._internalKeyResolvers.size();
  }

  KeyResolverSpi itemInternalKeyResolver(int paramInt)
  {
    return (KeyResolverSpi)this._internalKeyResolvers.get(paramInt);
  }

  public void addStorageResolver(StorageResolver paramStorageResolver)
  {
    if (paramStorageResolver != null)
      this._storageResolvers.add(paramStorageResolver);
  }

  List getStorageResolvers()
  {
    return this._storageResolvers;
  }

  public static void init()
  {
    if (!_alreadyInitialized)
    {
      if (log == null)
      {
        log = LogFactory.getLog(KeyInfo.class.getName());
        log.error("Had to assign log in the init() function");
      }
      _alreadyInitialized = true;
    }
  }

  public String getBaseLocalName()
  {
    return "KeyInfo";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.KeyInfo
 * JD-Core Version:    0.6.0
 */