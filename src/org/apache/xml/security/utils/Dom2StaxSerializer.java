package org.apache.xml.security.utils;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Dom2StaxSerializer
{
  protected final XMLStreamWriter mWriter;
  protected final boolean mRepairing;

  public Dom2StaxSerializer(XMLStreamWriter paramXMLStreamWriter)
  {
    this.mWriter = paramXMLStreamWriter;
    this.mRepairing = true;
  }

  public void output(Document paramDocument)
    throws XMLStreamException
  {
    this.mWriter.writeStartDocument();
    NsStack localNsStack = NsStack.defaultInstance();
    Node localNode;
    doOutputNode(localNode, localNsStack);
    this.mWriter.writeEndDocument();
    doClose();
  }

  public void outputFragment(NodeList paramNodeList)
    throws XMLStreamException
  {
    NsStack localNsStack = NsStack.defaultInstance();
    int i = 0;
    int j = paramNodeList.getLength();
    while (i < j)
    {
      doOutputNode(paramNodeList.item(i), localNsStack);
      i++;
    }
  }

  public void outputFragment(Node paramNode)
    throws XMLStreamException
  {
    doOutputNode(paramNode, NsStack.defaultInstance());
  }

  protected void doOutputElement(Element paramElement, NsStack paramNsStack)
    throws XMLStreamException
  {
    int i = 1;
    String str1;
    if ((str1 = paramElement.getPrefix()) == null)
      str1 = "";
    String str2;
    if ((str2 = paramElement.getNamespaceURI()) == null)
      str2 = "";
    this.mWriter.writeStartElement(str1, paramElement.getLocalName(), str2);
    if ((!this.mRepairing) && (!paramNsStack.hasBinding(str1, str2)))
    {
      paramNsStack = paramNsStack.childInstance();
      i = 0;
      paramNsStack.addBinding(str1, str2);
      if (str1.length() == 0)
      {
        this.mWriter.setDefaultNamespace(str2);
        this.mWriter.writeDefaultNamespace(str2);
      }
      else
      {
        this.mWriter.setPrefix(str1, str2);
        this.mWriter.writeNamespace(str1, str2);
      }
    }
    NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
    int j = 0;
    int k = localNamedNodeMap.getLength();
    while (j < k)
    {
      Attr localAttr;
      String str3 = (localAttr = (Attr)localNamedNodeMap.item(j)).getPrefix();
      String str4 = localAttr.getName();
      String str5 = localAttr.getValue();
      String str6 = localAttr.getNamespaceURI();
      if ((str3 == null) || (str3.length() == 0))
      {
        this.mWriter.writeAttribute(str4, str5);
      }
      else
      {
        if ((!this.mRepairing) && (!paramNsStack.hasBinding(str3, str6)))
        {
          if (i != 0)
          {
            paramNsStack = paramNsStack.childInstance();
            i = 0;
          }
          paramNsStack.addBinding(str3, str6);
          this.mWriter.setPrefix(str3, str6);
          this.mWriter.writeNamespace(str3, str6);
        }
        this.mWriter.writeAttribute(str3, str6, localAttr.getLocalName(), localAttr.getValue());
      }
      j++;
    }
    Node localNode;
    doOutputNode(localNode, paramNsStack);
    this.mWriter.writeEndElement();
  }

  protected void doOutputNode(Node paramNode, NsStack paramNsStack)
    throws XMLStreamException
  {
    switch (paramNode.getNodeType())
    {
    case 1:
      doOutputElement((Element)paramNode, paramNsStack);
      return;
    case 3:
      this.mWriter.writeCharacters(paramNode.getNodeValue());
      return;
    case 4:
      this.mWriter.writeCData(paramNode.getNodeValue());
      return;
    case 8:
      this.mWriter.writeComment(paramNode.getNodeValue());
      return;
    case 5:
      this.mWriter.writeEntityRef(paramNode.getNodeName());
      return;
    case 7:
      String str1 = paramNode.getNodeName();
      String str2;
      if (((str2 = paramNode.getNodeValue()) == null) || (str2.length() == 0))
      {
        this.mWriter.writeProcessingInstruction(str1);
        return;
      }
      this.mWriter.writeProcessingInstruction(str1, str2);
      return;
    case 10:
      this.mWriter.writeDTD(buildDTD((DocumentType)paramNode));
      return;
    case 2:
    case 6:
    case 9:
    }
    throw new XMLStreamException("Unrecognized or unexpected node class: " + paramNode.getClass().getName());
  }

  protected void doClose()
    throws XMLStreamException
  {
    this.mWriter.close();
  }

  protected String buildDTD(DocumentType paramDocumentType)
  {
    StringBuffer localStringBuffer;
    (localStringBuffer = new StringBuffer()).append("<!DOCTYPE ");
    localStringBuffer.append(paramDocumentType.getName());
    String str1 = paramDocumentType.getPublicId();
    String str2 = paramDocumentType.getSystemId();
    if ((str1 == null) || (str1.length() == 0))
    {
      if ((str2 != null) && (str2.length() > 0))
      {
        localStringBuffer.append("SYSTEM \"");
        localStringBuffer.append(str2);
        localStringBuffer.append('"');
      }
    }
    else
    {
      localStringBuffer.append("PUBLIC \"");
      localStringBuffer.append(str1);
      localStringBuffer.append("\" \"");
      localStringBuffer.append(str2);
      localStringBuffer.append('"');
    }
    String str3;
    if (((str3 = paramDocumentType.getInternalSubset()) != null) && (str3.length() > 0))
    {
      localStringBuffer.append(" [");
      localStringBuffer.append(str3);
      localStringBuffer.append(']');
    }
    localStringBuffer.append('>');
    return localStringBuffer.toString();
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
    PrintWriter localPrintWriter = new PrintWriter(System.out);
    XMLOutputFactory localXMLOutputFactory;
    XMLStreamWriter localXMLStreamWriter = (localXMLOutputFactory = XMLOutputFactory.newInstance()).createXMLStreamWriter(localPrintWriter);
    Dom2StaxSerializer localDom2StaxSerializer;
    (localDom2StaxSerializer = new Dom2StaxSerializer(localXMLStreamWriter)).output(localDocument);
    localXMLStreamWriter.flush();
    localXMLStreamWriter.close();
  }

  private static final class NsStack
  {
    static final NsStack sEmptyStack;
    String[] mNsData;
    int mEnd = 0;

    private NsStack(String[] paramArrayOfString, int paramInt)
    {
      this.mNsData = paramArrayOfString;
      this.mEnd = paramInt;
    }

    public static NsStack defaultInstance()
    {
      return sEmptyStack;
    }

    public final NsStack childInstance()
    {
      if (this == sEmptyStack)
      {
        String[] arrayOfString = new String[16];
        System.arraycopy(this.mNsData, 0, arrayOfString, 0, this.mEnd);
        return new NsStack(arrayOfString, this.mEnd);
      }
      return new NsStack(this.mNsData, this.mEnd);
    }

    public final boolean hasBinding(String paramString1, String paramString2)
    {
      for (int i = this.mEnd - 2; i >= 0; i -= 2)
        if (this.mNsData[i].equals(paramString1))
          return this.mNsData[(i + 1)].equals(paramString2);
      return false;
    }

    public final void addBinding(String paramString1, String paramString2)
    {
      if (paramString1 == null)
        paramString1 = "";
      if (this.mEnd >= this.mNsData.length)
      {
        String[] arrayOfString = this.mNsData;
        this.mNsData = new String[arrayOfString.length * 2];
        System.arraycopy(arrayOfString, 0, this.mNsData, 0, arrayOfString.length);
      }
      this.mNsData[this.mEnd] = paramString1;
      this.mNsData[(this.mEnd + 1)] = paramString2;
      this.mEnd += 2;
    }

    static
    {
      String[] arrayOfString = { "xml", "http://www.w3.org/XML/1998/namespace", "xmlns", "http://www.w3.org/2000/xmlns/", "", "" };
      sEmptyStack = new NsStack(arrayOfString, arrayOfString.length);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.Dom2StaxSerializer
 * JD-Core Version:    0.6.0
 */