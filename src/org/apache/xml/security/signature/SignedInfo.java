package org.apache.xml.security.signature;

import B;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class SignedInfo extends Manifest
{
  private SignatureAlgorithm _signatureAlgorithm = null;
  private byte[] _c14nizedBytes = null;

  public SignedInfo(Document paramDocument)
    throws XMLSecurityException
  {
    this(paramDocument, "http://www.w3.org/2000/09/xmldsig#dsa-sha1", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
  }

  public SignedInfo(Document paramDocument, String paramString1, String paramString2)
    throws XMLSecurityException
  {
    this(paramDocument, paramString1, 0, paramString2);
  }

  public SignedInfo(Document paramDocument, String paramString1, int paramInt, String paramString2)
    throws XMLSecurityException
  {
    super(paramDocument);
    Element localElement;
    (localElement = XMLUtils.createElementInSignatureSpace(this._doc, "CanonicalizationMethod")).setAttributeNS(null, "Algorithm", paramString2);
    this._constructionElement.appendChild(localElement);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._signatureAlgorithm = (paramInt > 0 ? new SignatureAlgorithm(this._doc, paramString1, paramInt) : new SignatureAlgorithm(this._doc, paramString1));
    this._constructionElement.appendChild(this._signatureAlgorithm.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public SignedInfo(Document paramDocument, Element paramElement1, Element paramElement2)
    throws XMLSecurityException
  {
    super(paramDocument);
    this._constructionElement.appendChild(paramElement2);
    XMLUtils.addReturnToElement(this._constructionElement);
    this._signatureAlgorithm = new SignatureAlgorithm(paramElement1, null);
    this._constructionElement.appendChild(this._signatureAlgorithm.getElement());
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public SignedInfo(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    String str;
    if ((!(str = getCanonicalizationMethodURI()).equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) && (!str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) && (!str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")))
      try
      {
        Canonicalizer localCanonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI());
        this._c14nizedBytes = localCanonicalizer.canonicalizeSubtree(this._constructionElement);
        DocumentBuilderFactory localDocumentBuilderFactory;
        (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setNamespaceAware(true);
        DocumentBuilder localDocumentBuilder;
        Document localDocument = (localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder()).parse(new ByteArrayInputStream(this._c14nizedBytes));
        Node localNode = this._doc.importNode(localDocument.getDocumentElement(), true);
        this._constructionElement.getParentNode().replaceChild(localNode, this._constructionElement);
        this._constructionElement = ((Element)localNode);
      }
      catch (ParserConfigurationException localParserConfigurationException)
      {
        throw new XMLSecurityException("empty", localParserConfigurationException);
      }
      catch (IOException localIOException)
      {
        throw new XMLSecurityException("empty", localIOException);
      }
      catch (SAXException localSAXException)
      {
        throw new XMLSecurityException("empty", localSAXException);
      }
    this._signatureAlgorithm = new SignatureAlgorithm(getSignatureMethodElement(), getBaseURI());
  }

  public boolean verify()
    throws MissingResourceFailureException, XMLSecurityException
  {
    return super.verifyReferences(false);
  }

  public boolean verify(boolean paramBoolean)
    throws MissingResourceFailureException, XMLSecurityException
  {
    return super.verifyReferences(paramBoolean);
  }

  public byte[] getCanonicalizedOctetStream()
    throws CanonicalizationException, InvalidCanonicalizerException, XMLSecurityException
  {
    if (this._c14nizedBytes == null)
    {
      localObject = Canonicalizer.getInstance(getCanonicalizationMethodURI());
      this._c14nizedBytes = ((Canonicalizer)localObject).canonicalizeSubtree(this._constructionElement);
    }
    Object localObject = new byte[this._c14nizedBytes.length];
    System.arraycopy(this._c14nizedBytes, 0, localObject, 0, localObject.length);
    return (B)localObject;
  }

  public void signInOctectStream(OutputStream paramOutputStream)
    throws CanonicalizationException, InvalidCanonicalizerException, XMLSecurityException
  {
    if (this._c14nizedBytes == null)
    {
      Canonicalizer localCanonicalizer;
      (localCanonicalizer = Canonicalizer.getInstance(getCanonicalizationMethodURI())).setWriter(paramOutputStream);
      localCanonicalizer.canonicalizeSubtree(this._constructionElement);
      return;
    }
    try
    {
      paramOutputStream.write(this._c14nizedBytes);
      return;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("" + localIOException);
    }
  }

  public String getCanonicalizationMethodURI()
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "CanonicalizationMethod", 0)) == null)
      return null;
    return localElement.getAttributeNS(null, "Algorithm");
  }

  public String getSignatureMethodURI()
  {
    Element localElement;
    if ((localElement = getSignatureMethodElement()) != null)
      return localElement.getAttributeNS(null, "Algorithm");
    return null;
  }

  public Element getSignatureMethodElement()
  {
    return XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SignatureMethod", 0);
  }

  public SecretKey createSecretKey(byte[] paramArrayOfByte)
  {
    return new SecretKeySpec(paramArrayOfByte, this._signatureAlgorithm.getJCEAlgorithmString());
  }

  public String getBaseLocalName()
  {
    return "SignedInfo";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.signature.SignedInfo
 * JD-Core Version:    0.6.0
 */