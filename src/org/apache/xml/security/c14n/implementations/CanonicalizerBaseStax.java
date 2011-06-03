package org.apache.xml.security.c14n.implementations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.CanonicalizerSpi;
import org.apache.xml.security.c14n.helper.AttrCompareStax;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public abstract class CanonicalizerBaseStax extends CanonicalizerSpi
{
  private static final byte[] _END_PI = { 63, 62 };
  private static final byte[] _BEGIN_PI = { 60, 63 };
  private static final byte[] _END_COMM = { 45, 45, 62 };
  private static final byte[] _BEGIN_COMM = { 60, 33, 45, 45 };
  private static final byte[] __XA_ = { 38, 35, 120, 65, 59 };
  private static final byte[] __X9_ = { 38, 35, 120, 57, 59 };
  private static final byte[] _QUOT_ = { 38, 113, 117, 111, 116, 59 };
  private static final byte[] __XD_ = { 38, 35, 120, 68, 59 };
  private static final byte[] _GT_ = { 38, 103, 116, 59 };
  private static final byte[] _LT_ = { 38, 108, 116, 59 };
  private static final byte[] _END_TAG = { 60, 47 };
  private static final byte[] _AMP_ = { 38, 97, 109, 112, 59 };
  static final AttrCompareStax COMPARE = new AttrCompareStax();
  static final String XML = "xml";
  static final String XMLNS = "xmlns";
  static final byte[] equalsStr = { 61, 34 };
  static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
  static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
  static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
  protected static final Attr nullNode;
  boolean _mProcessFirstNext;
  boolean _includeComments;
  Set _xpathNodeSet = null;
  Node _excludeNode = null;
  OutputStream _writer = new ByteArrayOutputStream();
  int PositionRelativeToDocumentElement;
  int documentPosition = 0;

  public void setProcessFirstNext(boolean paramBoolean)
  {
    this._mProcessFirstNext = paramBoolean;
  }

  public CanonicalizerBaseStax(boolean paramBoolean)
  {
    this._includeComments = paramBoolean;
  }

  public byte[] engineCanonicalizeSubTree(XMLStreamReader paramXMLStreamReader)
    throws CanonicalizationException
  {
    return engineCanonicalizeSubTree(paramXMLStreamReader, (Node)null);
  }

  public byte[] engineCanonicalizeSubTree(XMLStreamReader paramXMLStreamReader, Node paramNode)
    throws CanonicalizationException
  {
    this._excludeNode = paramNode;
    try
    {
      NameSpaceSymbTableStax localNameSpaceSymbTableStax = new NameSpaceSymbTableStax();
      canonicalizeSubTree(paramXMLStreamReader, localNameSpaceSymbTableStax);
      this._writer.close();
      if ((this._writer instanceof ByteArrayOutputStream))
      {
        byte[] arrayOfByte = ((ByteArrayOutputStream)this._writer).toByteArray();
        if (this.reset)
          ((ByteArrayOutputStream)this._writer).reset();
        return arrayOfByte;
      }
      return null;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new CanonicalizationException("empty", localUnsupportedEncodingException);
    }
    catch (IOException localIOException)
    {
      throw new CanonicalizationException("empty", localIOException);
    }
    catch (XMLStreamException localXMLStreamException)
    {
    }
    throw new CanonicalizationException("empty", localXMLStreamException);
  }

  public void writeStartElement(String paramString1, String paramString2, String paramString3, Iterator paramIterator, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws IOException, CanonicalizationException
  {
    OutputStream localOutputStream = this._writer;
    paramNameSpaceSymbTableStax.outputNodePush();
    localOutputStream.write(60);
    String str;
    writeStringToUtf8(str = paramString1 + ":" + paramString2, localOutputStream);
    Iterator localIterator;
    if ((localIterator = handleAttributesSubtree(paramIterator, new AttrStream((paramString1 == null) || (paramString1 == "") ? null : "xmlns", (paramString1 == null) || (paramString1 == "") ? "xmlns" : paramString1, "http://www.w3.org/2000/xmlns/", paramString3), paramNameSpaceSymbTableStax)) != null)
      while (localIterator.hasNext())
      {
        AttrStream localAttrStream;
        outputAttrToWriter((localAttrStream = (AttrStream)localIterator.next()).getNodeName(), localAttrStream.getNodeValue(), localOutputStream);
      }
    localOutputStream.write(62);
  }

  public void writeComment(String paramString)
    throws IOException
  {
    if (this._includeComments)
      outputCommentToWriter(paramString, this._writer);
  }

  public void writeCharacters(String paramString)
    throws IOException
  {
    outputTextToWriter(paramString, this._writer);
  }

  public void writeEndElement(String paramString1, String paramString2, String paramString3, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws IOException
  {
    OutputStream localOutputStream = this._writer;
    String str = paramString1 + ":" + paramString2;
    localOutputStream.write(_END_TAG);
    writeStringToUtf8(str, localOutputStream);
    localOutputStream.write(62);
    paramNameSpaceSymbTableStax.outputNodePop();
  }

  public void writeProcessingInstruction(String paramString1, String paramString2)
    throws IOException
  {
    outputPItoWriter(paramString1, paramString2, this._writer);
  }

  public byte[] getStreamResult()
    throws IOException
  {
    this._writer.close();
    if ((this._writer instanceof ByteArrayOutputStream))
    {
      byte[] arrayOfByte = ((ByteArrayOutputStream)this._writer).toByteArray();
      if (this.reset)
        ((ByteArrayOutputStream)this._writer).reset();
      return arrayOfByte;
    }
    return null;
  }

  final void canonicalizeSubTree(XMLStreamReader paramXMLStreamReader, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws CanonicalizationException, IOException, XMLStreamException
  {
    switch (paramXMLStreamReader.getEventType())
    {
    case 10:
    case 15:
      throw new CanonicalizationException("empty");
    case 7:
      this.documentPosition = -1;
    case 5:
    case 3:
    case 4:
    case 12:
    case 1:
      while (paramXMLStreamReader.next() != 8)
      {
        if (this.documentPosition == 0)
          this.documentPosition = (paramXMLStreamReader.getEventType() == 1 ? 0 : 1);
        canonicalizeSubTree(paramXMLStreamReader, paramNameSpaceSymbTableStax);
        tmpTernaryOp = this;
        continue;
        if (!this._includeComments)
          break;
        outputCommentToWriter(paramXMLStreamReader.getText(), this._writer);
        return;
        outputPItoWriter(paramXMLStreamReader.getPITarget(), paramXMLStreamReader.getPIData(), this._writer);
        return;
        outputTextToWriter(paramXMLStreamReader.getText(), this._writer);
        return;
        OutputStream localOutputStream = this._writer;
        paramNameSpaceSymbTableStax.outputNodePush();
        localOutputStream.write(60);
        String str;
        writeStringToUtf8(str = paramXMLStreamReader.getPrefix() + ":" + paramXMLStreamReader.getLocalName(), localOutputStream);
        Iterator localIterator;
        if ((localIterator = handleAttributesSubtree(paramXMLStreamReader, paramNameSpaceSymbTableStax)) != null)
          while (localIterator.hasNext())
          {
            AttrStream localAttrStream;
            outputAttrToWriter((localAttrStream = (AttrStream)localIterator.next()).getNodeName(), localAttrStream.getNodeValue(), localOutputStream);
          }
        localOutputStream.write(62);
        while (paramXMLStreamReader.next() != 2)
          canonicalizeSubTree(paramXMLStreamReader, paramNameSpaceSymbTableStax);
        localOutputStream.write(_END_TAG);
        writeStringToUtf8(str, localOutputStream);
        localOutputStream.write(62);
        paramNameSpaceSymbTableStax.outputNodePop();
      }
    case 2:
    case 6:
    case 8:
    case 9:
    case 11:
    case 13:
    case 14:
    }
  }

  final int getPositionRelativeToDocumentElement(XMLStreamReader paramXMLStreamReader)
  {
    return this.documentPosition;
  }

  public byte[] engineCanonicalizeXPathNodeSet(Set paramSet)
    throws CanonicalizationException
  {
    if (paramSet.size() == 0)
      return new byte[0];
    this._xpathNodeSet = paramSet;
    try
    {
      Document localDocument = XMLUtils.getOwnerDocument(this._xpathNodeSet);
      canonicalizeXPathNodeSet(localDocument, new NameSpaceSymbTable());
      this._writer.close();
      if ((this._writer instanceof ByteArrayOutputStream))
      {
        byte[] arrayOfByte = ((ByteArrayOutputStream)this._writer).toByteArray();
        if (this.reset)
          ((ByteArrayOutputStream)this._writer).reset();
        return arrayOfByte;
      }
      return null;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new CanonicalizationException("empty", localUnsupportedEncodingException);
    }
    catch (IOException localIOException)
    {
    }
    throw new CanonicalizationException("empty", localIOException);
  }

  final void canonicalizeXPathNodeSet(Node paramNode, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException, IOException
  {
    boolean bool = this._xpathNodeSet.contains(paramNode);
    Object localObject1;
    switch (paramNode.getNodeType())
    {
    case 5:
    case 10:
    default:
      return;
    case 2:
    case 6:
    case 11:
    case 12:
      throw new CanonicalizationException("empty");
    case 9:
      canonicalizeXPathNodeSet((Node)localObject1, paramNameSpaceSymbTable);
      return;
    case 8:
      if ((!bool) || (!this._includeComments))
        break;
      outputCommentToWriter(((Comment)paramNode).getData(), this._writer);
      return;
    case 7:
      if (!bool)
        break;
      outputPItoWriter(((ProcessingInstruction)paramNode).getTarget(), ((ProcessingInstruction)paramNode).getData(), this._writer);
      return;
    case 3:
    case 4:
      if (!bool)
        break;
      outputTextToWriter(paramNode.getNodeValue(), this._writer);
      outputTextToWriter(((Node)localObject1).getNodeValue(), this._writer);
      return;
    case 1:
      localObject1 = (Element)paramNode;
      OutputStream localOutputStream = this._writer;
      String str = ((Element)localObject1).getTagName();
      if (bool)
      {
        paramNameSpaceSymbTable.outputNodePush();
        localOutputStream.write(60);
        writeStringToUtf8(str, localOutputStream);
      }
      else
      {
        paramNameSpaceSymbTable.push();
      }
      Iterator localIterator = handleAttributes((Element)localObject1, paramNameSpaceSymbTable);
      Object localObject2;
      while (localIterator.hasNext())
        outputAttrToWriter((localObject2 = (Attr)localIterator.next()).getNodeName(), ((Attr)localObject2).getNodeValue(), localOutputStream);
      if (bool)
        localOutputStream.write(62);
      canonicalizeXPathNodeSet((Node)localObject2, paramNameSpaceSymbTable);
      if (bool)
      {
        localOutputStream.write(_END_TAG);
        writeStringToUtf8(str, localOutputStream);
        localOutputStream.write(62);
        paramNameSpaceSymbTable.outputNodePop();
        return;
      }
      paramNameSpaceSymbTable.pop();
    }
  }

  static final void getParentNameSpaces(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
  {
    ArrayList localArrayList = new ArrayList();
    Node localNode;
    if (!((localNode = paramElement.getParentNode()) instanceof Element))
      return;
    Element localElement;
    if ((localElement = (Element)paramElement.getParentNode()) != null)
      localArrayList.add(localElement);
    Object localObject1 = localArrayList.listIterator(localArrayList.size());
    Object localObject2;
    while (((ListIterator)localObject1).hasPrevious())
    {
      if (!(localObject2 = (Element)((ListIterator)localObject1).previous()).hasAttributes())
        continue;
      NamedNodeMap localNamedNodeMap;
      int i = (localNamedNodeMap = ((Element)localObject2).getAttributes()).getLength();
      for (int j = 0; j < i; j++)
      {
        Attr localAttr = (Attr)localNamedNodeMap.item(j);
        if (!"http://www.w3.org/2000/xmlns/".equals(localAttr.getNamespaceURI()))
          continue;
        String str1 = localAttr.getLocalName();
        String str2 = localAttr.getNodeValue();
        if (("xml".equals(str1)) && ("http://www.w3.org/XML/1998/namespace".equals(str2)))
          continue;
        paramNameSpaceSymbTable.addMapping(str1, str2, localAttr);
      }
    }
    if (((localObject2 = paramNameSpaceSymbTable.getMappingWithoutRendered("xmlns")) != null) && ("".equals(((Attr)localObject2).getValue())))
      paramNameSpaceSymbTable.addMappingAndRender("xmlns", "", nullNode);
  }

  static final void outputAttrToWriter(String paramString1, String paramString2, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(32);
    writeStringToUtf8(paramString1, paramOutputStream);
    paramOutputStream.write(equalsStr);
    int i = paramString2.length();
    for (int j = 0; j < i; j++)
    {
      char c;
      byte[] arrayOfByte;
      switch (c = paramString2.charAt(j))
      {
      case '&':
        arrayOfByte = _AMP_;
        break;
      case '<':
        arrayOfByte = _LT_;
        break;
      case '"':
        arrayOfByte = _QUOT_;
        break;
      case '\t':
        arrayOfByte = __X9_;
        break;
      case '\n':
        arrayOfByte = __XA_;
        break;
      case '\r':
        arrayOfByte = __XD_;
        break;
      default:
        writeCharToUtf8(c, paramOutputStream);
        break;
      }
      paramOutputStream.write(arrayOfByte);
    }
    paramOutputStream.write(34);
  }

  static final void writeCharToUtf8(char paramChar, OutputStream paramOutputStream)
    throws IOException
  {
    if (paramChar <= '')
    {
      paramOutputStream.write(paramChar);
      return;
    }
    int i;
    if (paramChar > '߿')
    {
      paramOutputStream.write((i = (char)(paramChar >>> '\f')) > 0 ? 0xE0 | i & 0xF : 224);
      paramOutputStream.write(0x80 | paramChar >>> '\006' & 0x3F);
      paramOutputStream.write(0x80 | paramChar & 0x3F);
      return;
    }
    paramOutputStream.write((i = (char)(paramChar >>> '\006')) > 0 ? 0xC0 | i & 0x1F : 192);
    paramOutputStream.write(0x80 | paramChar & 0x3F);
  }

  static final void writeStringToUtf8(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    int i = paramString.length();
    int j = 0;
    int m;
    while ((m = paramString.charAt(j++)) <= '')
      paramOutputStream.write(m);
    int k;
    paramOutputStream.write((k = (char)(m >>> 12)) > 0 ? 0xE0 | k & 0xF : 224);
    paramOutputStream.write(0x80 | m >>> 6 & 0x3F);
    paramOutputStream.write(0x80 | m & 0x3F);
    paramOutputStream.write((k = (char)(m >>> 6)) > 0 ? 0xC0 | k & 0x1F : 192);
    paramOutputStream.write(0x80 | m & 0x3F);
  }

  final void outputPItoWriter(String paramString1, String paramString2, OutputStream paramOutputStream)
    throws IOException
  {
    int i;
    if ((i = getPositionRelativeToDocumentElement(null)) == 1)
      paramOutputStream.write(10);
    paramOutputStream.write(_BEGIN_PI);
    int j = paramString1.length();
    char c;
    for (int k = 0; k < j; k++)
      if ((c = paramString1.charAt(k)) == '\r')
        paramOutputStream.write(__XD_);
      else
        writeCharToUtf8(c, paramOutputStream);
    if ((j = paramString2.length()) > 0)
    {
      paramOutputStream.write(32);
      for (k = 0; k < j; k++)
        if ((c = paramString2.charAt(k)) == '\r')
          paramOutputStream.write(__XD_);
        else
          writeCharToUtf8(c, paramOutputStream);
    }
    paramOutputStream.write(_END_PI);
    if (i == -1)
      paramOutputStream.write(10);
  }

  final void outputCommentToWriter(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    int i;
    if ((i = getPositionRelativeToDocumentElement(null)) == 1)
      paramOutputStream.write(10);
    paramOutputStream.write(_BEGIN_COMM);
    int j = paramString.length();
    for (int k = 0; k < j; k++)
    {
      char c;
      if ((c = paramString.charAt(k)) == '\r')
        paramOutputStream.write(__XD_);
      else
        writeCharToUtf8(c, paramOutputStream);
    }
    paramOutputStream.write(_END_COMM);
    if (i == -1)
      paramOutputStream.write(10);
  }

  static final void outputTextToWriter(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c;
      byte[] arrayOfByte;
      switch (c = paramString.charAt(j))
      {
      case '&':
        arrayOfByte = _AMP_;
        break;
      case '<':
        arrayOfByte = _LT_;
        break;
      case '>':
        arrayOfByte = _GT_;
        break;
      case '\r':
        arrayOfByte = __XD_;
        break;
      default:
        writeCharToUtf8(c, paramOutputStream);
        break;
      }
      paramOutputStream.write(arrayOfByte);
    }
  }

  abstract Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
    throws CanonicalizationException;

  abstract Iterator handleAttributesSubtree(XMLStreamReader paramXMLStreamReader, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws CanonicalizationException;

  abstract Iterator handleAttributesSubtree(Iterator paramIterator, AttrStream paramAttrStream, NameSpaceSymbTableStax paramNameSpaceSymbTableStax)
    throws CanonicalizationException;

  public final boolean is_includeComments()
  {
    return this._includeComments;
  }

  public final void set_includeComments(boolean paramBoolean)
  {
    this._includeComments = paramBoolean;
  }

  public void setWriter(OutputStream paramOutputStream)
  {
    this._writer = paramOutputStream;
  }

  static
  {
    try
    {
      nullNode = new DocumentBuilderFactoryImpl().newDocumentBuilder().newDocument().createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns");
      nullNode.setValue("");
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException("Unable to create nullNode" + localException);
    }
  }

  public static class AttrIterator
    implements Iterator
  {
    private XMLStreamReader reader;
    private int i = -1;

    public AttrIterator(XMLStreamReader paramXMLStreamReader)
    {
      this.reader = paramXMLStreamReader;
    }

    public boolean hasNext()
    {
      return this.i + 1 < this.reader.getAttributeCount() + this.reader.getNamespaceCount();
    }

    public Object next()
    {
      this.i += 1;
      if (this.i < this.reader.getAttributeCount())
        return new AttrStream(this.reader.getAttributePrefix(this.i), this.reader.getAttributeLocalName(this.i), this.reader.getAttributeNamespace(this.i), this.reader.getAttributeValue(this.i));
      if (this.i < this.reader.getAttributeCount() + this.reader.getNamespaceCount())
      {
        int j = this.i - this.reader.getAttributeCount();
        return new AttrStream((this.reader.getNamespacePrefix(j) == null) || (this.reader.getNamespacePrefix(j) == "") ? null : "xmlns", (this.reader.getNamespacePrefix(j) == null) || (this.reader.getNamespacePrefix(j) == "") ? "xmlns" : this.reader.getNamespacePrefix(j), "http://www.w3.org/2000/xmlns/", this.reader.getNamespaceURI(j));
      }
      throw new NoSuchElementException("Element not found");
    }

    public void remove()
    {
      throw new UnsupportedOperationException("Readonly");
    }

    public void resetIterator()
    {
      this.i = -1;
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.CanonicalizerBaseStax
 * JD-Core Version:    0.6.0
 */