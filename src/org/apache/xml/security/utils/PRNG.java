package org.apache.xml.security.utils;

import java.security.SecureRandom;

public class PRNG
{
  private static PRNG _prng = null;
  private SecureRandom _sr;

  private PRNG()
  {
  }

  private PRNG(SecureRandom paramSecureRandom)
  {
    this._sr = paramSecureRandom;
  }

  public static void init(SecureRandom paramSecureRandom)
  {
    if (_prng == null)
      _prng = new PRNG(paramSecureRandom);
  }

  public static PRNG getInstance()
  {
    if (_prng == null)
      init(new SecureRandom());
    return _prng;
  }

  public SecureRandom getSecureRandom()
  {
    return this._sr;
  }

  public static byte[] createBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    getInstance().nextBytes(arrayOfByte);
    return arrayOfByte;
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    this._sr.nextBytes(paramArrayOfByte);
  }

  public double nextDouble()
  {
    return this._sr.nextDouble();
  }

  public int nextInt()
  {
    return this._sr.nextInt();
  }

  public int nextInt(int paramInt)
  {
    return this._sr.nextInt(paramInt);
  }

  public boolean nextBoolean()
  {
    return this._sr.nextBoolean();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.PRNG
 * JD-Core Version:    0.6.0
 */