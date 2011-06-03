package org.apache.xml.security.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.xml.security.Init;

public class I18n
{
  public static final String NOT_INITIALIZED_MSG = "You must initialize the xml-security library correctly before you use it. Call the static method \"org.apache.xml.security.Init.init();\" to do that before you use any functionality from that library.";
  public static String defaultLanguageCode;
  public static String defaultCountryCode;
  protected static ResourceBundle resourceBundle;
  protected static boolean alreadyInitialized = false;
  protected static String _languageCode = null;
  protected static String _countryCode = null;

  public static String translate(String paramString, Object[] paramArrayOfObject)
  {
    return getExceptionMessage(paramString, paramArrayOfObject);
  }

  public static String translate(String paramString)
  {
    return getExceptionMessage(paramString);
  }

  public static String getExceptionMessage(String paramString)
  {
    try
    {
      String str;
      return str = resourceBundle.getString(paramString);
    }
    catch (Throwable localThrowable)
    {
      if (Init.isInitialized())
        return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "org/apache/xml/security/resource/xmlsecurity" + "\"";
    }
    return "You must initialize the xml-security library correctly before you use it. Call the static method \"org.apache.xml.security.Init.init();\" to do that before you use any functionality from that library.";
  }

  public static String getExceptionMessage(String paramString, Exception paramException)
  {
    try
    {
      Object[] arrayOfObject = { paramException.getMessage() };
      String str;
      return str = MessageFormat.format(resourceBundle.getString(paramString), arrayOfObject);
    }
    catch (Throwable localThrowable)
    {
      if (Init.isInitialized())
        return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "org/apache/xml/security/resource/xmlsecurity" + "\". Original Exception was a " + paramException.getClass().getName() + " and message " + paramException.getMessage();
    }
    return "You must initialize the xml-security library correctly before you use it. Call the static method \"org.apache.xml.security.Init.init();\" to do that before you use any functionality from that library.";
  }

  public static String getExceptionMessage(String paramString, Object[] paramArrayOfObject)
  {
    try
    {
      String str;
      return str = MessageFormat.format(resourceBundle.getString(paramString), paramArrayOfObject);
    }
    catch (Throwable localThrowable)
    {
      if (Init.isInitialized())
        return "No message with ID \"" + paramString + "\" found in resource bundle \"" + "org/apache/xml/security/resource/xmlsecurity" + "\"";
    }
    return "You must initialize the xml-security library correctly before you use it. Call the static method \"org.apache.xml.security.Init.init();\" to do that before you use any functionality from that library.";
  }

  public static void init(String paramString1, String paramString2)
  {
    defaultLanguageCode = paramString1;
    if (defaultLanguageCode == null)
      defaultLanguageCode = Locale.getDefault().getLanguage();
    defaultCountryCode = paramString2;
    if (defaultCountryCode == null)
      defaultCountryCode = Locale.getDefault().getCountry();
    initLocale(defaultLanguageCode, defaultCountryCode);
  }

  public static void initLocale(String paramString1, String paramString2)
  {
    if ((alreadyInitialized) && (paramString1.equals(_languageCode)) && (paramString2.equals(_countryCode)))
      return;
    if ((paramString1 != null) && (paramString2 != null) && (paramString1.length() > 0) && (paramString2.length() > 0))
    {
      _languageCode = paramString1;
      _countryCode = paramString2;
    }
    else
    {
      _countryCode = defaultCountryCode;
      _languageCode = defaultLanguageCode;
    }
    resourceBundle = ResourceBundle.getBundle("org/apache/xml/security/resource/xmlsecurity", new Locale(_languageCode, _countryCode));
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.I18n
 * JD-Core Version:    0.6.0
 */