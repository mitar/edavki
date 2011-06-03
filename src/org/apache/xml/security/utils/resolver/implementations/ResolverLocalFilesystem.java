package org.apache.xml.security.utils.resolver.implementations;

import java.io.FileInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.apache.xml.utils.URI;
import org.w3c.dom.Attr;

public class ResolverLocalFilesystem extends ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResolverLocalFilesystem.class.getName());

  public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    try
    {
      URI localURI1 = new URI(new URI(paramString), paramAttr.getNodeValue());
      URI localURI2;
      (localURI2 = new URI(localURI1)).setFragment(null);
      String str = translateUriToFilename(localURI2.toString());
      FileInputStream localFileInputStream = new FileInputStream(str);
      XMLSignatureInput localXMLSignatureInput;
      (localXMLSignatureInput = new XMLSignatureInput(localFileInputStream)).setSourceURI(localURI1.toString());
      return localXMLSignatureInput;
    }
    catch (Exception localException)
    {
    }
    throw new ResourceResolverException("generic.EmptyMessage", localException, paramAttr, paramString);
  }

  private static String translateUriToFilename(String paramString)
  {
    String str;
    if ((str = paramString.substring("file:/".length())).indexOf("%20") > -1)
    {
      int i = 0;
      int j = 0;
      StringBuffer localStringBuffer = new StringBuffer(str.length());
      do
        if ((j = str.indexOf("%20", i)) == -1)
        {
          localStringBuffer.append(str.substring(i));
        }
        else
        {
          localStringBuffer.append(str.substring(i, j));
          localStringBuffer.append(' ');
          i = j + 3;
        }
      while (j != -1);
      str = localStringBuffer.toString();
    }
    if (str.charAt(1) == ':')
      return str;
    return "/" + str;
  }

  public boolean engineCanResolve(Attr paramAttr, String paramString)
  {
    if (paramAttr == null)
      return false;
    String str;
    if (((str = paramAttr.getNodeValue()).equals("")) || (str.startsWith("#")))
      return false;
    try
    {
      URI localURI = new URI(new URI(paramString), paramAttr.getNodeValue());
      if (log.isDebugEnabled())
        log.debug("I was asked whether I can resolve " + localURI.toString());
      if (localURI.getScheme().equals("file"))
      {
        if (log.isDebugEnabled())
          log.debug("I state that I can resolve " + localURI.toString());
        return true;
      }
    }
    catch (Exception localException)
    {
    }
    log.debug("But I can't");
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.implementations.ResolverLocalFilesystem
 * JD-Core Version:    0.6.0
 */