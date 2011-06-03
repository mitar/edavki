package org.apache.xml.security.utils;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.NodeIterator;

public class IdResolver
{
  static Log log = LogFactory.getLog(IdResolver.class.getName());
  static WeakHashMap docMap = new WeakHashMap();

  public static void registerElementById(Element paramElement, String paramString)
  {
    Document localDocument = paramElement.getOwnerDocument();
    WeakHashMap localWeakHashMap;
    if ((localWeakHashMap = (WeakHashMap)docMap.get(localDocument)) == null)
    {
      localWeakHashMap = new WeakHashMap();
      docMap.put(localDocument, localWeakHashMap);
    }
    localWeakHashMap.put(paramString, new WeakReference(paramElement));
  }

  public static void registerElementById(Element paramElement, Attr paramAttr)
  {
    registerElementById(paramElement, paramAttr.getNodeValue());
  }

  public static Element getElementById(Document paramDocument, String paramString)
  {
    Element localElement = null;
    if ((localElement = getElementByIdType(paramDocument, paramString)) != null)
    {
      log.debug("I could find an Element using the simple getElementByIdType method: " + localElement.getTagName());
      return localElement;
    }
    if ((localElement = getElementByIdUsingDOM(paramDocument, paramString)) != null)
    {
      log.debug("I could find an Element using the simple getElementByIdUsingDOM method: " + localElement.getTagName());
      return localElement;
    }
    CachedXPathAPIHolder.setDoc(paramDocument);
    CachedXPathAPI localCachedXPathAPI = CachedXPathAPIHolder.getCachedXPathAPI();
    if ((localElement = getElementByIdInDSNamespace(paramDocument, paramString, localCachedXPathAPI)) != null)
    {
      log.debug("Found an Element using an insecure Id/ID/id search method: " + localElement.getTagName());
      registerElementById(localElement, paramString);
      return localElement;
    }
    if ((localElement = getElementByIdInXENCNamespace(paramDocument, paramString, localCachedXPathAPI)) != null)
    {
      log.debug("I could find an Element using the advanced xenc:Namespace searcher method: " + localElement.getTagName());
      registerElementById(localElement, paramString);
      return localElement;
    }
    if ((localElement = getElementByIdInSOAPSignatureNamespace(paramDocument, paramString, localCachedXPathAPI)) != null)
    {
      log.debug("I could find an Element using the advanced SOAP-SEC:id searcher method: " + localElement.getTagName());
      registerElementById(localElement, paramString);
      return localElement;
    }
    if ((localElement = getElementByIdInXKMSNamespace(paramDocument, paramString, localCachedXPathAPI)) != null)
    {
      log.debug("I could find an Element using the XKMS searcher method: " + localElement.getTagName());
      registerElementById(localElement, paramString);
      return localElement;
    }
    if ((localElement = getElementByIdUnsafeMatchByIdName(paramDocument, paramString, localCachedXPathAPI)) != null)
    {
      log.warn("Found an Element using an insecure Id/ID/id search method: " + localElement.getTagName());
      return localElement;
    }
    return null;
  }

  private static Element getElementByIdUsingDOM(Document paramDocument, String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdUsingDOM() Search for ID " + paramString);
    return paramDocument.getElementById(paramString);
  }

  private static Element getElementByIdType(Document paramDocument, String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdType() Search for ID " + paramString);
    WeakHashMap localWeakHashMap;
    WeakReference localWeakReference;
    if (((localWeakHashMap = (WeakHashMap)docMap.get(paramDocument)) != null) && ((localWeakReference = (WeakReference)localWeakHashMap.get(paramString)) != null))
      return (Element)localWeakReference.get();
    return null;
  }

  private static Element getElementByIdInDSNamespace(Document paramDocument, String paramString, CachedXPathAPI paramCachedXPathAPI)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdInDSNamespace() Search for ID " + paramString);
    try
    {
      Element localElement1 = XMLUtils.createDSctx(paramDocument, "ds", "http://www.w3.org/2000/09/xmldsig#");
      Element localElement2;
      return localElement2 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//ds:*[@Id='" + paramString + "']", localElement1);
    }
    catch (TransformerException localTransformerException)
    {
      log.fatal("empty", localTransformerException);
    }
    return null;
  }

  private static Element getElementByIdInXENCNamespace(Document paramDocument, String paramString, CachedXPathAPI paramCachedXPathAPI)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdInXENCNamespace() Search for ID " + paramString);
    try
    {
      Element localElement1 = XMLUtils.createDSctx(paramDocument, "xenc", "http://www.w3.org/2001/04/xmlenc#");
      Element localElement2;
      return localElement2 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//xenc:*[@Id='" + paramString + "']", localElement1);
    }
    catch (TransformerException localTransformerException)
    {
      log.fatal("empty", localTransformerException);
    }
    return null;
  }

  private static Element getElementByIdInSOAPSignatureNamespace(Document paramDocument, String paramString, CachedXPathAPI paramCachedXPathAPI)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdInSOAPSignatureNamespace() Search for ID " + paramString);
    try
    {
      Element localElement1 = XMLUtils.createDSctx(paramDocument, "SOAP-SEC", "http://schemas.xmlsoap.org/soap/security/2000-12");
      Element localElement2;
      return localElement2 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//*[@SOAP-SEC:id='" + paramString + "']", localElement1);
    }
    catch (TransformerException localTransformerException)
    {
      log.fatal("empty", localTransformerException);
    }
    return null;
  }

  private static Element getElementByIdInXKMSNamespace(Document paramDocument, String paramString, CachedXPathAPI paramCachedXPathAPI)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdInXKMSNamespace() Search for ID " + paramString);
    try
    {
      Element localElement1 = XMLUtils.createDSctx(paramDocument, "xkms", "http://www.w3.org/2002/03/xkms#");
      String[] arrayOfString = { "ID", "OriginalRequestID", "RequestID", "ResponseID" };
      for (int i = 0; i < arrayOfString.length; i++)
      {
        String str = arrayOfString[i];
        Element localElement2;
        if ((localElement2 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//xkms:*[@" + str + "='" + paramString + "']", localElement1)) != null)
          return localElement2;
      }
      return null;
    }
    catch (TransformerException localTransformerException)
    {
      log.fatal("empty", localTransformerException);
    }
    return null;
  }

  private static Element getElementByIdUnsafeMatchByIdName(Document paramDocument, String paramString, CachedXPathAPI paramCachedXPathAPI)
  {
    if (log.isDebugEnabled())
      log.debug("getElementByIdUnsafeMatchByIdName() Search for ID " + paramString);
    String str = "//*[@Id='" + paramString + "']";
    try
    {
      Element localElement1;
      if ((localElement1 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, str)) != null)
        return localElement1;
      Element localElement2;
      if ((localElement2 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//*[@ID='" + paramString + "']")) != null)
        return localElement2;
      Element localElement3;
      if ((localElement3 = (Element)paramCachedXPathAPI.selectSingleNode(paramDocument, "//*[@id='" + paramString + "']")) != null)
        return localElement3;
      XObject localXObject;
      Element localElement4;
      if ((localElement4 = (Element)(localXObject = XPathAPI.eval(paramDocument.getFirstChild(), str)).nodeset().nextNode()) != null)
        return localElement4;
    }
    catch (TransformerException localTransformerException)
    {
      log.fatal("empty", localTransformerException);
    }
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.IdResolver
 * JD-Core Version:    0.6.0
 */