package org.apache.xml.security.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.helper.AttrCompare;
import org.apache.xml.security.c14n.implementations.Canonicalizer20010315OmitComments;
import org.apache.xml.security.utils.CachedXPathAPIHolder;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

public class XMLSignatureInput
{
  InputStream _inputOctetStreamProxy = null;
  Set _inputNodeSet = null;
  Node _subNode = null;
  Node excludeNode = null;
  boolean excludeComments = false;
  byte[] bytes = null;
  private String _MIMEType = null;
  private String _SourceURI = null;
  OutputStream outputStream = null;

  public XMLSignatureInput(byte[] paramArrayOfByte)
  {
    this.bytes = paramArrayOfByte;
  }

  public XMLSignatureInput(InputStream paramInputStream)
  {
    this._inputOctetStreamProxy = paramInputStream;
  }

  public XMLSignatureInput(String paramString)
  {
    this(paramString.getBytes());
  }

  public XMLSignatureInput(String paramString1, String paramString2)
    throws UnsupportedEncodingException
  {
    this(paramString1.getBytes(paramString2));
  }

  public XMLSignatureInput(Node paramNode)
  {
    this._subNode = paramNode;
  }

  public XMLSignatureInput(Set paramSet)
  {
    this._inputNodeSet = paramSet;
  }

  public XMLSignatureInput(NodeList paramNodeList)
  {
    this(XMLUtils.convertNodelistToSet(paramNodeList));
  }

  public Set getNodeSet()
    throws CanonicalizationException, ParserConfigurationException, IOException, SAXException
  {
    return getNodeSet(false);
  }

  public Set getNodeSet(boolean paramBoolean)
    throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
  {
    if (this._inputNodeSet != null)
      return this._inputNodeSet;
    if (isElement())
    {
      if (paramBoolean)
        XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(this._subNode));
      this._inputNodeSet = new HashSet();
      XMLUtils.getSet(this._subNode, this._inputNodeSet, this.excludeNode, this.excludeComments);
      return this._inputNodeSet;
    }
    if (isOctetStream())
    {
      DocumentBuilderFactory localDocumentBuilderFactory;
      (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setValidating(false);
      localDocumentBuilderFactory.setNamespaceAware(true);
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      try
      {
        localDocumentBuilder.setErrorHandler(new IgnoreAllErrorHandler());
        Document localDocument1;
        XMLUtils.circumventBug2650(localDocument1 = localDocumentBuilder.parse(getOctetStream()));
        localObject = new HashSet();
        XMLUtils.getSet(localDocument1.getDocumentElement(), (Set)localObject, null, false);
        return localObject;
      }
      catch (SAXException localSAXException)
      {
        Object localObject;
        (localObject = new ByteArrayOutputStream()).write("<container>".getBytes());
        ((ByteArrayOutputStream)localObject).write(getBytes());
        ((ByteArrayOutputStream)localObject).write("</container>".getBytes());
        byte[] arrayOfByte = ((ByteArrayOutputStream)localObject).toByteArray();
        Document localDocument2 = localDocumentBuilder.parse(new ByteArrayInputStream(arrayOfByte));
        try
        {
          NodeList localNodeList;
          return XMLUtils.convertNodelistToSet(localNodeList = CachedXPathAPIHolder.getCachedXPathAPI().selectNodeList(localDocument2, "(//. | //@* | //namespace::*)[not(self::node()=/) and not(self::node=/container)]"));
        }
        catch (TransformerException localTransformerException)
        {
          throw new CanonicalizationException("generic.EmptyMessage", localTransformerException);
        }
      }
    }
    throw new RuntimeException("getNodeSet() called but no input data present");
  }

  public InputStream getOctetStream()
    throws IOException, CanonicalizationException
  {
    return getResetableInputStream();
  }

  public InputStream getOctetStreamReal()
  {
    return this._inputOctetStreamProxy;
  }

  public byte[] getBytes()
    throws IOException, CanonicalizationException
  {
    if (this.bytes != null)
      return this.bytes;
    InputStream localInputStream;
    if ((localInputStream = getResetableInputStream()) != null)
    {
      if (this.bytes == null)
      {
        localInputStream.reset();
        this.bytes = JavaUtils.getBytesFromStream(localInputStream);
      }
      return this.bytes;
    }
    Canonicalizer20010315OmitComments localCanonicalizer20010315OmitComments;
    if (isElement())
    {
      localCanonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
      this.bytes = localCanonicalizer20010315OmitComments.engineCanonicalizeSubTree(this._subNode, this.excludeNode);
      return this.bytes;
    }
    if (isNodeSet())
    {
      localCanonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
      if (this._inputNodeSet.size() == 0)
        return null;
      this.bytes = localCanonicalizer20010315OmitComments.engineCanonicalizeXPathNodeSet(this._inputNodeSet);
      return this.bytes;
    }
    throw new RuntimeException("getBytes() called but no input data present");
  }

  public boolean isNodeSet()
  {
    return (this._inputOctetStreamProxy == null) && (this._inputNodeSet != null);
  }

  public boolean isElement()
  {
    return (this._inputOctetStreamProxy == null) && (this._subNode != null) && (this._inputNodeSet == null);
  }

  public boolean isOctetStream()
  {
    return ((this._inputOctetStreamProxy != null) || (this.bytes != null)) && (this._inputNodeSet == null) && (this._subNode == null);
  }

  public boolean isByteArray()
  {
    return (this.bytes != null) && (this._inputNodeSet == null) && (this._subNode == null);
  }

  public boolean isInitialized()
  {
    return (isOctetStream()) || (isNodeSet());
  }

  public String getMIMEType()
  {
    return this._MIMEType;
  }

  public void setMIMEType(String paramString)
  {
    this._MIMEType = paramString;
  }

  public String getSourceURI()
  {
    return this._SourceURI;
  }

  public void setSourceURI(String paramString)
  {
    this._SourceURI = paramString;
  }

  public String toString()
  {
    if (isNodeSet())
      return "XMLSignatureInput/NodeSet/" + this._inputNodeSet.size() + " nodes/" + getSourceURI();
    if (isElement())
      return "XMLSignatureInput/Element/" + this._subNode + " exclude " + this.excludeNode + " comments:" + this.excludeComments + "/" + getSourceURI();
    try
    {
      return "XMLSignatureInput/OctetStream/" + getBytes().length + " octets/" + getSourceURI();
    }
    catch (Exception localException)
    {
    }
    return "XMLSignatureInput/OctetStream//" + getSourceURI();
  }

  public String getHTMLRepresentation()
    throws XMLSignatureException
  {
    XMLSignatureInputDebugger localXMLSignatureInputDebugger;
    return (localXMLSignatureInputDebugger = new XMLSignatureInputDebugger(this)).getHTMLRepresentation();
  }

  public String getHTMLRepresentation(Set paramSet)
    throws XMLSignatureException
  {
    XMLSignatureInputDebugger localXMLSignatureInputDebugger;
    return (localXMLSignatureInputDebugger = new XMLSignatureInputDebugger(this, paramSet)).getHTMLRepresentation();
  }

  public Node getExcludeNode()
  {
    return this.excludeNode;
  }

  public void setExcludeNode(Node paramNode)
  {
    this.excludeNode = paramNode;
  }

  public Node getSubNode()
  {
    return this._subNode;
  }

  public boolean isExcludeComments()
  {
    return this.excludeComments;
  }

  public void setExcludeComments(boolean paramBoolean)
  {
    this.excludeComments = paramBoolean;
  }

  public void updateOutputStream(OutputStream paramOutputStream)
    throws CanonicalizationException, IOException
  {
    if (paramOutputStream == this.outputStream)
      return;
    if (this.bytes != null)
    {
      paramOutputStream.write(this.bytes);
      return;
    }
    if (isElement())
    {
      (localObject = new Canonicalizer20010315OmitComments()).setWriter(paramOutputStream);
      ((Canonicalizer20010315OmitComments)localObject).engineCanonicalizeSubTree(this._subNode, this.excludeNode);
      return;
    }
    if (isNodeSet())
    {
      (localObject = new Canonicalizer20010315OmitComments()).setWriter(paramOutputStream);
      if (this._inputNodeSet.size() == 0)
        return;
      ((Canonicalizer20010315OmitComments)localObject).engineCanonicalizeXPathNodeSet(this._inputNodeSet);
      return;
    }
    Object localObject = getResetableInputStream();
    if (this.bytes != null)
    {
      paramOutputStream.write(this.bytes, 0, this.bytes.length);
      return;
    }
    ((InputStream)localObject).reset();
    byte[] arrayOfByte = new byte[1024];
    int i;
    while ((i = ((InputStream)localObject).read(arrayOfByte)) > 0)
      paramOutputStream.write(arrayOfByte, 0, i);
  }

  public void setOutputStream(OutputStream paramOutputStream)
  {
    this.outputStream = paramOutputStream;
  }

  protected InputStream getResetableInputStream()
    throws IOException
  {
    if ((this._inputOctetStreamProxy instanceof ByteArrayInputStream))
    {
      if (!this._inputOctetStreamProxy.markSupported())
        throw new RuntimeException("Accepted as Markable but not truly been" + this._inputOctetStreamProxy);
      return this._inputOctetStreamProxy;
    }
    if (this.bytes != null)
    {
      this._inputOctetStreamProxy = new ByteArrayInputStream(this.bytes);
      return this._inputOctetStreamProxy;
    }
    if (this._inputOctetStreamProxy == null)
      return null;
    if (this._inputOctetStreamProxy.markSupported())
      System.err.println("Mark Suported but not used as reset");
    this.bytes = JavaUtils.getBytesFromStream(this._inputOctetStreamProxy);
    this._inputOctetStreamProxy = new ByteArrayInputStream(this.bytes);
    return this._inputOctetStreamProxy;
  }

  public class XMLSignatureInputDebugger
  {
    private Set _xpathNodeSet;
    private Set _inclusiveNamespaces;
    private Document _doc;
    private Writer _writer;
    static final String HTMLPrefix = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n";
    static final String HTMLSuffix = "</pre></body></html>";
    static final String HTMLExcludePrefix = "<span class=\"EXCLUDED\">";
    static final String HTMLExcludeSuffix = "</span>";
    static final String HTMLIncludePrefix = "<span class=\"INCLUDED\">";
    static final String HTMLIncludeSuffix = "</span>";
    static final String HTMLIncludedInclusiveNamespacePrefix = "<span class=\"INCLUDEDINCLUSIVENAMESPACE\">";
    static final String HTMLIncludedInclusiveNamespaceSuffix = "</span>";
    static final String HTMLExcludedInclusiveNamespacePrefix = "<span class=\"EXCLUDEDINCLUSIVENAMESPACE\">";
    static final String HTMLExcludedInclusiveNamespaceSuffix = "</span>";
    private static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
    private static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
    private static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
    private final XMLSignatureInput this$0;

    private XMLSignatureInputDebugger()
    {
      this.this$0 = this$1;
      this._doc = null;
      this._writer = null;
    }

    public XMLSignatureInputDebugger(XMLSignatureInput arg2)
    {
      this.this$0 = this$1;
      this._doc = null;
      this._writer = null;
      Object localObject;
      this._xpathNodeSet = (!localObject.isNodeSet() ? null : localObject._inputNodeSet);
    }

    public XMLSignatureInputDebugger(XMLSignatureInput paramSet, Set arg3)
    {
      this(paramSet);
      Object localObject;
      this._inclusiveNamespaces = localObject;
    }

    public String getHTMLRepresentation()
      throws XMLSignatureException
    {
      if ((this._xpathNodeSet == null) || (this._xpathNodeSet.size() == 0))
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n<blink>no node set, sorry</blink></pre></body></html>";
      Object localObject1 = (Node)this._xpathNodeSet.iterator().next();
      this._doc = XMLUtils.getOwnerDocument((Node)localObject1);
      try
      {
        this._writer = new StringWriter();
        canonicalizeXPathNodeSet(this._doc);
        this._writer.close();
        localObject1 = this._writer.toString();
      }
      catch (IOException localIOException)
      {
        throw new XMLSignatureException("empty", localIOException);
      }
      finally
      {
        this._xpathNodeSet = null;
        this._doc = null;
        this._writer = null;
      }
    }

    private void canonicalizeXPathNodeSet(Node paramNode)
      throws XMLSignatureException, IOException
    {
      int i;
      int j;
      Object localObject;
      switch (i = paramNode.getNodeType())
      {
      case 5:
      case 10:
      default:
        return;
      case 2:
      case 6:
      case 11:
      case 12:
        throw new XMLSignatureException("empty");
      case 9:
        this._writer.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n");
        Node localNode1;
        canonicalizeXPathNodeSet(localNode1);
        tmpTernaryOp = "</pre></body></html>";
        break;
      case 8:
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        if ((j = getPositionRelativeToDocumentElement(paramNode)) == 1)
          this._writer.write("\n");
        outputCommentToWriter((Comment)paramNode);
        if (j == -1)
          this._writer.write("\n");
        tmpTernaryOp = this;
        break;
      case 7:
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        if ((j = getPositionRelativeToDocumentElement(paramNode)) == 1)
          this._writer.write("\n");
        outputPItoWriter((ProcessingInstruction)paramNode);
        if (j == -1)
          this._writer.write("\n");
        tmpTernaryOp = this;
        break;
      case 3:
      case 4:
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        outputTextToWriter(paramNode.getNodeValue());
        outputTextToWriter(((Node)localObject).getNodeValue());
        tmpTernaryOp = this;
        break;
      case 1:
        localObject = (Element)paramNode;
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        this._writer.write("&lt;");
        this._writer.write(((Element)localObject).getTagName());
        if (this._xpathNodeSet.contains(paramNode));
        this._writer.write("</span>");
        NamedNodeMap localNamedNodeMap;
        int k;
        Object[] arrayOfObject1 = new Object[k = (localNamedNodeMap = ((Element)localObject).getAttributes()).getLength()];
        int m = 0;
        arrayOfObject1[m] = localNamedNodeMap.item(m);
        m++;
        Arrays.sort(arrayOfObject1, new AttrCompare());
        Object[] arrayOfObject2 = arrayOfObject1;
        int n = 0;
        Attr localAttr = (Attr)arrayOfObject2[n];
        boolean bool1 = this._xpathNodeSet.contains(localAttr);
        boolean bool2 = this._inclusiveNamespaces.contains(localAttr.getName());
        this._writer.write(bool2 ? "<span class=\"EXCLUDEDINCLUSIVENAMESPACE\">" : bool1 ? "<span class=\"INCLUDED\">" : bool2 ? "<span class=\"INCLUDEDINCLUSIVENAMESPACE\">" : "<span class=\"EXCLUDED\">");
        outputAttrToWriter(localAttr.getNodeName(), localAttr.getNodeValue());
        if ((bool2 ? this._writer : bool2) != 0);
        (bool1 ? this : this)._writer.write("</span>");
        n++;
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        this._writer.write("&gt;");
        if (this._xpathNodeSet.contains(paramNode));
        this._writer.write("</span>");
        Node localNode2;
        canonicalizeXPathNodeSet(localNode2);
        this._writer.write(this._xpathNodeSet.contains(paramNode) ? "<span class=\"INCLUDED\">" : "<span class=\"EXCLUDED\">");
        this._writer.write("&lt;/");
        this._writer.write(((Element)localObject).getTagName());
        this._writer.write("&gt;");
        if ((this._xpathNodeSet.contains(paramNode) ? this._writer : this._xpathNodeSet.contains(paramNode) ? this._writer : this._xpathNodeSet.contains(paramNode) ? this._writer : this._xpathNodeSet.contains(paramNode)) == 0);
      }
      this._writer.write("</span>");
    }

    private int getPositionRelativeToDocumentElement(Node paramNode)
    {
      if (paramNode == null)
        return 0;
      Document localDocument = paramNode.getOwnerDocument();
      if (paramNode.getParentNode() != localDocument)
        return 0;
      Element localElement;
      if ((localElement = localDocument.getDocumentElement()) == null)
        return 0;
      if (localElement == paramNode)
        return 0;
      Node localNode;
      if (localNode == localElement)
        return -1;
      return 1;
    }

    private void outputAttrToWriter(String paramString1, String paramString2)
      throws IOException
    {
      this._writer.write(" ");
      this._writer.write(paramString1);
      this._writer.write("=\"");
      int i = paramString2.length();
      for (int j = 0; j < i; j++)
      {
        int k;
        switch (k = paramString2.charAt(j))
        {
        case '&':
          this._writer.write("&amp;amp;");
          break;
        case '<':
          this._writer.write("&amp;lt;");
          break;
        case '"':
          this._writer.write("&amp;quot;");
          break;
        case '\t':
          this._writer.write("&amp;#x9;");
          break;
        case '\n':
          this._writer.write("&amp;#xA;");
          break;
        case '\r':
          this._writer.write("&amp;#xD;");
          break;
        default:
          this._writer.write(k);
        }
      }
      this._writer.write("\"");
    }

    private void outputPItoWriter(ProcessingInstruction paramProcessingInstruction)
      throws IOException
    {
      if (paramProcessingInstruction == null)
        return;
      this._writer.write("&lt;?");
      String str1;
      int i = (str1 = paramProcessingInstruction.getTarget()).length();
      int k;
      for (int j = 0; j < i; j++)
        switch (k = str1.charAt(j))
        {
        case '\r':
          this._writer.write("&amp;#xD;");
          break;
        case ' ':
          this._writer.write("&middot;");
          break;
        case '\n':
          this._writer.write("&para;\n");
          break;
        default:
          this._writer.write(k);
        }
      String str2;
      i = (str2 = paramProcessingInstruction.getData()).length();
      if ((str2 != null) && (i > 0))
      {
        this._writer.write(" ");
        for (k = 0; k < i; k++)
        {
          int m;
          switch (m = str2.charAt(k))
          {
          case '\r':
            this._writer.write("&amp;#xD;");
            break;
          default:
            this._writer.write(m);
          }
        }
      }
      this._writer.write("?&gt;");
    }

    private void outputCommentToWriter(Comment paramComment)
      throws IOException
    {
      if (paramComment == null)
        return;
      this._writer.write("&lt;!--");
      String str;
      int i = (str = paramComment.getData()).length();
      for (int j = 0; j < i; j++)
      {
        int k;
        switch (k = str.charAt(j))
        {
        case '\r':
          this._writer.write("&amp;#xD;");
          break;
        case ' ':
          this._writer.write("&middot;");
          break;
        case '\n':
          this._writer.write("&para;\n");
          break;
        default:
          this._writer.write(k);
        }
      }
      this._writer.write("--&gt;");
    }

    private void outputTextToWriter(String paramString)
      throws IOException
    {
      if (paramString == null)
        return;
      int i = paramString.length();
      for (int j = 0; j < i; j++)
      {
        int k;
        switch (k = paramString.charAt(j))
        {
        case '&':
          this._writer.write("&amp;amp;");
          break;
        case '<':
          this._writer.write("&amp;lt;");
          break;
        case '>':
          this._writer.write("&amp;gt;");
          break;
        case '\r':
          this._writer.write("&amp;#xD;");
          break;
        case ' ':
          this._writer.write("&middot;");
          break;
        case '\n':
          this._writer.write("&para;\n");
          break;
        default:
          this._writer.write(k);
        }
      }
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.XMLSignatureInput
 * JD-Core Version:    0.6.0
 */