package org.apache.xml.security.transforms.params;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.TransformParam;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPath2FilterContainer04 extends ElementProxy
  implements TransformParam
{
  private static final String _ATT_FILTER = "Filter";
  private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
  private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
  private static final String _ATT_FILTER_VALUE_UNION = "union";
  public static final String _TAG_XPATH2 = "XPath";
  public static final String XPathFilter2NS = "http://www.w3.org/2002/04/xmldsig-filter2";

  private XPath2FilterContainer04()
  {
  }

  private XPath2FilterContainer04(Document paramDocument, String paramString1, String paramString2)
  {
    super(paramDocument);
    this._constructionElement.setAttributeNS(null, "Filter", paramString2);
    if ((paramString1.length() > 2) && (!Character.isWhitespace(paramString1.charAt(0))))
    {
      this._constructionElement.appendChild(paramDocument.createTextNode("\n" + paramString1 + "\n"));
      return;
    }
    this._constructionElement.appendChild(paramDocument.createTextNode(paramString1));
  }

  private XPath2FilterContainer04(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    String str;
    if ((!(str = this._constructionElement.getAttributeNS(null, "Filter")).equals("intersect")) && (!str.equals("subtract")) && (!str.equals("union")))
    {
      Object[] arrayOfObject = { "Filter", str, "intersect, subtract or union" };
      throw new XMLSecurityException("attributeValueIllegal", arrayOfObject);
    }
  }

  public static XPath2FilterContainer04 newInstanceIntersect(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer04(paramDocument, paramString, "intersect");
  }

  public static XPath2FilterContainer04 newInstanceSubtract(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer04(paramDocument, paramString, "subtract");
  }

  public static XPath2FilterContainer04 newInstanceUnion(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer04(paramDocument, paramString, "union");
  }

  public static XPath2FilterContainer04 newInstance(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    return new XPath2FilterContainer04(paramElement, paramString);
  }

  public boolean isIntersect()
  {
    return this._constructionElement.getAttributeNS(null, "Filter").equals("intersect");
  }

  public boolean isSubtract()
  {
    return this._constructionElement.getAttributeNS(null, "Filter").equals("subtract");
  }

  public boolean isUnion()
  {
    return this._constructionElement.getAttributeNS(null, "Filter").equals("union");
  }

  public String getXPathFilterStr()
  {
    return getTextFromTextChild();
  }

  public Node getXPathFilterTextNode()
  {
    NodeList localNodeList;
    int i = (localNodeList = this._constructionElement.getChildNodes()).getLength();
    for (int j = 0; j < i; j++)
      if (localNodeList.item(j).getNodeType() == 3)
        return localNodeList.item(j);
    return null;
  }

  public final String getBaseLocalName()
  {
    return "XPath";
  }

  public final String getBaseNamespace()
  {
    return "http://www.w3.org/2002/04/xmldsig-filter2";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.params.XPath2FilterContainer04
 * JD-Core Version:    0.6.0
 */