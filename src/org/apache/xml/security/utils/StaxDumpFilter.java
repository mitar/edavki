package org.apache.xml.security.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.xml.security.c14n.implementations.AttrStream;

public class StaxDumpFilter
  implements StreamFilter
{
  public XMLStreamWriter writer;
  public ByteArrayOutputStream output;
  public boolean doOutput = true;
  public List addAttributes = null;

  public StaxDumpFilter()
    throws XMLStreamException, FileNotFoundException
  {
    XMLOutputFactory localXMLOutputFactory = XMLOutputFactory.newInstance();
    this.writer = localXMLOutputFactory.createXMLStreamWriter(new FileOutputStream("h:/Repositories/aktrp/trunk/src/ESignDocJ/out/out_full_doc.xml"), "UTF-8");
  }

  public StaxDumpFilter(ByteArrayOutputStream paramByteArrayOutputStream, boolean paramBoolean)
    throws XMLStreamException, IOException
  {
    this.output = paramByteArrayOutputStream;
    XMLOutputFactory localXMLOutputFactory = XMLOutputFactory.newInstance();
    this.writer = localXMLOutputFactory.createXMLStreamWriter(paramBoolean ? new GZIPOutputStream(this.output) : this.output, "UTF-8");
  }

  public void Close()
    throws XMLStreamException
  {
    this.writer.flush();
    this.writer.close();
  }

  public void WriteEvent(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    switch (paramXMLStreamReader.getEventType())
    {
    case 7:
      this.writer.writeStartDocument("1.0");
      return;
    case 1:
      String str1 = paramXMLStreamReader.getName().getPrefix();
      String str2 = paramXMLStreamReader.getName().getLocalPart();
      this.writer.writeStartElement(str1 + ":" + str2);
      if (this.addAttributes == null)
      {
        for (int i = 0; i < paramXMLStreamReader.getNamespaceCount(); i++)
          this.writer.writeNamespace(paramXMLStreamReader.getNamespacePrefix(i), paramXMLStreamReader.getNamespaceURI(i));
        for (i = 0; i < paramXMLStreamReader.getAttributeCount(); i++)
          this.writer.writeAttribute(paramXMLStreamReader.getAttributePrefix(i), paramXMLStreamReader.getAttributeNamespace(i), paramXMLStreamReader.getAttributeLocalName(i), paramXMLStreamReader.getAttributeValue(i));
        return;
      }
      List localList = StaxUtils.CombineAttributes(paramXMLStreamReader, this.addAttributes, null);
      for (int j = 0; j < localList.size(); j++)
      {
        AttrStream localAttrStream = (AttrStream)localList.get(j);
        this.writer.writeAttribute(localAttrStream.getPrefix(), localAttrStream.getNamespaceURI(), localAttrStream.getLocalName(), localAttrStream.getNodeValue());
      }
      return;
    case 2:
      this.writer.writeEndElement();
      return;
    case 12:
      this.writer.writeCData(paramXMLStreamReader.getText());
      return;
    case 4:
      this.writer.writeCharacters(paramXMLStreamReader.getText());
      return;
    case 5:
      this.writer.writeComment(paramXMLStreamReader.getText());
      return;
    case 8:
      this.writer.writeEndDocument();
      return;
    case 3:
      this.writer.writeProcessingInstruction(paramXMLStreamReader.getPITarget(), paramXMLStreamReader.getPIData());
    case 6:
    case 9:
    case 10:
    case 11:
    }
  }

  public boolean accept(XMLStreamReader paramXMLStreamReader)
  {
    try
    {
      if (this.doOutput)
        WriteEvent(paramXMLStreamReader);
    }
    catch (XMLStreamException localXMLStreamException2)
    {
      XMLStreamException localXMLStreamException1;
      (localXMLStreamException1 = localXMLStreamException2).printStackTrace();
    }
    return true;
  }

  public void WriteCustomStartElement(XMLStreamReader paramXMLStreamReader, Dictionary paramDictionary)
    throws XMLStreamException
  {
    String str1 = paramXMLStreamReader.getName().getPrefix();
    String str2 = paramXMLStreamReader.getName().getLocalPart();
    this.writer.writeStartElement(str1 + ":" + str2);
    for (int i = 0; i < paramXMLStreamReader.getNamespaceCount(); i++)
      this.writer.writeNamespace(paramXMLStreamReader.getNamespacePrefix(i), paramXMLStreamReader.getNamespaceURI(i));
    String str3;
    for (i = 0; i < paramXMLStreamReader.getAttributeCount(); i++)
      if ((str3 = (String)paramDictionary.get(paramXMLStreamReader.getAttributeLocalName(i))) != null)
      {
        this.writer.writeAttribute(paramXMLStreamReader.getAttributeLocalName(i), str3);
        paramDictionary.remove(paramXMLStreamReader.getAttributeLocalName(i));
      }
      else
      {
        this.writer.writeAttribute(paramXMLStreamReader.getAttributeLocalName(i), paramXMLStreamReader.getAttributeValue(i));
      }
    Enumeration localEnumeration = paramDictionary.keys();
    while (localEnumeration.hasMoreElements())
    {
      str3 = (String)localEnumeration.nextElement();
      this.writer.writeAttribute(str3, (String)paramDictionary.get(str3));
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.StaxDumpFilter
 * JD-Core Version:    0.6.0
 */