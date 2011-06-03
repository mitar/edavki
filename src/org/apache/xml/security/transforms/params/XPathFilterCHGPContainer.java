package org.apache.xml.security.transforms.params;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.TransformParam;
import org.apache.xml.security.utils.ElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XPathFilterCHGPContainer extends ElementProxy
  implements TransformParam
{
  private static final String _TAG_INCLUDE_BUT_SEARCH = "IncludeButSearch";
  private static final String _TAG_EXCLUDE_BUT_SEARCH = "ExcludeButSearch";
  private static final String _TAG_EXCLUDE = "Exclude";
  public static final String _TAG_XPATHCHGP = "XPathAlternative";
  public static final String _ATT_INCLUDESLASH = "IncludeSlashPolicy";
  public static final boolean IncludeSlash = true;
  public static final boolean ExcludeSlash = false;

  private XPathFilterCHGPContainer()
  {
  }

  private XPathFilterCHGPContainer(Document paramDocument, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
  {
    super(paramDocument);
    this._constructionElement.setAttributeNS(null, "IncludeSlashPolicy", paramBoolean ? "true" : "false");
    Element localElement;
    if ((paramString1 != null) && (paramString1.trim().length() > 0))
    {
      (localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "IncludeButSearch")).appendChild(this._doc.createTextNode(indentXPathText(paramString1)));
      this._constructionElement.appendChild(paramDocument.createTextNode("\n"));
      this._constructionElement.appendChild(localElement);
    }
    if ((paramString2 != null) && (paramString2.trim().length() > 0))
    {
      (localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "ExcludeButSearch")).appendChild(this._doc.createTextNode(indentXPathText(paramString2)));
      this._constructionElement.appendChild(paramDocument.createTextNode("\n"));
      this._constructionElement.appendChild(localElement);
    }
    if ((paramString3 != null) && (paramString3.trim().length() > 0))
    {
      (localElement = ElementProxy.createElementForFamily(paramDocument, getBaseNamespace(), "Exclude")).appendChild(this._doc.createTextNode(indentXPathText(paramString3)));
      this._constructionElement.appendChild(paramDocument.createTextNode("\n"));
      this._constructionElement.appendChild(localElement);
    }
    this._constructionElement.appendChild(paramDocument.createTextNode("\n"));
  }

  static String indentXPathText(String paramString)
  {
    if ((paramString.length() > 2) && (!Character.isWhitespace(paramString.charAt(0))))
      return "\n" + paramString + "\n";
    return paramString;
  }

  private XPathFilterCHGPContainer(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public static XPathFilterCHGPContainer getInstance(Document paramDocument, boolean paramBoolean, String paramString1, String paramString2, String paramString3)
  {
    return new XPathFilterCHGPContainer(paramDocument, paramBoolean, paramString1, paramString2, paramString3);
  }

  public static XPathFilterCHGPContainer getInstance(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    return new XPathFilterCHGPContainer(paramElement, paramString);
  }

  private String getXStr(String paramString)
  {
    if (length(getBaseNamespace(), paramString) != 1)
      return "";
    Element localElement;
    return XMLUtils.getFullTextChildrenFromElement(localElement = XMLUtils.selectNode(this._constructionElement.getFirstChild(), getBaseNamespace(), paramString, 0));
  }

  public String getIncludeButSearch()
  {
    return getXStr("IncludeButSearch");
  }

  public String getExcludeButSearch()
  {
    return getXStr("ExcludeButSearch");
  }

  public String getExclude()
  {
    return getXStr("Exclude");
  }

  public boolean getIncludeSlashPolicy()
  {
    return this._constructionElement.getAttributeNS(null, "IncludeSlashPolicy").equals("true");
  }

  private Node getHereContextNode(String paramString)
  {
    if (length(getBaseNamespace(), paramString) != 1)
      return null;
    return XMLUtils.selectNodeText(this._constructionElement.getFirstChild(), getBaseNamespace(), paramString, 0);
  }

  public Node getHereContextNodeIncludeButSearch()
  {
    return getHereContextNode("IncludeButSearch");
  }

  public Node getHereContextNodeExcludeButSearch()
  {
    return getHereContextNode("ExcludeButSearch");
  }

  public Node getHereContextNodeExclude()
  {
    return getHereContextNode("Exclude");
  }

  public final String getBaseLocalName()
  {
    return "XPathAlternative";
  }

  public final String getBaseNamespace()
  {
    return "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.params.XPathFilterCHGPContainer
 * JD-Core Version:    0.6.0
 */