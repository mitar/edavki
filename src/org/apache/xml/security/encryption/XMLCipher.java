package org.apache.xml.security.encryption;

import B;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.implementations.EncryptedKeyResolver;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.InvalidTransformException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.ElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLCipher
{
  private static Log logger = LogFactory.getLog(XMLCipher.class.getName());
  public static final String TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
  public static final String AES_128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
  public static final String AES_256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
  public static final String AES_192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
  public static final String RSA_v1dot5 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
  public static final String RSA_OAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
  public static final String DIFFIE_HELLMAN = "http://www.w3.org/2001/04/xmlenc#dh";
  public static final String TRIPLEDES_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
  public static final String AES_128_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
  public static final String AES_256_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
  public static final String AES_192_KeyWrap = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
  public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
  public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
  public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
  public static final String RIPEMD_160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";
  public static final String XML_DSIG = "http://www.w3.org/2000/09/xmldsig#";
  public static final String N14C_XML = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String N14C_XML_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String EXCL_XML_N14C = "http://www.w3.org/2001/10/xml-exc-c14n#";
  public static final String EXCL_XML_N14C_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
  public static final String BASE64_ENCODING = "http://www.w3.org/2000/09/xmldsig#base64";
  public static final int ENCRYPT_MODE = 1;
  public static final int DECRYPT_MODE = 2;
  public static final int UNWRAP_MODE = 4;
  public static final int WRAP_MODE = 3;
  private static final String ENC_ALGORITHMS = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n";
  private Cipher _contextCipher;
  private int _cipherMode = -2147483648;
  private String _algorithm = null;
  private String _requestedJCEProvider = null;
  private Canonicalizer _canon;
  private Document _contextDocument;
  private Factory _factory;
  private Serializer _serializer;
  private Key _key;
  private Key _kek;
  private EncryptedKey _ek;
  private EncryptedData _ed;

  private XMLCipher()
  {
    logger.debug("Constructing XMLCipher...");
    this._factory = new Factory(null);
    this._serializer = new Serializer();
  }

  private static boolean isValidEncryptionAlgorithm(String paramString)
  {
    int i;
    return i = (paramString.equals("http://www.w3.org/2001/04/xmlenc#tripledes-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes128-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes256-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#aes192-cbc")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#rsa-1_5")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-tripledes")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes128")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes256")) || (paramString.equals("http://www.w3.org/2001/04/xmlenc#kw-aes192")) ? 1 : 0;
  }

  public static XMLCipher getInstance(String paramString)
    throws XMLEncryptionException
  {
    logger.debug("Getting XMLCipher...");
    if (null == paramString)
      logger.error("Transformation unexpectedly null...");
    if (!isValidEncryptionAlgorithm(paramString))
      logger.warn("Algorithm non-standard, expected one of http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n");
    XMLCipher localXMLCipher;
    (localXMLCipher = new XMLCipher())._algorithm = paramString;
    localXMLCipher._key = null;
    localXMLCipher._kek = null;
    try
    {
      localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
    }
    String str = JCEMapper.translateURItoJCEID(paramString);
    try
    {
      localXMLCipher._contextCipher = Cipher.getInstance(str);
      logger.debug("cihper.algoritm = " + localXMLCipher._contextCipher.getAlgorithm());
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new XMLEncryptionException("empty", localNoSuchPaddingException);
    }
    return localXMLCipher;
  }

  public static XMLCipher getInstance(String paramString1, String paramString2)
    throws XMLEncryptionException
  {
    XMLCipher localXMLCipher = getInstance(paramString1);
    if (paramString2 != null)
      try
      {
        localXMLCipher._canon = Canonicalizer.getInstance(paramString2);
      }
      catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
      {
        throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
      }
    return localXMLCipher;
  }

  public static XMLCipher getProviderInstance(String paramString1, String paramString2)
    throws XMLEncryptionException
  {
    logger.debug("Getting XMLCipher...");
    if (null == paramString1)
      logger.error("Transformation unexpectedly null...");
    if (null == paramString2)
      logger.error("Provider unexpectedly null..");
    if ("" == paramString2)
      logger.error("Provider's value unexpectedly not specified...");
    if (!isValidEncryptionAlgorithm(paramString1))
      logger.warn("Algorithm non-standard, expected one of http://www.w3.org/2001/04/xmlenc#tripledes-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes128-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes256-cbc\nhttp://www.w3.org/2001/04/xmlenc#aes192-cbc\nhttp://www.w3.org/2001/04/xmlenc#rsa-1_5\nhttp://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\nhttp://www.w3.org/2001/04/xmlenc#kw-tripledes\nhttp://www.w3.org/2001/04/xmlenc#kw-aes128\nhttp://www.w3.org/2001/04/xmlenc#kw-aes256\nhttp://www.w3.org/2001/04/xmlenc#kw-aes192\n");
    XMLCipher localXMLCipher;
    (localXMLCipher = new XMLCipher())._algorithm = paramString1;
    localXMLCipher._requestedJCEProvider = paramString2;
    localXMLCipher._key = null;
    localXMLCipher._kek = null;
    try
    {
      localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
    }
    try
    {
      String str = JCEMapper.translateURItoJCEID(paramString1);
      localXMLCipher._contextCipher = Cipher.getInstance(str, paramString2);
      logger.debug("cipher._algorithm = " + localXMLCipher._contextCipher.getAlgorithm());
      logger.debug("provider.name = " + paramString2);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new XMLEncryptionException("empty", localNoSuchProviderException);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new XMLEncryptionException("empty", localNoSuchPaddingException);
    }
    return localXMLCipher;
  }

  public static XMLCipher getProviderInstance(String paramString1, String paramString2, String paramString3)
    throws XMLEncryptionException
  {
    XMLCipher localXMLCipher = getProviderInstance(paramString1, paramString2);
    if (paramString3 != null)
      try
      {
        localXMLCipher._canon = Canonicalizer.getInstance(paramString3);
      }
      catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
      {
        throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
      }
    return localXMLCipher;
  }

  public static XMLCipher getInstance()
    throws XMLEncryptionException
  {
    logger.debug("Getting XMLCipher for no transformation...");
    XMLCipher localXMLCipher;
    (localXMLCipher = new XMLCipher())._algorithm = null;
    localXMLCipher._requestedJCEProvider = null;
    localXMLCipher._key = null;
    localXMLCipher._kek = null;
    localXMLCipher._contextCipher = null;
    try
    {
      localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
    }
    return localXMLCipher;
  }

  public static XMLCipher getProviderInstance(String paramString)
    throws XMLEncryptionException
  {
    logger.debug("Getting XMLCipher, provider but no transformation");
    if (null == paramString)
      logger.error("Provider unexpectedly null..");
    if ("" == paramString)
      logger.error("Provider's value unexpectedly not specified...");
    XMLCipher localXMLCipher;
    (localXMLCipher = new XMLCipher())._algorithm = null;
    localXMLCipher._requestedJCEProvider = paramString;
    localXMLCipher._key = null;
    localXMLCipher._kek = null;
    localXMLCipher._contextCipher = null;
    try
    {
      localXMLCipher._canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new XMLEncryptionException("empty", localInvalidCanonicalizerException);
    }
    return localXMLCipher;
  }

  public void init(int paramInt, Key paramKey)
    throws XMLEncryptionException
  {
    logger.debug("Initializing XMLCipher...");
    this._ek = null;
    this._ed = null;
    switch (paramInt)
    {
    case 1:
      logger.debug("opmode = ENCRYPT_MODE");
      this._ed = createEncryptedData(1, "NO VALUE YET");
      break;
    case 2:
      logger.debug("opmode = DECRYPT_MODE");
      break;
    case 3:
      logger.debug("opmode = WRAP_MODE");
      this._ek = createEncryptedKey(1, "NO VALUE YET");
      break;
    case 4:
      logger.debug("opmode = UNWRAP_MODE");
      break;
    default:
      logger.error("Mode unexpectedly invalid");
      throw new XMLEncryptionException("Invalid mode in init");
    }
    this._cipherMode = paramInt;
    this._key = paramKey;
  }

  public EncryptedData getEncryptedData()
  {
    logger.debug("Returning EncryptedData");
    return this._ed;
  }

  public EncryptedKey getEncryptedKey()
  {
    logger.debug("Returning EncryptedKey");
    return this._ek;
  }

  public void setKEK(Key paramKey)
  {
    this._kek = paramKey;
  }

  public Element martial(EncryptedData paramEncryptedData)
  {
    return this._factory.toElement(paramEncryptedData);
  }

  public Element martial(EncryptedKey paramEncryptedKey)
  {
    return this._factory.toElement(paramEncryptedKey);
  }

  public Element martial(Document paramDocument, EncryptedData paramEncryptedData)
  {
    this._contextDocument = paramDocument;
    return this._factory.toElement(paramEncryptedData);
  }

  public Element martial(Document paramDocument, EncryptedKey paramEncryptedKey)
  {
    this._contextDocument = paramDocument;
    return this._factory.toElement(paramEncryptedKey);
  }

  private Document encryptElement(Element paramElement)
    throws Exception
  {
    logger.debug("Encrypting element...");
    if (null == paramElement)
      logger.error("Element unexpectedly null...");
    if (this._cipherMode != 1)
      logger.debug("XMLCipher unexpectedly not in ENCRYPT_MODE...");
    if (this._algorithm == null)
      throw new XMLEncryptionException("XMLCipher instance without transformation specified");
    encryptData(this._contextDocument, paramElement, false);
    Element localElement = this._factory.toElement(this._ed);
    Node localNode;
    (localNode = paramElement.getParentNode()).replaceChild(localElement, paramElement);
    return this._contextDocument;
  }

  private Document encryptElementContent(Element paramElement)
    throws Exception
  {
    logger.debug("Encrypting element content...");
    if (null == paramElement)
      logger.error("Element unexpectedly null...");
    if (this._cipherMode != 1)
      logger.debug("XMLCipher unexpectedly not in ENCRYPT_MODE...");
    if (this._algorithm == null)
      throw new XMLEncryptionException("XMLCipher instance without transformation specified");
    encryptData(this._contextDocument, paramElement, true);
    Element localElement = this._factory.toElement(this._ed);
    removeContent(paramElement);
    paramElement.appendChild(localElement);
    return this._contextDocument;
  }

  public Document doFinal(Document paramDocument1, Document paramDocument2)
    throws Exception
  {
    logger.debug("Processing source document...");
    if (null == paramDocument1)
      logger.error("Context document unexpectedly null...");
    if (null == paramDocument2)
      logger.error("Source document unexpectedly null...");
    this._contextDocument = paramDocument1;
    Document localDocument = null;
    switch (this._cipherMode)
    {
    case 2:
      localDocument = decryptElement(paramDocument2.getDocumentElement());
      break;
    case 1:
      localDocument = encryptElement(paramDocument2.getDocumentElement());
      break;
    case 4:
      break;
    case 3:
      break;
    default:
      throw new XMLEncryptionException("empty", new IllegalStateException());
    }
    return localDocument;
  }

  public Document doFinal(Document paramDocument, Element paramElement)
    throws Exception
  {
    logger.debug("Processing source element...");
    if (null == paramDocument)
      logger.error("Context document unexpectedly null...");
    if (null == paramElement)
      logger.error("Source element unexpectedly null...");
    this._contextDocument = paramDocument;
    Document localDocument = null;
    switch (this._cipherMode)
    {
    case 2:
      localDocument = decryptElement(paramElement);
      break;
    case 1:
      localDocument = encryptElement(paramElement);
      break;
    case 4:
      break;
    case 3:
      break;
    default:
      throw new XMLEncryptionException("empty", new IllegalStateException());
    }
    return localDocument;
  }

  public Document doFinal(Document paramDocument, Element paramElement, boolean paramBoolean)
    throws Exception
  {
    logger.debug("Processing source element...");
    if (null == paramDocument)
      logger.error("Context document unexpectedly null...");
    if (null == paramElement)
      logger.error("Source element unexpectedly null...");
    this._contextDocument = paramDocument;
    Document localDocument = null;
    switch (this._cipherMode)
    {
    case 2:
      if (paramBoolean)
        localDocument = decryptElementContent(paramElement);
      else
        localDocument = decryptElement(paramElement);
      break;
    case 1:
      if (paramBoolean)
        localDocument = encryptElementContent(paramElement);
      else
        localDocument = encryptElement(paramElement);
      break;
    case 4:
      break;
    case 3:
      break;
    default:
      throw new XMLEncryptionException("empty", new IllegalStateException());
    }
    return localDocument;
  }

  public EncryptedData encryptData(Document paramDocument, Element paramElement)
    throws Exception
  {
    return encryptData(paramDocument, paramElement, false);
  }

  private EncryptedData encryptData(Document paramDocument, Element paramElement, boolean paramBoolean)
    throws Exception
  {
    logger.debug("Encrypting element...");
    if (null == paramDocument)
      logger.error("Context document unexpectedly null...");
    if (null == paramElement)
      logger.error("Element unexpectedly null...");
    if (this._cipherMode != 1)
      logger.debug("XMLCipher unexpectedly not in ENCRYPT_MODE...");
    this._contextDocument = paramDocument;
    if (this._algorithm == null)
      throw new XMLEncryptionException("XMLCipher instance without transformation specified");
    String str1 = null;
    Object localObject2;
    if (paramBoolean)
    {
      localObject1 = paramElement.getChildNodes();
      if (null != localObject1)
      {
        str1 = this._serializer.serialize((NodeList)localObject1);
      }
      else
      {
        localObject2 = new Object[] { "Element has no content." };
        throw new XMLEncryptionException("empty", localObject2);
      }
    }
    else
    {
      str1 = this._serializer.serialize(paramElement);
    }
    logger.debug("Serialized octets:\n" + str1);
    Object localObject1 = null;
    if (this._contextCipher == null)
    {
      String str2 = JCEMapper.translateURItoJCEID(this._algorithm);
      logger.debug("alg = " + str2);
      try
      {
        if (this._requestedJCEProvider == null)
          localObject2 = Cipher.getInstance(str2);
        else
          localObject2 = Cipher.getInstance(str2, this._requestedJCEProvider);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
      }
      catch (NoSuchProviderException localNoSuchProviderException)
      {
        throw new XMLEncryptionException("empty", localNoSuchProviderException);
      }
      catch (NoSuchPaddingException localNoSuchPaddingException)
      {
        throw new XMLEncryptionException("empty", localNoSuchPaddingException);
      }
    }
    else
    {
      localObject2 = this._contextCipher;
    }
    try
    {
      ((Cipher)localObject2).init(this._cipherMode, this._key);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLEncryptionException("empty", localInvalidKeyException);
    }
    try
    {
      localObject1 = ((Cipher)localObject2).doFinal(str1.getBytes("UTF-8"));
      logger.debug("Expected cipher.outputSize = " + Integer.toString(((Cipher)localObject2).getOutputSize(str1.getBytes().length)));
      logger.debug("Actual cipher.outputSize = " + Integer.toString(localObject1.length));
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new XMLEncryptionException("empty", localIllegalStateException);
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
    }
    catch (BadPaddingException localBadPaddingException)
    {
      throw new XMLEncryptionException("empty", localBadPaddingException);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new XMLEncryptionException("empty", localUnsupportedEncodingException);
    }
    byte[] arrayOfByte1;
    byte[] arrayOfByte2 = new byte[(arrayOfByte1 = ((Cipher)localObject2).getIV()).length + localObject1.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(localObject1, 0, arrayOfByte2, arrayOfByte1.length, localObject1.length);
    String str3 = Base64.encode(arrayOfByte2);
    logger.debug("Encrypted octets:\n" + str3);
    logger.debug("Encrypted octets length = " + str3.length());
    try
    {
      CipherData localCipherData;
      CipherValue localCipherValue;
      (localCipherValue = (localCipherData = this._ed.getCipherData()).getCipherValue()).setValue(str3);
      this._ed.setType((paramBoolean ? new URI("http://www.w3.org/2001/04/xmlenc#Content") : new URI("http://www.w3.org/2001/04/xmlenc#Element")).toString());
      localEncryptionMethod = this._factory.newEncryptionMethod(new URI(this._algorithm).toString());
      this._ed.setEncryptionMethod(localEncryptionMethod);
      tmpTernaryOp = this._ed;
    }
    catch (URI.MalformedURIException localMalformedURIException)
    {
      EncryptionMethod localEncryptionMethod;
      throw new XMLEncryptionException("empty", localMalformedURIException);
    }
    return (EncryptedData)(EncryptedData)this._ed;
  }

  public EncryptedData loadEncryptedData(Document paramDocument, Element paramElement)
    throws XMLEncryptionException
  {
    logger.debug("Loading encrypted element...");
    if (null == paramDocument)
      logger.error("Context document unexpectedly null...");
    if (null == paramElement)
      logger.error("Element unexpectedly null...");
    if (this._cipherMode != 2)
      logger.error("XMLCipher unexpectedly not in DECRYPT_MODE...");
    this._contextDocument = paramDocument;
    this._ed = this._factory.newEncryptedData(paramElement);
    return this._ed;
  }

  public EncryptedKey loadEncryptedKey(Document paramDocument, Element paramElement)
    throws XMLEncryptionException
  {
    logger.debug("Loading encrypted key...");
    if (null == paramDocument)
      logger.error("Context document unexpectedly null...");
    if (null == paramElement)
      logger.error("Element unexpectedly null...");
    if ((this._cipherMode != 4) && (this._cipherMode != 2))
      logger.debug("XMLCipher unexpectedly not in UNWRAP_MODE or DECRYPT_MODE...");
    this._contextDocument = paramDocument;
    this._ek = this._factory.newEncryptedKey(paramElement);
    return this._ek;
  }

  public EncryptedKey loadEncryptedKey(Element paramElement)
    throws XMLEncryptionException
  {
    return loadEncryptedKey(paramElement.getOwnerDocument(), paramElement);
  }

  public EncryptedKey encryptKey(Document paramDocument, Key paramKey)
    throws XMLEncryptionException
  {
    logger.debug("Encrypting key ...");
    if (null == paramKey)
      logger.error("Key unexpectedly null...");
    if (this._cipherMode != 3)
      logger.debug("XMLCipher unexpectedly not in WRAP_MODE...");
    if (this._algorithm == null)
      throw new XMLEncryptionException("XMLCipher instance without transformation specified");
    this._contextDocument = paramDocument;
    byte[] arrayOfByte = null;
    Cipher localCipher;
    if (this._contextCipher == null)
    {
      String str1 = JCEMapper.translateURItoJCEID(this._algorithm);
      logger.debug("alg = " + str1);
      try
      {
        if (this._requestedJCEProvider == null)
          localCipher = Cipher.getInstance(str1);
        else
          localCipher = Cipher.getInstance(str1, this._requestedJCEProvider);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
      }
      catch (NoSuchProviderException localNoSuchProviderException)
      {
        throw new XMLEncryptionException("empty", localNoSuchProviderException);
      }
      catch (NoSuchPaddingException localNoSuchPaddingException)
      {
        throw new XMLEncryptionException("empty", localNoSuchPaddingException);
      }
    }
    else
    {
      localCipher = this._contextCipher;
    }
    try
    {
      localCipher.init(3, this._key);
      arrayOfByte = localCipher.wrap(paramKey);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLEncryptionException("empty", localInvalidKeyException);
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
    }
    String str2 = Base64.encode(arrayOfByte);
    logger.debug("Encrypted key octets:\n" + str2);
    logger.debug("Encrypted key octets length = " + str2.length());
    CipherValue localCipherValue;
    (localCipherValue = this._ek.getCipherData().getCipherValue()).setValue(str2);
    try
    {
      EncryptionMethod localEncryptionMethod = this._factory.newEncryptionMethod(new URI(this._algorithm).toString());
      this._ek.setEncryptionMethod(localEncryptionMethod);
    }
    catch (URI.MalformedURIException localMalformedURIException)
    {
      throw new XMLEncryptionException("empty", localMalformedURIException);
    }
    return this._ek;
  }

  public Key decryptKey(EncryptedKey paramEncryptedKey, String paramString)
    throws XMLEncryptionException
  {
    logger.debug("Decrypting key from previously loaded EncryptedKey...");
    if (this._cipherMode != 4)
      logger.debug("XMLCipher unexpectedly not in UNWRAP_MODE...");
    if (paramString == null)
      throw new XMLEncryptionException("Cannot decrypt a key without knowing the algorithm");
    Object localObject1;
    if (this._key == null)
    {
      logger.debug("Trying to find a KEK via key resolvers");
      if ((localObject1 = paramEncryptedKey.getKeyInfo()) != null)
        try
        {
          this._key = ((KeyInfo)localObject1).getSecretKey();
        }
        catch (Exception localException)
        {
        }
      if (this._key == null)
      {
        logger.error("XMLCipher::decryptKey called without a KEK and cannot resolve");
        throw new XMLEncryptionException("Unable to decrypt without a KEK");
      }
    }
    byte[] arrayOfByte = (localObject1 = new XMLCipherInput(paramEncryptedKey)).getBytes();
    String str = JCEMapper.getJCEKeyAlgorithmFromURI(paramString);
    Object localObject2;
    Cipher localCipher;
    if (this._contextCipher == null)
    {
      localObject2 = JCEMapper.translateURItoJCEID(paramEncryptedKey.getEncryptionMethod().getAlgorithm());
      logger.debug("JCE Algorithm = " + (String)localObject2);
      try
      {
        if (this._requestedJCEProvider == null)
          localCipher = Cipher.getInstance((String)localObject2);
        else
          localCipher = Cipher.getInstance((String)localObject2, this._requestedJCEProvider);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException1)
      {
        throw new XMLEncryptionException("empty", localNoSuchAlgorithmException1);
      }
      catch (NoSuchProviderException localNoSuchProviderException)
      {
        throw new XMLEncryptionException("empty", localNoSuchProviderException);
      }
      catch (NoSuchPaddingException localNoSuchPaddingException)
      {
        throw new XMLEncryptionException("empty", localNoSuchPaddingException);
      }
    }
    else
    {
      localCipher = this._contextCipher;
    }
    try
    {
      localCipher.init(4, this._key);
      localObject2 = localCipher.unwrap(arrayOfByte, str, 3);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLEncryptionException("empty", localInvalidKeyException);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException2)
    {
      throw new XMLEncryptionException("empty", localNoSuchAlgorithmException2);
    }
    logger.info("Decryption of key type " + paramString + " OK");
    return (Key)(Key)localObject2;
  }

  public Key decryptKey(EncryptedKey paramEncryptedKey)
    throws XMLEncryptionException
  {
    return decryptKey(paramEncryptedKey, this._ed.getEncryptionMethod().getAlgorithm());
  }

  private void removeContent(Node paramNode)
  {
    NodeList localNodeList;
    if ((localNodeList = paramNode.getChildNodes()).getLength() > 0)
    {
      Node localNode = localNodeList.item(0);
      if (null != localNode)
        localNode.getParentNode().removeChild(localNode);
      removeContent(paramNode);
    }
  }

  private Document decryptElement(Element paramElement)
    throws XMLEncryptionException
  {
    logger.debug("Decrypting element...");
    if (this._cipherMode != 2)
      logger.error("XMLCipher unexpectedly not in DECRYPT_MODE...");
    String str;
    try
    {
      str = new String(decryptToByteArray(paramElement), "UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new XMLEncryptionException("empty", localUnsupportedEncodingException);
    }
    logger.debug("Decrypted octets:\n" + str);
    Node localNode = paramElement.getParentNode();
    DocumentFragment localDocumentFragment = this._serializer.deserialize(str, localNode);
    if ((localNode instanceof Document))
    {
      this._contextDocument.removeChild(this._contextDocument.getDocumentElement());
      this._contextDocument.appendChild(localDocumentFragment);
    }
    else
    {
      localNode.replaceChild(localDocumentFragment, paramElement);
    }
    return this._contextDocument;
  }

  private Document decryptElementContent(Element paramElement)
    throws XMLEncryptionException
  {
    Element localElement = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);
    if (null == localElement)
      throw new XMLEncryptionException("No EncryptedData child element.");
    return decryptElement(localElement);
  }

  public byte[] decryptToByteArray(Element paramElement)
    throws XMLEncryptionException
  {
    logger.debug("Decrypting to ByteArray...");
    if (this._cipherMode != 2)
      logger.error("XMLCipher unexpectedly not in DECRYPT_MODE...");
    EncryptedData localEncryptedData = this._factory.newEncryptedData(paramElement);
    Object localObject;
    if (this._key == null)
    {
      if ((localObject = localEncryptedData.getKeyInfo()) != null)
        try
        {
          ((KeyInfo)localObject).registerInternalKeyResolver(new EncryptedKeyResolver(localEncryptedData.getEncryptionMethod().getAlgorithm(), this._kek));
          this._key = ((KeyInfo)localObject).getSecretKey();
        }
        catch (KeyResolverException localKeyResolverException)
        {
        }
      if (this._key == null)
      {
        logger.error("XMLCipher::decryptElement called without a key and unable to resolve");
        throw new XMLEncryptionException("encryption.nokey");
      }
    }
    byte[] arrayOfByte1 = (localObject = new XMLCipherInput(localEncryptedData)).getBytes();
    String str = JCEMapper.translateURItoJCEID(localEncryptedData.getEncryptionMethod().getAlgorithm());
    Cipher localCipher;
    try
    {
      if (this._requestedJCEProvider == null)
        localCipher = Cipher.getInstance(str);
      else
        localCipher = Cipher.getInstance(str, this._requestedJCEProvider);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new XMLEncryptionException("empty", localNoSuchAlgorithmException);
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new XMLEncryptionException("empty", localNoSuchProviderException);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new XMLEncryptionException("empty", localNoSuchPaddingException);
    }
    int i;
    byte[] arrayOfByte2 = new byte[i = localCipher.getBlockSize()];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
    IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte2);
    try
    {
      localCipher.init(this._cipherMode, this._key, localIvParameterSpec);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLEncryptionException("empty", localInvalidKeyException);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new XMLEncryptionException("empty", localInvalidAlgorithmParameterException);
    }
    byte[] arrayOfByte3;
    try
    {
      arrayOfByte3 = localCipher.doFinal(arrayOfByte1, i, arrayOfByte1.length - i);
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      throw new XMLEncryptionException("empty", localIllegalBlockSizeException);
    }
    catch (BadPaddingException localBadPaddingException)
    {
      throw new XMLEncryptionException("empty", localBadPaddingException);
    }
    return (B)arrayOfByte3;
  }

  public EncryptedData createEncryptedData(int paramInt, String paramString)
    throws XMLEncryptionException
  {
    EncryptedData localEncryptedData = null;
    CipherData localCipherData = null;
    switch (paramInt)
    {
    case 2:
      CipherReference localCipherReference = this._factory.newCipherReference(paramString);
      (localCipherData = this._factory.newCipherData(paramInt)).setCipherReference(localCipherReference);
      break;
    case 1:
      CipherValue localCipherValue = this._factory.newCipherValue(paramString);
      (localCipherData = this._factory.newCipherData(paramInt)).setCipherValue(localCipherValue);
      localEncryptedData = this._factory.newEncryptedData(localCipherData);
    }
    return localEncryptedData;
  }

  public EncryptedKey createEncryptedKey(int paramInt, String paramString)
    throws XMLEncryptionException
  {
    EncryptedKey localEncryptedKey = null;
    CipherData localCipherData = null;
    switch (paramInt)
    {
    case 2:
      CipherReference localCipherReference = this._factory.newCipherReference(paramString);
      (localCipherData = this._factory.newCipherData(paramInt)).setCipherReference(localCipherReference);
      break;
    case 1:
      CipherValue localCipherValue = this._factory.newCipherValue(paramString);
      (localCipherData = this._factory.newCipherData(paramInt)).setCipherValue(localCipherValue);
      localEncryptedKey = this._factory.newEncryptedKey(localCipherData);
    }
    return localEncryptedKey;
  }

  public AgreementMethod createAgreementMethod(String paramString)
  {
    return this._factory.newAgreementMethod(paramString);
  }

  public CipherData createCipherData(int paramInt)
  {
    return this._factory.newCipherData(paramInt);
  }

  public CipherReference createCipherReference(String paramString)
  {
    return this._factory.newCipherReference(paramString);
  }

  public CipherValue createCipherValue(String paramString)
  {
    return this._factory.newCipherValue(paramString);
  }

  public EncryptionMethod createEncryptionMethod(String paramString)
  {
    return this._factory.newEncryptionMethod(paramString);
  }

  public EncryptionProperties createEncryptionProperties()
  {
    return this._factory.newEncryptionProperties();
  }

  public EncryptionProperty createEncryptionProperty()
  {
    return this._factory.newEncryptionProperty();
  }

  public ReferenceList createReferenceList(int paramInt)
  {
    return this._factory.newReferenceList(paramInt);
  }

  public Transforms createTransforms()
  {
    return this._factory.newTransforms();
  }

  public Transforms createTransforms(Document paramDocument)
  {
    return this._factory.newTransforms(paramDocument);
  }

  private class Factory
  {
    private final XMLCipher this$0;

    private Factory()
    {
      this.this$0 = this$1;
    }

    AgreementMethod newAgreementMethod(String paramString)
    {
      return new AgreementMethodImpl(paramString);
    }

    CipherData newCipherData(int paramInt)
    {
      return new CipherDataImpl(paramInt);
    }

    CipherReference newCipherReference(String paramString)
    {
      return new CipherReferenceImpl(paramString);
    }

    CipherValue newCipherValue(String paramString)
    {
      return new CipherValueImpl(paramString);
    }

    EncryptedData newEncryptedData(CipherData paramCipherData)
    {
      return new EncryptedDataImpl(paramCipherData);
    }

    EncryptedKey newEncryptedKey(CipherData paramCipherData)
    {
      return new EncryptedKeyImpl(paramCipherData);
    }

    EncryptionMethod newEncryptionMethod(String paramString)
    {
      return new EncryptionMethodImpl(paramString);
    }

    EncryptionProperties newEncryptionProperties()
    {
      return new EncryptionPropertiesImpl();
    }

    EncryptionProperty newEncryptionProperty()
    {
      return new EncryptionPropertyImpl();
    }

    ReferenceList newReferenceList(int paramInt)
    {
      return new ReferenceListImpl(paramInt);
    }

    Transforms newTransforms()
    {
      return new TransformsImpl();
    }

    Transforms newTransforms(Document paramDocument)
    {
      return new TransformsImpl(paramDocument);
    }

    AgreementMethod newAgreementMethod(Element paramElement)
      throws XMLEncryptionException
    {
      String str = paramElement.getAttributeNS(null, "Algorithm");
      AgreementMethod localAgreementMethod = newAgreementMethod(str);
      Element localElement1 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KA-Nonce").item(0);
      if (null != localElement1)
        localAgreementMethod.setKANonce(localElement1.getNodeValue().getBytes());
      Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "OriginatorKeyInfo").item(0);
      if (null != localElement2)
        try
        {
          localAgreementMethod.setOriginatorKeyInfo(new KeyInfo(localElement2, null));
        }
        catch (XMLSecurityException localXMLSecurityException1)
        {
          throw new XMLEncryptionException("empty", localXMLSecurityException1);
        }
      Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "RecipientKeyInfo").item(0);
      if (null != localElement3)
        try
        {
          localAgreementMethod.setRecipientKeyInfo(new KeyInfo(localElement3, null));
        }
        catch (XMLSecurityException localXMLSecurityException2)
        {
          throw new XMLEncryptionException("empty", localXMLSecurityException2);
        }
      return localAgreementMethod;
    }

    CipherData newCipherData(Element paramElement)
      throws XMLEncryptionException
    {
      int i = 0;
      Element localElement = null;
      i = 1;
      if (paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherReference").getLength() > 0)
      {
        i = 2;
        localElement = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherValue").getLength() > 0 ? "CipherValue" : "CipherReference").item(0);
      }
      CipherData localCipherData = newCipherData(i);
      localCipherData.setCipherValue(newCipherValue(localElement));
      if (i == 2)
        localCipherData.setCipherReference(newCipherReference(localElement));
      return localCipherData;
    }

    CipherReference newCipherReference(Element paramElement)
      throws XMLEncryptionException
    {
      Attr localAttr = paramElement.getAttributeNodeNS(null, "URI");
      CipherReferenceImpl localCipherReferenceImpl = new CipherReferenceImpl(localAttr);
      NodeList localNodeList;
      Element localElement;
      if ((localElement = (Element)(localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "Transforms")).item(0)) != null)
      {
        XMLCipher.logger.debug("Creating a DSIG based Transforms element");
        try
        {
          localCipherReferenceImpl.setTransforms(new TransformsImpl(localElement));
        }
        catch (XMLSignatureException localXMLSignatureException)
        {
          throw new XMLEncryptionException("empty", localXMLSignatureException);
        }
        catch (InvalidTransformException localInvalidTransformException)
        {
          throw new XMLEncryptionException("empty", localInvalidTransformException);
        }
        catch (XMLSecurityException localXMLSecurityException)
        {
          throw new XMLEncryptionException("empty", localXMLSecurityException);
        }
      }
      return localCipherReferenceImpl;
    }

    CipherValue newCipherValue(Element paramElement)
    {
      String str = XMLUtils.getFullTextChildrenFromElement(paramElement);
      CipherValue localCipherValue;
      return localCipherValue = newCipherValue(str);
    }

    EncryptedData newEncryptedData(Element paramElement)
      throws XMLEncryptionException
    {
      EncryptedData localEncryptedData = null;
      NodeList localNodeList;
      Element localElement1 = (Element)(localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherData")).item(localNodeList.getLength() - 1);
      CipherData localCipherData = newCipherData(localElement1);
      localEncryptedData = newEncryptedData(localCipherData);
      try
      {
        localEncryptedData.setId(paramElement.getAttributeNS(null, "Id"));
        localEncryptedData.setType(new URI(paramElement.getAttributeNS(null, "Type")).toString());
        localEncryptedData.setMimeType(paramElement.getAttributeNS(null, "MimeType"));
        localEncryptedData.setEncoding(new URI(paramElement.getAttributeNS(null, "Encoding")).toString());
      }
      catch (URI.MalformedURIException localMalformedURIException)
      {
      }
      Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod").item(0);
      if (null != localElement2)
        localEncryptedData.setEncryptionMethod(newEncryptionMethod(localElement2));
      Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo").item(0);
      if (null != localElement3)
        try
        {
          localEncryptedData.setKeyInfo(new KeyInfo(localElement3, null));
        }
        catch (XMLSecurityException localXMLSecurityException)
        {
          throw new XMLEncryptionException("Error loading Key Info", localXMLSecurityException);
        }
      Element localElement4 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties").item(0);
      if (null != localElement4)
        localEncryptedData.setEncryptionProperties(newEncryptionProperties(localElement4));
      return localEncryptedData;
    }

    EncryptedKey newEncryptedKey(Element paramElement)
      throws XMLEncryptionException
    {
      EncryptedKey localEncryptedKey = null;
      NodeList localNodeList;
      Element localElement1 = (Element)(localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CipherData")).item(localNodeList.getLength() - 1);
      CipherData localCipherData = newCipherData(localElement1);
      localEncryptedKey = newEncryptedKey(localCipherData);
      try
      {
        localEncryptedKey.setId(paramElement.getAttributeNS(null, "Id"));
        localEncryptedKey.setType(new URI(paramElement.getAttributeNS(null, "Type")).toString());
        localEncryptedKey.setMimeType(paramElement.getAttributeNS(null, "MimeType"));
        localEncryptedKey.setEncoding(new URI(paramElement.getAttributeNS(null, "Encoding")).toString());
        localEncryptedKey.setRecipient(paramElement.getAttributeNS(null, "Recipient"));
      }
      catch (URI.MalformedURIException localMalformedURIException)
      {
      }
      Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod").item(0);
      if (null != localElement2)
        localEncryptedKey.setEncryptionMethod(newEncryptionMethod(localElement2));
      Element localElement3 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo").item(0);
      if (null != localElement3)
        try
        {
          localEncryptedKey.setKeyInfo(new KeyInfo(localElement3, null));
        }
        catch (XMLSecurityException localXMLSecurityException)
        {
          throw new XMLEncryptionException("Error loading Key Info", localXMLSecurityException);
        }
      Element localElement4 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties").item(0);
      if (null != localElement4)
        localEncryptedKey.setEncryptionProperties(newEncryptionProperties(localElement4));
      Element localElement5 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "ReferenceList").item(0);
      if (null != localElement5)
        localEncryptedKey.setReferenceList(newReferenceList(localElement5));
      Element localElement6 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName").item(0);
      if (null != localElement6)
        localEncryptedKey.setCarriedName(localElement6.getNodeValue());
      return localEncryptedKey;
    }

    EncryptionMethod newEncryptionMethod(Element paramElement)
    {
      String str = paramElement.getAttributeNS(null, "Algorithm");
      EncryptionMethod localEncryptionMethod = newEncryptionMethod(str);
      Element localElement1 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeySize").item(0);
      if (null != localElement1)
        localEncryptionMethod.setKeySize(Integer.valueOf(localElement1.getFirstChild().getNodeValue()).intValue());
      Element localElement2 = (Element)paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "OAEPparams").item(0);
      if (null != localElement2)
        localEncryptionMethod.setOAEPparams(localElement2.getNodeValue().getBytes());
      return localEncryptionMethod;
    }

    EncryptionProperties newEncryptionProperties(Element paramElement)
    {
      EncryptionProperties localEncryptionProperties;
      (localEncryptionProperties = newEncryptionProperties()).setId(paramElement.getAttributeNS(null, "Id"));
      NodeList localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperty");
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        Node localNode = localNodeList.item(i);
        if (null == localNode)
          continue;
        localEncryptionProperties.addEncryptionProperty(newEncryptionProperty((Element)localNode));
      }
      return localEncryptionProperties;
    }

    EncryptionProperty newEncryptionProperty(Element paramElement)
    {
      EncryptionProperty localEncryptionProperty = newEncryptionProperty();
      try
      {
        localEncryptionProperty.setTarget(new URI(paramElement.getAttributeNS(null, "Target")).toString());
      }
      catch (URI.MalformedURIException localMalformedURIException)
      {
      }
      localEncryptionProperty.setId(paramElement.getAttributeNS(null, "Id"));
      return localEncryptionProperty;
    }

    ReferenceList newReferenceList(Element paramElement)
    {
      int i = 0;
      if (null != paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeyReference").item(0))
        i = null != paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "DataReference").item(0) ? 1 : 2;
      ReferenceListImpl localReferenceListImpl = new ReferenceListImpl(i);
      NodeList localNodeList = null;
      int j;
      String str;
      switch (i)
      {
      case 1:
        localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "DataReference");
        for (j = 0; j < localNodeList.getLength(); j++)
        {
          str = null;
          try
          {
            str = new URI(((Element)localNodeList.item(0)).getNodeValue()).toString();
          }
          catch (URI.MalformedURIException localMalformedURIException1)
          {
          }
          localReferenceListImpl.add(localReferenceListImpl.newDataReference(str));
        }
      case 2:
        localNodeList = paramElement.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "KeyReference");
        for (j = 0; j < localNodeList.getLength(); j++)
        {
          str = null;
          try
          {
            str = new URI(((Element)localNodeList.item(0)).getNodeValue()).toString();
          }
          catch (URI.MalformedURIException localMalformedURIException2)
          {
          }
          localReferenceListImpl.add(localReferenceListImpl.newKeyReference(str));
        }
      }
      return localReferenceListImpl;
    }

    Transforms newTransforms(Element paramElement)
    {
      return null;
    }

    Element toElement(AgreementMethod paramAgreementMethod)
    {
      return ((AgreementMethodImpl)paramAgreementMethod).toElement();
    }

    Element toElement(CipherData paramCipherData)
    {
      return ((CipherDataImpl)paramCipherData).toElement();
    }

    Element toElement(CipherReference paramCipherReference)
    {
      return ((CipherReferenceImpl)paramCipherReference).toElement();
    }

    Element toElement(CipherValue paramCipherValue)
    {
      return ((CipherValueImpl)paramCipherValue).toElement();
    }

    Element toElement(EncryptedData paramEncryptedData)
    {
      return ((EncryptedDataImpl)paramEncryptedData).toElement();
    }

    Element toElement(EncryptedKey paramEncryptedKey)
    {
      return ((EncryptedKeyImpl)paramEncryptedKey).toElement();
    }

    Element toElement(EncryptionMethod paramEncryptionMethod)
    {
      return ((EncryptionMethodImpl)paramEncryptionMethod).toElement();
    }

    Element toElement(EncryptionProperties paramEncryptionProperties)
    {
      return ((EncryptionPropertiesImpl)paramEncryptionProperties).toElement();
    }

    Element toElement(EncryptionProperty paramEncryptionProperty)
    {
      return ((EncryptionPropertyImpl)paramEncryptionProperty).toElement();
    }

    Element toElement(ReferenceList paramReferenceList)
    {
      return ((ReferenceListImpl)paramReferenceList).toElement();
    }

    Element toElement(Transforms paramTransforms)
    {
      return ((TransformsImpl)paramTransforms).toElement();
    }

    Factory(XMLCipher.1 arg2)
    {
      this();
    }

    private class ReferenceListImpl
      implements ReferenceList
    {
      private Class sentry;
      private List references;

      public ReferenceListImpl(int arg2)
      {
        int i;
        if (i == 1)
          this.sentry = DataReference.class;
        else if (i == 2)
          this.sentry = KeyReference.class;
        else
          throw new IllegalArgumentException();
        this.references = new LinkedList();
      }

      public void add(Reference paramReference)
      {
        if (!paramReference.getClass().equals(this.sentry))
          throw new IllegalArgumentException();
        this.references.add(paramReference);
      }

      public void remove(Reference paramReference)
      {
        if (!paramReference.getClass().equals(this.sentry))
          throw new IllegalArgumentException();
        this.references.remove(paramReference);
      }

      public int size()
      {
        return this.references.size();
      }

      public boolean isEmpty()
      {
        return this.references.isEmpty();
      }

      public Iterator getReferences()
      {
        return this.references.iterator();
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "ReferenceList");
        Iterator localIterator = this.references.iterator();
        while (localIterator.hasNext())
        {
          Reference localReference = (Reference)localIterator.next();
          localElement.appendChild(((ReferenceImpl)localReference).toElement());
        }
        return localElement;
      }

      public Reference newDataReference(String paramString)
      {
        return new DataReference(paramString);
      }

      public Reference newKeyReference(String paramString)
      {
        return new KeyReference(paramString);
      }

      private class KeyReference extends XMLCipher.Factory.ReferenceListImpl.ReferenceImpl
      {
        KeyReference(String arg2)
        {
          super(str);
        }

        public Element toElement()
        {
          return super.toElement("KeyReference");
        }
      }

      private class DataReference extends XMLCipher.Factory.ReferenceListImpl.ReferenceImpl
      {
        DataReference(String arg2)
        {
          super(str);
        }

        public Element toElement()
        {
          return super.toElement("DataReference");
        }
      }

      private abstract class ReferenceImpl
        implements Reference
      {
        private String uri;
        private List referenceInformation;

        ReferenceImpl(String arg2)
        {
          Object localObject;
          this.uri = localObject;
          this.referenceInformation = new LinkedList();
        }

        public String getURI()
        {
          return this.uri;
        }

        public Iterator getElementRetrievalInformation()
        {
          return this.referenceInformation.iterator();
        }

        public void setURI(String paramString)
        {
          this.uri = paramString;
        }

        public void removeElementRetrievalInformation(Element paramElement)
        {
          this.referenceInformation.remove(paramElement);
        }

        public void addElementRetrievalInformation(Element paramElement)
        {
          this.referenceInformation.add(paramElement);
        }

        public abstract Element toElement();

        Element toElement(String paramString)
        {
          Element localElement;
          (localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", paramString)).setAttribute("URI", this.uri);
          return localElement;
        }
      }
    }

    private class TransformsImpl extends org.apache.xml.security.transforms.Transforms
      implements Transforms
    {
      public TransformsImpl()
      {
        super();
      }

      public TransformsImpl(Document arg2)
      {
        super();
      }

      public TransformsImpl(Element arg2)
        throws XMLSignatureException, InvalidTransformException, XMLSecurityException, TransformationException
      {
        super("");
      }

      public Element toElement()
      {
        if (this._doc == null)
          this._doc = XMLCipher.this._contextDocument;
        return getElement();
      }

      public org.apache.xml.security.transforms.Transforms getDSTransforms()
      {
        return this;
      }

      public String getBaseNamespace()
      {
        return "http://www.w3.org/2001/04/xmlenc#";
      }
    }

    private class EncryptionPropertyImpl
      implements EncryptionProperty
    {
      private String target = null;
      private String id = null;
      private String attributeName = null;
      private String attributeValue = null;
      private List encryptionInformation = null;

      public EncryptionPropertyImpl()
      {
      }

      public String getTarget()
      {
        return this.target;
      }

      public void setTarget(String paramString)
      {
        URI localURI = null;
        try
        {
          localURI = new URI(paramString);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        this.target = localURI.toString();
      }

      public String getId()
      {
        return this.id;
      }

      public void setId(String paramString)
      {
        this.id = paramString;
      }

      public String getAttribute(String paramString)
      {
        return this.attributeValue;
      }

      public void setAttribute(String paramString1, String paramString2)
      {
        this.attributeName = paramString1;
        this.attributeValue = paramString2;
      }

      public Iterator getEncryptionInformation()
      {
        return this.encryptionInformation.iterator();
      }

      public void addEncryptionInformation(Element paramElement)
      {
        this.encryptionInformation.add(paramElement);
      }

      public void removeEncryptionInformation(Element paramElement)
      {
        this.encryptionInformation.remove(paramElement);
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionProperty");
        if (null != this.target)
          localElement.setAttributeNS(null, "Target", this.target.toString());
        if (null != this.id)
          localElement.setAttributeNS(null, "Id", this.id);
        return localElement;
      }
    }

    private class EncryptionPropertiesImpl
      implements EncryptionProperties
    {
      private String id = null;
      private List encryptionProperties = null;

      public EncryptionPropertiesImpl()
      {
      }

      public String getId()
      {
        return this.id;
      }

      public void setId(String paramString)
      {
        this.id = paramString;
      }

      public Iterator getEncryptionProperties()
      {
        return this.encryptionProperties.iterator();
      }

      public void addEncryptionProperty(EncryptionProperty paramEncryptionProperty)
      {
        this.encryptionProperties.add(paramEncryptionProperty);
      }

      public void removeEncryptionProperty(EncryptionProperty paramEncryptionProperty)
      {
        this.encryptionProperties.remove(paramEncryptionProperty);
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties");
        if (null != this.id)
          localElement.setAttributeNS(null, "Id", this.id);
        Iterator localIterator = getEncryptionProperties();
        while (localIterator.hasNext())
          localElement.appendChild(((XMLCipher.Factory.EncryptionPropertyImpl)localIterator.next()).toElement());
        return localElement;
      }
    }

    private class EncryptionMethodImpl
      implements EncryptionMethod
    {
      private String algorithm = null;
      private int keySize = -2147483648;
      private byte[] oaepParams = null;
      private List encryptionMethodInformation = null;

      public EncryptionMethodImpl(String arg2)
      {
        URI localURI = null;
        try
        {
          String str;
          localURI = new URI(str);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        this.algorithm = localURI.toString();
        this.encryptionMethodInformation = new LinkedList();
      }

      public String getAlgorithm()
      {
        return this.algorithm;
      }

      public int getKeySize()
      {
        return this.keySize;
      }

      public void setKeySize(int paramInt)
      {
        this.keySize = paramInt;
      }

      public byte[] getOAEPparams()
      {
        return this.oaepParams;
      }

      public void setOAEPparams(byte[] paramArrayOfByte)
      {
        this.oaepParams = paramArrayOfByte;
      }

      public Iterator getEncryptionMethodInformation()
      {
        return this.encryptionMethodInformation.iterator();
      }

      public void addEncryptionMethodInformation(Element paramElement)
      {
        this.encryptionMethodInformation.add(paramElement);
      }

      public void removeEncryptionMethodInformation(Element paramElement)
      {
        this.encryptionMethodInformation.remove(paramElement);
      }

      Element toElement()
      {
        Element localElement;
        (localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod")).setAttributeNS(null, "Algorithm", this.algorithm.toString());
        if (this.keySize > 0)
          localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "KeySize").appendChild(XMLCipher.this._contextDocument.createTextNode(String.valueOf(this.keySize))));
        if (null != this.oaepParams)
          localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "OAEPparams").appendChild(XMLCipher.this._contextDocument.createTextNode(new String(this.oaepParams))));
        if (!this.encryptionMethodInformation.isEmpty())
        {
          Iterator localIterator = this.encryptionMethodInformation.iterator();
          localElement.appendChild((Element)localIterator.next());
        }
        return localElement;
      }
    }

    private abstract class EncryptedTypeImpl
    {
      private String id = null;
      private String type = null;
      private String mimeType = null;
      private String encoding = null;
      private EncryptionMethod encryptionMethod = null;
      private KeyInfo keyInfo = null;
      private CipherData cipherData = null;
      private EncryptionProperties encryptionProperties = null;

      protected EncryptedTypeImpl(CipherData arg2)
      {
        Object localObject;
        this.cipherData = localObject;
      }

      public String getId()
      {
        return this.id;
      }

      public void setId(String paramString)
      {
        this.id = paramString;
      }

      public String getType()
      {
        return this.type;
      }

      public void setType(String paramString)
      {
        URI localURI = null;
        try
        {
          localURI = new URI(paramString);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        this.type = localURI.toString();
      }

      public String getMimeType()
      {
        return this.mimeType;
      }

      public void setMimeType(String paramString)
      {
        this.mimeType = paramString;
      }

      public String getEncoding()
      {
        return this.encoding;
      }

      public void setEncoding(String paramString)
      {
        URI localURI = null;
        try
        {
          localURI = new URI(paramString);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        this.encoding = localURI.toString();
      }

      public EncryptionMethod getEncryptionMethod()
      {
        return this.encryptionMethod;
      }

      public void setEncryptionMethod(EncryptionMethod paramEncryptionMethod)
      {
        this.encryptionMethod = paramEncryptionMethod;
      }

      public KeyInfo getKeyInfo()
      {
        return this.keyInfo;
      }

      public void setKeyInfo(KeyInfo paramKeyInfo)
      {
        this.keyInfo = paramKeyInfo;
      }

      public CipherData getCipherData()
      {
        return this.cipherData;
      }

      public EncryptionProperties getEncryptionProperties()
      {
        return this.encryptionProperties;
      }

      public void setEncryptionProperties(EncryptionProperties paramEncryptionProperties)
      {
        this.encryptionProperties = paramEncryptionProperties;
      }
    }

    private class EncryptedKeyImpl extends XMLCipher.Factory.EncryptedTypeImpl
      implements EncryptedKey
    {
      private String keyRecipient = null;
      private ReferenceList referenceList = null;
      private String carriedName = null;

      public EncryptedKeyImpl(CipherData arg2)
      {
        super(localCipherData);
      }

      public String getRecipient()
      {
        return this.keyRecipient;
      }

      public void setRecipient(String paramString)
      {
        this.keyRecipient = paramString;
      }

      public ReferenceList getReferenceList()
      {
        return this.referenceList;
      }

      public void setReferenceList(ReferenceList paramReferenceList)
      {
        this.referenceList = paramReferenceList;
      }

      public String getCarriedName()
      {
        return this.carriedName;
      }

      public void setCarriedName(String paramString)
      {
        this.carriedName = paramString;
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptedKey");
        if (null != super.getId())
          localElement.setAttributeNS(null, "Id", super.getId());
        if (null != super.getType())
          localElement.setAttributeNS(null, "Type", super.getType().toString());
        if (null != super.getMimeType())
          localElement.setAttributeNS(null, "MimeType", super.getMimeType());
        if (null != super.getEncoding())
          localElement.setAttributeNS(null, "Encoding", super.getEncoding().toString());
        if (null != getRecipient())
          localElement.setAttributeNS(null, "Recipient", getRecipient());
        if (null != super.getEncryptionMethod())
          localElement.appendChild(((XMLCipher.Factory.EncryptionMethodImpl)super.getEncryptionMethod()).toElement());
        if (null != super.getKeyInfo())
          localElement.appendChild(super.getKeyInfo().getElement());
        localElement.appendChild(((XMLCipher.Factory.CipherDataImpl)super.getCipherData()).toElement());
        if (null != super.getEncryptionProperties())
          localElement.appendChild(((XMLCipher.Factory.EncryptionPropertiesImpl)super.getEncryptionProperties()).toElement());
        if ((this.referenceList != null) && (!this.referenceList.isEmpty()))
          localElement.appendChild(((XMLCipher.Factory.ReferenceListImpl)getReferenceList()).toElement());
        if (null != this.carriedName)
          localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName").appendChild(XMLCipher.this._contextDocument.createTextNode(this.carriedName)));
        return localElement;
      }
    }

    private class EncryptedDataImpl extends XMLCipher.Factory.EncryptedTypeImpl
      implements EncryptedData
    {
      public EncryptedDataImpl(CipherData arg2)
      {
        super(localCipherData);
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "EncryptedData");
        if (null != super.getId())
          localElement.setAttributeNS(null, "Id", super.getId());
        if (null != super.getType())
          localElement.setAttributeNS(null, "Type", super.getType().toString());
        if (null != super.getMimeType())
          localElement.setAttributeNS(null, "MimeType", super.getMimeType());
        if (null != super.getEncoding())
          localElement.setAttributeNS(null, "Encoding", super.getEncoding().toString());
        if (null != super.getEncryptionMethod())
          localElement.appendChild(((XMLCipher.Factory.EncryptionMethodImpl)super.getEncryptionMethod()).toElement());
        if (null != super.getKeyInfo())
          localElement.appendChild(super.getKeyInfo().getElement());
        localElement.appendChild(((XMLCipher.Factory.CipherDataImpl)super.getCipherData()).toElement());
        if (null != super.getEncryptionProperties())
          localElement.appendChild(((XMLCipher.Factory.EncryptionPropertiesImpl)super.getEncryptionProperties()).toElement());
        return localElement;
      }
    }

    private class CipherValueImpl
      implements CipherValue
    {
      private String cipherValue = null;

      public CipherValueImpl(String arg2)
      {
        Object localObject;
        this.cipherValue = localObject;
      }

      public String getValue()
      {
        return this.cipherValue;
      }

      public void setValue(String paramString)
      {
        this.cipherValue = paramString;
      }

      Element toElement()
      {
        Element localElement;
        (localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherValue")).appendChild(XMLCipher.this._contextDocument.createTextNode(new String(this.cipherValue)));
        return localElement;
      }
    }

    private class CipherReferenceImpl
      implements CipherReference
    {
      private String referenceURI = null;
      private Transforms referenceTransforms = null;
      private Attr referenceNode = null;

      public CipherReferenceImpl(String arg2)
      {
        Object localObject;
        this.referenceURI = localObject;
        this.referenceNode = null;
      }

      public CipherReferenceImpl(Attr arg2)
      {
        Object localObject;
        this.referenceURI = localObject.getNodeValue();
        this.referenceNode = localObject;
      }

      public String getURI()
      {
        return this.referenceURI;
      }

      public Attr getURIAsAttr()
      {
        return this.referenceNode;
      }

      public Transforms getTransforms()
      {
        return this.referenceTransforms;
      }

      public void setTransforms(Transforms paramTransforms)
      {
        this.referenceTransforms = paramTransforms;
      }

      Element toElement()
      {
        Element localElement;
        (localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherReference")).setAttributeNS(null, "URI", this.referenceURI);
        if (null != this.referenceTransforms)
          localElement.appendChild(((XMLCipher.Factory.TransformsImpl)this.referenceTransforms).toElement());
        return localElement;
      }
    }

    private class CipherDataImpl
      implements CipherData
    {
      private static final String valueMessage = "Data type is reference type.";
      private static final String referenceMessage = "Data type is value type.";
      private CipherValue cipherValue = null;
      private CipherReference cipherReference = null;
      private int cipherType = -2147483648;

      public CipherDataImpl(int arg2)
      {
        int i;
        this.cipherType = i;
      }

      public CipherValue getCipherValue()
      {
        return this.cipherValue;
      }

      public void setCipherValue(CipherValue paramCipherValue)
        throws XMLEncryptionException
      {
        if (this.cipherType == 2)
          throw new XMLEncryptionException("empty", new UnsupportedOperationException("Data type is reference type."));
        this.cipherValue = paramCipherValue;
      }

      public CipherReference getCipherReference()
      {
        return this.cipherReference;
      }

      public void setCipherReference(CipherReference paramCipherReference)
        throws XMLEncryptionException
      {
        if (this.cipherType == 1)
          throw new XMLEncryptionException("empty", new UnsupportedOperationException("Data type is value type."));
        this.cipherReference = paramCipherReference;
      }

      public int getDataType()
      {
        return this.cipherType;
      }

      Element toElement()
      {
        Element localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "CipherData");
        if (this.cipherType == 1)
          localElement.appendChild(((XMLCipher.Factory.CipherValueImpl)this.cipherValue).toElement());
        else if (this.cipherType == 2)
          localElement.appendChild(((XMLCipher.Factory.CipherReferenceImpl)this.cipherReference).toElement());
        return localElement;
      }
    }

    private class AgreementMethodImpl
      implements AgreementMethod
    {
      private byte[] kaNonce = null;
      private List agreementMethodInformation = null;
      private KeyInfo originatorKeyInfo = null;
      private KeyInfo recipientKeyInfo = null;
      private String algorithmURI = null;

      public AgreementMethodImpl(String arg2)
      {
        URI localURI = null;
        try
        {
          String str;
          localURI = new URI(str);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        this.algorithmURI = localURI.toString();
      }

      public byte[] getKANonce()
      {
        return this.kaNonce;
      }

      public void setKANonce(byte[] paramArrayOfByte)
      {
        this.kaNonce = paramArrayOfByte;
      }

      public Iterator getAgreementMethodInformation()
      {
        return this.agreementMethodInformation.iterator();
      }

      public void addAgreementMethodInformation(Element paramElement)
      {
        this.agreementMethodInformation.add(paramElement);
      }

      public void revoveAgreementMethodInformation(Element paramElement)
      {
        this.agreementMethodInformation.remove(paramElement);
      }

      public KeyInfo getOriginatorKeyInfo()
      {
        return this.originatorKeyInfo;
      }

      public void setOriginatorKeyInfo(KeyInfo paramKeyInfo)
      {
        this.originatorKeyInfo = paramKeyInfo;
      }

      public KeyInfo getRecipientKeyInfo()
      {
        return this.recipientKeyInfo;
      }

      public void setRecipientKeyInfo(KeyInfo paramKeyInfo)
      {
        this.recipientKeyInfo = paramKeyInfo;
      }

      public String getAlgorithm()
      {
        return this.algorithmURI;
      }

      public void setAlgorithm(String paramString)
      {
        URI localURI = null;
        try
        {
          localURI = new URI(paramString);
        }
        catch (URI.MalformedURIException localMalformedURIException)
        {
        }
        localURI.toString();
      }

      Element toElement()
      {
        Element localElement;
        (localElement = ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "AgreementMethod")).setAttributeNS(null, "Algorithm", this.algorithmURI);
        if (null != this.kaNonce)
          localElement.appendChild(ElementProxy.createElementForFamily(XMLCipher.this._contextDocument, "http://www.w3.org/2001/04/xmlenc#", "KA-Nonce")).appendChild(XMLCipher.this._contextDocument.createTextNode(new String(this.kaNonce)));
        if (!this.agreementMethodInformation.isEmpty())
        {
          Iterator localIterator = this.agreementMethodInformation.iterator();
          while (localIterator.hasNext())
            localElement.appendChild((Element)localIterator.next());
        }
        if (null != this.originatorKeyInfo)
          localElement.appendChild(this.originatorKeyInfo.getElement());
        if (null != this.recipientKeyInfo)
          localElement.appendChild(this.recipientKeyInfo.getElement());
        return localElement;
      }
    }
  }

  private class Serializer
  {
    Serializer()
    {
    }

    String serialize(Document paramDocument)
      throws Exception
    {
      return canonSerialize(paramDocument);
    }

    String serialize(Element paramElement)
      throws Exception
    {
      return canonSerialize(paramElement);
    }

    String serialize(NodeList paramNodeList)
      throws Exception
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      XMLCipher.this._canon.setWriter(localByteArrayOutputStream);
      XMLCipher.this._canon.notReset();
      for (int i = 0; i < paramNodeList.getLength(); i++)
        XMLCipher.this._canon.canonicalizeSubtree(paramNodeList.item(i));
      localByteArrayOutputStream.close();
      return localByteArrayOutputStream.toString("UTF-8");
    }

    String canonSerialize(Node paramNode)
      throws Exception
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      XMLCipher.this._canon.setWriter(localByteArrayOutputStream);
      XMLCipher.this._canon.notReset();
      XMLCipher.this._canon.canonicalizeSubtree(paramNode);
      localByteArrayOutputStream.close();
      return localByteArrayOutputStream.toString("UTF-8");
    }

    DocumentFragment deserialize(String paramString, Node paramNode)
      throws XMLEncryptionException
    {
      StringBuffer localStringBuffer;
      (localStringBuffer = new StringBuffer()).append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><fragment");
      Node localNode1;
      int i = (localObject1 = localNode1.getAttributes()) != null ? ((NamedNodeMap)localObject1).getLength() : 0;
      Object localObject2;
      Object localObject3;
      for (int j = 0; j < i; j++)
      {
        if ((!(localObject2 = ((NamedNodeMap)localObject1).item(j)).getNodeName().startsWith("xmlns:")) && (!((Node)localObject2).getNodeName().equals("xmlns")))
          continue;
        localObject3 = paramNode;
        int k = 0;
        while (localObject3 != localNode1)
        {
          NamedNodeMap localNamedNodeMap;
          if (((localNamedNodeMap = ((Node)localObject3).getAttributes()) != null) && (localNamedNodeMap.getNamedItem(((Node)localObject2).getNodeName()) != null))
          {
            k = 1;
            break;
          }
          localObject3 = ((Node)localObject3).getParentNode();
        }
        if (k != 0)
          continue;
        localStringBuffer.append(" " + ((Node)localObject2).getNodeName() + "=\"" + ((Node)localObject2).getNodeValue() + "\"");
      }
      localStringBuffer.append(">" + paramString + "</" + "fragment" + ">");
      Object localObject1 = localStringBuffer.toString();
      DocumentFragment localDocumentFragment;
      try
      {
        DocumentBuilderFactory localDocumentBuilderFactory;
        (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setNamespaceAware(true);
        localDocumentBuilderFactory.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        DocumentBuilder localDocumentBuilder;
        localObject2 = (localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder()).parse(new InputSource(new StringReader((String)localObject1)));
        localObject3 = (Element)XMLCipher.this._contextDocument.importNode(((Document)localObject2).getDocumentElement(), true);
        localDocumentFragment = XMLCipher.this._contextDocument.createDocumentFragment();
        Node localNode2;
        while ((localNode2 = ((Element)localObject3).getFirstChild()) != null)
        {
          ((Element)localObject3).removeChild(localNode2);
          localDocumentFragment.appendChild(localNode2);
        }
      }
      catch (SAXException localSAXException)
      {
        throw new XMLEncryptionException("empty", localSAXException);
      }
      catch (ParserConfigurationException localParserConfigurationException)
      {
        throw new XMLEncryptionException("empty", localParserConfigurationException);
      }
      catch (IOException localIOException)
      {
        throw new XMLEncryptionException("empty", localIOException);
      }
      return (DocumentFragment)(DocumentFragment)(DocumentFragment)localDocumentFragment;
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.XMLCipher
 * JD-Core Version:    0.6.0
 */