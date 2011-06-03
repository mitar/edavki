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

public class ResolverFragment extends ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResolverFragment.class.getName());

  public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    String str = paramAttr.getNodeValue();
    Document localDocument = paramAttr.getOwnerElement().getOwnerDocument();
    Object localObject1 = null;
    Object localObject2;
    Object localObject3;
    if (str.equals(""))
    {
      log.debug("ResolverFragment with empty URI (means complete document)");
      localObject1 = localDocument;
    }
    else
    {
      localObject2 = str.substring(1);
      if ((localObject1 = IdResolver.getElementById(localDocument, (String)localObject2)) == null)
      {
        localObject3 = new Object[] { localObject2 };
        throw new ResourceResolverException("signature.Verification.MissingID", localObject3, paramAttr, paramString);
      }
      if (log.isDebugEnabled())
        log.debug("Try to catch an Element with ID " + (String)localObject2 + " and Element was " + localObject1);
    }
    CachedXPathAPIHolder.setDoc(localDocument);
    (localObject2 = new XMLSignatureInput((Node)localObject1)).setExcludeComments(true);
    ((XMLSignatureInput)localObject2).setMIMEType("text/xml");
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
    {
      log.debug("Quick fail for null uri");
      return false;
    }
    String str;
    if (((str = paramAttr.getNodeValue()).equals("")) || ((str.startsWith("#")) && (!str.startsWith("#xpointer("))))
    {
      if (log.isDebugEnabled())
        log.debug("State I can resolve reference: \"" + str + "\"");
      return true;
    }
    if (log.isDebugEnabled())
      log.debug("Do not seem to be able to resolve reference: \"" + str + "\"");
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.implementations.ResolverFragment
 * JD-Core Version:    0.6.0
 */