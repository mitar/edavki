package org.apache.xml.security.utils;

import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Document;

public class CachedXPathAPIHolder
{
  static ThreadLocal local = new ThreadLocal();
  static ThreadLocal localDoc = new ThreadLocal();

  public static void setDoc(Document paramDocument)
  {
    if (localDoc.get() != paramDocument)
    {
      CachedXPathAPI localCachedXPathAPI;
      if ((localCachedXPathAPI = (CachedXPathAPI)local.get()) == null)
      {
        localCachedXPathAPI = new CachedXPathAPI();
        local.set(localCachedXPathAPI);
        localDoc.set(paramDocument);
        return;
      }
      localCachedXPathAPI.getXPathContext().reset();
      localDoc.set(paramDocument);
    }
  }

  public static CachedXPathAPI getCachedXPathAPI()
  {
    CachedXPathAPI localCachedXPathAPI;
    if ((localCachedXPathAPI = (CachedXPathAPI)local.get()) == null)
    {
      localCachedXPathAPI = new CachedXPathAPI();
      local.set(localCachedXPathAPI);
      localDoc.set(null);
    }
    return localCachedXPathAPI;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.CachedXPathAPIHolder
 * JD-Core Version:    0.6.0
 */