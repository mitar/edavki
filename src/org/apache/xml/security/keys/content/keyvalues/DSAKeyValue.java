package org.apache.xml.security.keys.content.keyvalues;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DSAKeyValue extends SignatureElementProxy
  implements KeyValueContent
{
  static Log log = LogFactory.getLog(DSAKeyValue.class.getName());

  public DSAKeyValue(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public DSAKeyValue(Document paramDocument, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    addBigIntegerElement(paramBigInteger1, "P");
    addBigIntegerElement(paramBigInteger2, "Q");
    addBigIntegerElement(paramBigInteger3, "G");
    addBigIntegerElement(paramBigInteger4, "Y");
  }

  public DSAKeyValue(Document paramDocument, Key paramKey)
    throws IllegalArgumentException
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
    if (JavaUtils.implementsInterface(paramKey, "java.security.interfaces.DSAPublicKey"))
    {
      addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getP(), "P");
      addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getQ(), "Q");
      addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getG(), "G");
      addBigIntegerElement(((DSAPublicKey)paramKey).getY(), "Y");
      return;
    }
    Object[] arrayOfObject = { "DSAKeyValue", paramKey.getClass().getName() };
    throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", arrayOfObject));
  }

  public PublicKey getPublicKey()
    throws XMLSecurityException
  {
    try
    {
      DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(getBigIntegerFromChildElement("Y", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("P", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("Q", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("G", "http://www.w3.org/2000/09/xmldsig#"));
      KeyFactory localKeyFactory;
      PublicKey localPublicKey;
      return localPublicKey = (localKeyFactory = KeyFactory.getInstance("DSA")).generatePublic(localDSAPublicKeySpec);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new XMLSecurityException("empty", localNoSuchAlgorithmException);
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
    }
    throw new XMLSecurityException("empty", localInvalidKeySpecException);
  }

  public String getBaseLocalName()
  {
    return "DSAKeyValue";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.keyvalues.DSAKeyValue
 * JD-Core Version:    0.6.0
 */