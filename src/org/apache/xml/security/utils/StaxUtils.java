package org.apache.xml.security.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.implementations.AttrStream;
import org.apache.xml.security.c14n.implementations.Canonicalizer20010315;
import org.apache.xml.security.c14n.implementations.Canonicalizer20010315Stax;
import org.apache.xml.security.c14n.implementations.CanonicalizerBaseStax.AttrIterator;
import org.apache.xml.security.c14n.implementations.NameSpaceSymbTableStax;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.IReferences;
import si.hermes.security.SignedInfo.ISignedInfo;

public class StaxUtils
{
  public static void CheckNextElement(XMLStreamReader paramXMLStreamReader, String paramString1, String paramString2, String paramString3)
    throws XMLStreamException
  {
    while (paramXMLStreamReader.next() != 1);
    if ((!paramXMLStreamReader.getLocalName().equals(paramString2)) || (!paramXMLStreamReader.getNamespaceURI().equals(paramString1)))
      throw new XMLStreamException(paramString3);
  }

  public static void CheckNextElementNoNext(XMLStreamReader paramXMLStreamReader, String paramString1, String paramString2, String paramString3)
    throws XMLStreamException
  {
    while ((paramXMLStreamReader.getEventType() != 1) && (paramXMLStreamReader.next() != 1));
    if ((!paramXMLStreamReader.getLocalName().equals(paramString2)) || (!paramXMLStreamReader.getNamespaceURI().equals(paramString1)))
      throw new XMLStreamException(paramString3);
  }

  public static void CheckLastElement(XMLStreamReader paramXMLStreamReader, String paramString1, String paramString2, String paramString3)
    throws XMLStreamException
  {
    CheckLastElementNoNext(paramXMLStreamReader, paramString1, paramString2, paramString3);
    paramXMLStreamReader.next();
  }

  public static void CheckLastElementNoNext(XMLStreamReader paramXMLStreamReader, String paramString1, String paramString2, String paramString3)
    throws XMLStreamException
  {
    while (paramXMLStreamReader.getEventType() != 2)
      paramXMLStreamReader.next();
    if ((!paramXMLStreamReader.getLocalName().equals(paramString2)) || (!paramXMLStreamReader.getNamespaceURI().equals(paramString1)))
      throw new XMLStreamException(paramString3);
  }

  public static void SkipNode(XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException
  {
    int i = 1;
    while (i != 0)
    {
      paramXMLStreamReader.next();
      switch (paramXMLStreamReader.getEventType())
      {
      case 1:
        i++;
        break;
      case 2:
        i--;
      }
    }
  }

  public static void AddOrUpdateAttribute(List paramList, AttrStream paramAttrStream, boolean paramBoolean)
  {
    int i = 0;
    for (int j = 0; j < paramList.size(); j++)
    {
      AttrStream localAttrStream = (AttrStream)paramList.get(j);
      if (!paramAttrStream.getName().equals(localAttrStream.getName()))
        continue;
      if (paramBoolean)
        localAttrStream.setNodeValue(paramAttrStream.getNodeValue());
      i = 1;
    }
    if (i == 0)
      paramList.add(paramAttrStream);
  }

  public static void AddAttributes(Element paramElement, List paramList)
  {
    for (int i = 0; i < paramList.size(); i++)
      paramElement.setAttributeNS("http://www.w3.org/2000/xmlns/", ((AttrStream)paramList.get(i)).getNodeName(), ((AttrStream)paramList.get(i)).getNodeValue());
  }

  public static List CombineAttributes(XMLStreamReader paramXMLStreamReader, List paramList1, List paramList2)
  {
    Object localObject = new CanonicalizerBaseStax.AttrIterator(paramXMLStreamReader);
    ArrayList localArrayList = new ArrayList();
    while (((Iterator)localObject).hasNext())
      localArrayList.add(((Iterator)localObject).next());
    if (paramList1 != null)
    {
      localObject = paramList1.iterator();
      while (((Iterator)localObject).hasNext())
        AddOrUpdateAttribute(localArrayList, (AttrStream)((Iterator)localObject).next(), true);
    }
    if (paramList2 != null)
    {
      localObject = paramList2.iterator();
      while (((Iterator)localObject).hasNext())
        AddOrUpdateAttribute(localArrayList, (AttrStream)((Iterator)localObject).next(), false);
    }
    return (List)localArrayList;
  }

  public static byte[] DoC14nTransformation(XMLStreamReader paramXMLStreamReader, List paramList1, List paramList2)
    throws XMLStreamException, CanonicalizationException, IOException
  {
    1 local1 = new Canonicalizer20010315Stax(false)
    {
      public String engineGetURI()
      {
        return null;
      }

      public boolean engineGetIncludeComments()
      {
        return false;
      }

      public byte[] engineCanonicalizeSubTree(Node paramNode)
        throws CanonicalizationException
      {
        return null;
      }
    };
    NameSpaceSymbTableStax localNameSpaceSymbTableStax = new NameSpaceSymbTableStax();
    CanonicalizerBaseStax.AttrIterator localAttrIterator = new CanonicalizerBaseStax.AttrIterator(paramXMLStreamReader);
    if ((paramList1 != null) || (paramList2 != null))
    {
      List localList = CombineAttributes(paramXMLStreamReader, paramList1, paramList2);
      local1.writeStartElement(paramXMLStreamReader.getPrefix(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getNamespaceURI(), localList.listIterator(), localNameSpaceSymbTableStax);
    }
    else
    {
      local1.writeStartElement(paramXMLStreamReader.getPrefix(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getNamespaceURI(), localAttrIterator, localNameSpaceSymbTableStax);
      localAttrIterator.resetIterator();
    }
    int i = 1;
    while (i != 0)
    {
      paramXMLStreamReader.next();
      switch (paramXMLStreamReader.getEventType())
      {
      case 1:
        local1.writeStartElement(paramXMLStreamReader.getPrefix(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getNamespaceURI(), localAttrIterator, localNameSpaceSymbTableStax);
        localAttrIterator.resetIterator();
        i++;
        break;
      case 2:
        i--;
        local1.writeEndElement(paramXMLStreamReader.getPrefix(), paramXMLStreamReader.getLocalName(), paramXMLStreamReader.getNamespaceURI(), localNameSpaceSymbTableStax);
        break;
      case 4:
      case 12:
        local1.writeCharacters(paramXMLStreamReader.getText());
        break;
      case 5:
        local1.writeComment(paramXMLStreamReader.getText());
        break;
      case 3:
        local1.writeProcessingInstruction(paramXMLStreamReader.getPITarget(), paramXMLStreamReader.getPIData());
        break;
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      default:
        throw new CanonicalizationException("C14n: Internal error!!!!" + paramXMLStreamReader.getEventType());
      }
    }
    return local1.getStreamResult();
  }

  public static byte[] DoC14nTransformation(Element paramElement)
    throws XMLStreamException, CanonicalizationException, UnsupportedEncodingException
  {
    2 local2;
    return (local2 = new Canonicalizer20010315(false)
    {
      public String engineGetURI()
      {
        return null;
      }

      public boolean engineGetIncludeComments()
      {
        return false;
      }
    }).engineCanonicalizeSubTree(paramElement);
  }

  public static boolean HasReference(IHSLSignature paramIHSLSignature, String paramString)
  {
    for (int i = 0; i < paramIHSLSignature.getSignedInfo().getCount(); i++)
      if (paramIHSLSignature.getSignedInfo().getReferences().getItem(i).getUri().equals(paramString))
        return true;
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.StaxUtils
 * JD-Core Version:    0.6.0
 */