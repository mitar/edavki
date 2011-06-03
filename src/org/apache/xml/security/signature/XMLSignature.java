package org.apache.xml.security.signature;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.HexDump;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.SignerOutputStream;
import org.apache.xml.security.utils.UnsyncBufferedOutputStream;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import si.hermes.security.CertificateProvider;

public final class XMLSignature extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(XMLSignature.class.getName());
  public static final String ALGO_ID_MAC_HMAC_SHA1 = "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
  public static final String ALGO_ID_SIGNATURE_DSA = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
  public static final String ALGO_ID_SIGNATURE_RSA = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
  public static final String ALGO_ID_SIGNATURE_RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
  public static final String ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5 = "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
  public static final String ALGO_ID_SIGNATURE_RSA_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
  public static final String ALGO_ID_SIGNATURE_RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
  public static final String ALGO_ID_SIGNATURE_RSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
  public static final String ALGO_ID_SIGNATURE_RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
  public static final String ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
  public static final String ALGO_ID_MAC_HMAC_RIPEMD160 = "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
  public static final String ALGO_ID_MAC_HMAC_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
  public static final String ALGO_ID_MAC_HMAC_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
  public static final String ALGO_ID_MAC_HMAC_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
  private SignedInfo _signedInfo = null;
  private KeyInfo _keyInfo = null;
  private boolean _followManifestsDuringValidation = false;

  public XMLSignature(Document paramDocument, String paramString1, String paramString2)
    throws XMLSecurityException
  {
    this(paramDocument, paramString1, paramString2, 0, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
  }

  public XMLSignature(Document paramDocument, String paramString1, String paramString2, int paramInt)
    throws XMLSecurityException
  {
    this(paramDocument, paramString1, paramString2, paramInt, "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
  }

  public XMLSignature(Document paramDocument, String paramString1, String paramString2, String paramString3)
    throws XMLSecurityException
  {
    this(paramDocument, paramString1, paramString2, 0, paramString3);
  }

  public XMLSignature(Document paramDocument, String paramString1, String paramString2, int paramInt, String paramString3)
    throws XMLSecurityException
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._baseURI = paramString1;
    this._signedInfo = new SignedInfo(this._doc, paramString2, paramInt, paramString3);
    this._constructionElement.appendChild(this._signedInfo.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
    Element localElement = XMLUtils.createElementInSignatureSpace(this._doc, "SignatureValue");
    this._constructionElement.appendChild(localElement);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public XMLSignature(Document paramDocument, String paramString, Element paramElement1, Element paramElement2)
    throws XMLSecurityException
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._baseURI = paramString;
    this._signedInfo = new SignedInfo(this._doc, paramElement1, paramElement2);
    this._constructionElement.appendChild(this._signedInfo.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
    Element localElement = XMLUtils.createElementInSignatureSpace(this._doc, "SignatureValue");
    this._constructionElement.appendChild(localElement);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public XMLSignature(Element paramElement, String paramString)
    throws XMLSignatureException, XMLSecurityException
  {
    super(paramElement, paramString);
    Element localElement;
    Object localObject1;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SignedInfo", 0)) == null)
    {
      localObject1 = new Object[] { "SignedInfo", "Signature" };
      throw new XMLSignatureException("xml.WrongContent", localObject1);
    }
    this._signedInfo = new SignedInfo(localElement, paramString);
    Object localObject2;
    if ((localObject1 = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SignatureValue", 0)) == null)
    {
      localObject2 = new Object[] { "SignatureValue", "Signature" };
      throw new XMLSignatureException("xml.WrongContent", localObject2);
    }
    if ((localObject2 = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "KeyInfo", 0)) != null)
      this._keyInfo = new KeyInfo((Element)localObject2, paramString);
  }

  public final void setId(String paramString)
  {
    if ((this._state == 0) && (paramString != null))
    {
      this._constructionElement.setAttributeNS(null, "Id", paramString);
      IdResolver.registerElementById(this._constructionElement, paramString);
    }
  }

  public final String getId()
  {
    return this._constructionElement.getAttributeNS(null, "Id");
  }

  public final SignedInfo getSignedInfo()
  {
    return this._signedInfo;
  }

  public final byte[] getSignatureValue()
    throws XMLSignatureException
  {
    try
    {
      Element localElement;
      byte[] arrayOfByte;
      return arrayOfByte = Base64.decode(localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SignatureValue", 0));
    }
    catch (Base64DecodingException localBase64DecodingException)
    {
    }
    throw new XMLSignatureException("empty", localBase64DecodingException);
  }

  private void setSignatureValueElement(byte[] paramArrayOfByte)
  {
    if (this._state == 0)
    {
      Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SignatureValue", 0);
      while (localElement.hasChildNodes())
        localElement.removeChild(localElement.getFirstChild());
      String str;
      if ((str = Base64.encode(paramArrayOfByte)).length() > 76)
        str = "\n" + str + "\n";
      Text localText = this._doc.createTextNode(str);
      localElement.appendChild(localText);
    }
  }

  public final KeyInfo getKeyInfo()
  {
    if ((this._state == 0) && (this._keyInfo == null))
    {
      this._keyInfo = new KeyInfo(this._doc);
      Element localElement1 = this._keyInfo.getElement();
      Element localElement2 = null;
      Node localNode;
      if ((localElement2 = XMLUtils.selectDsNode(localNode = this._constructionElement.getFirstChild(), "Object", 0)) != null)
      {
        this._constructionElement.insertBefore(localElement1, localElement2);
        this._constructionElement.insertBefore(this._doc.createTextNode("\n"), localElement2);
      }
      else
      {
        this._constructionElement.appendChild(localElement1);
        XMLUtils.addReturnToElement(this._constructionElement);
      }
    }
    return this._keyInfo;
  }

  public final void appendObject(ObjectContainer paramObjectContainer)
    throws XMLSignatureException
  {
    try
    {
      if (this._state != 0)
        throw new XMLSignatureException("signature.operationOnlyBeforeSign");
      this._constructionElement.appendChild(paramObjectContainer.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
      return;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      throw new XMLSignatureException("empty", localXMLSecurityException);
    }
  }

  public final ObjectContainer getObjectItem(int paramInt)
  {
    Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "Object", paramInt);
    try
    {
      return new ObjectContainer(localElement, this._baseURI);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    return null;
  }

  public final int getObjectLength()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "Object");
  }

  public final void sign(Key paramKey)
    throws XMLSignatureException
  {
    if ((paramKey instanceof PublicKey))
      throw new IllegalArgumentException(I18n.translate("algorithms.operationOnlyVerification"));
    try
    {
      if (this._state == 0)
      {
        Element localElement = this._signedInfo.getSignatureMethodElement();
        SignatureAlgorithm localSignatureAlgorithm;
        (localSignatureAlgorithm = new SignatureAlgorithm(localElement, getBaseURI())).initSign(paramKey);
        SignedInfo localSignedInfo;
        (localSignedInfo = getSignedInfo()).generateDigestValues();
        UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(new SignerOutputStream(localSignatureAlgorithm));
        try
        {
          localUnsyncBufferedOutputStream.close();
        }
        catch (IOException localIOException)
        {
        }
        localSignedInfo.signInOctectStream(localUnsyncBufferedOutputStream);
        byte[] arrayOfByte = localSignatureAlgorithm.sign();
        setSignatureValueElement(arrayOfByte);
      }
      return;
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
      throw new XMLSignatureException("empty", localXMLSecurityException);
    }
  }

  public final void addResourceResolver(ResourceResolver paramResourceResolver)
  {
    getSignedInfo().addResourceResolver(paramResourceResolver);
  }

  public final void addResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
  {
    getSignedInfo().addResourceResolver(paramResourceResolverSpi);
  }

  public final boolean checkSignatureValue(X509Certificate paramX509Certificate)
    throws XMLSignatureException
  {
    if (paramX509Certificate != null)
      return checkSignatureValue(paramX509Certificate.getPublicKey());
    Object[] arrayOfObject = { "Didn't get a certificate" };
    throw new XMLSignatureException("empty", arrayOfObject);
  }

  public final boolean checkSignatureValue(Key paramKey)
    throws XMLSignatureException
  {
    if (paramKey == null)
    {
      Object[] arrayOfObject = { "Didn't get a key" };
      throw new XMLSignatureException("empty", arrayOfObject);
    }
    try
    {
      if (!getSignedInfo().verify(this._followManifestsDuringValidation))
        return false;
      byte[] arrayOfByte = getSignatureValue();
      if (log.isDebugEnabled())
        log.debug("SignatureValue = " + HexDump.byteArrayToHexString(arrayOfByte));
      SignatureAlgorithm localSignatureAlgorithm = new SignatureAlgorithm(getSignedInfo().getSignatureMethodElement(), getBaseURI());
      if (log.isDebugEnabled())
      {
        log.debug("SignatureMethodURI = " + localSignatureAlgorithm.getAlgorithmURI());
        log.debug("jceSigAlgorithm    = " + localSignatureAlgorithm.getJCEAlgorithmString());
        log.debug("jceSigProvider     = " + localSignatureAlgorithm.getJCEProviderName());
        log.debug("PublicKey = " + paramKey);
      }
      localSignatureAlgorithm.initVerify(CertificateProvider.convertToPublicKey(paramKey));
      SignerOutputStream localSignerOutputStream = new SignerOutputStream(localSignatureAlgorithm);
      UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localSignerOutputStream);
      this._signedInfo.signInOctectStream(localUnsyncBufferedOutputStream);
      try
      {
        localUnsyncBufferedOutputStream.close();
      }
      catch (IOException localIOException)
      {
      }
      boolean bool;
      if (!(bool = localSignatureAlgorithm.verify(arrayOfByte)))
        log.error("Signature value invalid.");
      return bool;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new XMLSignatureException("empty", localXMLSecurityException);
  }

  public final void addDocument(String paramString1, Transforms paramTransforms, String paramString2, String paramString3, String paramString4)
    throws XMLSignatureException
  {
    this._signedInfo.addDocument(this._baseURI, paramString1, paramTransforms, paramString2, paramString3, paramString4);
  }

  public final void addDocument(String paramString1, Transforms paramTransforms, String paramString2)
    throws XMLSignatureException
  {
    this._signedInfo.addDocument(this._baseURI, paramString1, paramTransforms, paramString2, null, null);
  }

  public final void addDocument(String paramString, Transforms paramTransforms)
    throws XMLSignatureException
  {
    this._signedInfo.addDocument(this._baseURI, paramString, paramTransforms, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
  }

  public final void addDocument(String paramString)
    throws XMLSignatureException
  {
    this._signedInfo.addDocument(this._baseURI, paramString, null, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
  }

  public final void addKeyInfo(X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    X509Data localX509Data;
    (localX509Data = new X509Data(this._doc)).addCertificate(paramX509Certificate);
    getKeyInfo().add(localX509Data);
  }

  public final void addKeyInfo(PublicKey paramPublicKey)
  {
    getKeyInfo().add(paramPublicKey);
  }

  public final SecretKey createSecretKey(byte[] paramArrayOfByte)
  {
    return getSignedInfo().createSecretKey(paramArrayOfByte);
  }

  public final void setFollowNestedManifests(boolean paramBoolean)
  {
    this._followManifestsDuringValidation = paramBoolean;
  }

  public final String getBaseLocalName()
  {
    return "Signature";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.XMLSignature
 * JD-Core Version:    0.6.0
 */