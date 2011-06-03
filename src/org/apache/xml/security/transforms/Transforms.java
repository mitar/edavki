package org.apache.xml.security.transforms;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Transforms extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(Transforms.class.getName());
  public static final String TRANSFORM_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String TRANSFORM_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String TRANSFORM_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
  public static final String TRANSFORM_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
  public static final String TRANSFORM_XSLT = "http://www.w3.org/TR/1999/REC-xslt-19991116";
  public static final String TRANSFORM_BASE64_DECODE = "http://www.w3.org/2000/09/xmldsig#base64";
  public static final String TRANSFORM_XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
  public static final String TRANSFORM_ENVELOPED_SIGNATURE = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
  public static final String TRANSFORM_XPOINTER = "http://www.w3.org/TR/2001/WD-xptr-20010108";
  public static final String TRANSFORM_XPATH2FILTER04 = "http://www.w3.org/2002/04/xmldsig-filter2";
  public static final String TRANSFORM_XPATH2FILTER = "http://www.w3.org/2002/06/xmldsig-filter2";
  public static final String TRANSFORM_XPATHFILTERCHGP = "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
  Element[] transforms;

  public Transforms(Document paramDocument)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public Transforms(Element paramElement, String paramString)
    throws DOMException, XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
  {
    super(paramElement, paramString);
    int i;
    if ((i = getLength()) == 0)
    {
      Object[] arrayOfObject = { "Transform", "Transforms" };
      throw new TransformationException("xml.WrongContent", arrayOfObject);
    }
  }

  public void addTransform(String paramString)
    throws TransformationException
  {
    try
    {
      if (log.isDebugEnabled())
        log.debug("Transforms.addTransform(" + paramString + ")");
      Transform localTransform = Transform.getInstance(this._doc, paramString);
      addTransform(localTransform);
      return;
    }
    catch (InvalidTransformException localInvalidTransformException)
    {
      throw new TransformationException("empty", localInvalidTransformException);
    }
  }

  public void addTransform(String paramString, Element paramElement)
    throws TransformationException
  {
    try
    {
      if (log.isDebugEnabled())
        log.debug("Transforms.addTransform(" + paramString + ")");
      Transform localTransform = Transform.getInstance(this._doc, paramString, paramElement);
      addTransform(localTransform);
      return;
    }
    catch (InvalidTransformException localInvalidTransformException)
    {
      throw new TransformationException("empty", localInvalidTransformException);
    }
  }

  public void addTransform(String paramString, NodeList paramNodeList)
    throws TransformationException
  {
    try
    {
      Transform localTransform = Transform.getInstance(this._doc, paramString, paramNodeList);
      addTransform(localTransform);
      return;
    }
    catch (InvalidTransformException localInvalidTransformException)
    {
      throw new TransformationException("empty", localInvalidTransformException);
    }
  }

  private void addTransform(Transform paramTransform)
  {
    if (log.isDebugEnabled())
      log.debug("Transforms.addTransform(" + paramTransform.getURI() + ")");
    Element localElement = paramTransform.getElement();
    this._constructionElement.appendChild(localElement);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public XMLSignatureInput performTransforms(XMLSignatureInput paramXMLSignatureInput)
    throws TransformationException
  {
    return performTransforms(paramXMLSignatureInput, null);
  }

  public XMLSignatureInput performTransforms(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws TransformationException
  {
    try
    {
      int i;
      int j = (i = getLength()) - 1;
      for (int k = 0; k < i; k++)
      {
        Transform localTransform = item(k);
        if (log.isDebugEnabled())
          log.debug("Preform the (" + k + ")th " + localTransform.getURI() + " transform");
        paramXMLSignatureInput = k == j ? localTransform.performTransform(paramXMLSignatureInput, paramOutputStream) : localTransform.performTransform(paramXMLSignatureInput);
      }
      return paramXMLSignatureInput;
    }
    catch (IOException localIOException)
    {
      throw new TransformationException("empty", localIOException);
    }
    catch (CanonicalizationException localCanonicalizationException)
    {
      throw new TransformationException("empty", localCanonicalizationException);
    }
    catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
    {
    }
    throw new TransformationException("empty", localInvalidCanonicalizerException);
  }

  public int getLength()
  {
    if (this.transforms == null)
      this.transforms = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Transform");
    return this.transforms.length;
  }

  public Transform item(int paramInt)
    throws TransformationException
  {
    try
    {
      if (this.transforms == null)
        this.transforms = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Transform");
      return new Transform(this.transforms[paramInt], this._baseURI);
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new TransformationException("empty", localXMLSecurityException);
  }

  public String getBaseLocalName()
  {
    return "Transforms";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.Transforms
 * JD-Core Version:    0.6.0
 */