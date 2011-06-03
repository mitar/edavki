package org.apache.xml.security.c14n.helper;

import java.util.Comparator;
import org.apache.xml.security.c14n.implementations.AttrStream;

public class AttrCompareStax
  implements Comparator
{
  private final int ATTR0_BEFORE_ATTR1 = -1;
  private final int ATTR1_BEFORE_ATTR0 = 1;
  private static final String XMLNS = "http://www.w3.org/2000/xmlns/";

  public int compare(Object paramObject1, Object paramObject2)
  {
    String str1 = ((AttrStream)paramObject1).getNamespaceURI();
    String str2 = ((AttrStream)paramObject2).getNamespaceURI();
    boolean bool1 = "http://www.w3.org/2000/xmlns/".equals(str1);
    boolean bool2 = "http://www.w3.org/2000/xmlns/".equals(str2);
    String str3;
    if (bool1)
    {
      if (bool2)
      {
        str3 = ((AttrStream)paramObject1).getLocalName();
        str4 = ((AttrStream)paramObject2).getLocalName();
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
        str3 = ((AttrStream)paramObject1).getName();
        str4 = ((AttrStream)paramObject2).getName();
        return str3.compareTo(str4);
      }
      return -1;
    }
    if (str2 == null)
      return 1;
    int i;
    if ((i = str1.compareTo(str2)) != 0)
      return i;
    String str4 = ((AttrStream)paramObject1).getLocalName();
    String str5 = ((AttrStream)paramObject2).getLocalName();
    return str4.compareTo(str5);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.helper.AttrCompareStax
 * JD-Core Version:    0.6.0
 */