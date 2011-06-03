package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.params.XPath2FilterContainer;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.CachedXPathFuncHereAPI;
import org.apache.xml.security.utils.HelperNodeList;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TransformXPath2Filter extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/2002/06/xmldsig-filter2";
  List _filterTypes = new ArrayList();
  List _filterNodes = new ArrayList();
  Set _F = null;
  List _ancestors = null;
  private static final String FUnion = "union";
  private static final String FSubtract = "subtract";
  private static final String FIntersect = "intersect";
  Set _inputSet = null;

  public boolean wantsOctetStream()
  {
    return false;
  }

  public boolean wantsNodeSet()
  {
    return true;
  }

  public boolean returnsOctetStream()
  {
    return false;
  }

  public boolean returnsNodeSet()
  {
    return true;
  }

  protected String engineGetURI()
  {
    return "http://www.w3.org/2002/06/xmldsig-filter2";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    try
    {
      this._inputSet = paramXMLSignatureInput.getNodeSet(true);
      if (this._inputSet.size() == 0)
        return paramXMLSignatureInput;
      CachedXPathFuncHereAPI localCachedXPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
      Document localDocument = XMLUtils.getOwnerDocument(this._inputSet);
      Element[] arrayOfElement;
      int i;
      Object localObject1;
      if ((i = (arrayOfElement = XMLUtils.selectNodes(this._transformObject.getElement().getFirstChild(), "http://www.w3.org/2002/06/xmldsig-filter2", "XPath")).length) == 0)
      {
        localObject1 = new Object[] { "http://www.w3.org/2002/06/xmldsig-filter2", "XPath" };
        throw new TransformationException("xml.WrongContent", localObject1);
      }
      this._filterTypes.add("union");
      (localObject1 = new HelperNodeList()).appendChild(localDocument);
      this._filterNodes.add(localObject1);
      Object localObject3;
      for (int j = 0; j < i; j++)
      {
        if ((localObject3 = XPath2FilterContainer.newInstance(localObject2 = XMLUtils.selectNode(this._transformObject.getElement().getFirstChild(), "http://www.w3.org/2002/06/xmldsig-filter2", "XPath", j), paramXMLSignatureInput.getSourceURI())).isIntersect())
          this._filterTypes.add("intersect");
        else if (((XPath2FilterContainer)localObject3).isSubtract())
          this._filterTypes.add("subtract");
        else if (((XPath2FilterContainer)localObject3).isUnion())
          this._filterTypes.add("union");
        else
          this._filterTypes.add(null);
        NodeList localNodeList = localCachedXPathFuncHereAPI.selectNodeList(localDocument, ((XPath2FilterContainer)localObject3).getXPathFilterTextNode(), CachedXPathFuncHereAPI.getStrFromNode(((XPath2FilterContainer)localObject3).getXPathFilterTextNode()), ((XPath2FilterContainer)localObject3).getElement());
        this._filterNodes.add(localNodeList);
      }
      this._F = new HashSet();
      this._ancestors = new ArrayList();
      traversal(localDocument);
      HashSet localHashSet = new HashSet();
      Object localObject2 = this._inputSet.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = ((Iterator)localObject2).next();
        if (this._F.contains(localObject3))
          localHashSet.add(localObject3);
      }
      (localObject3 = new XMLSignatureInput(localHashSet)).setSourceURI(paramXMLSignatureInput.getSourceURI());
      return localObject3;
    }
    catch (TransformerException localTransformerException)
    {
      throw new TransformationException("empty", localTransformerException);
    }
    catch (DOMException localDOMException)
    {
      throw new TransformationException("empty", localDOMException);
    }
    catch (IOException localIOException)
    {
      throw new TransformationException("empty", localIOException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new TransformationException("empty", localCanonicalizationException);
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      throw new TransformationException("empty", localInvalidCanonicalizerException);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      throw new TransformationException("empty", localParserConfigurationException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      throw new TransformationException("empty", localXMLSecurityException);
    }
    catch (SAXException localSAXException)
    {
    }
    throw new TransformationException("empty", localSAXException);
  }

  private void traversal(Node paramNode)
  {
    this._ancestors.add(paramNode);
    int j;
    int k;
    if (this._inputSet.contains(paramNode))
    {
      int i = this._filterTypes.size();
      j = 0;
      NodeList localNodeList1;
      for (j = i - 1; j >= 0; j--)
      {
        localNodeList1 = (NodeList)this._filterNodes.get(j);
        String str1;
        if (((str1 = (String)this._filterTypes.get(j)) == "union") && (rooted(this._ancestors, localNodeList1)))
          break;
      }
      if ((localNodeList1 = j) == -1)
        k = 0;
      int m = 1;
      for (int n = k; n < i; n++)
      {
        NodeList localNodeList2 = (NodeList)this._filterNodes.get(n);
        String str2 = (String)this._filterTypes.get(n);
        boolean bool = rooted(this._ancestors, localNodeList2);
        if ((str2 == "intersect") && (!bool))
        {
          m = 0;
          break;
        }
        if ((str2 != "subtract") || (!bool))
          continue;
        m = 0;
        break;
      }
      if (m != 0)
        this._F.add(paramNode);
    }
    Object localObject;
    if (paramNode.getNodeType() == 1)
    {
      j = (localObject = ((Element)paramNode).getAttributes()).getLength();
      for (k = 0; k < j; k++)
      {
        Node localNode = ((NamedNodeMap)localObject).item(k);
        traversal(localNode);
      }
    }
    traversal((Node)localObject);
    this._ancestors.remove(paramNode);
  }

  boolean rooted(List paramList, NodeList paramNodeList)
  {
    int i = paramNodeList.getLength();
    for (int j = 0; j < i; j++)
    {
      Node localNode = paramNodeList.item(j);
      if (paramList.contains(localNode))
        return true;
    }
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXPath2Filter
 * JD-Core Version:    0.6.0
 */