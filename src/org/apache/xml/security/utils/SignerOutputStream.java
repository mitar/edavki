package org.apache.xml.security.utils;

import java.io.ByteArrayOutputStream;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.signature.XMLSignatureException;

public class SignerOutputStream extends ByteArrayOutputStream
{
  static final byte[] none = "error".getBytes();
  final SignatureAlgorithm sa;

  public SignerOutputStream(SignatureAlgorithm paramSignatureAlgorithm)
  {
    this.sa = paramSignatureAlgorithm;
  }

  public byte[] toByteArray()
  {
    return none;
  }

  public void write(byte[] paramArrayOfByte)
  {
    try
    {
      this.sa.update(paramArrayOfByte);
      return;
    }
    catch (XMLSignatureException localXMLSignatureException)
    {
      throw new RuntimeException("" + localXMLSignatureException);
    }
  }

  public void write(int paramInt)
  {
    try
    {
      this.sa.update((byte)paramInt);
      return;
    }
    catch (XMLSignatureException localXMLSignatureException)
    {
      throw new RuntimeException("" + localXMLSignatureException);
    }
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      this.sa.update(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (XMLSignatureException localXMLSignatureException)
    {
      throw new RuntimeException("" + localXMLSignatureException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.SignerOutputStream
 * JD-Core Version:    0.6.0
 */