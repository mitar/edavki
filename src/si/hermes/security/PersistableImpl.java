package si.hermes.security;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.utils.IdResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class PersistableImpl
  implements IPersistable
{
  protected Element fNode;

  public String getToString()
    throws IOException, ESignDocException
  {
    Synchronize(false);
    if (this.fNode == null)
      return new String("");
    return Utility.createStringFromDomElement(this.fNode);
  }

  public Element GetXml()
    throws ESignDocException
  {
    return GetXml(null);
  }

  public Element GetXml(Element paramElement)
    throws ESignDocException
  {
    if (paramElement != null)
      this.fNode = paramElement;
    Synchronize(false);
    return this.fNode;
  }

  public final void LoadXml(Element paramElement)
    throws ESignDocException
  {
    this.fNode = paramElement;
    Synchronize(true);
  }

  protected abstract void Synchronize(boolean paramBoolean)
    throws ESignDocException;

  protected final String EnsureAttribute(Element paramElement, String paramString1, String paramString2, boolean paramBoolean)
  {
    return EnsureAttribute(paramElement, paramString1, paramString2, paramBoolean, true);
  }

  protected final String EnsureAttribute(Element paramElement, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!paramBoolean1)
    {
      if ((!paramBoolean2) && ((paramString2 == null) || ("".equals(paramString2))))
      {
        paramElement.removeAttribute(paramString1);
        break label91;
      }
      paramElement.setAttribute(paramString1, paramString2);
      if (!"Id".equals(paramString1))
        break label91;
    }
    else
    {
      if (paramElement == null)
        return null;
      paramString2 = paramElement.getAttribute(paramString1);
      if ((!"Id".equals(paramString1)) || (paramString2 == null) || (paramString2 == ""))
        break label91;
    }
    IdResolver.registerElementById(paramElement, paramString2);
    label91: return paramString2;
  }

  protected final void DeleteAttribute(Element paramElement, String paramString)
  {
    paramElement.removeAttribute(paramString);
  }

  protected final String EnsureValue(Element paramElement, String paramString, boolean paramBoolean)
    throws ESignDocException
  {
    if (!paramBoolean)
    {
      Utility.setTextNodeValue(paramElement, paramString);
    }
    else
    {
      String str;
      paramString = str = Utility.getTextNodeValue(paramElement);
    }
    return paramString;
  }

  protected final String EnsureValueCheckNode(Element paramElement, String paramString, boolean paramBoolean)
    throws ESignDocException
  {
    if (paramElement != null)
      if (!paramBoolean)
      {
        Utility.setTextNodeValue(paramElement, paramString);
      }
      else
      {
        String str;
        paramString = str = Utility.getTextNodeValue(paramElement);
      }
    return paramString;
  }

  protected final Element EnsureChildNode(Element paramElement1, Element paramElement2, boolean paramBoolean)
  {
    Node localNode = null;
    if (paramElement1 != null)
      localNode = paramElement1.getFirstChild();
    if (!paramBoolean)
    {
      if (localNode != null)
      {
        if (localNode.getOwnerDocument() != paramElement2.getOwnerDocument())
          paramElement2 = (Element)paramElement1.getOwnerDocument().importNode(paramElement2, true);
        paramElement1.replaceChild(paramElement2, localNode);
      }
      else
      {
        if (paramElement1.getOwnerDocument() != paramElement2.getOwnerDocument())
          paramElement2 = (Element)paramElement1.getOwnerDocument().importNode(paramElement2, true);
        paramElement1.appendChild(paramElement2);
      }
      return paramElement2;
    }
    return (Element)localNode;
  }

  protected final void RemoveChildren(Element paramElement, String paramString1, String paramString2)
  {
    Node localNode;
    if ((paramString1.equals(localNode.getLocalName())) && (paramString2.equals(localNode.getNamespaceURI())))
      paramElement.removeChild(localNode);
  }

  protected final void RemoveAllChildren(Element paramElement)
  {
    Node localNode;
    paramElement.removeChild(localNode);
  }

  protected final Element EnsureBaseElement(Element paramElement, String paramString1, String paramString2)
    throws ParserConfigurationException, ESignDocException
  {
    if (paramElement == null)
    {
      Element localElement;
      (localElement = Utility.CreateElement(paramString1, paramString2)).getOwnerDocument().appendChild(localElement);
      return localElement;
    }
    if ((paramString1.equals(paramElement.getLocalName())) && (paramString2.equals(paramElement.getNamespaceURI())))
      return paramElement;
    throw new ESignDocException(14);
  }

  protected final Element EnsureElement(Element paramElement, String paramString1, String paramString2)
  {
    return EnsureElement(paramElement, paramString1, paramString2, true);
  }

  protected final Element EnsureElement(Element paramElement, String paramString1, String paramString2, boolean paramBoolean)
  {
    Node localNode;
    if ((paramString1.equals(localNode.getLocalName())) && (paramString2.equals(localNode.getNamespaceURI())))
      return (Element)localNode;
    if (paramBoolean)
    {
      Element localElement = Utility.CreateElement(paramElement.getOwnerDocument(), paramString1, paramString2);
      paramElement.appendChild(localElement);
      return localElement;
    }
    return null;
  }

  protected final Element EnsureElementList(Element paramElement, String paramString1, String paramString2, int paramInt)
  {
    Node localNode;
    if ((paramString1.equals(localNode.getLocalName())) && (paramString2.equals(localNode.getNamespaceURI())))
    {
      paramInt--;
      if (paramInt == 0)
        return (Element)localNode;
    }
    Element localElement = Utility.CreateElement(paramElement.getOwnerDocument(), paramString1, paramString2);
    paramElement.appendChild(localElement);
    return localElement;
  }

  protected final paramHelper ensureOptionalElementWithValue(Element paramElement, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws ESignDocException
  {
    Element localElement = null;
    if ((localElement = EnsureElement(paramElement, paramString1, paramString2, (!paramBoolean) && (!"".equals(paramString3)))) != null)
    {
      paramString3 = EnsureValue(localElement, paramString3, paramBoolean);
      if (("".equals(paramString3)) && (!paramBoolean))
        localElement.getParentNode().removeChild(localElement);
    }
    else
    {
      paramString3 = "";
    }
    paramHelper localparamHelper;
    (localparamHelper = new paramHelper()).elem1 = localElement;
    localparamHelper.str1 = paramString3;
    return localparamHelper;
  }

  protected final paramHelper ensureElementAfter(Element paramElement1, String paramString1, String paramString2, Element paramElement2, boolean paramBoolean1, boolean paramBoolean2)
    throws ESignDocException
  {
    paramHelper localparamHelper;
    (localparamHelper = new paramHelper()).elem1 = null;
    if (paramString1 == null)
      throw new ESignDocException(3, "nodename");
    if (paramString2 == null)
      throw new ESignDocException(3, "ns");
    for (int i = 0; i < paramElement1.getChildNodes().getLength(); i++)
    {
      Node localNode = paramElement1.getChildNodes().item(i);
      if ((!paramString1.equals(localNode.getLocalName())) || (!paramString2.equals(localNode.getNamespaceURI())))
        continue;
      localparamHelper.elem1 = ((Element)localNode);
      if (paramBoolean2)
        localparamHelper.elem1.getParentNode().removeChild(localparamHelper.elem1);
      else
        localparamHelper.elem2 = localparamHelper.elem1;
      return localparamHelper;
    }
    if (paramBoolean1)
    {
      localparamHelper.elem1 = Utility.CreateElement(paramElement1.getOwnerDocument(), paramString1, paramString2);
      if (localparamHelper.elem1.getOwnerDocument() != paramElement1.getOwnerDocument())
        localparamHelper.elem1 = ((Element)paramElement1.getOwnerDocument().importNode(localparamHelper.elem1, true));
      if (paramElement2 == null)
        paramElement1.insertBefore(localparamHelper.elem1, paramElement1.getFirstChild());
      else if (paramElement2.getNextSibling() == null)
        paramElement1.appendChild(localparamHelper.elem1);
      else
        paramElement1.insertBefore(localparamHelper.elem1, paramElement2.getNextSibling());
      localparamHelper.elem2 = localparamHelper.elem1;
    }
    return localparamHelper;
  }

  protected final Element EnsureElementBefore(Element paramElement, String paramString1, String paramString2, Node paramNode, boolean paramBoolean1, boolean paramBoolean2)
    throws ESignDocException
  {
    Element localElement = null;
    if (paramString1 == null)
      throw new ESignDocException(3, "nodename");
    if (paramString2 == null)
      throw new ESignDocException(3, "ns");
    for (int i = 0; i < paramElement.getChildNodes().getLength(); i++)
    {
      Node localNode = paramElement.getChildNodes().item(i);
      if ((!paramString1.equals(localNode.getLocalName())) || (!paramString2.equals(localNode.getNamespaceURI())))
        continue;
      localElement = (Element)localNode;
      if (paramBoolean2)
      {
        localElement.getParentNode().removeChild(localElement);
        localElement = null;
      }
      return localElement;
    }
    if (paramBoolean1)
    {
      localElement = Utility.CreateElement(paramElement.getOwnerDocument(), paramString1, paramString2);
      paramElement.insertBefore(localElement, paramNode);
    }
    return localElement;
  }

  protected final paramHelper ensureOptionalElementAfterWithValue(Element paramElement1, String paramString1, String paramString2, Element paramElement2, String paramString3, boolean paramBoolean)
    throws ESignDocException
  {
    paramHelper localparamHelper1 = new paramHelper();
    if (paramString1 == null)
      throw new ESignDocException(3, "nodename");
    if (paramString2 == null)
      throw new ESignDocException(3, "ns");
    int i = ("".equals(paramString3)) || (paramString3 == null) ? 1 : 0;
    paramHelper localparamHelper2;
    paramString3 = (localparamHelper2 = ensureElementAfter(paramElement1, paramString1, paramString2, paramElement2, (!paramBoolean) && (i == 0), (i != 0) && (!paramBoolean))).elem1 != null ? EnsureValue(localparamHelper2.elem1, paramString3, paramBoolean) : "";
    localparamHelper1.elem1 = localparamHelper2.elem1;
    localparamHelper1.str1 = paramString3;
    return localparamHelper1;
  }

  protected final paramHelper EnsureElementWithValue(Element paramElement, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws ESignDocException
  {
    paramHelper localparamHelper = new paramHelper();
    Element localElement;
    paramString3 = (localElement = EnsureElement(paramElement, paramString1, paramString2, !paramBoolean)) != null ? EnsureValue(localElement, paramString3, paramBoolean) : "";
    localparamHelper.elem1 = localElement;
    localparamHelper.str1 = paramString3;
    return localparamHelper;
  }

  public final Node findAfter(List paramList, Node paramNode)
  {
    Node localNode;
    int i;
    int j;
    if ((localNode = paramNode.getFirstChild()) != null)
      if (localNode.getNodeType() == 1)
      {
        i = 0;
        if ((localNode.getLocalName() != null) && (localNode.getNamespaceURI() != null) && (localNode.getLocalName().equals(paramList.get(j))) && (localNode.getNamespaceURI().equals(paramList.get(j + 1))))
          i = 1;
      }
    return localNode;
  }

  public final Object clone()
    throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }

  protected final boolean IsNullOrEmpty(String paramString)
  {
    if (paramString == null)
      return true;
    return "".equals(paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.PersistableImpl
 * JD-Core Version:    0.6.0
 */