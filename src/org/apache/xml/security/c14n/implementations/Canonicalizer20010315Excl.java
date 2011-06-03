package org.apache.xml.security.c14n.implementations;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.helper.C14nHelper;
import org.apache.xml.security.transforms.params.InclusiveNamespaces;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Canonicalizer20010315Excl extends CanonicalizerBase
{
  TreeSet _inclusiveNSSet = null;
  static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";

  public Canonicalizer20010315Excl(boolean paramBoolean)
  {
    super(paramBoolean);
  }

  public byte[] engineCanonicalizeSubTree(Node paramNode)
    throws CanonicalizationException
  {
    return engineCanonicalizeSubTree(paramNode, "", null);
  }

  public byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
    throws CanonicalizationException
  {
    return engineCanonicalizeSubTree(paramNode, paramString, null);
  }

  public byte[] engineCanonicalizeSubTree(Node paramNode1, String paramString, Node paramNode2)
    throws CanonicalizationException
  {
    this._inclusiveNSSet = ((TreeSet)InclusiveNamespaces.prefixStr2Set(paramString));
    return super.engineCanonicalizeSubTree(paramNode1, paramNode2);
  }

  Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException
  {
    TreeSet localTreeSet = new TreeSet(COMPARE);
    NamedNodeMap localNamedNodeMap = null;
    int i = 0;
    if (paramElement.hasAttributes())
      i = (localNamedNodeMap = paramElement.getAttributes()).getLength();
    SortedSet localSortedSet = (SortedSet)this._inclusiveNSSet.clone();
    Object localObject2;
    Object localObject3;
    for (int j = 0; j < i; j++)
    {
      localObject3 = (localObject2 = (Attr)localNamedNodeMap.item(j)).getLocalName();
      String str = ((Attr)localObject2).getNodeValue();
      Object localObject4;
      if (!"http://www.w3.org/2000/xmlns/".equals(((Attr)localObject2).getNamespaceURI()))
      {
        if (((localObject4 = ((Attr)localObject2).getPrefix()) != null) && (!((String)localObject4).equals("xml")) && (!((String)localObject4).equals("xmlns")))
          localSortedSet.add(localObject4);
        localTreeSet.add(localObject2);
      }
      else
      {
        if ((!paramNameSpaceSymbTable.addMapping((String)localObject3, str, (Attr)localObject2)) || (!C14nHelper.namespaceIsRelative(str)))
          continue;
        localObject4 = new Object[] { paramElement.getTagName(), localObject3, ((Attr)localObject2).getNodeValue() };
        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", localObject4);
      }
    }
    if (paramElement.getNamespaceURI() != null)
    {
      if (((localObject1 = paramElement.getPrefix()) == null) || (((String)localObject1).length() == 0))
        localSortedSet.add("xmlns");
      else
        localSortedSet.add(localObject1);
    }
    else
      localSortedSet.add("xmlns");
    Object localObject1 = localSortedSet.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (String)((Iterator)localObject1).next();
      if ((localObject3 = paramNameSpaceSymbTable.getMapping((String)localObject2)) == null)
        continue;
      localTreeSet.add(localObject3);
    }
    return (Iterator)(Iterator)(Iterator)(Iterator)localTreeSet.iterator();
  }

  public byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
    throws CanonicalizationException
  {
    try
    {
      this._inclusiveNSSet = ((TreeSet)InclusiveNamespaces.prefixStr2Set(paramString));
      byte[] arrayOfByte = super.engineCanonicalizeXPathNodeSet(paramSet);
      return arrayOfByte;
    }
    finally
    {
      this._inclusiveNSSet = null;
    }
    throw localObject;
  }

  public byte[] engineCanonicalizeXPathNodeSet(Set paramSet)
    throws CanonicalizationException
  {
    return engineCanonicalizeXPathNodeSet(paramSet, "");
  }

  final Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException
  {
    TreeSet localTreeSet = new TreeSet(COMPARE);
    NamedNodeMap localNamedNodeMap = null;
    int i = 0;
    if (paramElement.hasAttributes())
      i = (localNamedNodeMap = paramElement.getAttributes()).getLength();
    Set localSet = null;
    boolean bool;
    if ((bool = this._xpathNodeSet.contains(paramElement)))
      localSet = (Set)this._inclusiveNSSet.clone();
    Object localObject2;
    Object localObject3;
    Object localObject4;
    for (int j = 0; j < i; j++)
    {
      localObject3 = (localObject2 = (Attr)localNamedNodeMap.item(j)).getLocalName();
      localObject4 = ((Attr)localObject2).getNodeValue();
      if (!this._xpathNodeSet.contains(localObject2))
        continue;
      Object localObject5;
      if (!"http://www.w3.org/2000/xmlns/".equals(((Attr)localObject2).getNamespaceURI()))
      {
        if (!bool)
          continue;
        if (((localObject5 = ((Attr)localObject2).getPrefix()) != null) && (!((String)localObject5).equals("xml")) && (!((String)localObject5).equals("xmlns")))
          localSet.add(localObject5);
        localTreeSet.add(localObject2);
      }
      else
      {
        if ((!paramNameSpaceSymbTable.addMapping((String)localObject3, (String)localObject4, (Attr)localObject2)) || (!C14nHelper.namespaceIsRelative((String)localObject4)))
          continue;
        localObject5 = new Object[] { paramElement.getTagName(), localObject3, ((Attr)localObject2).getNodeValue() };
        throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", localObject5);
      }
    }
    Object localObject1;
    if (bool)
    {
      if (((localObject1 = paramElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns")) != null) && (!this._xpathNodeSet.contains(localObject1)))
        paramNameSpaceSymbTable.addMapping("xmlns", "", nullNode);
      if (paramElement.getNamespaceURI() != null)
      {
        if (((localObject2 = paramElement.getPrefix()) == null) || (((String)localObject2).length() == 0))
          localSet.add("xmlns");
        else
          localSet.add(localObject2);
      }
      else
        localSet.add("xmlns");
      localObject2 = localSet.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (String)((Iterator)localObject2).next();
        if ((localObject4 = paramNameSpaceSymbTable.getMapping((String)localObject3)) == null)
          continue;
        localTreeSet.add(localObject4);
      }
    }
    else
    {
      localObject1 = this._inclusiveNSSet.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (String)((Iterator)localObject1).next();
        if ((localObject3 = paramNameSpaceSymbTable.getMappingWithoutRendered((String)localObject2)) == null)
          continue;
        localTreeSet.add(localObject3);
      }
    }
    return (Iterator)(Iterator)(Iterator)(Iterator)(Iterator)localTreeSet.iterator();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.Canonicalizer20010315Excl
 * JD-Core Version:    0.6.0
 */