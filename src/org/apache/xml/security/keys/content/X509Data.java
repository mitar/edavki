package org.apache.xml.security.keys.content;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.x509.XMLX509CRL;
import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.keys.content.x509.XMLX509IssuerSerial;
import org.apache.xml.security.keys.content.x509.XMLX509SKI;
import org.apache.xml.security.keys.content.x509.XMLX509SubjectName;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class X509Data extends SignatureElementProxy
  implements KeyInfoContent
{
  static Log log = LogFactory.getLog(X509Data.class.getName());

  public X509Data(Document paramDocument)
  {
    super(paramDocument);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public X509Data(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
    int i = 1;
    Node localNode = this._constructionElement.getFirstChild();
    Object localObject1;
    while (true)
    {
      i = 0;
      localObject1 = (Element)localNode;
      localNode = localNode.getNextSibling();
      String str = ((Element)localObject1).getLocalName();
      if (str.equals("X509IssuerSerial"))
      {
        localObject2 = new XMLX509IssuerSerial((Element)localObject1, paramString);
        add((XMLX509IssuerSerial)localObject2);
        continue;
      }
      if (str.equals("X509SKI"))
      {
        localObject2 = new XMLX509SKI((Element)localObject1, paramString);
        add((XMLX509SKI)localObject2);
        continue;
      }
      if (str.equals("X509SubjectName"))
      {
        localObject2 = new XMLX509SubjectName((Element)localObject1, paramString);
        add((XMLX509SubjectName)localObject2);
        continue;
      }
      if (str.equals("X509Certificate"))
      {
        localObject2 = new XMLX509Certificate((Element)localObject1, paramString);
        add((XMLX509Certificate)localObject2);
        continue;
      }
      if (!str.equals("X509CRL"))
        break;
      Object localObject2 = new XMLX509CRL((Element)localObject1, paramString);
      add((XMLX509CRL)localObject2);
    }
    log.warn("Found a " + ((Element)localObject1).getTagName() + " element in " + "X509Data");
    addUnknownElement((Element)localObject1);
    if (i != 0)
    {
      localObject1 = new Object[] { "Elements", "X509Data" };
      throw new XMLSecurityException("xml.WrongContent", localObject1);
    }
  }

  public void addIssuerSerial(String paramString, BigInteger paramBigInteger)
  {
    add(new XMLX509IssuerSerial(this._doc, paramString, paramBigInteger));
  }

  public void addIssuerSerial(String paramString1, String paramString2)
  {
    add(new XMLX509IssuerSerial(this._doc, paramString1, paramString2));
  }

  public void addIssuerSerial(String paramString, int paramInt)
  {
    add(new XMLX509IssuerSerial(this._doc, paramString, paramInt));
  }

  public void add(XMLX509IssuerSerial paramXMLX509IssuerSerial)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramXMLX509IssuerSerial.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addSKI(byte[] paramArrayOfByte)
  {
    add(new XMLX509SKI(this._doc, paramArrayOfByte));
  }

  public void addSKI(X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    add(new XMLX509SKI(this._doc, paramX509Certificate));
  }

  public void add(XMLX509SKI paramXMLX509SKI)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramXMLX509SKI.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addSubjectName(String paramString)
  {
    add(new XMLX509SubjectName(this._doc, paramString));
  }

  public void addSubjectName(X509Certificate paramX509Certificate)
  {
    add(new XMLX509SubjectName(this._doc, paramX509Certificate));
  }

  public void add(XMLX509SubjectName paramXMLX509SubjectName)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramXMLX509SubjectName.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addCertificate(X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    add(new XMLX509Certificate(this._doc, paramX509Certificate));
  }

  public void addCertificate(byte[] paramArrayOfByte)
  {
    add(new XMLX509Certificate(this._doc, paramArrayOfByte));
  }

  public void add(XMLX509Certificate paramXMLX509Certificate)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramXMLX509Certificate.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addCRL(byte[] paramArrayOfByte)
  {
    add(new XMLX509CRL(this._doc, paramArrayOfByte));
  }

  public void add(XMLX509CRL paramXMLX509CRL)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramXMLX509CRL.getElement());
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addUnknownElement(Element paramElement)
  {
    if (this._state == 0)
    {
      this._constructionElement.appendChild(paramElement);
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public int lengthIssuerSerial()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
  }

  public int lengthSKI()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
  }

  public int lengthSubjectName()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
  }

  public int lengthCertificate()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
  }

  public int lengthCRL()
  {
    return length("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
  }

  public int lengthUnknownElement()
  {
    int i = 0;
    Node localNode;
    if ((localNode.getNodeType() == 1) && (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
      i++;
    return i;
  }

  public XMLX509IssuerSerial itemIssuerSerial(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509IssuerSerial", paramInt)) != null)
      return new XMLX509IssuerSerial(localElement, this._baseURI);
    return null;
  }

  public XMLX509SKI itemSKI(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509SKI", paramInt)) != null)
      return new XMLX509SKI(localElement, this._baseURI);
    return null;
  }

  public XMLX509SubjectName itemSubjectName(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509SubjectName", paramInt)) != null)
      return new XMLX509SubjectName(localElement, this._baseURI);
    return null;
  }

  public XMLX509Certificate itemCertificate(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509Certificate", paramInt)) != null)
      return new XMLX509Certificate(localElement, this._baseURI);
    return null;
  }

  public XMLX509CRL itemCRL(int paramInt)
    throws XMLSecurityException
  {
    Element localElement;
    if ((localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509CRL", paramInt)) != null)
      return new XMLX509CRL(localElement, this._baseURI);
    return null;
  }

  public Element itemUnknownElement(int paramInt)
  {
    return null;
  }

  public boolean containsIssuerSerial()
  {
    return lengthIssuerSerial() > 0;
  }

  public boolean containsSKI()
  {
    return lengthSKI() > 0;
  }

  public boolean containsSubjectName()
  {
    return lengthSubjectName() > 0;
  }

  public boolean containsCertificate()
  {
    return lengthCertificate() > 0;
  }

  public boolean containsCRL()
  {
    return lengthCRL() > 0;
  }

  public boolean containsUnknownElement()
  {
    return lengthUnknownElement() > 0;
  }

  public String getBaseLocalName()
  {
    return "X509Data";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.X509Data
 * JD-Core Version:    0.6.0
 */