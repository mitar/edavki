package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.implementations.Canonicalizer20010315OmitComments;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.TransformSpi;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TransformC14N extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";

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
    return "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws IOException, CanonicalizationException
  {
    return enginePerformTransform(paramXMLSignatureInput, null);
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws IOException, CanonicalizationException
  {
    Object localObject1;
    Object localObject2;
    try
    {
      Canonicalizer20010315OmitComments localCanonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
      if (paramOutputStream != null)
        localCanonicalizer20010315OmitComments.setWriter(paramOutputStream);
      localObject1 = null;
      localObject2 = paramXMLSignatureInput.getExcludeNode();
      localObject2 = paramXMLSignatureInput.getNodeSet(true);
      localObject1 = paramXMLSignatureInput.isElement() ? localCanonicalizer20010315OmitComments.engineCanonicalizeSubTree(paramXMLSignatureInput.getSubNode(), (Node)localObject2) : paramXMLSignatureInput.isOctetStream() ? localCanonicalizer20010315OmitComments.engineCanonicalize(paramXMLSignatureInput.getBytes()) : localCanonicalizer20010315OmitComments.engineCanonicalizeXPathNodeSet((Set)localObject2);
      localObject2 = new XMLSignatureInput(localObject1);
      if (paramOutputStream != null)
        ((XMLSignatureInput)localObject2).setOutputStream(paramOutputStream);
      return localObject2;
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      localObject1 = new Object[] { localParserConfigurationException.getMessage() };
      throw (localObject2 = new CanonicalizationException("c14n.Canonicalizer.ParserConfigurationException", localObject1));
    }
    catch (SAXException localSAXException)
    {
      localObject1 = new Object[] { localSAXException.toString() };
    }
    throw (localObject2 = new CanonicalizationException("c14n.Canonicalizer.SAXException", localObject1));
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformC14N
 * JD-Core Version:    0.6.0
 */