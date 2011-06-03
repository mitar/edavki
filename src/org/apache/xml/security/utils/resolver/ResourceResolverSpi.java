package org.apache.xml.security.utils.resolver;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.utils.URI;
import org.w3c.dom.Attr;

public abstract class ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResourceResolverSpi.class.getName());
  protected Map _properties = new HashMap(10);

  public abstract XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException;

  public void engineSetProperty(String paramString1, String paramString2)
  {
    Iterator localIterator = this._properties.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals(paramString1))
      {
        paramString1 = str;
        break;
      }
    }
    this._properties.put(paramString1, paramString2);
  }

  public String engineGetProperty(String paramString)
  {
    Iterator localIterator = this._properties.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals(paramString))
      {
        paramString = str;
        break;
      }
    }
    return (String)this._properties.get(paramString);
  }

  public void engineAddProperies(Map paramMap)
  {
    this._properties.putAll(paramMap);
  }

  public abstract boolean engineCanResolve(Attr paramAttr, String paramString);

  public String[] engineGetPropertyKeys()
  {
    return new String[0];
  }

  public boolean understandsProperty(String paramString)
  {
    String[] arrayOfString;
    if ((arrayOfString = engineGetPropertyKeys()) != null)
      for (int i = 0; i < arrayOfString.length; i++)
        if (arrayOfString[i].equals(paramString))
          return true;
    return false;
  }

  public static String expandSystemId(String paramString1, String paramString2)
    throws Exception
  {
    String str1 = paramString1;
    if ((paramString1 == null) || (str1.length() == 0))
      return paramString1;
    URI localURI2;
    try
    {
      new URI(str1);
      localURI1 = null;
      return paramString1;
    }
    catch (Exception localException1)
    {
      URI localURI1;
      str1 = fixURI(str1);
      localURI2 = null;
      try
      {
        String str2;
        try
        {
          str2 = fixURI(System.getProperty("user.dir"));
        }
        catch (SecurityException localSecurityException)
        {
          str2 = "";
        }
        if (!str2.endsWith("/"))
          str2 = str2 + "/";
        paramString2 = fixURI(paramString2);
        localURI1 = paramString2 == null ? new URI("file", "", str2, null, null) : new URI(paramString2);
        localURI2 = new URI(localURI1, str1);
      }
      catch (Exception localException2)
      {
      }
      if (localURI2 == null)
        return paramString1;
    }
    return localURI2.toString();
  }

  public static String fixURI(String paramString)
  {
    int i;
    int j;
    if ((paramString = paramString.replace(File.separatorChar, '/')).length() >= 4)
    {
      i = Character.toUpperCase(paramString.charAt(0));
      j = paramString.charAt(1);
      int k = paramString.charAt(2);
      int m = paramString.charAt(3);
      int n;
      if (((n = (65 <= i) && (i <= 90) && (j == 58) && (k == 47) && (m != 47) ? 1 : 0) != 0) && (log.isDebugEnabled()))
        log.debug("Found DOS filename: " + paramString);
    }
    if ((paramString.length() >= 2) && ((i = paramString.charAt(1)) == ':'))
    {
      j = Character.toUpperCase(paramString.charAt(0));
      if ((65 <= j) && (j <= 90))
        paramString = "/" + paramString;
    }
    return paramString;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.ResourceResolverSpi
 * JD-Core Version:    0.6.0
 */