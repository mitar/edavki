package org.apache.xml.security.utils.resolver.implementations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ResolverXPointer extends ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResolverXPointer.class.getName());

  public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    Object localObject1 = null;
    Document localDocument = paramAttr.getOwnerElement().getOwnerDocument();
    Object localObject2;
    Object localObject3;
    if (isXPointerSlash(paramAttr))
    {
      localObject1 = localDocument;
    }
    else if (isXPointerId(paramAttr))
    {
      localObject2 = getXPointerId(paramAttr);
      if ((localObject1 = IdResolver.getElementById(localDocument, (String)localObject2)) == null)
      {
        localObject3 = new Object[] { localObject2 };
        throw new ResourceResolverException("signature.Verification.MissingID", localObject3, paramAttr, paramString);
      }
    }
    CachedXPathAPIHolder.setDoc(localDocument);
    (localObject2 = new XMLSignatureInput((Node)localObject1)).setMIMEType("text/xml");
    try
    {
      localObject3 = new URI(new URI(paramString), paramAttr.getNodeValue());
      ((XMLSignatureInput)localObject2).setSourceURI(((URI)localObject3).toString());
    }
    catch (URI.MalformedURIException localMalformedURIException)
    {
      ((XMLSignatureInput)localObject2).setSourceURI(paramString);
    }
    return (XMLSignatureInput)(XMLSignatureInput)(XMLSignatureInput)localObject2;
  }

  public boolean engineCanResolve(Attr paramAttr, String paramString)
  {
    if (paramAttr == null)
      return false;
    return (isXPointerSlash(paramAttr)) || (isXPointerId(paramAttr));
  }

  private static boolean isXPointerSlash(Attr paramAttr)
  {
    return paramAttr.getNodeValue().equals("#xpointer(/)");
  }

  private static boolean isXPointerId(Attr paramAttr)
  {
    String str1;
    String str2;
    if (((str1 = paramAttr.getNodeValue()).startsWith("#xpointer(id(")) && (str1.endsWith("))")) && ((((str2 = str1.substring("#xpointer(id(".length(), str1.length() - "))".length())).charAt(0) == '"') && (str2.charAt(str2.length() - 1) == '"')) || ((str2.charAt(0) == '\'') && (str2.charAt(str2.length() - 1) == '\''))))
    {
      if (log.isDebugEnabled())
        log.debug("Id=" + str2.substring(1, str2.length() - 1));
      return true;
    }
    return false;
  }

  private static String getXPointerId(Attr paramAttr)
  {
    String str1;
    String str2;
    if (((str1 = paramAttr.getNodeValue()).startsWith("#xpointer(id(")) && (str1.endsWith("))")) && ((((str2 = str1.substring("#xpointer(id(".length(), str1.length() - "))".length())).charAt(0) == '"') && (str2.charAt(str2.length() - 1) == '"')) || ((str2.charAt(0) == '\'') && (str2.charAt(str2.length() - 1) == '\''))))
      return str2.substring(1, str2.length() - 1);
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.implementations.ResolverXPointer
 * JD-Core Version:    0.6.0
 */