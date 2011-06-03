package org.apache.xml.security.utils;

import javax.xml.transform.TransformerException;
import org.apache.xml.security.transforms.implementations.FuncHereContext;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;

public class XPathFuncHereAPI
{
  public static Node selectSingleNode(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectSingleNode(paramNode1, paramNode2, paramNode1);
  }

  public static Node selectSingleNode(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    NodeIterator localNodeIterator;
    return (localNodeIterator = selectNodeIterator(paramNode1, paramNode2, paramNode3)).nextNode();
  }

  public static NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectNodeIterator(paramNode1, paramNode2, paramNode1);
  }

  public static NodeIterator selectNodeIterator(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = eval(paramNode1, paramNode2, paramNode3)).nodeset();
  }

  public static NodeList selectNodeList(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return selectNodeList(paramNode1, paramNode2, paramNode1);
  }

  public static NodeList selectNodeList(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = eval(paramNode1, paramNode2, paramNode3)).nodelist();
  }

  public static XObject eval(Node paramNode1, Node paramNode2)
    throws TransformerException
  {
    return eval(paramNode1, paramNode2, paramNode1);
  }

  public static XObject eval(Node paramNode1, Node paramNode2, Node paramNode3)
    throws TransformerException
  {
    FuncHereContext localFuncHereContext = new FuncHereContext(paramNode2);
    PrefixResolverDefault localPrefixResolverDefault = new PrefixResolverDefault(paramNode3.getNodeType() == 9 ? ((Document)paramNode3).getDocumentElement() : paramNode3);
    String str = getStrFromNode(paramNode2);
    XPath localXPath = new XPath(str, null, localPrefixResolverDefault, 0, null);
    int i = localFuncHereContext.getDTMHandleFromNode(paramNode1);
    return localXPath.execute(localFuncHereContext, i, localPrefixResolverDefault);
  }

  public static XObject eval(Node paramNode1, Node paramNode2, PrefixResolver paramPrefixResolver)
    throws TransformerException
  {
    String str = getStrFromNode(paramNode2);
    XPath localXPath = new XPath(str, null, paramPrefixResolver, 0, null);
    FuncHereContext localFuncHereContext;
    int i = (localFuncHereContext = new FuncHereContext(paramNode2)).getDTMHandleFromNode(paramNode1);
    return localXPath.execute(localFuncHereContext, i, paramPrefixResolver);
  }

  private static String getStrFromNode(Node paramNode)
  {
    if (paramNode.getNodeType() == 3)
      return ((Text)paramNode).getData();
    if (paramNode.getNodeType() == 2)
      return ((Attr)paramNode).getNodeValue();
    if (paramNode.getNodeType() == 7)
      return ((ProcessingInstruction)paramNode).getNodeValue();
    return "";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.XPathFuncHereAPI
 * JD-Core Version:    0.6.0
 */