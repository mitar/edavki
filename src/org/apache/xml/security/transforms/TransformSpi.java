package org.apache.xml.security.transforms;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.xml.sax.SAXException;

public abstract class TransformSpi
{
  static Log log = LogFactory.getLog(TransformSpi.class.getName());
  protected Transform _transformObject = null;

  protected void setTransform(Transform paramTransform)
  {
    this._transformObject = paramTransform;
  }

  public abstract boolean wantsOctetStream();

  public abstract boolean wantsNodeSet();

  public abstract boolean returnsOctetStream();

  public abstract boolean returnsNodeSet();

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException, ParserConfigurationException, SAXException
  {
    return enginePerformTransform(paramXMLSignatureInput);
  }

  protected abstract XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException, ParserConfigurationException, SAXException;

  protected abstract String engineGetURI();
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.TransformSpi
 * JD-Core Version:    0.6.0
 */