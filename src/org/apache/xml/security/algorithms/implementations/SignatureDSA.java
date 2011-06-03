package org.apache.xml.security.algorithms.implementations;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.SignatureAlgorithmSpi;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.Base64;

public class SignatureDSA extends SignatureAlgorithmSpi
{
  static Log log = LogFactory.getLog(SignatureDSA.class.getName());
  public static final String _URI = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
  private Signature _signatureAlgorithm = null;

  protected String engineGetURI()
  {
    return "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
  }

  public SignatureDSA()
    throws XMLSignatureException
  {
    String str1 = JCEMapper.translateURItoJCEID("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
    if (log.isDebugEnabled())
      log.debug("Created SignatureDSA using " + str1);
    try
    {
      String str2;
      if ((str2 = JCEMapper.getProviderId()) == null)
      {
        this._signatureAlgorithm = Signature.getInstance(str1);
      }
      else
      {
        this._signatureAlgorithm = Signature.getInstance(str1, str2);
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
    try
    {
      this._signatureAlgorithm.setParameter(paramAlgorithmParameterSpec);
      return;
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new XMLSignatureException("empty", localInvalidAlgorithmParameterException);
    }
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    try
    {
      if (log.isDebugEnabled())
        log.debug("Called DSA.verify() on " + Base64.encode(paramArrayOfByte));
      byte[] arrayOfByte = convertXMLDSIGtoASN1(paramArrayOfByte);
      return this._signatureAlgorithm.verify(arrayOfByte);
    }
    catch (SignatureException localSignatureException)
    {
      throw new XMLSignatureException("empty", localSignatureException);
    }
    catch (IOException localIOException)
    {
    }
    throw new XMLSignatureException("empty", localIOException);
  }

  protected void engineInitVerify(Key paramKey)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof PublicKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = PublicKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._signatureAlgorithm.initVerify((PublicKey)paramKey);
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
      byte[] arrayOfByte;
      return convertASN1toXMLDSIG(arrayOfByte = this._signatureAlgorithm.sign());
    }
    catch (IOException localIOException)
    {
      throw new XMLSignatureException("empty", localIOException);
    }
    catch (SignatureException localSignatureException)
    {
    }
    throw new XMLSignatureException("empty", localSignatureException);
  }

  protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof PrivateKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = PrivateKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._signatureAlgorithm.initSign((PrivateKey)paramKey, paramSecureRandom);
      return;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLSignatureException("empty", localInvalidKeyException);
    }
  }

  protected void engineInitSign(Key paramKey)
    throws XMLSignatureException
  {
    if (!(paramKey instanceof PrivateKey))
    {
      String str1 = paramKey.getClass().getName();
      String str2 = PrivateKey.class.getName();
      Object[] arrayOfObject = { str1, str2 };
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", arrayOfObject);
    }
    try
    {
      this._signatureAlgorithm.initSign((PrivateKey)paramKey);
      return;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new XMLSignatureException("empty", localInvalidKeyException);
    }
  }

  protected void engineUpdate(byte[] paramArrayOfByte)
    throws XMLSignatureException
  {
    try
    {
      this._signatureAlgorithm.update(paramArrayOfByte);
      return;
    }
    catch (SignatureException localSignatureException)
    {
      throw new XMLSignatureException("empty", localSignatureException);
    }
  }

  protected void engineUpdate(byte paramByte)
    throws XMLSignatureException
  {
    try
    {
      this._signatureAlgorithm.update(paramByte);
      return;
    }
    catch (SignatureException localSignatureException)
    {
      throw new XMLSignatureException("empty", localSignatureException);
    }
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws XMLSignatureException
  {
    try
    {
      this._signatureAlgorithm.update(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (SignatureException localSignatureException)
    {
      throw new XMLSignatureException("empty", localSignatureException);
    }
  }

  protected String engineGetJCEAlgorithmString()
  {
    return this._signatureAlgorithm.getAlgorithm();
  }

  protected String engineGetJCEProviderName()
  {
    return this._signatureAlgorithm.getProvider().getName();
  }

  private static byte[] convertASN1toXMLDSIG(byte[] paramArrayOfByte)
    throws IOException
  {
    int i;
    for (int j = i = paramArrayOfByte[3]; (j > 0) && (paramArrayOfByte[(4 + i - j)] == 0); j--);
    int k;
    for (int m = k = paramArrayOfByte[(5 + i)]; (m > 0) && (paramArrayOfByte[(6 + i + k - m)] == 0); m--);
    if ((paramArrayOfByte[0] != 48) || (paramArrayOfByte[1] != paramArrayOfByte.length - 2) || (paramArrayOfByte[2] != 2) || (j > 20) || (paramArrayOfByte[(4 + i)] != 2) || (m > 20))
      throw new IOException("Invalid ASN.1 format of DSA signature");
    byte[] arrayOfByte = new byte[40];
    System.arraycopy(paramArrayOfByte, 4 + i - j, arrayOfByte, 20 - j, j);
    System.arraycopy(paramArrayOfByte, 6 + i + k - m, arrayOfByte, 40 - m, m);
    return arrayOfByte;
  }

  private static byte[] convertXMLDSIGtoASN1(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte.length != 40)
      throw new IOException("Invalid XMLDSIG format of DSA signature");
    for (int i = 20; (i > 0) && (paramArrayOfByte[(20 - i)] == 0); i--);
    int j = i;
    if (paramArrayOfByte[(20 - i)] < 0)
      j++;
    for (int k = 20; (k > 0) && (paramArrayOfByte[(40 - k)] == 0); k--);
    int m = k;
    if (paramArrayOfByte[(40 - k)] < 0)
      m++;
    byte[] arrayOfByte;
    (arrayOfByte = new byte[6 + j + m])[0] = 48;
    arrayOfByte[1] = (byte)(4 + j + m);
    arrayOfByte[2] = 2;
    arrayOfByte[3] = (byte)j;
    System.arraycopy(paramArrayOfByte, 20 - i, arrayOfByte, 4 + j - i, i);
    arrayOfByte[(4 + j)] = 2;
    arrayOfByte[(5 + j)] = (byte)m;
    System.arraycopy(paramArrayOfByte, 40 - k, arrayOfByte, 6 + j + m - k, k);
    return arrayOfByte;
  }

  protected void engineSetHMACOutputLength(int paramInt)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
  }

  protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnDSA");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.implementations.SignatureDSA
 * JD-Core Version:    0.6.0
 */