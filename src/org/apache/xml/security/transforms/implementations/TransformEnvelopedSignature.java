package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TransformEnvelopedSignature extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";

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

  protected String engineGetURI()
  {
    return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    try
    {
      Element localElement;
      Object localObject1 = searchSignatureElement(localObject1 = localElement = this._transformObject.getElement());
      Object localObject2;
      if (paramXMLSignatureInput.isElement())
      {
        (localObject2 = new XMLSignatureInput(paramXMLSignatureInput.getSubNode())).setExcludeNode((Node)localObject1);
        ((XMLSignatureInput)localObject2).setExcludeComments(paramXMLSignatureInput.isExcludeComments());
        return localObject2;
      }
      if ((localObject2 = paramXMLSignatureInput.getNodeSet()).isEmpty())
      {
        localObject3 = new Object[] { "input node set contains no nodes" };
        throw new TransformationException("generic.EmptyMessage", localObject3);
      }
      Object localObject3 = XMLUtils.excludeNodeFromSet((Node)localObject1, (Set)localObject2);
      XMLSignatureInput localXMLSignatureInput;
      return localXMLSignatureInput = new XMLSignatureInput((Set)localObject3);
    }
    catch (IOException localIOException)
    {
      throw new TransformationException("empty", localIOException);
    }
    catch (SAXException localSAXException)
    {
      throw new TransformationException("empty", localSAXException);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      throw new TransformationException("empty", localParserConfigurationException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
    }
    throw new TransformationException("empty", localCanonicalizationException);
  }

  private static Node searchSignatureElement(Node paramNode)
    throws TransformationException
  {
    int i = 0;
    while ((paramNode != null) && (paramNode.getNodeType() != 9))
    {
      Element localElement;
      if (((localElement = (Element)paramNode).getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) && (localElement.getLocalName().equals("Signature")))
      {
        i = 1;
        break;
      }
      paramNode = paramNode.getParentNode();
    }
    if (i == 0)
      throw new TransformationException("envelopedSignatureTransformNotInSignatureElement");
    return paramNode;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformEnvelopedSignature
 * JD-Core Version:    0.6.0
 */