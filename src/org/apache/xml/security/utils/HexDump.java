package org.apache.xml.security.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import sun.misc.HexDumpEncoder;

public class HexDump
{
  private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private static char[] BIT_DIGIT = { '0', '1' };
  private static final byte[] COMPARE_BITS = { -128, 64, 32, 16, 8, 4, 2, 1 };
  private static char BYTE_SEPARATOR = ' ';
  private static boolean WITH_BYTE_SEPARATOR = true;

  public static String prettyPrintHex(byte[] paramArrayOfByte)
  {
    HexDumpEncoder localHexDumpEncoder;
    return (localHexDumpEncoder = new HexDumpEncoder()).encodeBuffer(paramArrayOfByte);
  }

  public static String prettyPrintHex(String paramString)
  {
    return prettyPrintHex(paramString.getBytes());
  }

  public static void setWithByteSeparator(boolean paramBoolean)
  {
    WITH_BYTE_SEPARATOR = paramBoolean;
  }

  public static void setByteSeparator(char paramChar)
  {
    BYTE_SEPARATOR = paramChar;
  }

  public static void setBitDigits(char[] paramArrayOfChar)
    throws Exception
  {
    if (paramArrayOfChar.length != 2)
      throw new Exception("wrong number of characters!");
    BIT_DIGIT = paramArrayOfChar;
  }

  public static void setBitDigits(char paramChar1, char paramChar2)
  {
    BIT_DIGIT[0] = paramChar1;
    BIT_DIGIT[1] = paramChar2;
  }

  public static String byteArrayToBinaryString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      byte2bin(paramArrayOfByte[j], localStringBuffer);
      if (!(j < i - 1 & WITH_BYTE_SEPARATOR))
        continue;
      localStringBuffer.append(BYTE_SEPARATOR);
    }
    return localStringBuffer.toString();
  }

  public static String toBinaryString(byte[] paramArrayOfByte)
  {
    return byteArrayToBinaryString(paramArrayOfByte);
  }

  public static String toBinaryString(byte paramByte)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[1])[0] = paramByte;
    return byteArrayToBinaryString(arrayOfByte);
  }

  public static String toBinaryString(short paramShort)
  {
    return toBinaryString(toByteArray(paramShort));
  }

  public static String toBinaryString(int paramInt)
  {
    return toBinaryString(toByteArray(paramInt));
  }

  public static String toBinaryString(long paramLong)
  {
    return toBinaryString(toByteArray(paramLong));
  }

  public static final byte[] toByteArray(short paramShort)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[2])[1] = (byte)paramShort;
    arrayOfByte[0] = (byte)(paramShort >> 8);
    return arrayOfByte;
  }

  public static final byte[] toByteArray(int paramInt)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[4])[3] = (byte)paramInt;
    arrayOfByte[2] = (byte)(paramInt >> 8);
    arrayOfByte[1] = (byte)(paramInt >> 16);
    arrayOfByte[0] = (byte)(paramInt >> 24);
    return arrayOfByte;
  }

  public static final byte[] toByteArray(long paramLong)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[8])[7] = (byte)(int)paramLong;
    arrayOfByte[6] = (byte)(int)(paramLong >> 8);
    arrayOfByte[5] = (byte)(int)(paramLong >> 16);
    arrayOfByte[4] = (byte)(int)(paramLong >> 24);
    arrayOfByte[3] = (byte)(int)(paramLong >> 32);
    arrayOfByte[2] = (byte)(int)(paramLong >> 40);
    arrayOfByte[1] = (byte)(int)(paramLong >> 48);
    arrayOfByte[0] = (byte)(int)(paramLong >> 56);
    return arrayOfByte;
  }

  public static String byteArrayToHexString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      byte2hex(paramArrayOfByte[j], localStringBuffer);
      if (!(j < i - 1 & WITH_BYTE_SEPARATOR))
        continue;
      localStringBuffer.append(BYTE_SEPARATOR);
    }
    return localStringBuffer.toString();
  }

  public static String stringToHexString(String paramString)
  {
    byte[] arrayOfByte;
    return toHexString(arrayOfByte = paramString.getBytes());
  }

  public static String byteArrayToHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramArrayOfByte.length;
    paramInt2 += paramInt1;
    if (i < paramInt2)
      paramInt2 = i;
    for (int j = 0 + paramInt1; j < paramInt2; j++)
    {
      byte2hex(paramArrayOfByte[j], localStringBuffer);
      if (j >= paramInt2 - 1)
        continue;
      localStringBuffer.append(":");
    }
    return localStringBuffer.toString();
  }

  public static String toHexString(byte[] paramArrayOfByte)
  {
    return toHexString(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static String toHexString(byte paramByte)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[1])[0] = paramByte;
    return toHexString(arrayOfByte, 0, arrayOfByte.length);
  }

  public static String toHexString(short paramShort)
  {
    return toHexString(toByteArray(paramShort));
  }

  public static String toHexString(int paramInt)
  {
    return toHexString(toByteArray(paramInt));
  }

  public static String toHexString(long paramLong)
  {
    return toHexString(toByteArray(paramLong));
  }

  public static String toString(byte[] paramArrayOfByte)
  {
    return new String(paramArrayOfByte).toString();
  }

  public static String toString(byte paramByte)
  {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[1])[0] = paramByte;
    return new String(arrayOfByte).toString();
  }

  public static String toHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2 * (WITH_BYTE_SEPARATOR ? 3 : 2)];
    int i = paramInt1;
    int j = 0;
    do
    {
      int k = paramArrayOfByte[(i++)];
      arrayOfChar[(j++)] = HEX_DIGITS[(k >>> 4 & 0xF)];
      arrayOfChar[(j++)] = HEX_DIGITS[(k & 0xF)];
    }
    while (!WITH_BYTE_SEPARATOR);
    arrayOfChar[(j++)] = BYTE_SEPARATOR;
    return new String(arrayOfChar);
  }

  public static byte[] hexStringToByteArray(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    int j = 1;
    int k = 0;
    int m;
    switch (Character.toUpperCase(m = paramString.charAt(k)))
    {
    case '0':
      i = 0;
      tmpTernaryOp = 0;
      break;
    case '1':
      i = 16;
      tmpTernaryOp = 1;
      break;
    case '2':
      i = 32;
      tmpTernaryOp = 2;
      break;
    case '3':
      i = 48;
      tmpTernaryOp = 3;
      break;
    case '4':
      i = 64;
      tmpTernaryOp = 4;
      break;
    case '5':
      i = 80;
      tmpTernaryOp = 5;
      break;
    case '6':
      i = 96;
      tmpTernaryOp = 6;
      break;
    case '7':
      i = 112;
      tmpTernaryOp = 7;
      break;
    case '8':
      i = -128;
      tmpTernaryOp = 8;
      break;
    case '9':
      i = -112;
      tmpTernaryOp = 9;
      break;
    case 'A':
      i = -96;
      tmpTernaryOp = 10;
      break;
    case 'B':
      i = -80;
      tmpTernaryOp = 11;
      break;
    case 'C':
      i = -64;
      tmpTernaryOp = 12;
      break;
    case 'D':
      i = -48;
      tmpTernaryOp = 13;
      break;
    case 'E':
      i = -32;
      tmpTernaryOp = 14;
      break;
    case 'F':
      i = -16;
      i = (byte)(i | 0xF);
      localByteArrayOutputStream.write(i);
      j = j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : j != 0 ? 0 : 1;
    case ':':
    case ';':
    case '<':
    case '=':
    case '>':
    case '?':
    case '@':
    }
    k++;
    if (j == 0)
      throw new RuntimeException("The String did not contain an equal number of hex digits");
    return localByteArrayOutputStream.toByteArray();
  }

  private static void byte2hex(byte paramByte, StringBuffer paramStringBuffer)
  {
    char[] arrayOfChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    int i = (paramByte & 0xF0) >> 4;
    int j = paramByte & 0xF;
    paramStringBuffer.append(arrayOfChar[i]);
    paramStringBuffer.append(arrayOfChar[j]);
  }

  private static void byte2bin(byte paramByte, StringBuffer paramStringBuffer)
  {
    for (int i = 0; i < 8; i++)
      if ((paramByte & COMPARE_BITS[i]) != 0)
        paramStringBuffer.append(BIT_DIGIT[1]);
      else
        paramStringBuffer.append(BIT_DIGIT[0]);
  }

  private static String intToHexString(int paramInt)
  {
    char[] arrayOfChar = new char[8];
    for (int i = 7; i >= 0; i--)
    {
      arrayOfChar[i] = HEX_DIGITS[(paramInt & 0xF)];
      paramInt >>>= 4;
    }
    return new String(arrayOfChar);
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("-test and demo of the converter ");
    String str1;
    byte[] arrayOfByte1 = (str1 = new String("Niko")).getBytes();
    System.out.println("to convert: " + str1);
    System.out.println("converted1: " + byteArrayToHexString(arrayOfByte1));
    System.out.println("converted1: " + byteArrayToHexString(arrayOfByte1, 0, arrayOfByte1.length));
    System.out.println("converted3: " + stringToHexString(str1));
    System.out.println("----Convert integer to hexString...");
    System.out.println("to convert: " + -2 + " -> " + intToHexString(-2));
    System.out.println("----Convert byte[] to binary String...");
    byte[] arrayOfByte2 = { -1, 0, 51, 17, -1, 95, 95, 79, 31, -1 };
    System.out.println("to convert: " + toHexString(arrayOfByte2) + " -> " + byteArrayToBinaryString(arrayOfByte2));
    setByteSeparator('-');
    System.out.println("to convert: " + toHexString(arrayOfByte2) + " -> " + byteArrayToBinaryString(arrayOfByte2));
    setByteSeparator('*');
    setWithByteSeparator(true);
    System.out.println("to convert: " + toHexString(arrayOfByte2) + " -> " + byteArrayToBinaryString(arrayOfByte2));
    char[] arrayOfChar = { 'a', 'b' };
    try
    {
      setBitDigits(arrayOfChar);
    }
    catch (Exception localException2)
    {
      Exception localException1;
      (localException1 = localException2).printStackTrace();
    }
    System.out.println("to convert: " + toHexString(arrayOfByte2) + " -> " + byteArrayToBinaryString(arrayOfByte2));
    setBitDigits('0', '1');
    System.out.println("---- Convert.toByteArray(int) ");
    for (int i = -10; i < 10; i++)
    {
      System.out.println("to convert = " + i + " = " + toBinaryString(i));
      byte[] arrayOfByte3 = toByteArray(i);
      System.out.println("convertet byteArray = " + toBinaryString(arrayOfByte3));
    }
    System.out.println("---- toHexString(int) ");
    System.out.println(-1 + " = 0x" + toHexString(-1) + " = " + toBinaryString(-1));
    System.out.println(0 + " = 0x" + toHexString(0) + " = " + toBinaryString(0));
    System.out.println("---- toHexString(long) ");
    long l = 100L;
    System.out.println(l + " = 0x" + toHexString(l) + " = " + toBinaryString(l));
    Random localRandom;
    l = (localRandom = new Random()).nextLong();
    System.out.println(l + " = 0x" + toHexString(l) + " = " + toBinaryString(l));
    System.out.println("---- toHexString(short) ");
    short s = 0;
    System.out.println(100 + " = 0x" + toHexString(100) + " = " + toBinaryString(100));
    s = (short)(localRandom = new Random()).nextInt();
    System.out.println(s + " = 0x" + toHexString(s) + " = " + toBinaryString(s));
    System.out.println("---- read file in Hex-Format ");
    String str2 = "12345654321";
    System.out.println(str2 + " = " + stringToHexString(str2));
    System.out.println("Das ist die Hex-Darstellung des obigen Strings");
    System.out.println("ba = " + toHexString(arrayOfByte1));
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.HexDump
 * JD-Core Version:    0.6.0
 */