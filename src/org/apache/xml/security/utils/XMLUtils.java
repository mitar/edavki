package org.apache.xml.security.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLUtils
{
  static Log log = LogFactory.getLog(XMLUtils.class.getName());
  private static String[] nodeTypeString = { "", "ELEMENT", "ATTRIBUTE", "TEXT_NODE", "CDATA_SECTION", "ENTITY_REFERENCE", "ENTITY", "PROCESSING_INSTRUCTION", "COMMENT", "DOCUMENT", "DOCUMENT_TYPE", "DOCUMENT_FRAGMENT", "NOTATION" };

  public static void getSet(Node paramNode1, Set paramSet, Node paramNode2, boolean paramBoolean)
  {
    if ((paramNode2 != null) && (isDescendantOrSelf(paramNode2, paramNode1)))
      return;
    getSetRec(paramNode1, paramSet, paramNode2, paramBoolean);
  }

  static final void getSetRec(Node paramNode1, Set paramSet, Node paramNode2, boolean paramBoolean)
  {
    if (paramNode1 == paramNode2)
      return;
    Object localObject;
    switch (paramNode1.getNodeType())
    {
    case 1:
      paramSet.add(paramNode1);
      Element localElement;
      if (!(localElement = (Element)paramNode1).hasAttributes())
        break;
      localObject = ((Element)paramNode1).getAttributes();
      for (int i = 0; i < ((NamedNodeMap)localObject).getLength(); i++)
        paramSet.add(((NamedNodeMap)localObject).item(i));
    case 9:
      if (((Node)localObject).getNodeType() == 3)
      {
        paramSet.add(localObject);
        while ((localObject != null) && (((Node)localObject).getNodeType() == 3))
          localObject = ((Node)localObject).getNextSibling();
        if (localObject == null)
          return;
      }
      getSetRec((Node)localObject, paramSet, paramNode2, paramBoolean);
      return;
    case 8:
      if (paramBoolean)
        paramSet.add(paramNode1);
      return;
    case 10:
      return;
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    paramSet.add(paramNode1);
  }

  public static String getXalanVersion()
  {
    String str;
    if ((str = getXalan1Version()) != null)
      return str;
    if ((str = getXalan20Version()) != null)
      return str;
    if ((str = getXalan2Version()) != null)
      return str;
    return "Apache Xalan not installed";
  }

  public static String getXercesVersion()
  {
    String str;
    if ((str = getXerces1Version()) != null)
      return str;
    if ((str = getXerces2Version()) != null)
      return str;
    return "Apache Xerces not installed";
  }

  private static String getXalan1Version()
  {
    try
    {
      Object localObject = null;
      Class localClass = classForName("org.apache.xalan.xslt.XSLProcessorVersion");
      StringBuffer localStringBuffer = new StringBuffer();
      Field localField = localClass.getField("PRODUCT");
      localStringBuffer.append(localField.get(null));
      localStringBuffer.append(';');
      localField = localClass.getField("LANGUAGE");
      localStringBuffer.append(localField.get(null));
      localStringBuffer.append(';');
      localField = localClass.getField("S_VERSION");
      localStringBuffer.append(localField.get(null));
      localStringBuffer.append(';');
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static String getXalan20Version()
  {
    try
    {
      Object localObject1 = null;
      Class[] arrayOfClass = new Class[0];
      Class localClass;
      Method localMethod;
      Object localObject2;
      return (String)(localObject2 = (localMethod = (localClass = classForName("org.apache.xalan.Version")).getMethod("getVersion", arrayOfClass)).invoke(null, new Object[0]));
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static String getXalan2Version()
  {
    try
    {
      Object localObject = null;
      Class localClass = classForName("org.apache.xalan.processor.XSLProcessorVersion");
      StringBuffer localStringBuffer = new StringBuffer();
      Field localField = localClass.getField("S_VERSION");
      localStringBuffer.append(localField.get(null));
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static String getXerces1Version()
  {
    try
    {
      Object localObject = null;
      Class localClass;
      Field localField;
      String str;
      return str = (String)(localField = (localClass = classForName("org.apache.xerces.framework.Version")).getField("fVersion")).get(null);
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static String getXerces2Version()
  {
    try
    {
      Object localObject = null;
      Class localClass;
      Field localField;
      String str;
      return str = (String)(localField = (localClass = classForName("org.apache.xerces.impl.Version")).getField("fVersion")).get(null);
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static Class classForName(String paramString)
    throws ClassNotFoundException
  {
    ClassLoader localClassLoader;
    if ((localClassLoader = findClassLoader()) == null)
      return Class.forName(paramString);
    return localClassLoader.loadClass(paramString);
  }

  protected static ClassLoader findClassLoader()
  {
    Method localMethod = null;
    try
    {
      localMethod = Thread.class.getMethod("getContextClassLoader", new Class[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      return XMLUtils.class.getClassLoader();
    }
    try
    {
      return (ClassLoader)localMethod.invoke(Thread.currentThread(), new Object[0]);
    }
    catch (Exception localException)
    {
    }
    throw new RuntimeException(localException.toString());
  }

  public static void spitOutVersions(Log paramLog)
  {
    if (paramLog.isDebugEnabled())
    {
      paramLog.debug(getXercesVersion());
      paramLog.debug(getXalanVersion());
    }
  }

  public static String getNodeTypeString(short paramShort)
  {
    if ((paramShort > 0) && (paramShort < 13))
      return nodeTypeString[paramShort];
    return "";
  }

  public static void outputDOM(Node paramNode, OutputStream paramOutputStream)
  {
    outputDOM(paramNode, paramOutputStream, false);
  }

  public static void outputDOM(Node paramNode, OutputStream paramOutputStream, boolean paramBoolean)
  {
    try
    {
      if (paramBoolean)
        paramOutputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
      paramOutputStream.write(Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments").canonicalizeSubtree(paramNode));
      return;
    }
    catch (IOException localIOException)
    {
      return;
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      (localObject = localInvalidCanonicalizerException).printStackTrace();
      return;
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      Object localObject;
      (localObject = localCanonicalizationException).printStackTrace();
    }
  }

  public static void outputDOMc14nWithComments(Node paramNode, OutputStream paramOutputStream)
  {
    try
    {
      paramOutputStream.write(Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments").canonicalizeSubtree(paramNode));
      return;
    }
    catch (IOException localIOException)
    {
      return;
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
      return;
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
    }
  }

  public static String getFullTextChildrenFromElement(Element paramElement)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    NodeList localNodeList;
    int i = (localNodeList = paramElement.getChildNodes()).getLength();
    for (int j = 0; j < i; j++)
    {
      Node localNode;
      if ((localNode = localNodeList.item(j)).getNodeType() != 3)
        continue;
      localStringBuffer.append(((Text)localNode).getData());
    }
    return localStringBuffer.toString();
  }

  public static Element createElementInSignatureSpace(Document paramDocument, String paramString)
  {
    if (paramDocument == null)
      throw new RuntimeException("Document is null");
    String str;
    Element localElement;
    if (((str = Constants.getSignatureSpecNSprefix()) == null) || (str.length() == 0))
    {
      (localElement = paramDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", paramString)).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
      return localElement;
    }
    (localElement = paramDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", str + ":" + paramString)).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2000/09/xmldsig#");
    return localElement;
  }

  public static boolean elementIsInSignatureSpace(Element paramElement, String paramString)
  {
    if ((paramElement == null) || (!"http://www.w3.org/2000/09/xmldsig#".equals(paramElement.getNamespaceURI())))
      return false;
    return paramElement.getLocalName().equals(paramString);
  }

  public static boolean elementIsInEncryptionSpace(Element paramElement, String paramString)
  {
    if ((paramElement == null) || (!"http://www.w3.org/2001/04/xmlenc#".equals(paramElement.getNamespaceURI())))
      return false;
    return paramElement.getLocalName().equals(paramString);
  }

  public static Document getOwnerDocument(Node paramNode)
  {
    if (paramNode.getNodeType() == 9)
      return (Document)paramNode;
    try
    {
      return paramNode.getOwnerDocument();
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localNullPointerException.getMessage() + "\"");
  }

  public static Document getOwnerDocument(Set paramSet)
  {
    NullPointerException localNullPointerException1 = null;
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Node localNode;
      int i;
      if ((i = (localNode = (Node)localIterator.next()).getNodeType()) == 9)
        return (Document)localNode;
      try
      {
        if (i == 2)
          return ((Attr)localNode).getOwnerElement().getOwnerDocument();
        return localNode.getOwnerDocument();
      }
      catch (NullPointerException localNullPointerException3)
      {
        NullPointerException localNullPointerException2;
        localNullPointerException1 = localNullPointerException2 = localNullPointerException3;
      }
    }
    throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + (localNullPointerException1 == null ? "" : localNullPointerException1.getMessage()) + "\"");
  }

  public static Element createDSctx(Document paramDocument, String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString1.trim().length() == 0))
      throw new IllegalArgumentException("You must supply a prefix");
    Element localElement;
    (localElement = paramDocument.createElementNS(null, "namespaceContext")).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + paramString1.trim(), paramString2);
    return localElement;
  }

  public static void addReturnToElement(Element paramElement)
  {
    Document localDocument = paramElement.getOwnerDocument();
    paramElement.appendChild(localDocument.createTextNode("\n"));
  }

  public static Set convertNodelistToSet(NodeList paramNodeList)
  {
    if (paramNodeList == null)
      return new HashSet();
    int i = paramNodeList.getLength();
    HashSet localHashSet = new HashSet(i);
    for (int j = 0; j < i; j++)
      localHashSet.add(paramNodeList.item(j));
    return localHashSet;
  }

  public static void circumventBug2650(Document paramDocument)
  {
    Element localElement;
    Attr localAttr;
    if ((localAttr = (localElement = paramDocument.getDocumentElement()).getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns")) == null)
      localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
    circumventBug2650recurse(paramDocument);
  }

  private static void circumventBug2650recurse(Node paramNode)
  {
    Object localObject;
    Node localNode;
    if ((paramNode.getNodeType() == 1) && ((localObject = (Element)paramNode).hasChildNodes()) && (((Element)localObject).hasAttributes()))
    {
      NamedNodeMap localNamedNodeMap;
      int i = (localNamedNodeMap = ((Element)localObject).getAttributes()).getLength();
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        for (int j = 0; j < i; j++)
        {
          Attr localAttr = (Attr)localNamedNodeMap.item(j);
          if ((!"http://www.w3.org/2000/xmlns/".equals(localAttr.getNamespaceURI())) || (localElement.hasAttributeNS("http://www.w3.org/2000/xmlns/", localAttr.getLocalName())))
            continue;
          localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", localAttr.getName(), localAttr.getNodeValue());
        }
      }
    }
    switch (((Node)localObject).getNodeType())
    {
    case 1:
    case 5:
    case 9:
      circumventBug2650recurse((Node)localObject);
    }
  }

  public static Element selectDsNode(Node paramNode, String paramString, int paramInt)
  {
    while (paramNode != null)
    {
      if ((paramString.equals(paramNode.getLocalName())) && ("http://www.w3.org/2000/09/xmldsig#".equals(paramNode.getNamespaceURI())))
      {
        if (paramInt == 0)
          return (Element)paramNode;
        paramInt--;
      }
      paramNode = paramNode.getNextSibling();
    }
    return null;
  }

  public static Element selectXencNode(Node paramNode, String paramString, int paramInt)
  {
    while (paramNode != null)
    {
      if ((paramString.equals(paramNode.getLocalName())) && ("http://www.w3.org/2001/04/xmlenc#".equals(paramNode.getNamespaceURI())))
      {
        if (paramInt == 0)
          return (Element)paramNode;
        paramInt--;
      }
      paramNode = paramNode.getNextSibling();
    }
    return null;
  }

  public static Text selectDsNodeText(Node paramNode, String paramString, int paramInt)
  {
    Object localObject;
    if ((localObject = selectDsNode(paramNode, paramString, paramInt)) == null)
      return null;
    return (Text)(Text)localObject;
  }

  public static Text selectNodeText(Node paramNode, String paramString1, String paramString2, int paramInt)
  {
    Object localObject;
    if ((localObject = selectNode(paramNode, paramString1, paramString2, paramInt)) == null)
      return null;
    return (Text)(Text)localObject;
  }

  public static Element selectNode(Node paramNode, String paramString1, String paramString2, int paramInt)
  {
    while (paramNode != null)
    {
      if ((paramString2.equals(paramNode.getLocalName())) && (paramString1.equals(paramNode.getNamespaceURI())))
      {
        if (paramInt == 0)
          return (Element)paramNode;
        paramInt--;
      }
      paramNode = paramNode.getNextSibling();
    }
    return null;
  }

  public static Element[] selectDsNodes(Node paramNode, String paramString)
  {
    return selectNodes(paramNode, "http://www.w3.org/2000/09/xmldsig#", paramString);
  }

  public static Element[] selectNodes(Node paramNode, String paramString1, String paramString2)
  {
    int i = 20;
    Object localObject = new Element[20];
    int j = 0;
    while (paramNode != null)
    {
      if ((paramString2.equals(paramNode.getLocalName())) && (paramString1.equals(paramNode.getNamespaceURI())))
      {
        localObject[(j++)] = ((Element)paramNode);
        if (i <= j)
        {
          int k;
          Element[] arrayOfElement2 = new Element[k = i << 2];
          System.arraycopy(localObject, 0, arrayOfElement2, 0, i);
          localObject = arrayOfElement2;
          i = k;
        }
      }
      paramNode = paramNode.getNextSibling();
    }
    Element[] arrayOfElement1 = new Element[j];
    System.arraycopy(localObject, 0, arrayOfElement1, 0, j);
    return (Element)arrayOfElement1;
  }

  public static Set excludeNodeFromSet(Node paramNode, Set paramSet)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Node localNode = (Node)localIterator.next();
      if (!isDescendantOrSelf(paramNode, localNode))
        localHashSet.add(localNode);
    }
    return localHashSet;
  }

  static boolean isDescendantOrSelf(Node paramNode1, Node paramNode2)
  {
    if (paramNode1 == paramNode2)
      return true;
    while (true)
    {
      Node localNode;
      if ((localNode = paramNode2) == null)
        return false;
      if (localNode == paramNode1)
        return true;
      tmpTernaryOp = localNode.getParentNode();
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.XMLUtils
 * JD-Core Version:    0.6.0
 */