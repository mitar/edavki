package org.apache.xml.security.algorithms.implementations;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.algorithms.SignatureAlgorithmSpi;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.HexDump;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public abstract class IntegrityHmac extends SignatureAlgorithmSpi
{
  static Log log = LogFactory.getLog(IntegrityHmacSHA1.class.getName());
  private Mac _macAlgorithm = null;
  int _HMACOutputLength = 0;

  public abstract String engineGetURI();

  public IntegrityHmac()
    throws XMLSignatureException
  {
    String str1 = JCEMapper.translateURItoJCEID(engineGetURI());
    if (log.isDebugEnabled())
      log.debug("Created IntegrityHmacSHA1 using " + str1);
    try
    {
      String str2;
      if ((str2 = JCEMapper.getProviderId()) == null)
      {
        this._macAlgorithm = Mac.getInstance(str1);
      }
      else
      {
        this._macAlgorithm = Mac.getInstance(str1, str2);
        return;
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      arrayOfObject = new Object[] { str1, localNoSuchAlgorithmException.getLocalizedMessage() };
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      Object[] arrayOfObject = { str1, localNoSuchProviderException.getLocalizedMessage() };
      throw new XMLSignatureException("algorithms.NoSuchProviderException", arrayOfObject);
    }
  }

  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("empty");
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    try
    {
      byte[] arrayOfByte1 = this._macAlgorithm.doFinal();
      if (log.isDebugEnabled())
      {
        log.debug("completeResult = " + HexDump.byteArrayToHexString(arrayOfByte1));
        log.debug("signature      = " + HexDump.byteArrayToHexString(paramArrayOfByte));
      }
      if ((this._HMACOutputLength == 0) || (this._HMACOutputLength >= 160))
        return MessageDigestAlgorithm.isEqual(arrayOfByte1, paramArrayOfByte);
      byte[] arrayOfByte2 = reduceBitLength(arrayOfByte1, this._HMACOutputLength);
      if (log.isDebugEnabled())
        log.debug("stripped       = " + HexDump.byteArrayToHexString(arrayOfByte2));
      return MessageDigestAlgorithm.isEqual(arrayOfByte2, paramArrayOfByte);
    }
    catch (IllegalStateException localIllegalStateException)
    {
    }
    throw new XMLSignatureException("empty", localIllegalStateException);
  }

  protected void engineInitVerify(Key paramKey)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof SecretKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = SecretKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._macAlgorithm.init(paramKey);
      return;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLSignatureException("empty", localInvalidKeyException);
    }
  }

  protected byte[] engineSign()
    throws XMLSignatureException
  {
    try
    {
      byte[] arrayOfByte = this._macAlgorithm.doFinal();
      if ((this._HMACOutputLength == 0) || (this._HMACOutputLength >= 160))
        return arrayOfByte;
      return reduceBitLength(arrayOfByte, this._HMACOutputLength);
    }
    catch (IllegalStateException localIllegalStateException)
    {
    }
    throw new XMLSignatureException("empty", localIllegalStateException);
  }

  private static byte[] reduceBitLength(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt / 8;
    int j = paramInt % 8;
    byte[] arrayOfByte1 = new byte[i + (j == 0 ? 0 : 1)];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, i);
    if (j > 0)
    {
      byte[] arrayOfByte2 = { 0, -128, -64, -32, -16, -8, -4, -2 };
      arrayOfByte1[i] = (byte)(paramArrayOfByte[i] & arrayOfByte2[j]);
    }
    return arrayOfByte1;
  }

  protected void engineInitSign(Key paramKey)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof SecretKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = SecretKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._macAlgorithm.init(paramKey);
      return;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLSignatureException("empty", localInvalidKeyException);
    }
  }

  protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof SecretKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = SecretKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._macAlgorithm.init(paramKey, paramAlgorithmParameterSpec);
      return;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLSignatureException("empty", localInvalidKeyException);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
    }
  }

  protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("algorithms.CannotUseSecureRandomOnMAC");
  }

  protected void engineUpdate(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    try
    {
      this._macAlgorithm.update(paramArrayOfByte);
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new XMLSignatureException("empty", localIllegalStateException);
    }
  }

  protected void engineUpdate(byte paramByte)
    throws XMLSignatureException
  {
    try
    {
      this._macAlgorithm.update(paramByte);
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new XMLSignatureException("empty", localIllegalStateException);
    }
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws XMLSignatureException
  {
    try
    {
      this._macAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new XMLSignatureException("empty", localIllegalStateException);
    }
  }

  protected String engineGetJCEAlgorithmString()
  {
    log.debug("engineGetJCEAlgorithmString()");
    return this._macAlgorithm.getAlgorithm();
  }

  protected String engineGetJCEProviderName()
  {
    return this._macAlgorithm.getProvider().getName();
  }

  protected void engineSetHMACOutputLength(int paramInt)
  {
    this._HMACOutputLength = paramInt;
  }

  protected void engineGetContextFromElement(Element paramElement)
  {
    super.engineGetContextFromElement(paramElement);
    if (paramElement == null)
      throw new IllegalArgumentException("element null");
    Text localText;
    if ((localText = XMLUtils.selectDsNodeText(paramElement.getFirstChild(), "HMACOutputLength", 0)) != null)
      this._HMACOutputLength = Integer.parseInt(localText.getData());
  }

  protected void engineAddContextToElement(Element paramElement)
  {
    if (paramElement == null)
      throw new IllegalArgumentException("null element");
    if (this._HMACOutputLength != 0)
    {
      Document localDocument;
      Element localElement = XMLUtils.createElementInSignatureSpace(localDocument = paramElement.getOwnerDocument(), "HMACOutputLength");
      Text localText = localDocument.createTextNode(new Integer(this._HMACOutputLength).toString());
      localElement.appendChild(localText);
      XMLUtils.addReturnToElement(paramElement);
      paramElement.appendChild(localElement);
      XMLUtils.addReturnToElement(paramElement);
    }
  }

  public static class IntegrityHmacMD5 extends IntegrityHmac
  {
    public IntegrityHmacMD5()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
    }
  }

  public static class IntegrityHmacRIPEMD160 extends IntegrityHmac
  {
    public IntegrityHmacRIPEMD160()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
    }
  }

  public static class IntegrityHmacSHA512 extends IntegrityHmac
  {
    public IntegrityHmacSHA512()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
    }
  }

  public static class IntegrityHmacSHA384 extends IntegrityHmac
  {
    public IntegrityHmacSHA384()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
    }
  }

  public static class IntegrityHmacSHA256 extends IntegrityHmac
  {
    public IntegrityHmacSHA256()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
    }
  }

  public static class IntegrityHmacSHA1 extends IntegrityHmac
  {
    public IntegrityHmacSHA1()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.implementations.IntegrityHmac
 * JD-Core Version:    0.6.0
 */