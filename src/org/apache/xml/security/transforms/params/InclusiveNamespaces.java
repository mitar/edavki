package org.apache.xml.security.transforms.params;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.TransformParam;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InclusiveNamespaces extends ElementProxy
  implements TransformParam
{
  public static final String _TAG_EC_INCLUSIVENAMESPACES = "InclusiveNamespaces";
  public static final String _ATT_EC_PREFIXLIST = "PrefixList";
  public static final String ExclusiveCanonicalizationNamespace = "http://www.w3.org/2001/10/xml-exc-c14n#";

  public InclusiveNamespaces(Document paramDocument, String paramString)
  {
    this(paramDocument, prefixStr2Set(paramString));
  }

  public InclusiveNamespaces(Document paramDocument, Set paramSet)
  {
    super(paramDocument);
    StringBuffer localStringBuffer = new StringBuffer();
    TreeSet localTreeSet;
    Iterator localIterator = (localTreeSet = new TreeSet(paramSet)).iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals("xmlns"))
      {
        localStringBuffer.append("#default ");
        continue;
      }
      localStringBuffer.append(str + " ");
    }
    this._constructionElement.setAttributeNS(null, "PrefixList", localStringBuffer.toString().trim());
  }

  public String getInclusiveNamespaces()
  {
    return this._constructionElement.getAttributeNS(null, "PrefixList");
  }

  public InclusiveNamespaces(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public static SortedSet prefixStr2Set(String paramString)
  {
    TreeSet localTreeSet = new TreeSet();
    if ((paramString == null) || (paramString.length() == 0))
      return localTreeSet;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " \t\r\n");
    while (localStringTokenizer.hasMoreTokens())
    {
      String str;
      if ((str = localStringTokenizer.nextToken()).equals("#default"))
      {
        localTreeSet.add("xmlns");
        continue;
      }
      localTreeSet.add(str);
    }
    return localTreeSet;
  }

  public String getBaseNamespace()
  {
    return "http://www.w3.org/2001/10/xml-exc-c14n#";
  }

  public String getBaseLocalName()
  {
    return "InclusiveNamespaces";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.params.InclusiveNamespaces
 * JD-Core Version:    0.6.0
 */