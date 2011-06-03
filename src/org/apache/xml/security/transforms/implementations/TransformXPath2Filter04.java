package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.params.XPath2FilterContainer04;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.CachedXPathFuncHereAPI;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TransformXPath2Filter04 extends TransformSpi
{
  static Log log = LogFactory.getLog(TransformXPath2Filter04.class.getName());
  public static final String implementedTransformURI = "http://www.w3.org/2002/04/xmldsig-filter2";

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
    return "http://www.w3.org/2002/04/xmldsig-filter2";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    try
    {
      Set localSet = paramXMLSignatureInput.getNodeSet(true);
      if (log.isDebugEnabled())
        log.debug("perform xfilter2 on " + localSet.size() + " nodes");
      CachedXPathFuncHereAPI localCachedXPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
      CachedXPathAPI localCachedXPathAPI = new CachedXPathAPI(CachedXPathAPIHolder.getCachedXPathAPI());
      Object localObject1;
      if (localSet.size() == 0)
      {
        localObject1 = new Object[] { "input node set contains no nodes" };
        throw new TransformationException("empty", localObject1);
      }
      if ((localObject1 = XMLUtils.selectNode(this._transformObject.getElement().getFirstChild(), "http://www.w3.org/2002/04/xmldsig-filter2", "XPath", 0)) == null)
      {
        localObject2 = new Object[] { "dsig-xpath:XPath", "Transform" };
        throw new TransformationException("xml.WrongContent", localObject2);
      }
      Object localObject2 = XPath2FilterContainer04.newInstance((Element)localObject1, paramXMLSignatureInput.getSourceURI());
      Document localDocument = this._transformObject.getElement().getOwnerDocument();
      NodeList localNodeList = localCachedXPathFuncHereAPI.selectNodeList(localDocument, ((XPath2FilterContainer04)localObject2).getXPathFilterTextNode(), CachedXPathFuncHereAPI.getStrFromNode(((XPath2FilterContainer04)localObject2).getXPathFilterTextNode()), ((XPath2FilterContainer04)localObject2).getElement());
      if (log.isDebugEnabled())
        log.debug("subtreeRoots contains " + localNodeList.getLength() + " nodes");
      HashSet localHashSet1 = new HashSet();
      Object localObject3;
      Object localObject5;
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        int j;
        if (((j = (localObject3 = localNodeList.item(i)).getNodeType()) == 1) || (j == 9))
        {
          int k = (localObject5 = localCachedXPathAPI.selectNodeList((Node)localObject3, "(.//. | .//@* | .//namespace::*)")).getLength();
          for (int m = 0; m < k; m++)
            localHashSet1.add(((NodeList)localObject5).item(m));
        }
        else if ((j == 2) || (j == 3) || (j == 4) || (j == 7))
        {
          localHashSet1.add(localObject3);
        }
        else
        {
          throw new RuntimeException("unknown node type: " + j + " " + localObject3);
        }
      }
      if (log.isDebugEnabled())
        log.debug("selection process identified " + localHashSet1.size() + " nodes");
      HashSet localHashSet2 = new HashSet();
      Object localObject4;
      if (((XPath2FilterContainer04)localObject2).isIntersect())
      {
        localObject3 = localSet.iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject4 = (Node)((Iterator)localObject3).next();
          if (localHashSet1.contains(localObject4))
            localHashSet2.add(localObject4);
        }
      }
      else if (((XPath2FilterContainer04)localObject2).isSubtract())
      {
        localObject3 = localSet.iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject4 = (Node)((Iterator)localObject3).next();
          if (!localHashSet1.contains(localObject4))
            localHashSet2.add(localObject4);
        }
      }
      else if (((XPath2FilterContainer04)localObject2).isUnion())
      {
        localObject3 = localSet.iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject4 = (Node)((Iterator)localObject3).next();
          localHashSet2.add(localObject4);
        }
        localObject4 = localHashSet1.iterator();
        while (((Iterator)localObject4).hasNext())
        {
          localObject5 = (Node)((Iterator)localObject4).next();
          localHashSet2.add(localObject5);
        }
      }
      else
      {
        throw new TransformationException("empty");
      }
      (localObject3 = new XMLSignatureInput(localHashSet2)).setSourceURI(paramXMLSignatureInput.getSourceURI());
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
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXPath2Filter04
 * JD-Core Version:    0.6.0
 */