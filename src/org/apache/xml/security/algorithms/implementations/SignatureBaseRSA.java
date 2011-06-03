package org.apache.xml.security.algorithms.implementations;

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

public abstract class SignatureBaseRSA extends SignatureAlgorithmSpi
{
  static Log log = LogFactory.getLog(SignatureBaseRSA.class.getName());
  private Signature _signatureAlgorithm = null;

  public abstract String engineGetURI();

  public SignatureBaseRSA()
    throws XMLSignatureException
  {
    String str1 = JCEMapper.translateURItoJCEID(engineGetURI());
    if (log.isDebugEnabled())
      log.debug("Created SignatureDSA using " + str1);
    String str2 = JCEMapper.getProviderId();
    try
    {
      if (str2 == null)
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
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", arrayOfObject);
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
      return this._signatureAlgorithm.verify(paramArrayOfByte);
    }
    catch (SignatureException localSignatureException)
    {
    }
    throw new XMLSignatureException("empty", localSignatureException);
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
      return this._signatureAlgorithm.sign();
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

  protected void engineSetHMACOutputLength(int paramInt)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
  }

  protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException
  {
    throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnRSA");
  }

  public static class SignatureRSAMD5 extends SignatureBaseRSA
  {
    public SignatureRSAMD5()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
    }
  }

  public static class SignatureRSARIPEMD160 extends SignatureBaseRSA
  {
    public SignatureRSARIPEMD160()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
    }
  }

  public static class SignatureRSASHA512 extends SignatureBaseRSA
  {
    public SignatureRSASHA512()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
    }
  }

  public static class SignatureRSASHA384 extends SignatureBaseRSA
  {
    public SignatureRSASHA384()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
    }
  }

  public static class SignatureRSASHA256 extends SignatureBaseRSA
  {
    public SignatureRSASHA256()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
    }
  }

  public static class SignatureRSASHA1 extends SignatureBaseRSA
  {
    public SignatureRSASHA1()
      throws XMLSignatureException
    {
    }

    public String engineGetURI()
    {
      return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.implementations.SignatureBaseRSA
 * JD-Core Version:    0.6.0
 */