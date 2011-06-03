package si.hermes.security.KeyInfo;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import si.hermes.security.Collections.IXmlStringList;
import si.hermes.security.Collections.XmlStringListImpl;
import si.hermes.security.DigSigLoader;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;

public final class KeyInfoX509DataImpl extends PersistableImpl
  implements IKeyInfoX509Data
{
  private static final long serialVersionUID = -1920638361997674305L;
  IXmlStringList fIssuerSerial;
  IXmlStringList fIssuerName;
  IXmlStringList fSubjectName;
  IXmlStringList fSKI;
  IXmlStringList fCertificate;
  IXmlStringList fCRL;

  public final IXmlStringList getCertificate()
    throws ESignDocException
  {
    if (this.fCertificate == null)
      this.fCertificate = new XmlStringListImpl();
    return this.fCertificate;
  }

  public final IXmlStringList getCRL()
    throws ESignDocException
  {
    if (this.fCRL == null)
      this.fCRL = new XmlStringListImpl();
    return this.fCRL;
  }

  public final IXmlStringList getIssuerName()
    throws ESignDocException
  {
    if (this.fIssuerName == null)
      this.fIssuerName = new XmlStringListImpl();
    return this.fIssuerName;
  }

  public final IXmlStringList getIssuerSerial()
    throws ESignDocException
  {
    if (this.fIssuerSerial == null)
      this.fIssuerSerial = new XmlStringListImpl();
    return this.fIssuerSerial;
  }

  public final IXmlStringList getSKI()
    throws ESignDocException
  {
    if (this.fSKI == null)
      this.fSKI = new XmlStringListImpl();
    return this.fSKI;
  }

  public final IXmlStringList getSubjectName()
    throws ESignDocException
  {
    if (this.fSubjectName == null)
      this.fSubjectName = new XmlStringListImpl();
    return this.fSubjectName;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      throw new ESignDocException(16, localParserConfigurationException);
    }
    if (paramBoolean)
    {
      this.fCertificate = new XmlStringListImpl();
      this.fCertificate.SetFilterName("X509Certificate", "http://www.w3.org/2000/09/xmldsig#");
      this.fCertificate.LoadXml(this.fNode);
      this.fCRL = new XmlStringListImpl();
      this.fCRL.SetFilterName("X509CRL", "http://www.w3.org/2000/09/xmldsig#");
      this.fCRL.LoadXml(this.fNode);
      this.fSKI = new XmlStringListImpl();
      this.fSKI.SetFilterName("X509SKI", "http://www.w3.org/2000/09/xmldsig#");
      this.fSKI.LoadXml(this.fNode);
      this.fSubjectName = new XmlStringListImpl();
      this.fSubjectName.SetFilterName("X509SubjectName", "http://www.w3.org/2000/09/xmldsig#");
      this.fSubjectName.LoadXml(this.fNode);
      this.fIssuerName = new XmlStringListImpl();
      this.fIssuerName.SetXPath("./dsig:X509IssuerSerial/dsig:X509IssuerName", DigSigLoader.getDigSigPrefixResolver());
      this.fIssuerName.LoadXml(this.fNode);
      this.fIssuerSerial = new XmlStringListImpl();
      this.fIssuerSerial.SetXPath("./dsig:X509IssuerSerial/dsig:X509SerialNumber", DigSigLoader.getDigSigPrefixResolver());
      this.fIssuerSerial.LoadXml(this.fNode);
    }
  }

  public final void AddCertificate(String paramString)
    throws ESignDocException, ParserConfigurationException
  {
    this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    Element localElement;
    Utility.setTextNodeValue(localElement = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509Certificate"), paramString);
    this.fNode.appendChild(localElement);
    Synchronize(true);
  }

  public final void AddCRL(String paramString)
    throws ESignDocException, ParserConfigurationException
  {
    this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    Element localElement;
    Utility.setTextNodeValue(localElement = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509CRL"), paramString);
    this.fNode.appendChild(localElement);
    Synchronize(true);
  }

  public final void AddIssuerSerial(String paramString1, String paramString2)
    throws ESignDocException, ParserConfigurationException
  {
    this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    Element localElement1 = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
    this.fNode.appendChild(localElement1);
    Element localElement2 = localElement1;
    Utility.setTextNodeValue(localElement1 = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509IssuerName"), paramString1);
    localElement2.appendChild(localElement1);
    Utility.setTextNodeValue(localElement1 = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber"), paramString2);
    localElement2.appendChild(localElement1);
    Synchronize(true);
  }

  public final void AddSKI(String paramString)
    throws ESignDocException, ParserConfigurationException
  {
    this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    Element localElement;
    Utility.setTextNodeValue(localElement = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509SKI"), paramString);
    this.fNode.appendChild(localElement);
    Synchronize(true);
  }

  public final void AddSubjectName(String paramString)
    throws ESignDocException, ParserConfigurationException
  {
    this.fNode = EnsureBaseElement(this.fNode, "X509Data", "http://www.w3.org/2000/09/xmldsig#");
    Element localElement;
    Utility.setTextNodeValue(localElement = this.fNode.getOwnerDocument().createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName"), paramString);
    this.fNode.appendChild(localElement);
    Synchronize(true);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.KeyInfoX509DataImpl
 * JD-Core Version:    0.6.0
 */