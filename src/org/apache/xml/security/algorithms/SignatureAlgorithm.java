package org.apache.xml.security.algorithms;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.AlgorithmAlreadyRegisteredException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SignatureAlgorithm extends Algorithm
{
  static Log log = LogFactory.getLog(SignatureAlgorithm.class.getName());
  static boolean _alreadyInitialized = false;
  static HashMap _algorithmHash = null;
  protected SignatureAlgorithmSpi _signatureAlgorithm = null;

  public SignatureAlgorithm(Document paramDocument, String paramString)
    throws XMLSecurityException
  {
    super(paramDocument, paramString);
    try
    {
      String str = getImplementingClass(paramString);
      if (log.isDebugEnabled())
        log.debug("Create URI \"" + paramString + "\" class \"" + str + "\"");
      this._signatureAlgorithm = ((SignatureAlgorithmSpi)Class.forName(str).newInstance());
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      arrayOfObject = new Object[] { paramString, localClassNotFoundException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localClassNotFoundException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      arrayOfObject = new Object[] { paramString, localIllegalAccessException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      arrayOfObject = new Object[] { paramString, localInstantiationException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localInstantiationException);
    }
    catch (NullPointerException localNullPointerException)
    {
      Object[] arrayOfObject = { paramString, localNullPointerException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localNullPointerException);
    }
  }

  public SignatureAlgorithm(Document paramDocument, String paramString, int paramInt)
    throws XMLSecurityException
  {
    this(paramDocument, paramString);
    this._signatureAlgorithm.engineSetHMACOutputLength(paramInt);
    this._signatureAlgorithm.engineAddContextToElement(this._constructionElement);
  }

  public SignatureAlgorithm(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    String str1 = getURI();
    try
    {
      String str2 = getImplementingClass(str1);
      if (log.isDebugEnabled())
        log.debug("Create URI \"" + str1 + "\" class \"" + str2 + "\"");
      this._signatureAlgorithm = ((SignatureAlgorithmSpi)Class.forName(str2).newInstance());
      this._signatureAlgorithm.engineGetContextFromElement(this._constructionElement);
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      arrayOfObject = new Object[] { str1, localClassNotFoundException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localClassNotFoundException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      arrayOfObject = new Object[] { str1, localIllegalAccessException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      arrayOfObject = new Object[] { str1, localInstantiationException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localInstantiationException);
    }
    catch (NullPointerException localNullPointerException)
    {
      Object[] arrayOfObject = { str1, localNullPointerException.getMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject, localNullPointerException);
    }
  }

  public byte[] sign()
    throws XMLSignatureException
  {
    return this._signatureAlgorithm.engineSign();
  }

  public String getJCEAlgorithmString()
  {
    return this._signatureAlgorithm.engineGetJCEAlgorithmString();
  }

  public String getJCEProviderName()
  {
    return this._signatureAlgorithm.engineGetJCEProviderName();
  }

  public void update(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineUpdate(paramArrayOfByte);
  }

  public void update(byte paramByte)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineUpdate(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void initSign(Key paramKey)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineInitSign(paramKey);
  }

  public void initSign(Key paramKey, SecureRandom paramSecureRandom)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineInitSign(paramKey, paramSecureRandom);
  }

  public void initSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineInitSign(paramKey, paramAlgorithmParameterSpec);
  }

  public void setParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineSetParameter(paramAlgorithmParameterSpec);
  }

  public void initVerify(Key paramKey)
    throws XMLSignatureException
  {
    this._signatureAlgorithm.engineInitVerify(paramKey);
  }

  public boolean verify(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    return this._signatureAlgorithm.engineVerify(paramArrayOfByte);
  }

  public final String getURI()
  {
    return this._constructionElement.getAttributeNS(null, "Algorithm");
  }

  public static void providerInit()
  {
    if (log == null)
      log = LogFactory.getLog(SignatureAlgorithm.class.getName());
    log.debug("Init() called");
    if (!_alreadyInitialized)
    {
      _algorithmHash = new HashMap(10);
      _alreadyInitialized = true;
    }
  }

  public static void register(String paramString1, String paramString2)
    throws AlgorithmAlreadyRegisteredException
  {
    if (log.isDebugEnabled())
      log.debug("Try to register " + paramString1 + " " + paramString2);
    String str;
    if (((str = getImplementingClass(paramString1)) != null) && (str.length() != 0))
    {
      Object[] arrayOfObject = { paramString1, str };
      throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
    }
    _algorithmHash.put(paramString1, paramString2);
  }

  private static String getImplementingClass(String paramString)
  {
    if (_algorithmHash == null)
      return null;
    return (String)_algorithmHash.get(paramString);
  }

  public String getBaseNamespace()
  {
    return "http://www.w3.org/2000/09/xmldsig#";
  }

  public String getBaseLocalName()
  {
    return "SignatureMethod";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.SignatureAlgorithm
 * JD-Core Version:    0.6.0
 */