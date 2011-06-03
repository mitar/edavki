package org.apache.xml.security.utils;

import java.util.HashMap;
import java.util.WeakHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class ReferenceResolver
{
  static Log log = LogFactory.getLog(ReferenceResolver.class.getName());
  static WeakHashMap docMap = new WeakHashMap();

  public static void registerReferenceDigestById(byte[] paramArrayOfByte, Document paramDocument, String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("registerReferenceDigestById() ID " + paramString);
    HashMap localHashMap;
    if ((localHashMap = (HashMap)docMap.get(paramDocument)) == null)
    {
      localHashMap = new HashMap();
      docMap.put(paramDocument, localHashMap);
    }
    localHashMap.put(paramString, paramArrayOfByte);
  }

  public static byte[] getReferenceDigestById(Document paramDocument, String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("getReferenceDigestById() Search for ID " + paramString);
    HashMap localHashMap;
    byte[] arrayOfByte;
    if (((localHashMap = (HashMap)docMap.get(paramDocument)) != null) && ((arrayOfByte = (byte[])localHashMap.get(paramString)) != null))
      return arrayOfByte;
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.ReferenceResolver
 * JD-Core Version:    0.6.0
 */