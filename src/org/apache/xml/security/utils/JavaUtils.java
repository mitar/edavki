package org.apache.xml.security.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JavaUtils
{
  static Log log = LogFactory.getLog(JavaUtils.class.getName());

  public static boolean implementsInterface(Object paramObject, String paramString)
  {
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    Class localClass;
    Class[] arrayOfClass = localClass.getInterfaces();
    String str1 = localClass.getName();
    localVector1.add(str1);
    for (int j = 0; j < arrayOfClass.length; j++)
    {
      String str2 = arrayOfClass[j].getName();
      localVector2.add(str2);
    }
    for (int i = 0; i < localVector2.size(); i++)
      if (((String)localVector2.get(i)).equals(paramString))
        return true;
    return false;
  }

  public static boolean instanceOf(Object paramObject, String paramString)
  {
    if (paramObject.getClass().getName().equals(paramString))
      return true;
    return implementsInterface(paramObject, paramString);
  }

  public static boolean binaryCompare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    return MessageDigest.isEqual(paramArrayOfByte1, paramArrayOfByte2);
  }

  public static byte[] getBytesFromFile(String paramString)
    throws FileNotFoundException, IOException
  {
    byte[] arrayOfByte1 = null;
    FileInputStream localFileInputStream = new FileInputStream(paramString);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte2 = new byte[1024];
    int i;
    while ((i = localFileInputStream.read(arrayOfByte2)) > 0)
      localByteArrayOutputStream.write(arrayOfByte2, 0, i);
    return arrayOfByte1 = localByteArrayOutputStream.toByteArray();
  }

  public static void writeBytesToFilename(String paramString, byte[] paramArrayOfByte)
  {
    try
    {
      if ((paramString != null) && (paramArrayOfByte != null))
      {
        File localFile = new File(paramString);
        FileOutputStream localFileOutputStream;
        (localFileOutputStream = new FileOutputStream(localFile)).write(paramArrayOfByte);
        localFileOutputStream.close();
      }
      else
      {
        log.debug("writeBytesToFilename got null byte[] pointed");
        return;
      }
    }
    catch (Exception localException)
    {
    }
  }

  public static byte[] getBytesFromStream(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte1 = null;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte2 = new byte[1024];
    int i;
    while ((i = paramInputStream.read(arrayOfByte2)) > 0)
      localByteArrayOutputStream.write(arrayOfByte2, 0, i);
    return arrayOfByte1 = localByteArrayOutputStream.toByteArray();
  }

  public static void runGC()
  {
    log.debug("<METHOD name=runGC()>");
    Runtime localRuntime;
    long l1 = (localRuntime = Runtime.getRuntime()).freeMemory();
    long l2 = localRuntime.totalMemory();
    long l3 = System.currentTimeMillis();
    localRuntime.gc();
    localRuntime.runFinalization();
    long l4;
    double d = ((l4 = System.currentTimeMillis()) - l3) / 1000.0D;
    long l5 = localRuntime.freeMemory();
    long l6 = localRuntime.totalMemory();
    if (log.isDebugEnabled())
    {
      log.debug("* Garbage collection took " + d + " seconds.");
      log.debug("* Memory before gc()... free:" + l1 + "= " + l1 / 1024L + "KB,...total:" + l2 + "= " + l2 / 1024L + "KB,...  used:" + (l2 - l1) + "= " + (l2 - l1) / 1024L + "KB");
      log.debug("* Memory after: gc()... free:" + l5 + "= " + l5 / 1024L + "KB,...total:" + l6 + "= " + l6 / 1024L + "KB,...  used:" + (l6 - l5) + "= " + (l6 - l5) / 1024L + "KB");
      log.debug("</METHOD>");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.JavaUtils
 * JD-Core Version:    0.6.0
 */