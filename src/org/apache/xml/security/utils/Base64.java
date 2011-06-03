package org.apache.xml.security.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.math.BigInteger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class Base64
{
  static Log log = LogFactory.getLog(Base64.class.getName());
  public static final int BASE64DEFAULTLENGTH = 76;
  static int _base64length = 76;
  private static final int BASELENGTH = 255;
  private static final int LOOKUPLENGTH = 64;
  private static final int TWENTYFOURBITGROUP = 24;
  private static final int EIGHTBIT = 8;
  private static final int SIXTEENBIT = 16;
  private static final int FOURBYTE = 4;
  private static final int SIGN = -128;
  private static final char PAD = '=';
  private static final boolean fDebug = false;
  private static final byte[] base64Alphabet = new byte['ÿ'];
  private static final char[] lookUpBase64Alphabet = new char[64];

  static byte[] getBytes(BigInteger paramBigInteger, int paramInt)
  {
    if ((paramInt = paramInt + 7 >> 3 << 3) < paramBigInteger.bitLength())
      throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if ((paramBigInteger.bitLength() % 8 != 0) && (paramBigInteger.bitLength() / 8 + 1 == paramInt / 8))
      return arrayOfByte1;
    int i = 0;
    int j = arrayOfByte1.length;
    if (paramBigInteger.bitLength() % 8 == 0)
    {
      i = 1;
      j--;
    }
    int k = paramInt / 8 - j;
    byte[] arrayOfByte2 = new byte[paramInt / 8];
    System.arraycopy(arrayOfByte1, i, arrayOfByte2, k, j);
    return arrayOfByte2;
  }

  public static String encode(BigInteger paramBigInteger)
  {
    return encode(getBytes(paramBigInteger, paramBigInteger.bitLength()));
  }

  public static byte[] encode(BigInteger paramBigInteger, int paramInt)
  {
    if ((paramInt = paramInt + 7 >> 3 << 3) < paramBigInteger.bitLength())
      throw new IllegalArgumentException(I18n.translate("utils.Base64.IllegalBitlength"));
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if ((paramBigInteger.bitLength() % 8 != 0) && (paramBigInteger.bitLength() / 8 + 1 == paramInt / 8))
      return arrayOfByte1;
    int i = 0;
    int j = arrayOfByte1.length;
    if (paramBigInteger.bitLength() % 8 == 0)
    {
      i = 1;
      j--;
    }
    int k = paramInt / 8 - j;
    byte[] arrayOfByte2 = new byte[paramInt / 8];
    System.arraycopy(arrayOfByte1, i, arrayOfByte2, k, j);
    return arrayOfByte2;
  }

  public static BigInteger decodeBigIntegerFromElement(Element paramElement)
    throws Base64DecodingException
  {
    return new BigInteger(1, decode(paramElement));
  }

  public static BigInteger decodeBigIntegerFromText(Text paramText)
    throws Base64DecodingException
  {
    return new BigInteger(1, decode(paramText.getData()));
  }

  public static void fillElementWithBigInteger(Element paramElement, BigInteger paramBigInteger)
  {
    String str;
    if ((str = encode(paramBigInteger)).length() > 76)
      str = "\n" + str + "\n";
    Document localDocument;
    Text localText = (localDocument = paramElement.getOwnerDocument()).createTextNode(str);
    paramElement.appendChild(localText);
  }

  public static byte[] decode(Element paramElement)
    throws Base64DecodingException
  {
    Node localNode = paramElement.getFirstChild();
    StringBuffer localStringBuffer = new StringBuffer();
    while (localNode != null)
    {
      if (localNode.getNodeType() == 3)
      {
        Text localText = (Text)localNode;
        localStringBuffer.append(localText.getData());
      }
      localNode = localNode.getNextSibling();
    }
    return decode(localStringBuffer.toString());
  }

  public static Element encodeToElement(Document paramDocument, String paramString, byte[] paramArrayOfByte)
  {
    Element localElement = XMLUtils.createElementInSignatureSpace(paramDocument, paramString);
    Text localText = paramDocument.createTextNode(encode(paramArrayOfByte));
    localElement.appendChild(localText);
    return localElement;
  }

  public static byte[] decode(byte[] paramArrayOfByte)
    throws Base64DecodingException
  {
    return decodeInternal(paramArrayOfByte);
  }

  public static String encode(byte[] paramArrayOfByte)
  {
    return encode(paramArrayOfByte, 76);
  }

  public static byte[] decode(BufferedReader paramBufferedReader)
    throws IOException, Base64DecodingException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    String str;
    while (null != (str = paramBufferedReader.readLine()))
    {
      byte[] arrayOfByte = decode(str);
      localByteArrayOutputStream.write(arrayOfByte);
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    DocumentBuilderFactory localDocumentBuilderFactory;
    DocumentBuilder localDocumentBuilder = (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).newDocumentBuilder();
    String str = "<container><base64 value=\"Should be 'Hallo'\">SGFsbG8=</base64></container>";
    InputSource localInputSource = new InputSource(new StringReader(str));
    Document localDocument;
    Element localElement = (Element)(localDocument = localDocumentBuilder.parse(localInputSource)).getDocumentElement().getChildNodes().item(0);
    System.out.println(new String(decode(localElement)));
  }

  protected static final boolean isWhiteSpace(byte paramByte)
  {
    return (paramByte == 32) || (paramByte == 13) || (paramByte == 10) || (paramByte == 9);
  }

  protected static final boolean isPad(byte paramByte)
  {
    return paramByte == 61;
  }

  protected static final boolean isData(byte paramByte)
  {
    return base64Alphabet[paramByte] != -1;
  }

  public static String encode(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt < 4)
      paramInt = 2147483647;
    if (paramArrayOfByte == null)
      return null;
    int i;
    if ((i = paramArrayOfByte.length * 8) == 0)
      return "";
    int j = i % 24;
    int k = i / 24;
    int m = j != 0 ? k + 1 : k;
    int n = paramInt / 4;
    int i1 = (m - 1) / n;
    char[] arrayOfChar = null;
    arrayOfChar = new char[m * 4 + i1];
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    int i9 = 0;
    int i11;
    int i12;
    for (int i10 = 0; i10 < i1; i10++)
    {
      for (i11 = 0; i11 < 19; i11++)
      {
        i4 = paramArrayOfByte[(i8++)];
        i5 = paramArrayOfByte[(i8++)];
        i6 = paramArrayOfByte[(i8++)];
        i3 = (byte)(i5 & 0xF);
        i2 = (byte)(i4 & 0x3);
        i12 = (byte)((i4 & 0xFFFFFF80) == 0 ? i4 >> 2 : i4 >> 2 ^ 0xC0);
        int i13 = (byte)((i5 & 0xFFFFFF80) == 0 ? i5 >> 4 : i5 >> 4 ^ 0xF0);
        int i14 = (byte)((i6 & 0xFFFFFF80) == 0 ? i6 >> 6 : i6 >> 6 ^ 0xFC);
        arrayOfChar[(i7++)] = lookUpBase64Alphabet[i12];
        arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i13 | i2 << 4)];
        arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i14)];
        arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
        i9++;
      }
      arrayOfChar[(i7++)] = '\n';
    }
    while (i9 < k)
    {
      i4 = paramArrayOfByte[(i8++)];
      i5 = paramArrayOfByte[(i8++)];
      i6 = paramArrayOfByte[(i8++)];
      i3 = (byte)(i5 & 0xF);
      i2 = (byte)(i4 & 0x3);
      i10 = (byte)((i4 & 0xFFFFFF80) == 0 ? i4 >> 2 : i4 >> 2 ^ 0xC0);
      i11 = (byte)((i5 & 0xFFFFFF80) == 0 ? i5 >> 4 : i5 >> 4 ^ 0xF0);
      i12 = (byte)((i6 & 0xFFFFFF80) == 0 ? i6 >> 6 : i6 >> 6 ^ 0xFC);
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i11 | i2 << 4)];
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i3 << 2 | i12)];
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i6 & 0x3F)];
      i9++;
    }
    i2 = (byte)((i4 = paramArrayOfByte[i8]) & 0x3);
    i10 = (byte)((i4 & 0xFFFFFF80) == 0 ? i4 >> 2 : i4 >> 2 ^ 0xC0);
    arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
    arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i2 << 4)];
    if (j == 16)
    {
      i4 = paramArrayOfByte[i8];
      i3 = (byte)((i5 = paramArrayOfByte[(i8 + 1)]) & 0xF);
      i2 = (byte)(i4 & 0x3);
      i10 = (byte)((i4 & 0xFFFFFF80) == 0 ? i4 >> 2 : i4 >> 2 ^ 0xC0);
      i11 = (byte)((i5 & 0xFFFFFF80) == 0 ? i5 >> 4 : i5 >> 4 ^ 0xF0);
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[i10];
      arrayOfChar[(i7++)] = lookUpBase64Alphabet[(i11 | i2 << 4)];
      arrayOfChar[(i7++)] = (j == 8 ? 61 : lookUpBase64Alphabet[(i3 << 2)]);
      arrayOfChar[i7] = '=';
    }
    return new String(arrayOfChar);
  }

  public static final byte[] decode(String paramString)
    throws Base64DecodingException
  {
    if (paramString == null)
      return null;
    return decodeInternal(paramString.getBytes());
  }

  protected static final byte[] decodeInternal(byte[] paramArrayOfByte)
    throws Base64DecodingException
  {
    int i;
    if ((i = removeWhiteSpace(paramArrayOfByte)) % 4 != 0)
      throw new Base64DecodingException("It should be dived by four");
    int j;
    if ((j = i / 4) == 0)
      return new byte[0];
    byte[] arrayOfByte = null;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    byte b1 = 0;
    byte b2 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    i6 = (j - 1) * 4;
    i5 = (j - 1) * 3;
    if ((!isData(i2 = paramArrayOfByte[(i6++)])) || (!isData(i3 = paramArrayOfByte[(i6++)])))
      throw new Base64DecodingException("decoding.general");
    k = base64Alphabet[i2];
    m = base64Alphabet[i3];
    b1 = paramArrayOfByte[(i6++)];
    b2 = paramArrayOfByte[i6];
    if ((!isData(b1)) || (!isData(b2)))
    {
      if ((m & 0xF) != 0)
        throw new Base64DecodingException("decoding.general");
      arrayOfByte = new byte[i5 + 1];
      if (((n = base64Alphabet[b1]) & 0x3) != 0)
        throw new Base64DecodingException("decoding.general");
      (arrayOfByte = new byte[i5 + 2])[(i5++)] = (byte)(k << 2 | m >> 4);
      throw new Base64DecodingException("decoding.general");
    }
    arrayOfByte = new byte[i5 + 3];
    n = base64Alphabet[b1];
    i1 = base64Alphabet[b2];
    arrayOfByte[(i5++)] = (byte)(k << 2 | m >> 4);
    arrayOfByte[(i5++)] = (byte)((m & 0xF) << 4 | n >> 2 & 0xF);
    arrayOfByte[i5] = (byte)(n << 6 | ((!isPad(b1)) && (isPad(b2)) ? n >> 2 & 0xF : (isPad(b1)) && (isPad(b2)) ? m >> 4 : i1));
    i5 = 0;
    i6 = 0;
    if ((!isData(i2 = paramArrayOfByte[(i6++)])) || (!isData(i3 = paramArrayOfByte[(i6++)])) || (!isData(b1 = paramArrayOfByte[(i6++)])) || (!isData(b2 = paramArrayOfByte[(i6++)])))
      throw new Base64DecodingException("decoding.general");
    k = base64Alphabet[i2];
    m = base64Alphabet[i3];
    n = base64Alphabet[b1];
    i1 = base64Alphabet[b2];
    arrayOfByte[(i5++)] = (byte)(k << 2 | m >> 4);
    arrayOfByte[(i5++)] = (byte)((m & 0xF) << 4 | n >> 2 & 0xF);
    arrayOfByte[(i5++)] = (byte)(n << 6 | i1);
    i4++;
    return arrayOfByte;
  }

  public static final void decode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws Base64DecodingException, IOException
  {
    int i;
    if ((i = removeWhiteSpace(paramArrayOfByte)) % 4 != 0)
      throw new Base64DecodingException("It should be dived by four");
    int j;
    if ((j = i / 4) == 0)
      return;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    byte b1 = 0;
    byte b2 = 0;
    int i4 = 0;
    int i5 = 0;
    while (i4 < j - 1)
    {
      if ((!isData(i2 = paramArrayOfByte[(i5++)])) || (!isData(i3 = paramArrayOfByte[(i5++)])) || (!isData(b1 = paramArrayOfByte[(i5++)])) || (!isData(b2 = paramArrayOfByte[(i5++)])))
        throw new Base64DecodingException("decoding.general");
      k = base64Alphabet[i2];
      m = base64Alphabet[i3];
      n = base64Alphabet[b1];
      i1 = base64Alphabet[b2];
      paramOutputStream.write((byte)(k << 2 | m >> 4));
      paramOutputStream.write((byte)((m & 0xF) << 4 | n >> 2 & 0xF));
      paramOutputStream.write((byte)(n << 6 | i1));
      i4++;
    }
    if ((!isData(i2 = paramArrayOfByte[(i5++)])) || (!isData(i3 = paramArrayOfByte[(i5++)])))
      throw new Base64DecodingException("decoding.general");
    k = base64Alphabet[i2];
    m = base64Alphabet[i3];
    b1 = paramArrayOfByte[(i5++)];
    b2 = paramArrayOfByte[i5];
    if ((!isData(b1)) || (!isData(b2)))
    {
      if ((m & 0xF) != 0)
        throw new Base64DecodingException("decoding.general");
      if (((n = base64Alphabet[b1]) & 0x3) != 0)
        throw new Base64DecodingException("decoding.general");
      paramOutputStream.write((byte)(k << 2 | m >> 4));
      throw new Base64DecodingException("decoding.general");
    }
    n = base64Alphabet[b1];
    i1 = base64Alphabet[b2];
    paramOutputStream.write((byte)(k << 2 | m >> 4));
    paramOutputStream.write((byte)((m & 0xF) << 4 | n >> 2 & 0xF));
    paramOutputStream.write((byte)(n << 6 | ((!isPad(b1)) && (isPad(b2)) ? n >> 2 & 0xF : (isPad(b1)) && (isPad(b2)) ? m >> 4 : i1)));
  }

  public static final void decode(InputStream paramInputStream, OutputStream paramOutputStream)
    throws Base64DecodingException, IOException
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    byte[] arrayOfByte = new byte[4];
    int i1;
    while ((i1 = paramInputStream.read()) > 0)
    {
      if (isWhiteSpace(b1 = (byte)i1))
        continue;
      if (isPad(b1))
      {
        arrayOfByte[(n++)] = b1;
        if (n != 3)
          break;
        arrayOfByte[n] = (byte)paramInputStream.read();
        break;
      }
      if (!isData(b1))
        throw new Base64DecodingException("decoding.general");
      arrayOfByte[(n++)] = b1;
      if (n != 4)
        continue;
      n = 0;
      i = base64Alphabet[arrayOfByte[0]];
      j = base64Alphabet[arrayOfByte[1]];
      k = base64Alphabet[arrayOfByte[2]];
      m = base64Alphabet[arrayOfByte[3]];
      paramOutputStream.write((byte)(i << 2 | j >> 4));
      paramOutputStream.write((byte)((j & 0xF) << 4 | k >> 2 & 0xF));
      paramOutputStream.write((byte)(k << 6 | m));
    }
    byte b1 = arrayOfByte[0];
    int i2 = arrayOfByte[1];
    byte b2 = arrayOfByte[2];
    byte b3 = arrayOfByte[3];
    i = base64Alphabet[b1];
    j = base64Alphabet[i2];
    if ((!isData(b2)) || (!isData(b3)))
    {
      if ((j & 0xF) != 0)
        throw new Base64DecodingException("decoding.general");
      if (((k = base64Alphabet[b2]) & 0x3) != 0)
        throw new Base64DecodingException("decoding.general");
      paramOutputStream.write((byte)(i << 2 | j >> 4));
      throw new Base64DecodingException("decoding.general");
    }
    k = base64Alphabet[b2];
    m = base64Alphabet[b3];
    paramOutputStream.write((byte)(i << 2 | j >> 4));
    paramOutputStream.write((byte)((j & 0xF) << 4 | k >> 2 & 0xF));
    paramOutputStream.write((byte)(k << 6 | ((!isPad(b2)) && (isPad(b3)) ? k >> 2 & 0xF : (isPad(b2)) && (isPad(b3)) ? j >> 4 : m)));
  }

  protected static int removeWhiteSpace(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return 0;
    int i = 0;
    int j = paramArrayOfByte.length;
    for (int k = 0; k < j; k++)
    {
      int m;
      if (isWhiteSpace(m = paramArrayOfByte[k]))
        continue;
      paramArrayOfByte[(i++)] = m;
    }
    return i;
  }

  static
  {
    for (int i = 0; i < 255; i++)
      base64Alphabet[i] = -1;
    for (i = 90; i >= 65; i--)
      base64Alphabet[i] = (byte)(i - 65);
    for (i = 122; i >= 97; i--)
      base64Alphabet[i] = (byte)(i - 97 + 26);
    for (i = 57; i >= 48; i--)
      base64Alphabet[i] = (byte)(i - 48 + 52);
    base64Alphabet[43] = 62;
    base64Alphabet[47] = 63;
    for (i = 0; i <= 25; i++)
      lookUpBase64Alphabet[i] = (char)(65 + i);
    i = 26;
    for (int j = 0; i <= 51; j++)
    {
      lookUpBase64Alphabet[i] = (char)(97 + j);
      i++;
    }
    i = 52;
    for (j = 0; i <= 61; j++)
    {
      lookUpBase64Alphabet[i] = (char)(48 + j);
      i++;
    }
    lookUpBase64Alphabet[62] = '+';
    lookUpBase64Alphabet[63] = '/';
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.Base64
 * JD-Core Version:    0.6.0
 */