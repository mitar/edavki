package org.apache.xml.security.transforms.implementations;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.implementations.Canonicalizer20010315ExclWithComments;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.params.InclusiveNamespaces;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TransformC14NExclusiveWithComments extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";

  public boolean wantsOctetStream()
  {
    return false;
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
    return "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws CanonicalizationException
  {
    return enginePerformTransform(paramXMLSignatureInput, null);
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws CanonicalizationException
  {
    try
    {
      String str = null;
      Object localObject1;
      if (this._transformObject.length("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces") == 1)
      {
        localObject1 = XMLUtils.selectNode(this._transformObject.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", 0);
        str = new InclusiveNamespaces((Element)localObject1, this._transformObject.getBaseURI()).getInclusiveNamespaces();
      }
      (localObject1 = new Canonicalizer20010315ExclWithComments()).set_includeComments(!paramXMLSignatureInput.isExcludeComments());
      if (paramOutputStream != null)
        ((Canonicalizer20010315ExclWithComments)localObject1).setWriter(paramOutputStream);
      Object localObject2 = paramXMLSignatureInput.getExcludeNode();
      byte[] arrayOfByte = paramXMLSignatureInput.isElement() ? ((Canonicalizer20010315ExclWithComments)localObject1).engineCanonicalizeSubTree(paramXMLSignatureInput.getSubNode(), str, (Node)localObject2) : paramXMLSignatureInput.isOctetStream() ? ((Canonicalizer20010315ExclWithComments)localObject1).engineCanonicalize(paramXMLSignatureInput.getBytes()) : ((Canonicalizer20010315ExclWithComments)localObject1).engineCanonicalizeXPathNodeSet(paramXMLSignatureInput.getNodeSet(), str);
      localObject2 = new XMLSignatureInput(arrayOfByte);
      if (paramOutputStream != null)
        ((XMLSignatureInput)localObject2).setOutputStream(paramOutputStream);
      return localObject2;
    }
    catch (IOException localIOException)
    {
      throw new CanonicalizationException("empty", localIOException);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      throw new CanonicalizationException("empty", localParserConfigurationException);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      throw new CanonicalizationException("empty", localXMLSecurityException);
    }
    catch (SAXException localSAXException)
    {
    }
    throw new CanonicalizationException("empty", localSAXException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformC14NExclusiveWithComments
 * JD-Core Version:    0.6.0
 */