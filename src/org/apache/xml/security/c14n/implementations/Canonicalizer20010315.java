package org.apache.xml.security.c14n.implementations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.helper.C14nHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Canonicalizer20010315 extends CanonicalizerBase
{
  boolean firstCall = true;
  static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
  static final String XML_LANG_URI = "http://www.w3.org/XML/1998/namespace";

  public Canonicalizer20010315(boolean paramBoolean)
  {
    super(paramBoolean);
  }

  Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException
  {
    TreeSet localTreeSet = new TreeSet(COMPARE);
    NamedNodeMap localNamedNodeMap;
    int i = (localNamedNodeMap = paramElement.getAttributes()) == null ? 0 : localNamedNodeMap.getLength();
    String str1 = "xmlns:" + paramElement.getPrefix();
    Attr localAttr1;
    (localAttr1 = paramElement.getOwnerDocument().createAttributeNS("http://www.w3.org/2000/xmlns/", str1)).setValue(paramElement.getNamespaceURI() == null ? "" : paramElement.getNamespaceURI());
    for (int j = -1; j < i; j++)
    {
      Attr localAttr2;
      String str2 = (localAttr2 = j == -1 ? localAttr1 : (Attr)localNamedNodeMap.item(j)).getLocalName();
      String str3 = localAttr2.getValue();
      String str4 = localAttr2.getNamespaceURI();
      if (!"http://www.w3.org/2000/xmlns/".equals(str4))
      {
        localTreeSet.add(localAttr2);
      }
      else
      {
        Node localNode;
        if ((("xml".equals(str2)) && ("http://www.w3.org/XML/1998/namespace".equals(str3))) || ((str1.equals(str2)) && (j != -1)) || ((localNode = paramNameSpaceSymbTable.addMappingAndRender(str2, str3, localAttr2)) == null))
          continue;
        localTreeSet.add(localNode);
        if (!C14nHelper.namespaceIsRelative(localAttr2))
          continue;
        Object[] arrayOfObject = { paramElement.getTagName(), str2, localAttr2.getNodeValue() };
        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
      }
    }
    if (this.firstCall)
    {
      paramNameSpaceSymbTable.getUnrenderedNodes(localTreeSet);
      addXmlAttributesSubtree(paramElement, localTreeSet);
      this.firstCall = false;
    }
    return localTreeSet.iterator();
  }

  private void addXmlAttributesSubtree(Element paramElement, SortedSet paramSortedSet)
  {
    Node localNode1 = paramElement.getParentNode();
    HashMap localHashMap = new HashMap();
    Node localNode2;
    if ((localNode1 != null) && (localNode1.getNodeType() == 1))
    {
      Element localElement;
      if ((localElement = (Element)localNode2).hasAttributes())
      {
        NamedNodeMap localNamedNodeMap = localElement.getAttributes();
        for (int i = 0; i < localNamedNodeMap.getLength(); i++)
        {
          Attr localAttr = (Attr)localNamedNodeMap.item(i);
          if ((!"http://www.w3.org/XML/1998/namespace".equals(localAttr.getNamespaceURI())) || (paramElement.hasAttributeNS("http://www.w3.org/XML/1998/namespace", localAttr.getLocalName())) || (localHashMap.containsKey(localAttr.getName())))
            continue;
          localHashMap.put(localAttr.getName(), localAttr);
        }
      }
    }
    paramSortedSet.addAll(localHashMap.values());
  }

  Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException
  {
    boolean bool = this._xpathNodeSet.contains(paramElement);
    NamedNodeMap localNamedNodeMap = null;
    int i = 0;
    if (paramElement.hasAttributes())
      i = (localNamedNodeMap = paramElement.getAttributes()).getLength();
    TreeSet localTreeSet = new TreeSet(COMPARE);
    Object localObject;
    for (int j = 0; j < i; j++)
    {
      String str1 = (localObject = (Attr)localNamedNodeMap.item(j)).getLocalName();
      String str2 = ((Attr)localObject).getValue();
      String str3 = ((Attr)localObject).getNamespaceURI();
      if (!"http://www.w3.org/2000/xmlns/".equals(str3))
      {
        if (!bool)
          continue;
        localTreeSet.add(localObject);
      }
      else
      {
        Node localNode;
        if ((("xml".equals(str1)) && ("http://www.w3.org/XML/1998/namespace".equals(str2))) || (!this._xpathNodeSet.contains(localObject)) || ((localNode = paramNameSpaceSymbTable.addMappingAndRenderXNodeSet(str1, str2, (Attr)localObject, bool)) == null))
          continue;
        localTreeSet.add(localNode);
        if (!C14nHelper.namespaceIsRelative((Attr)localObject))
          continue;
        Object[] arrayOfObject = { paramElement.getTagName(), str1, ((Attr)localObject).getNodeValue() };
        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
      }
    }
    if (bool)
    {
      Attr localAttr = paramElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
      localObject = null;
      if (!this._xpathNodeSet.contains(localAttr))
        localObject = localAttr == null ? paramNameSpaceSymbTable.getMapping("xmlns") : paramNameSpaceSymbTable.addMappingAndRenderXNodeSet("xmlns", "", nullNode, true);
      if (localObject != null)
        localTreeSet.add(localObject);
      addXmlAttributes(paramElement, localTreeSet);
    }
    return (Iterator)localTreeSet.iterator();
  }

  private void addXmlAttributes(Element paramElement, SortedSet paramSortedSet)
  {
    Node localNode1 = paramElement.getParentNode();
    HashMap localHashMap = new HashMap();
    Node localNode2;
    if ((localNode1 != null) && (localNode1.getNodeType() == 1) && (!this._xpathNodeSet.contains(localNode1)))
    {
      Element localElement;
      if ((localElement = (Element)localNode2).hasAttributes())
      {
        NamedNodeMap localNamedNodeMap = localElement.getAttributes();
        for (int i = 0; i < localNamedNodeMap.getLength(); i++)
        {
          Attr localAttr = (Attr)localNamedNodeMap.item(i);
          if ((!"http://www.w3.org/XML/1998/namespace".equals(localAttr.getNamespaceURI())) || (paramElement.hasAttributeNS("http://www.w3.org/XML/1998/namespace", localAttr.getLocalName())) || (localHashMap.containsKey(localAttr.getName())))
            continue;
          localHashMap.put(localAttr.getName(), localAttr);
        }
      }
    }
    paramSortedSet.addAll(localHashMap.values());
  }

  public byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
    throws CanonicalizationException
  {
    throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
  }

  public byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
    throws CanonicalizationException
  {
    throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.Canonicalizer20010315
 * JD-Core Version:    0.6.0
 */