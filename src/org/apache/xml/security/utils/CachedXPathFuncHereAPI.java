package org.apache.xml.security.utils;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.security.transforms.implementations.FuncHereContext;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;

public class CachedXPathFuncHereAPI
{
  FuncHereContext _funcHereContext = null;
  DTMManager _dtmManager = null;
  XPathContext _context = null;
  String xpathStr = null;
  XPath xpath = null;

  public FuncHereContext getFuncHereContext()
  {
    return this._funcHereContext;
  }

  private CachedXPathFuncHereAPI()
  {
  }

  public CachedXPathFuncHereAPI(XPathContext paramXPathContext)
  {
    this._dtmManager = paramXPathContext.getDTMManager();
    this._context = paramXPathContext;
  }

  public CachedXPathFuncHereAPI(CachedXPathAPI paramCachedXPathAPI)
  {
    this._dtmManager = paramCachedXPathAPI.getXPathContext().getDTMManager();
    this._context = paramCachedXPathAPI.getXPathContext();
  }

  public Node selectSingleNode(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectSingleNode(paramNode1, paramNode2, paramNode1);
  }

  public Node selectSingleNode(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    NodeIterator localNodeIterator;
    return (localNodeIterator = selectNodeIterator(paramNode1, paramNode2, paramNode3)).nextNode();
  }

  public NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectNodeIterator(paramNode1, paramNode2, paramNode1);
  }

  public NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = eval(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode3)).nodeset();
  }

  public NodeList selectNodeList(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectNodeList(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode1);
  }

  public NodeList selectNodeList(Node paramNode1, Node paramNode2, String paramString, Node paramNode3)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = eval(paramNode1, paramNode2, paramString, paramNode3)).nodelist();
  }

  public XObject eval(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return eval(paramNode1, paramNode2, getStrFromNode(paramNode2), paramNode1);
  }

  public XObject eval(Node paramNode1, Node paramNode2, String paramString, Node paramNode3)
    throws TransformerException
  {
    if (this._funcHereContext == null)
      this._funcHereContext = new FuncHereContext(paramNode2, this._dtmManager);
    PrefixResolverDefault localPrefixResolverDefault = new PrefixResolverDefault(paramNode3.getNodeType() == 9 ? ((Document)paramNode3).getDocumentElement() : paramNode3);
    if (paramString != this.xpathStr)
    {
      if (paramString.indexOf("here()") > 0)
      {
        this._context.reset();
        this._dtmManager = this._context.getDTMManager();
      }
      this.xpath = new XPath(paramString, null, localPrefixResolverDefault, 0, null);
      this.xpathStr = paramString;
    }
    int i = this._funcHereContext.getDTMHandleFromNode(paramNode1);
    return this.xpath.execute(this._funcHereContext, i, localPrefixResolverDefault);
  }

  public XObject eval(Node paramNode1, Node paramNode2, String paramString, PrefixResolver paramPrefixResolver)
    throws TransformerException
  {
    if (paramString != this.xpathStr)
    {
      if (paramString.indexOf("here()") > 0)
      {
        this._context.reset();
        this._dtmManager = this._context.getDTMManager();
      }
      try
      {
        this.xpath = new XPath(paramString, null, paramPrefixResolver, 0, null);
      }
      catch (TransformerException localTransformerException2)
      {
        TransformerException localTransformerException1;
        Throwable localThrowable;
        if ((((localThrowable = (localTransformerException1 = localTransformerException2).getCause()) instanceof ClassNotFoundException)) && (localThrowable.getMessage().indexOf("FuncHere") > 0))
          throw new RuntimeException(I18n.translate("endorsed.jdk1.4.0") + localTransformerException1);
        throw localTransformerException1;
      }
      this.xpathStr = paramString;
    }
    if (this._funcHereContext == null)
      this._funcHereContext = new FuncHereContext(paramNode2, this._dtmManager);
    int i = this._funcHereContext.getDTMHandleFromNode(paramNode1);
    return this.xpath.execute(this._funcHereContext, i, paramPrefixResolver);
  }

  public static String getStrFromNode(Node paramNode)
  {
    if (paramNode.getNodeType() == 3)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      Node localNode;
      if (localNode.getNodeType() == 3)
        localStringBuffer.append(((Text)localNode).getData());
      return localStringBuffer.toString();
    }
    if (paramNode.getNodeType() == 2)
      return ((Attr)paramNode).getNodeValue();
    if (paramNode.getNodeType() == 7)
      return ((ProcessingInstruction)paramNode).getNodeValue();
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.CachedXPathFuncHereAPI
 * JD-Core Version:    0.6.0
 */