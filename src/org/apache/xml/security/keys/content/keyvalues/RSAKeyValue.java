package org.apache.xml.security.keys.content.keyvalues;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RSAKeyValue extends SignatureElementProxy
  implements KeyValueContent
{
  static Log log = LogFactory.getLog(RSAKeyValue.class.getName());

  public RSAKeyValue(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public RSAKeyValue(Document paramDocument, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    addBigIntegerElement(paramBigInteger1, "Modulus");
    addBigIntegerElement(paramBigInteger2, "Exponent");
  }

  public RSAKeyValue(Document paramDocument, Key paramKey)
    throws IllegalArgumentException
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    if (JavaUtils.implementsInterface(paramKey, "java.security.interfaces.RSAPublicKey"))
    {
      addBigIntegerElement(((RSAPublicKey)paramKey).getModulus(), "Modulus");
      addBigIntegerElement(((RSAPublicKey)paramKey).getPublicExponent(), "Exponent");
      return;
    }
    Object[] arrayOfObject = { "RSAKeyValue", paramKey.getClass().getName() };
    throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", arrayOfObject));
  }

  public PublicKey getPublicKey()
    throws XMLSecurityException
  {
    try
    {
      String str;
      KeyFactory localKeyFactory = (str = JCEMapper.getProviderId()) == null ? KeyFactory.getInstance("RSA") : KeyFactory.getInstance("RSA", str);
      RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(getBigIntegerFromChildElement("Modulus", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("Exponent", "http://www.w3.org/2000/09/xmldsig#"));
      PublicKey localPublicKey;
      return localPublicKey = localKeyFactory.generatePublic(localRSAPublicKeySpec);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new XMLSecurityException("empty", localNoSuchAlgorithmException);
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new XMLSecurityException("empty", localNoSuchProviderException);
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
    }
    throw new XMLSecurityException("empty", localInvalidKeySpecException);
  }

  public String getBaseLocalName()
  {
    return "RSAKeyValue";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.keyvalues.RSAKeyValue
 * JD-Core Version:    0.6.0
 */