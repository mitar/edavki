package org.apache.xml.security.transforms.implementations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformSpi;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class TransformXSLT extends TransformSpi
{
  public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xslt-19991116";
  static final String XSLTSpecNS = "http://www.w3.org/1999/XSL/Transform";
  static final String defaultXSLTSpecNSprefix = "xslt";
  static final String XSLTSTYLESHEET = "stylesheet";

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
    return true;
  }

  protected String engineGetURI()
  {
    return "http://www.w3.org/TR/1999/REC-xslt-19991116";
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput)
    throws IOException, TransformationException
  {
    return enginePerformTransform(paramXMLSignatureInput, null);
  }

  protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws IOException, TransformationException
  {
    Object localObject1;
    try
    {
      Element localElement;
      if ((localObject1 = XMLUtils.selectNode((localElement = this._transformObject.getElement()).getFirstChild(), "http://www.w3.org/1999/XSL/Transform", "stylesheet", 0)) == null)
      {
        localObject2 = new Object[] { "xslt:stylesheet", "Transform" };
        throw new TransformationException("xml.WrongContent", localObject2);
      }
      Object localObject2 = TransformerFactoryImpl.newInstance();
      SAXSource localSAXSource1;
      try
      {
        SAXParserFactoryImpl localSAXParserFactoryImpl1;
        (localSAXParserFactoryImpl1 = new SAXParserFactoryImpl()).setNamespaceAware(true);
        localObject3 = localSAXParserFactoryImpl1.newSAXParser();
        localSAXSource1 = new SAXSource(((SAXParser)localObject3).getXMLReader(), new InputSource(new ByteArrayInputStream(paramXMLSignatureInput.getBytes())));
      }
      catch (Exception localException1)
      {
        localObject3 = new Object[] { localException1.getMessage() };
        throw new TransformationException("generic.EmptyMessage", localObject3, localException1);
      }
      Object localObject3 = new ByteArrayOutputStream();
      Object localObject4 = ((TransformerFactory)localObject2).newTransformer();
      Object localObject5 = new DOMSource((Node)localObject1);
      StreamResult localStreamResult = new StreamResult((OutputStream)localObject3);
      ((Transformer)localObject4).transform((Source)localObject5, localStreamResult);
      SAXSource localSAXSource2;
      try
      {
        SAXParserFactoryImpl localSAXParserFactoryImpl2;
        (localSAXParserFactoryImpl2 = new SAXParserFactoryImpl()).setNamespaceAware(true);
        localObject6 = localSAXParserFactoryImpl2.newSAXParser();
        localSAXSource2 = new SAXSource(((SAXParser)localObject6).getXMLReader(), new InputSource(new ByteArrayInputStream(((ByteArrayOutputStream)localObject3).toByteArray())));
      }
      catch (Exception localException2)
      {
        Object localObject6 = { localException2.getMessage() };
        throw new TransformationException("generic.EmptyMessage", localObject6, localException2);
      }
      localObject3 = ((TransformerFactory)localObject2).newTransformer(localSAXSource2);
      if (paramOutputStream == null)
      {
        localObject4 = new ByteArrayOutputStream();
        localObject5 = new StreamResult((OutputStream)localObject4);
        ((Transformer)localObject3).transform(localSAXSource1, (Result)localObject5);
        return new XMLSignatureInput(((ByteArrayOutputStream)localObject4).toByteArray());
      }
      localObject4 = new StreamResult(paramOutputStream);
      ((Transformer)localObject3).transform(localSAXSource1, (Result)localObject4);
      (localObject5 = new XMLSignatureInput((byte[])null)).setOutputStream(paramOutputStream);
      return localObject5;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      localObject1 = new Object[] { localXMLSecurityException.getMessage() };
      throw new TransformationException("generic.EmptyMessage", localObject1, localXMLSecurityException);
    }
    catch (TransformerConfigurationException localTransformerConfigurationException)
    {
      localObject1 = new Object[] { localTransformerConfigurationException.getMessage() };
      throw new TransformationException("generic.EmptyMessage", localObject1, localTransformerConfigurationException);
    }
    catch (TransformerException localTransformerException)
    {
      localObject1 = new Object[] { localTransformerException.getMessage() };
    }
    throw new TransformationException("generic.EmptyMessage", localObject1, localTransformerException);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.TransformXSLT
 * JD-Core Version:    0.6.0
 */