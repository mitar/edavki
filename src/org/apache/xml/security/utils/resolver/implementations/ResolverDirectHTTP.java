package org.apache.xml.security.utils.resolver.implementations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;
import org.w3c.dom.Attr;

public class ResolverDirectHTTP extends ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResolverDirectHTTP.class.getName());
  static final String[] properties = { "http.proxy.host", "http.proxy.port", "http.proxy.username", "http.proxy.password", "http.basic.username", "http.basic.password" };
  private static final int HttpProxyHost = 0;
  private static final int HttpProxyPort = 1;
  private static final int HttpProxyUser = 2;
  private static final int HttpProxyPass = 3;
  private static final int HttpBasicUser = 4;
  private static final int HttpBasicPass = 5;

  public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    try
    {
      int i = 0;
      String str1 = engineGetProperty(properties[0]);
      String str2 = engineGetProperty(properties[1]);
      if ((str1 != null) && (str2 != null))
        i = 1;
      String str3 = (String)System.getProperties().get("http.proxySet");
      String str4 = (String)System.getProperties().get("http.proxyHost");
      String str5 = (String)System.getProperties().get("http.proxyPort");
      int j = (str3 != null) && (str4 != null) && (str5 != null) ? 1 : 0;
      if (i != 0)
      {
        if (log.isDebugEnabled())
          log.debug("Use of HTTP proxy enabled: " + str1 + ":" + str2);
        System.getProperties().put("http.proxySet", "true");
        System.getProperties().put("http.proxyHost", str1);
        System.getProperties().put("http.proxyPort", str2);
      }
      URI localURI1 = getNewURI(paramAttr.getNodeValue(), paramString);
      URI localURI2;
      (localURI2 = new URI(localURI1)).setFragment(null);
      URL localURL;
      URLConnection localURLConnection = (localURL = new URL(localURI2.toString())).openConnection();
      String str6 = engineGetProperty(properties[2]);
      Object localObject1 = engineGetProperty(properties[3]);
      if ((str6 != null) && (localObject1 != null))
      {
        localObject3 = Base64.encode((localObject2 = str6 + ":" + (String)localObject1).getBytes());
        localURLConnection.setRequestProperty("Proxy-Authorization", (String)localObject3);
      }
      if (((str6 = localURLConnection.getHeaderField("WWW-Authenticate")) != null) && (str6.startsWith("Basic")))
      {
        localObject1 = engineGetProperty(properties[4]);
        localObject2 = engineGetProperty(properties[5]);
        if ((localObject1 != null) && (localObject2 != null))
        {
          localURLConnection = localURL.openConnection();
          String str7 = Base64.encode((localObject3 = (String)localObject1 + ":" + (String)localObject2).getBytes());
          localURLConnection.setRequestProperty("Authorization", "Basic " + str7);
        }
      }
      str6 = localURLConnection.getHeaderField("Content-Type");
      localObject1 = localURLConnection.getInputStream();
      Object localObject2 = new ByteArrayOutputStream();
      Object localObject3 = new byte[4096];
      int k = 0;
      int m = 0;
      ((ByteArrayOutputStream)localObject2).write(localObject3, 0, k);
      log.debug("Fetched " + m + " bytes from URI " + localURI1.toString());
      XMLSignatureInput localXMLSignatureInput;
      (localXMLSignatureInput = new XMLSignatureInput(((ByteArrayOutputStream)localObject2).toByteArray())).setSourceURI(localURI1.toString());
      localXMLSignatureInput.setMIMEType(str6);
      if (j != 0)
      {
        System.getProperties().put("http.proxySet", str3);
        System.getProperties().put("http.proxyHost", str4);
        System.getProperties().put("http.proxyPort", str5);
      }
      return localXMLSignatureInput;
    }
    catch (MalformedURLException localMalformedURLException)
    {
      throw new ResourceResolverException("generic.EmptyMessage", localMalformedURLException, paramAttr, paramString);
    }
    catch (IOException localIOException)
    {
    }
    throw new ResourceResolverException("generic.EmptyMessage", localIOException, paramAttr, paramString);
  }

  public boolean engineCanResolve(Attr paramAttr, String paramString)
  {
    if (paramAttr == null)
    {
      log.debug("quick fail, uri == null");
      return false;
    }
    String str;
    if (((str = paramAttr.getNodeValue()).equals("")) || (str.startsWith("#")))
    {
      log.debug("quick fail for empty URIs and local ones");
      return false;
    }
    URI localURI = null;
    try
    {
      localURI = getNewURI(paramAttr.getNodeValue(), paramString);
    }
    catch (URI.MalformedURIException localMalformedURIException)
    {
      log.debug("", localMalformedURIException);
    }
    if ((localURI != null) && (localURI.getScheme().equals("http")))
    {
      if (log.isDebugEnabled())
        log.debug("I state that I can resolve " + localURI.toString());
      return true;
    }
    if (log.isDebugEnabled())
      log.debug("I state that I can't resolve " + localURI.toString());
    return false;
  }

  public String[] engineGetPropertyKeys()
  {
    return properties;
  }

  private URI getNewURI(String paramString1, String paramString2)
    throws URI.MalformedURIException
  {
    if ((paramString2 == null) || ("".equals(paramString2)))
      return new URI(paramString1);
    return new URI(new URI(paramString2), paramString1);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.implementations.ResolverDirectHTTP
 * JD-Core Version:    0.6.0
 */