package org.apache.xml.security.utils;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Stax2DomBuilder
{
  protected boolean mCfgIgnoreWs = false;
  protected boolean mNsAware = true;
  protected boolean mProcessFirstNext = false;
  protected String mLastPrefix = null;
  protected String mLastLocalName = null;
  protected String mLastQName = null;

  public void setIgnoreWhitespace(boolean paramBoolean)
  {
    this.mCfgIgnoreWs = paramBoolean;
  }

  public void setProcessFirstNext(boolean paramBoolean)
  {
    this.mProcessFirstNext = paramBoolean;
  }

  public Document build(XMLStreamReader paramXMLStreamReader)
    throws ParserConfigurationException, XMLStreamException
  {
    return build(paramXMLStreamReader, DocumentBuilderFactory.newInstance().newDocumentBuilder());
  }

  public Document build(XMLStreamReader paramXMLStreamReader, DocumentBuilder paramDocumentBuilder)
    throws XMLStreamException
  {
    Document localDocument = paramDocumentBuilder.newDocument();
    build(paramXMLStreamReader, localDocument);
    return localDocument;
  }

  public void build(XMLStreamReader paramXMLStreamReader, Document paramDocument)
    throws XMLStreamException
  {
    buildTree(paramXMLStreamReader, paramDocument);
  }

  protected void buildTree(XMLStreamReader paramXMLStreamReader, Document paramDocument)
    throws XMLStreamException
  {
    checkReaderSettings(paramXMLStreamReader);
    Object localObject1 = paramDocument;
    while (true)
    {
      int i;
      if (this.mProcessFirstNext)
      {
        i = paramXMLStreamReader.next();
      }
      else
      {
        i = paramXMLStreamReader.getEventType();
        this.mProcessFirstNext = true;
      }
      Object localObject2;
      switch (i)
      {
      case 12:
        localObject2 = paramDocument.createCDATASection(paramXMLStreamReader.getText());
        break;
      case 6:
        if ((this.mCfgIgnoreWs) || (localObject1 == paramDocument))
          continue;
      case 4:
        String str1 = "";
        while (paramXMLStreamReader.getEventType() == 4)
        {
          str1 = str1 + paramXMLStreamReader.getText();
          paramXMLStreamReader.next();
        }
        localObject2 = paramDocument.createTextNode(str1);
        this.mProcessFirstNext = false;
        break;
      case 5:
        localObject2 = paramDocument.createComment(paramXMLStreamReader.getText());
        break;
      case 8:
        return;
      case 2:
        if (((localObject1 = ((Node)localObject1).getParentNode()) == null) || (localObject1 == paramDocument))
          return;
      case 14:
      case 15:
        break;
      case 9:
        localObject2 = paramDocument.createEntityReference(paramXMLStreamReader.getLocalName());
        break;
      case 3:
        localObject2 = paramDocument.createProcessingInstruction(paramXMLStreamReader.getPITarget(), paramXMLStreamReader.getPIData());
        break;
      case 1:
        String str2 = paramXMLStreamReader.getLocalName();
        String str3;
        Element localElement = paramDocument.createElementNS(paramXMLStreamReader.getNamespaceURI(), ((str3 = paramXMLStreamReader.getPrefix()) != null) && (str3.length() > 0) ? getQualified(str3, str2) : str2);
        localElement = this.mNsAware ? paramXMLStreamReader.getNamespaceURI() : paramDocument.createElement(str2);
        Object localObject3;
        if (this.mNsAware)
        {
          j = 0;
          k = paramXMLStreamReader.getNamespaceCount();
          (localObject3 = paramDocument.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str2)).setValue(paramXMLStreamReader.getNamespaceURI(j));
          localElement.setAttributeNode((Attr)localObject3);
          j++;
        }
        int j = 0;
        int k = paramXMLStreamReader.getAttributeCount();
        str2 = paramXMLStreamReader.getAttributeLocalName(j);
        if (((localObject3 = paramXMLStreamReader.getAttributePrefix(j)) != null) && (((String)localObject3).length() > 0))
          str2 = getQualified((String)localObject3, str2);
        Attr localAttr;
        (localAttr = paramDocument.createAttributeNS(paramXMLStreamReader.getAttributeNamespace(j), str2)).setValue(paramXMLStreamReader.getAttributeValue(j));
        localElement.setAttributeNodeNS(localAttr);
        (localObject3 = paramDocument.createAttribute(str2)).setValue(paramXMLStreamReader.getAttributeValue(j));
        (this.mNsAware ? paramDocument : localElement.setAttributeNode((Attr)localObject3));
        j++;
        ((Node)localObject1).appendChild(localElement);
        tmpTernaryOp = localElement;
        break;
      case 7:
        break;
      case 11:
        break;
      case 10:
      case 13:
      default:
        throw new XMLStreamException("Unrecognized iterator event type: " + paramXMLStreamReader.getEventType() + "; should not receive such types (broken stream reader?)");
        if (localObject2 != null)
          ((Node)localObject1).appendChild((Node)localObject2);
      }
    }
  }

  protected String getQualified(String paramString1, String paramString2)
  {
    if ((paramString2 == this.mLastLocalName) && (paramString1 == this.mLastPrefix))
      return this.mLastQName;
    String str = paramString1 + ":" + paramString2;
    this.mLastQName = str;
    return str;
  }

  protected void checkReaderSettings(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    Object localObject;
    this.mNsAware = ((!((localObject = paramXMLStreamReader.getProperty("javax.xml.stream.isNamespaceAware")) instanceof Boolean)) || (((Boolean)localObject).booleanValue()));
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    if (paramArrayOfString.length != 1)
    {
      System.err.println("Usage: java ... [file]");
      System.exit(1);
    }
    String str = paramArrayOfString[0];
    FileReader localFileReader = new FileReader(str);
    XMLInputFactory localXMLInputFactory;
    XMLStreamReader localXMLStreamReader = (localXMLInputFactory = XMLInputFactory.newInstance()).createXMLStreamReader(localFileReader);
    Stax2DomBuilder localStax2DomBuilder;
    Document localDocument = (localStax2DomBuilder = new Stax2DomBuilder()).build(localXMLStreamReader);
    System.out.println("Done [with " + localXMLStreamReader.getClass() + "]:");
    System.out.println("----- Dom -----");
    PrintWriter localPrintWriter;
    (localPrintWriter = new PrintWriter(System.out)).println(localDocument.toString());
    localPrintWriter.flush();
    System.out.println("----- /Dom -----");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.Stax2DomBuilder
 * JD-Core Version:    0.6.0
 */