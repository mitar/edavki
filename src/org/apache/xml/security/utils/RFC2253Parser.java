package org.apache.xml.security.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

public class RFC2253Parser
{
  static boolean _TOXML = true;
  static int counter = 0;

  public static String rfc2253toXMLdsig(String paramString)
  {
    _TOXML = true;
    String str;
    return rfctoXML(str = normalize(paramString));
  }

  public static String xmldsigtoRFC2253(String paramString)
  {
    _TOXML = false;
    String str;
    return xmltoRFC(str = normalize(paramString));
  }

  public static String normalize(String paramString)
  {
    if ((paramString == null) || (paramString.equals("")))
      return "";
    try
    {
      String str = semicolonToComma(paramString);
      StringBuffer localStringBuffer = new StringBuffer();
      int i = 0;
      int j = 0;
      int m = 0;
      int k;
      j += countQuotes(str, m, k);
      if ((k > 0) && (str.charAt(k - 1) != '\\') && (j % 2 != 1))
      {
        localStringBuffer.append(parseRDN(str.substring(i, k).trim()) + ",");
        i = k + 1;
        j = 0;
      }
      localStringBuffer.append(parseRDN(trim(str.substring(i))));
      return localStringBuffer.toString();
    }
    catch (IOException localIOException)
    {
    }
    return paramString;
  }

  static String parseRDN(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = 0;
    int m = 0;
    int k;
    j += countQuotes(paramString, m, k);
    if ((k > 0) && (paramString.charAt(k - 1) != '\\') && (j % 2 != 1))
    {
      localStringBuffer.append(parseATAV(trim(paramString.substring(i, k))) + "+");
      i = k + 1;
      j = 0;
    }
    localStringBuffer.append(parseATAV(trim(paramString.substring(i))));
    return localStringBuffer.toString();
  }

  static String parseATAV(String paramString)
    throws IOException
  {
    int i;
    if (((i = paramString.indexOf("=")) == -1) || ((i > 0) && (paramString.charAt(i - 1) == '\\')))
      return paramString;
    String str1 = normalizeAT(paramString.substring(0, i));
    String str2 = normalizeV(paramString.substring(i + 1));
    return str1 + "=" + str2;
  }

  static String normalizeAT(String paramString)
  {
    String str;
    if ((str = paramString.toUpperCase().trim()).startsWith("OID"))
      str = str.substring(3);
    return str;
  }

  static String normalizeV(String paramString)
    throws IOException
  {
    String str;
    if ((str = trim(paramString)).startsWith("\""))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      StringReader localStringReader = new StringReader(str.substring(1, str.length() - 1));
      int i = 0;
      while ((i = localStringReader.read()) > -1)
      {
        char c;
        if (((c = (char)i) == ',') || (c == '=') || (c == '+') || (c == '<') || (c == '>') || (c == '#') || (c == ';'))
          localStringBuffer.append('\\');
        localStringBuffer.append(c);
      }
      str = trim(localStringBuffer.toString());
    }
    if (str.startsWith("#"))
      if (str.startsWith("\\#"))
        str = _TOXML == true ? '\\' + str : str.substring(1);
    return str;
  }

  static String rfctoXML(String paramString)
  {
    try
    {
      String str;
      return changeWStoXML(str = changeLess32toXML(paramString));
    }
    catch (Exception localException)
    {
    }
    return paramString;
  }

  static String xmltoRFC(String paramString)
  {
    try
    {
      String str;
      return changeWStoRFC(str = changeLess32toRFC(paramString));
    }
    catch (Exception localException)
    {
    }
    return paramString;
  }

  static String changeLess32toRFC(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    StringReader localStringReader = new StringReader(paramString);
    int i = 0;
    while ((i = localStringReader.read()) > -1)
    {
      char c1;
      if ((c1 = (char)i) == '\\')
      {
        localStringBuffer.append(c1);
        char c2 = (char)localStringReader.read();
        char c3 = (char)localStringReader.read();
        if (((c2 >= '0') && (c2 <= '9')) || ((c2 >= 'A') && (c2 <= 'F')) || ((c2 >= 'a') && (c2 <= 'f') && (((c3 >= '0') && (c3 <= '9')) || ((c3 >= 'A') && (c3 <= 'F')) || ((c3 >= 'a') && (c3 <= 'f')))))
        {
          char c4 = (char)Byte.parseByte("" + c2 + c3, 16);
          localStringBuffer.append(c4);
          continue;
        }
        localStringBuffer.append(c2);
        localStringBuffer.append(c3);
        continue;
      }
      localStringBuffer.append(c1);
    }
    return localStringBuffer.toString();
  }

  static String changeLess32toXML(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    StringReader localStringReader = new StringReader(paramString);
    int i = 0;
    while ((i = localStringReader.read()) > -1)
    {
      if (i < 32)
      {
        localStringBuffer.append('\\');
        localStringBuffer.append(Integer.toHexString(i));
        continue;
      }
      localStringBuffer.append((char)i);
    }
    return localStringBuffer.toString();
  }

  static String changeWStoXML(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    StringReader localStringReader = new StringReader(paramString);
    int i = 0;
    while ((i = localStringReader.read()) > -1)
    {
      char c1;
      if ((c1 = (char)i) == '\\')
      {
        char c2;
        if ((c2 = (char)localStringReader.read()) == ' ')
        {
          localStringBuffer.append('\\');
          String str = "20";
          localStringBuffer.append(str);
          continue;
        }
        localStringBuffer.append('\\');
        localStringBuffer.append(c2);
        continue;
      }
      localStringBuffer.append(c1);
    }
    return localStringBuffer.toString();
  }

  static String changeWStoRFC(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int k = 0;
    int j;
    localStringBuffer.append(trim(paramString.substring(i, j)) + "\\ ");
    i = j + 3;
    localStringBuffer.append(paramString.substring(i));
    return localStringBuffer.toString();
  }

  static String semicolonToComma(String paramString)
  {
    return removeWSandReplace(paramString, ";", ",");
  }

  static String removeWhiteSpace(String paramString1, String paramString2)
  {
    return removeWSandReplace(paramString1, paramString2, paramString2);
  }

  static String removeWSandReplace(String paramString1, String paramString2, String paramString3)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = 0;
    int m = 0;
    int k;
    j += countQuotes(paramString1, m, k);
    if ((k > 0) && (paramString1.charAt(k - 1) != '\\') && (j % 2 != 1))
    {
      localStringBuffer.append(trim(paramString1.substring(i, k)) + paramString3);
      i = k + 1;
      j = 0;
    }
    localStringBuffer.append(trim(paramString1.substring(i)));
    return localStringBuffer.toString();
  }

  private static int countQuotes(String paramString, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = paramInt1; j < paramInt2; j++)
    {
      if (paramString.charAt(j) != '"')
        continue;
      i++;
    }
    return i;
  }

  static String trim(String paramString)
  {
    String str = paramString.trim();
    int i = paramString.indexOf(str.substring(0)) + str.length();
    if ((paramString.length() > i) && (str.endsWith("\\")) && (!str.endsWith("\\\\")) && (paramString.charAt(i) == ' '))
      str = str + " ";
    return str;
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    testToXML("CN=\"Steve, Kille\",  O=Isode Limited, C=GB");
    testToXML("CN=Steve Kille    ,   O=Isode Limited,C=GB");
    testToXML("\\ OU=Sales+CN=J. Smith,O=Widget Inc.,C=US\\ \\ ");
    testToXML("CN=L. Eagle,O=Sue\\, Grabbit and Runn,C=GB");
    testToXML("CN=Before\\0DAfter,O=Test,C=GB");
    testToXML("CN=\"L. Eagle,O=Sue, = + < > # ;Grabbit and Runn\",C=GB");
    testToXML("1.3.6.1.4.1.1466.0=#04024869,O=Test,C=GB");
    StringBuffer localStringBuffer;
    (localStringBuffer = new StringBuffer()).append('L');
    localStringBuffer.append('u');
    localStringBuffer.append(50317);
    localStringBuffer.append('i');
    localStringBuffer.append(50311);
    String str;
    testToXML(str = "SN=" + localStringBuffer.toString());
    testToRFC("CN=\"Steve, Kille\",  O=Isode Limited, C=GB");
    testToRFC("CN=Steve Kille    ,   O=Isode Limited,C=GB");
    testToRFC("\\20OU=Sales+CN=J. Smith,O=Widget Inc.,C=US\\20\\20 ");
    testToRFC("CN=L. Eagle,O=Sue\\, Grabbit and Runn,C=GB");
    testToRFC("CN=Before\\12After,O=Test,C=GB");
    testToRFC("CN=\"L. Eagle,O=Sue, = + < > # ;Grabbit and Runn\",C=GB");
    testToRFC("1.3.6.1.4.1.1466.0=\\#04024869,O=Test,C=GB");
    (localStringBuffer = new StringBuffer()).append('L');
    localStringBuffer.append('u');
    localStringBuffer.append(50317);
    localStringBuffer.append('i');
    localStringBuffer.append(50311);
    testToRFC(str = "SN=" + localStringBuffer.toString());
  }

  static void testToXML(String paramString)
  {
    System.out.println("start " + counter++ + ": " + paramString);
    System.out.println("         " + rfc2253toXMLdsig(paramString));
    System.out.println("");
  }

  static void testToRFC(String paramString)
  {
    System.out.println("start " + counter++ + ": " + paramString);
    System.out.println("         " + xmldsigtoRFC2253(paramString));
    System.out.println("");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.RFC2253Parser
 * JD-Core Version:    0.6.0
 */