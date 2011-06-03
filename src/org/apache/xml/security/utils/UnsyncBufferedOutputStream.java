package org.apache.xml.security.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class UnsyncBufferedOutputStream extends OutputStream
{
  final OutputStream out;
  private final boolean fPrintSignedTrace = false;
  private BufferedOutputStream buff = null;
  private Random rnd = new Random();
  final byte[] buf = (byte[])bufCahce.get();
  static final int size = 8192;
  private static ThreadLocal bufCahce = new ThreadLocal()
  {
    protected synchronized Object initialValue()
    {
      return new byte[8192];
    }
  };
  int pointer = 0;

  public UnsyncBufferedOutputStream(OutputStream paramOutputStream)
  {
    this.out = paramOutputStream;
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void writeTrimmed(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    this.out.write(arrayOfByte, 0, paramInt2);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if ((i = this.pointer + paramInt2) > 8192)
    {
      flushBuffer();
      if (paramInt2 > 8192)
      {
        writeTrimmed(paramArrayOfByte, paramInt1, paramInt2);
        return;
      }
      i = paramInt2;
    }
    System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pointer, paramInt2);
    this.pointer = i;
  }

  private final void flushBuffer()
    throws IOException
  {
    if (this.pointer > 0)
      writeTrimmed(this.buf, 0, this.pointer);
    this.pointer = 0;
  }

  public void write(int paramInt)
    throws IOException
  {
    if (this.pointer >= 8192)
      flushBuffer();
    this.buf[(this.pointer++)] = (byte)paramInt;
  }

  public void flush()
    throws IOException
  {
    flushBuffer();
    this.out.flush();
  }

  public void close()
    throws IOException
  {
    flush();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.UnsyncBufferedOutputStream
 * JD-Core Version:    0.6.0
 */