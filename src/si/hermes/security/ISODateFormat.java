package si.hermes.security;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ISODateFormat
{
  public static String DateTime2ISO(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat;
    String str = (localSimpleDateFormat = new SimpleDateFormat("Z")).format(paramDate);
    return DateTime2ISOWithoutTZ(paramDate) + str.substring(0, 3) + ":" + str.substring(3);
  }

  public static String DateTime2ISOZ(Date paramDate)
  {
    return DateTime2ISOWithoutTZ(new Date(paramDate.getTime() + paramDate.getTimezoneOffset() * 1000 * 60)) + "Z";
  }

  public static String DateTime2ISOWithoutTZ(Date paramDate)
  {
    String str1 = "yyyy-MM-dd'T'HH:mm:ss.S";
    SimpleDateFormat localSimpleDateFormat;
    String str2;
    if (str2.charAt(str2.length() - 1) == '.')
      str2 = str2.substring(0, str2.length() - 1);
    return str2;
  }

  public static String Date2ISO(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat;
    String str = (localSimpleDateFormat = new SimpleDateFormat("Z")).format(paramDate);
    return Date2ISOWithoutTZ(paramDate) + str.substring(0, 3) + ":" + str.substring(3);
  }

  public static String Date2ISOZ(Date paramDate)
  {
    return Date2ISOWithoutTZ(paramDate) + "Z";
  }

  public static String Date2ISOWithoutTZ(Date paramDate)
  {
    String str1 = "yyyy-MM-dd";
    SimpleDateFormat localSimpleDateFormat;
    String str2;
    if (str2.charAt(str2.length() - 1) == '.')
      str2 = str2.substring(0, str2.length() - 1);
    return str2;
  }

  public static Date ISO2DateTime(String paramString)
    throws ESignDocException
  {
    paramString = paramString.replaceFirst("Z", "-00:00");
    int i = 0;
    String str1;
    if ((((str1 = paramString.substring(paramString.length() - 6)).charAt(0) == '+') || (str1.charAt(0) == '-')) && (str1.charAt(3) == ':'))
    {
      paramString = paramString.substring(0, paramString.length() - 3) + paramString.substring(paramString.length() - 2);
      i = 1;
    }
    String str2 = "yyyy-MM-dd";
    if (paramString.indexOf("T") != -1)
      str2 = str2 + "'T'HH:mm:ss";
    if (paramString.indexOf(".") != -1)
      str2 = str2 + ".SSS";
    if (i != 0)
      str2 = str2 + "Z";
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(str2);
    Date localDate = null;
    ParsePosition localParsePosition = new ParsePosition(0);
    try
    {
      if ((localDate = localSimpleDateFormat.parse(paramString, localParsePosition)) == null)
        throw new NullPointerException();
    }
    catch (NullPointerException localNullPointerException)
    {
      throw new ESignDocException(18, "Parsing Date failed for " + paramString + " (" + str2 + ")", localNullPointerException);
    }
    return localDate;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ISODateFormat
 * JD-Core Version:    0.6.0
 */