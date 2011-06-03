package org.apache.xml.security.transforms.params;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.TransformParam;
import org.apache.xml.security.utils.ElementProxy;
import org.apache.xml.security.utils.HelperNodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPath2FilterContainer extends ElementProxy
  implements TransformParam
{
  private static final String _ATT_FILTER = "Filter";
  private static final String _ATT_FILTER_VALUE_INTERSECT = "intersect";
  private static final String _ATT_FILTER_VALUE_SUBTRACT = "subtract";
  private static final String _ATT_FILTER_VALUE_UNION = "union";
  public static final String INTERSECT = "intersect";
  public static final String SUBTRACT = "subtract";
  public static final String UNION = "union";
  public static final String _TAG_XPATH2 = "XPath";
  public static final String XPathFilter2NS = "http://www.w3.org/2002/06/xmldsig-filter2";

  private XPath2FilterContainer()
  {
  }

  private XPath2FilterContainer(Document paramDocument, String paramString1, String paramString2)
  {
    super(paramDocument);
    this._constructionElement.setAttributeNS(null, "Filter", paramString2);
    this._constructionElement.appendChild(paramDocument.createTextNode(paramString1));
  }

  private XPath2FilterContainer(Element paramElement, String paramString)
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

  public static XPath2FilterContainer newInstanceIntersect(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer(paramDocument, paramString, "intersect");
  }

  public static XPath2FilterContainer newInstanceSubtract(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer(paramDocument, paramString, "subtract");
  }

  public static XPath2FilterContainer newInstanceUnion(Document paramDocument, String paramString)
  {
    return new XPath2FilterContainer(paramDocument, paramString, "union");
  }

  public static NodeList newInstances(Document paramDocument, String[][] paramArrayOfString)
  {
    HelperNodeList localHelperNodeList;
    (localHelperNodeList = new HelperNodeList()).appendChild(paramDocument.createTextNode("\n"));
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      String str1 = paramArrayOfString[i][0];
      String str2 = paramArrayOfString[i][1];
      if ((!str1.equals("intersect")) && (!str1.equals("subtract")) && (!str1.equals("union")))
        throw new IllegalArgumentException("The type(" + i + ")=\"" + str1 + "\" is illegal");
      XPath2FilterContainer localXPath2FilterContainer = new XPath2FilterContainer(paramDocument, str2, str1);
      localHelperNodeList.appendChild(localXPath2FilterContainer.getElement());
      localHelperNodeList.appendChild(paramDocument.createTextNode("\n"));
    }
    return localHelperNodeList;
  }

  public static XPath2FilterContainer newInstance(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    return new XPath2FilterContainer(paramElement, paramString);
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
    return "http://www.w3.org/2002/06/xmldsig-filter2";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.params.XPath2FilterContainer
 * JD-Core Version:    0.6.0
 */