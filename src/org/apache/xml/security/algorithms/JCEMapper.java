package org.apache.xml.security.algorithms;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JCEMapper
{
  static Log log = LogFactory.getLog(JCEMapper.class.getName());
  private static Map uriToJCEName = new HashMap();
  private static Map algorithmsMap = new HashMap();
  private static String providerName = null;

  public static void init(Element paramElement)
    throws Exception
  {
    loadAlgorithms((Element)paramElement.getElementsByTagName("Algorithms").item(0));
  }

  static void loadAlgorithms(Element paramElement)
  {
    Element[] arrayOfElement = XMLUtils.selectNodes(paramElement.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Algorithm");
    for (int i = 0; i < arrayOfElement.length; i++)
    {
      Element localElement;
      String str1 = (localElement = arrayOfElement[i]).getAttribute("URI");
      String str2 = localElement.getAttribute("JCEName");
      uriToJCEName.put(str1, str2);
      algorithmsMap.put(str1, new Algorithm(localElement));
    }
  }

  static Algorithm getAlgorithmMapping(String paramString)
  {
    return (Algorithm)algorithmsMap.get(paramString);
  }

  public static String translateURItoJCEID(String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("Request for URI " + paramString);
    String str;
    return str = (String)uriToJCEName.get(paramString);
  }

  public static String getAlgorithmClassFromURI(String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("Request for URI " + paramString);
    return ((Algorithm)algorithmsMap.get(paramString)).algorithmClass;
  }

  public static int getKeyLengthFromURI(String paramString)
  {
    return Integer.parseInt(((Algorithm)algorithmsMap.get(paramString)).keyLength);
  }

  public static String getJCEKeyAlgorithmFromURI(String paramString)
  {
    return ((Algorithm)algorithmsMap.get(paramString)).requiredKey;
  }

  public static String getProviderId()
  {
    return providerName;
  }

  public static void setProviderId(String paramString)
  {
    providerName = paramString;
  }

  public static class Algorithm
  {
    String algorithmClass;
    String keyLength;
    String requiredKey;

    public Algorithm(Element paramElement)
    {
      this.algorithmClass = paramElement.getAttribute("AlgorithmClass");
      this.keyLength = paramElement.getAttribute("KeyLength");
      this.requiredKey = paramElement.getAttribute("RequiredKey");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.JCEMapper
 * JD-Core Version:    0.6.0
 */