package org.apache.xml.security.utils;

import java.io.ByteArrayOutputStream;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;

public class DigesterOutputStream extends ByteArrayOutputStream
{
  static final byte[] none = "error".getBytes();
  final MessageDigestAlgorithm mda;

  public DigesterOutputStream(MessageDigestAlgorithm paramMessageDigestAlgorithm)
  {
    this.mda = paramMessageDigestAlgorithm;
  }

  public byte[] toByteArray()
  {
    return none;
  }

  public void write(byte[] paramArrayOfByte)
  {
    this.mda.update(paramArrayOfByte);
  }

  public void write(int paramInt)
  {
    this.mda.update((byte)paramInt);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.mda.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public byte[] getDigestValue()
  {
    return this.mda.digest();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.DigesterOutputStream
 * JD-Core Version:    0.6.0
 */