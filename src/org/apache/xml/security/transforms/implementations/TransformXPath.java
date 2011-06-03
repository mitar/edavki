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
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.CachedXPathFuncHereAPI;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TransformXPath extends TransformSpi
{
  static Log log = LogFactory.getLog(TransformXPath.class.getName());
  public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xpath-19991116";

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
    return "http://www.w3.org/TR/1999/REC-xpath-19991116";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    try
    {
      CachedXPathFuncHereAPI localCachedXPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
      Element localElement;
      Object localObject1;
      if ((localElement = XMLUtils.selectDsNode(this._transformObject.getElement().getFirstChild(), "XPath", 0)) == null)
      {
        localObject1 = new Object[] { "ds:XPath", "Transform" };
        throw new TransformationException("xml.WrongContent", localObject1);
      }
      String str = CachedXPathFuncHereAPI.getStrFromNode(localObject1 = localElement.getChildNodes().item(0));
      boolean bool = needsCircunvent(str);
      Set localSet;
      if ((localSet = paramXMLSignatureInput.getNodeSet(bool)).size() == 0)
      {
        localObject2 = new Object[] { "input node set contains no nodes" };
        throw new TransformationException("empty", localObject2);
      }
      Object localObject2 = new HashSet();
      PrefixResolverDefault localPrefixResolverDefault = new PrefixResolverDefault(localElement);
      if (localObject1 == null)
        throw new DOMException(3, "Text must be in ds:Xpath");
      Iterator localIterator = localSet.iterator();
      Object localObject3;
      while (localIterator.hasNext())
      {
        localObject3 = (Node)localIterator.next();
        XObject localXObject;
        if ((localXObject = localCachedXPathFuncHereAPI.eval((Node)localObject3, (Node)localObject1, str, localPrefixResolverDefault)).bool())
          ((Set)localObject2).add(localObject3);
      }
      (localObject3 = new XMLSignatureInput((Set)localObject2)).setSourceURI(paramXMLSignatureInput.getSourceURI());
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

  private boolean needsCircunvent(String paramString)
  {
    return true;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXPath
 * JD-Core Version:    0.6.0
 */