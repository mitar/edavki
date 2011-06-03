package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.params.XPathFilterCHGPContainer;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.CachedXPathFuncHereAPI;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

public class TransformXPathFilterCHGP extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
  static final Integer STATE_INCLUDE = new Integer(0);
  static final Integer STATE_EXCLUDE_BUT_SEARCH = new Integer(1);
  static final Integer STATE_EXCLUDE = new Integer(2);
  Integer state = STATE_EXCLUDE_BUT_SEARCH;
  Stack stateStack = new Stack();
  Set inputSet;
  Set includeSearchSet;
  Set excludeSearchSet;
  Set excludeSet;
  Set resultSet;

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
    return "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    try
    {
      this.inputSet = paramXMLSignatureInput.getNodeSet(true);
      CachedXPathFuncHereAPI localCachedXPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
      Object localObject1;
      if (this.inputSet.size() == 0)
      {
        localObject1 = new Object[] { "input node set contains no nodes" };
        throw new TransformationException("empty", localObject1);
      }
      Document localDocument1 = (localObject1 = this._transformObject.getElement()).getOwnerDocument();
      Element localElement;
      if ((localElement = XMLUtils.selectNode(((Element)localObject1).getFirstChild(), "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter", "XPathAlternative", 0)) == null)
      {
        localObject2 = new Object[] { "{http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter}XPath", "Transform" };
        throw new TransformationException("xml.WrongContent", localObject2);
      }
      Object localObject2 = XPathFilterCHGPContainer.getInstance(localElement, paramXMLSignatureInput.getSourceURI());
      Document localDocument2 = null;
      if ((localObject3 = this.inputSet.iterator()).hasNext())
        localDocument2 = XMLUtils.getOwnerDocument((Node)((Iterator)localObject3).next());
      Object localObject3 = ((XPathFilterCHGPContainer)localObject2).getHereContextNodeIncludeButSearch();
      Object localObject4 = null;
      if (localObject3 != null)
        localObject4 = localCachedXPathFuncHereAPI.selectNodeList(localDocument1, (Node)localObject3, CachedXPathFuncHereAPI.getStrFromNode((Node)localObject3), ((XPathFilterCHGPContainer)localObject2).getElement());
      this.includeSearchSet = nodeListToSet((NodeList)localObject4);
      localObject3 = ((XPathFilterCHGPContainer)localObject2).getHereContextNodeExcludeButSearch();
      localObject4 = null;
      if (localObject3 != null)
        localObject4 = localCachedXPathFuncHereAPI.selectNodeList(localDocument1, (Node)localObject3, CachedXPathFuncHereAPI.getStrFromNode((Node)localObject3), ((XPathFilterCHGPContainer)localObject2).getElement());
      this.excludeSearchSet = nodeListToSet((NodeList)localObject4);
      localObject3 = ((XPathFilterCHGPContainer)localObject2).getHereContextNodeExclude();
      localObject4 = null;
      if (localObject3 != null)
        localObject4 = localCachedXPathFuncHereAPI.selectNodeList(localDocument1, (Node)localObject3, CachedXPathFuncHereAPI.getStrFromNode((Node)localObject3), ((XPathFilterCHGPContainer)localObject2).getElement());
      this.excludeSet = nodeListToSet((NodeList)localObject4);
      if (((XPathFilterCHGPContainer)localObject2).getIncludeSlashPolicy() == true)
        this.includeSearchSet.add(localDocument2);
      else
        this.excludeSearchSet.add(localDocument2);
      this.resultSet = new HashSet();
      localObject3 = (DocumentTraversal)localDocument2;
      localObject4 = localDocument2;
      AlwaysAcceptNodeFilter localAlwaysAcceptNodeFilter = new AlwaysAcceptNodeFilter();
      TreeWalker localTreeWalker = ((DocumentTraversal)localObject3).createTreeWalker((Node)localObject4, -1, localAlwaysAcceptNodeFilter, true);
      process(localTreeWalker);
      (localObject3 = new XMLSignatureInput(this.resultSet)).setSourceURI(paramXMLSignatureInput.getSourceURI());
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

  private void process(TreeWalker paramTreeWalker)
  {
    Node localNode1 = paramTreeWalker.getCurrentNode();
    if (this.excludeSet.contains(localNode1))
    {
      paramTreeWalker.setCurrentNode(localNode1);
      return;
    }
    if (this.excludeSearchSet.contains(localNode1))
      this.state = (this.includeSearchSet.contains(localNode1) ? STATE_INCLUDE : STATE_EXCLUDE_BUT_SEARCH);
    Object localObject;
    if ((this.inputSet.contains(localNode1)) && (this.state == STATE_INCLUDE))
    {
      this.resultSet.add(localNode1);
      if (localNode1.getNodeType() == 1)
      {
        localObject = ((Element)localNode1).getAttributes();
        int i = 0;
        Node localNode2 = ((NamedNodeMap)localObject).item(i);
        if ((this.inputSet.contains(localNode2)) && (!this.excludeSearchSet.contains(localNode2)) && (!this.excludeSet.contains(localNode2)))
          this.resultSet.add(localNode2);
        i++;
      }
    }
    this.stateStack.push(this.state);
    process(paramTreeWalker);
    this.state = ((Integer)this.stateStack.pop());
    paramTreeWalker.setCurrentNode(localNode1);
  }

  private static Set nodeListToSet(NodeList paramNodeList)
  {
    HashSet localHashSet = new HashSet();
    if (paramNodeList == null)
      return localHashSet;
    int i = paramNodeList.getLength();
    for (int j = 0; j < i; j++)
      localHashSet.add(paramNodeList.item(j));
    return localHashSet;
  }

  public class AlwaysAcceptNodeFilter
    implements NodeFilter
  {
    public AlwaysAcceptNodeFilter()
    {
    }

    public short acceptNode(Node paramNode)
    {
      return 1;
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXPathFilterCHGP
 * JD-Core Version:    0.6.0
 */