package org.apache.xml.security.transforms.implementations;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class TransformBase64Decode extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#base64";

  protected String engineGetURI()
  {
    return "http://www.w3.org/2000/09/xmldsig#base64";
  }

  public boolean wantsOctetStream()
  {
    return true;
  }

  public boolean wantsNodeSet()
  {
    return true;
  }

  public boolean returnsOctetStream()
  {
    return true;
  }

  public boolean returnsNodeSet()
  {
    return false;
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws IOException, CanonicalizationException, TransformationException
  {
    return enginePerformTransform(paramXMLSignatureInput, null);
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws IOException, CanonicalizationException, TransformationException
  {
    try
    {
      Object localObject1;
      Object localObject2;
      Object localObject3;
      if (paramXMLSignatureInput.isElement())
      {
        localObject1 = paramXMLSignatureInput.getSubNode();
        if (paramXMLSignatureInput.getSubNode().getNodeType() == 3)
          localObject1 = ((Node)localObject1).getParentNode();
        localObject2 = new StringBuffer();
        traverseElement((Element)localObject1, (StringBuffer)localObject2);
        if (paramOutputStream == null)
        {
          localObject3 = Base64.decode(((StringBuffer)localObject2).toString());
          return new XMLSignatureInput(localObject3);
        }
        Base64.decode(((StringBuffer)localObject2).toString().getBytes(), paramOutputStream);
        (localObject3 = new XMLSignatureInput((byte[])null)).setOutputStream(paramOutputStream);
        return localObject3;
      }
      if ((paramXMLSignatureInput.isOctetStream()) || (paramXMLSignatureInput.isNodeSet()))
      {
        if (paramOutputStream == null)
        {
          localObject2 = Base64.decode(localObject1 = paramXMLSignatureInput.getBytes());
          return new XMLSignatureInput(localObject2);
        }
        if (paramXMLSignatureInput.isByteArray())
          Base64.decode(paramXMLSignatureInput.getBytes(), paramOutputStream);
        else
          Base64.decode(new BufferedInputStream(paramXMLSignatureInput.getOctetStreamReal()), paramOutputStream);
        (localObject1 = new XMLSignatureInput((byte[])null)).setOutputStream(paramOutputStream);
        return localObject1;
      }
      try
      {
        localObject2 = (localObject1 = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(paramXMLSignatureInput.getOctetStream())).getDocumentElement();
        localObject3 = new StringBuffer();
        traverseElement((Element)localObject2, (StringBuffer)localObject3);
        byte[] arrayOfByte = Base64.decode(((StringBuffer)localObject3).toString());
        return new XMLSignatureInput(arrayOfByte);
      }
      catch (ParserConfigurationException localParserConfigurationException)
      {
        throw new TransformationException("c14n.Canonicalizer.Exception", localParserConfigurationException);
      }
      catch (SAXException localSAXException)
      {
        throw new TransformationException("SAX exception", localSAXException);
      }
    }
    catch (Base64DecodingException localBase64DecodingException)
    {
    }
    throw new TransformationException("Base64Decoding", localBase64DecodingException);
  }

  void traverseElement(Element paramElement, StringBuffer paramStringBuffer)
  {
    Node localNode;
    switch (localNode.getNodeType())
    {
    case 1:
      traverseElement((Element)localNode, paramStringBuffer);
      break;
    case 3:
      paramStringBuffer.append(((Text)localNode).getData());
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformBase64Decode
 * JD-Core Version:    0.6.0
 */