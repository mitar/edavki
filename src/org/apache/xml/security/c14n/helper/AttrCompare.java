package org.apache.xml.security.c14n.helper;

import java.util.Comparator;
import org.w3c.dom.Attr;

public class AttrCompare
  implements Comparator
{
  private final int ATTR0_BEFORE_ATTR1 = -1;
  private final int ATTR1_BEFORE_ATTR0 = 1;
  private static final String XMLNS = "http://www.w3.org/2000/xmlns/";

  public int compare(Object paramObject1, Object paramObject2)
  {
    Attr localAttr1 = (Attr)paramObject1;
    Attr localAttr2 = (Attr)paramObject2;
    String str1 = localAttr1.getNamespaceURI();
    String str2 = localAttr2.getNamespaceURI();
    boolean bool1 = "http://www.w3.org/2000/xmlns/".equals(str1);
    boolean bool2 = "http://www.w3.org/2000/xmlns/".equals(str2);
    String str3;
    if (bool1)
    {
      if (bool2)
      {
        str3 = localAttr1.getLocalName();
        str4 = localAttr2.getLocalName();
        if (str3.equals("xmlns"))
          str3 = "";
        if (str4.equals("xmlns"))
          str4 = "";
        return str3.compareTo(str4);
      }
      return -1;
    }
    if (bool2)
      return 1;
    if (str1 == null)
    {
      if (str2 == null)
      {
        str3 = localAttr1.getName();
        str4 = localAttr2.getName();
        return str3.compareTo(str4);
      }
      return -1;
    }
    if (str2 == null)
      return 1;
    int i;
    if ((i = str1.compareTo(str2)) != 0)
      return i;
    String str4 = localAttr1.getLocalName();
    String str5 = localAttr2.getLocalName();
    return str4.compareTo(str5);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.helper.AttrCompare
 * JD-Core Version:    0.6.0
 */