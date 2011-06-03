package org.apache.xml.security.c14n.implementations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.stream.XMLStreamReader;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.helper.C14nHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Canonicalizer20010315Stax extends CanonicalizerBaseStax
{
  boolean firstCall = true;
  static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
  static final String XML_LANG_URI = "http://www.w3.org/XML/1998/namespace";

  public Canonicalizer20010315Stax(boolean paramBoolean)
  {
    super(paramBoolean);
  }

  Iterator handleAttributesSubtree(Iterator paramIterator, AttrStream paramAttrStream, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws CanonicalizationException
  {
    TreeSet localTreeSet = new TreeSet(COMPARE);
    AttrStream localAttrStream1;
    if ((localAttrStream1 = paramNameSpaceSymbTableStax.addMappingAndRender(paramAttrStream.getLocalName(), paramAttrStream.getNodeValue(), paramAttrStream)) != null)
      localTreeSet.add(localAttrStream1);
    while (paramIterator.hasNext())
    {
      AttrStream localAttrStream2 = (AttrStream)paramIterator.next();
      if (!"http://www.w3.org/2000/xmlns/".equals(localAttrStream2.getNamespaceURI()))
      {
        localTreeSet.add(localAttrStream2);
        continue;
      }
      if ((("xml".equals(localAttrStream2.getLocalName())) && ("http://www.w3.org/XML/1998/namespace".equals(localAttrStream2.getNodeValue()))) || (localAttrStream2.getNodeName().equals(paramAttrStream.getNodeName())))
        continue;
      AttrStream localAttrStream3;
      if ((localAttrStream3 = paramNameSpaceSymbTableStax.addMappingAndRender(localAttrStream2.getLocalName(), localAttrStream2.getNodeValue(), localAttrStream2)) != null)
        localTreeSet.add(localAttrStream3);
    }
    if (this.firstCall)
    {
      paramNameSpaceSymbTableStax.getUnrenderedNodes(localTreeSet);
      this.firstCall = false;
    }
    return localTreeSet.iterator();
  }

  Iterator handleAttributesSubtree(XMLStreamReader paramXMLStreamReader, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws CanonicalizationException
  {
    TreeSet localTreeSet = new TreeSet(COMPARE);
    int i = paramXMLStreamReader == null ? 0 : paramXMLStreamReader.getAttributeCount() + paramXMLStreamReader.getNamespaceCount();
    String str1 = "xmlns:" + paramXMLStreamReader.getPrefix();
    for (int j = -1; j < i; j++)
    {
      String str2;
      String str3;
      String str4;
      if (j == -1)
      {
        str2 = (paramXMLStreamReader.getPrefix() == null) || (paramXMLStreamReader.getPrefix() == "") ? "xmlns" : paramXMLStreamReader.getPrefix();
        str3 = (paramXMLStreamReader.getPrefix() == null) || (paramXMLStreamReader.getPrefix() == "") ? null : "xmlns";
        str4 = paramXMLStreamReader.getNamespaceURI() == null ? "" : paramXMLStreamReader.getNamespaceURI();
      }
      else
      {
        str2 = paramXMLStreamReader.getAttributeLocalName(j);
        str3 = paramXMLStreamReader.getAttributePrefix(j);
        str4 = paramXMLStreamReader.getAttributeValue(j);
        str2 = (paramXMLStreamReader.getNamespacePrefix(j - paramXMLStreamReader.getAttributeCount()) == null) || (paramXMLStreamReader.getNamespacePrefix(j - paramXMLStreamReader.getAttributeCount()) == "") ? "xmlns" : paramXMLStreamReader.getNamespacePrefix(j - paramXMLStreamReader.getAttributeCount());
        str3 = (paramXMLStreamReader.getNamespacePrefix(j - paramXMLStreamReader.getAttributeCount()) == null) || (paramXMLStreamReader.getNamespacePrefix(j - paramXMLStreamReader.getAttributeCount()) == "") ? null : "xmlns";
        str4 = paramXMLStreamReader.getNamespaceURI(j - paramXMLStreamReader.getAttributeCount());
      }
      String str5 = j < paramXMLStreamReader.getAttributeCount() ? paramXMLStreamReader.getAttributeNamespace(j) : "http://www.w3.org/2000/xmlns/";
      if (!"http://www.w3.org/2000/xmlns/".equals(str5))
      {
        localTreeSet.add(new AttrStream(str3, str2, str5, str4));
      }
      else
      {
        AttrStream localAttrStream;
        if ((("xml".equals(str2)) && ("http://www.w3.org/XML/1998/namespace".equals(str4))) || ((str1.equals(str2)) && (j != -1)) || ((localAttrStream = paramNameSpaceSymbTableStax.addMappingAndRender(str2, str4, new AttrStream(str3, str2, str5, str4))) == null))
          continue;
        localTreeSet.add(localAttrStream);
      }
    }
    if (this.firstCall)
    {
      paramNameSpaceSymbTableStax.getUnrenderedNodes(localTreeSet);
      this.firstCall = false;
    }
    return localTreeSet.iterator();
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
 * Qualified Name:     org.apache.xml.security.c14n.implementations.Canonicalizer20010315Stax
 * JD-Core Version:    0.6.0
 */