package org.apache.xml.security.transforms.params;

import org.apache.xml.security.transforms.TransformParam;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathContainer extends SignatureElementProxy
  implements TransformParam
{
  public XPathContainer(Document paramDocument)
  {
    super(paramDocument);
  }

  public void setXPath(String paramString)
  {
    if (this._constructionElement.getChildNodes() != null)
    {
      localObject = this._constructionElement.getChildNodes();
      for (int i = 0; i < ((NodeList)localObject).getLength(); i++)
        this._constructionElement.removeChild(((NodeList)localObject).item(i));
    }
    Object localObject = this._doc.createTextNode(paramString);
    this._constructionElement.appendChild((Node)localObject);
  }

  public String getXPath()
  {
    return getTextFromTextChild();
  }

  public String getBaseLocalName()
  {
    return "XPath";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.params.XPathContainer
 * JD-Core Version:    0.6.0
 */