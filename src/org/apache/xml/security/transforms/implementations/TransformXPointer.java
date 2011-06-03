package org.apache.xml.security.transforms.implementations;

import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;

public class TransformXPointer extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/TR/2001/WD-xptr-20010108";

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
    return false;
  }

  public boolean returnsNodeSet()
  {
    return true;
  }

  protected String engineGetURI()
  {
    return "http://www.w3.org/TR/2001/WD-xptr-20010108";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    Object[] arrayOfObject = { "http://www.w3.org/TR/2001/WD-xptr-20010108" };
    throw new TransformationException("signature.Transform.NotYetImplemented", arrayOfObject);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXPointer
 * JD-Core Version:    0.6.0
 */