package si.hermes.security.Collections;

import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import si.hermes.security.DigSigLoader;
import si.hermes.security.ESignDocException;
import si.hermes.security.ESignDocImpl;
import si.hermes.security.ISODateFormat;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;
import si.hermes.security.XAdES.EXAdESException;

public class TimestampProviderImpl
  implements ITimestampProvider
{
  static Log log = LogFactory.getLog(TimestampProviderImpl.class.getName());
  private String fCertificate;
  private String fServerUri = "TEST";
  private String fSerialNumber;
  private String fIssuerCN;
  private String fPassword;
  private Date fTSDate;
  private Element fNode;
  private VerifyStatus fStatus = null;
  private LinkedHashMap namespaces = new LinkedHashMap();
  private String fSoapAction;
  private final String cTimestampInfo = "<ts:TimeStampInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ts=\"http://www.entrust.com/schemas/timestamp-protocol-20020207\"><ts:Policy id=\"your policy uri here\" /><ts:Digest><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><ds:DigestValue>_VALUE_</ds:DigestValue></ts:Digest><ts:SerialNumber>_SERIAL_</ts:SerialNumber><ts:CreationTime>_TIME_</ts:CreationTime><ts:Nonce>_NONCE_</ts:Nonce></ts:TimeStampInfo>";

  protected void setSoapAction(String paramString)
  {
    this.fSoapAction = paramString;
  }

  public void setTestSignatureData(String paramString1, String paramString2, String paramString3)
  {
    this.fSerialNumber = paramString1;
    this.fIssuerCN = paramString2;
    this.fPassword = paramString3;
  }

  private NodeList getTimestampInfo(String paramString)
    throws ESignDocException
  {
    String str = (str = (str = "<ts:TimeStampInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ts=\"http://www.entrust.com/schemas/timestamp-protocol-20020207\"><ts:Policy id=\"your policy uri here\" /><ts:Digest><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><ds:DigestValue>_VALUE_</ds:DigestValue></ts:Digest><ts:SerialNumber>_SERIAL_</ts:SerialNumber><ts:CreationTime>_TIME_</ts:CreationTime><ts:Nonce>_NONCE_</ts:Nonce></ts:TimeStampInfo>".replaceFirst("_VALUE_", paramString)).replaceFirst("_SERIAL_", Utility.generateRandom(20))).replaceFirst("_TIME_", ISODateFormat.DateTime2ISOZ(new Date()));
    try
    {
      str = str.replaceFirst("_NONCE_", Utility.generateSecureRandom(20, null));
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new ESignDocException(36, "Cannot generate random number", localNoSuchAlgorithmException);
    }
    try
    {
      Document localDocument;
      return (localDocument = Utility.createDomDocumentFromString(str)).cloneNode(true).getChildNodes();
    }
    catch (Exception localException)
    {
    }
    throw new ESignDocException(16, localException);
  }

  private String CreateTimestampTest(String paramString)
    throws ESignDocException, ParserConfigurationException, HeadlessException, CertificateException, XMLSecurityException, SAXException, IOException, TransformerException, EXAdESException
  {
    if (log.isDebugEnabled())
      log.debug("TimestampProviderImpl.CreateTimestampTest ENTER (" + paramString + ")");
    if ((this.fSerialNumber == null) || (this.fIssuerCN == null))
      throw new ESignDocException(3, "Test signing certificate has not been set");
    String str = "TimestampInfo-" + Utility.generateRandom(20);
    SignatureImpl localSignatureImpl;
    (localSignatureImpl = new SignatureImpl()).setId("TimeStampToken-" + Utility.generateRandom(20));
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri("#" + str);
    localSignatureImpl.getSignedInfo().getReferences().Add(localReferenceImpl);
    XmlDataObjectImpl localXmlDataObjectImpl;
    (localXmlDataObjectImpl = new XmlDataObjectImpl()).setId(str);
    localXmlDataObjectImpl.setData(getTimestampInfo(paramString));
    localSignatureImpl.addObject(localXmlDataObjectImpl);
    localSignatureImpl.signWithCertificate(this.fSerialNumber, this.fIssuerCN, true, this.fPassword);
    if (log.isDebugEnabled())
      log.debug("TimestampProviderImpl.CreateTimestampTest EXIT");
    return localSignatureImpl.getToString();
  }

  protected String generateRequest(String paramString1, String paramString2)
    throws ParserConfigurationException, ESignDocException
  {
    throw new ESignDocException(26);
  }

  protected String RemoveSoapEnvelope(Element paramElement)
    throws IOException, SAXException, ESignDocException, ParserConfigurationException, TransformerException
  {
    if (paramElement == null)
      throw new ESignDocException(23);
    XObject localXObject;
    NodeList localNodeList;
    if (((localNodeList = (localXObject = XPathAPI.eval(paramElement, "//dsig:Signature", DigSigLoader.getDigSigPrefixResolver())).nodelist()) != null) && (localNodeList.getLength() != 0))
    {
      Node localNode = localNodeList.item(0);
      this.fNode = ((Element)localNode);
      return Utility.createStringFromDomElement(this.fNode);
    }
    throw new ESignDocException(23, Utility.createStringFromDomElement(paramElement));
  }

  protected void addNamespace(String paramString1, String paramString2)
  {
    this.namespaces.put(paramString1, paramString2);
  }

  public String CreateTimestamp(String paramString)
    throws ESignDocException, NoSuchAlgorithmException, ParserConfigurationException, SOAPException, IOException, SAXException, TransformerException
  {
    try
    {
      if ("TEST".equals(this.fServerUri))
        return CreateTimestampTest(paramString);
    }
    catch (Exception localException)
    {
      throw new ESignDocException(29, "Creating test timestamp signature failed", localException);
    }
    if (log.isDebugEnabled())
      log.debug("TimestampProviderImpl.CreateTimestamp ENTER (" + paramString + "), (" + this.fServerUri + ")");
    String str1 = null;
    str1 = Utility.generateSecureRandom(25, null);
    String str2 = generateRequest(paramString, str1);
    String str3 = this.fServerUri;
    MimeHeaders localMimeHeaders;
    (localMimeHeaders = new MimeHeaders()).addHeader("Content-Type", "text/xml");
    localMimeHeaders.addHeader("SOAPAction", this.fSoapAction);
    MessageFactory localMessageFactory;
    SOAPMessage localSOAPMessage1 = (localMessageFactory = MessageFactory.newInstance()).createMessage(localMimeHeaders, new ByteArrayInputStream(str2.getBytes()));
    SOAPConnection localSOAPConnection;
    SOAPMessage localSOAPMessage2 = (localSOAPConnection = SOAPConnectionFactory.newInstance().createConnection()).call(localSOAPMessage1, str3);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localSOAPMessage2.writeTo(localByteArrayOutputStream);
    if (log.isDebugEnabled())
      log.debug("TimestampProviderImpl.CreateTimestamp EXIT)");
    Document localDocument = Utility.createDomDocumentFromString(new String(localByteArrayOutputStream.toByteArray()));
    return RemoveSoapEnvelope(localDocument != null ? localDocument.getDocumentElement() : null);
  }

  public boolean getIsEncapsulated()
  {
    return false;
  }

  public String getTimestampCertificate()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificate;
  }

  public Date getTimestampDate()
    throws ESignDocException
  {
    if (this.fNode == null)
      throw new ESignDocException(28);
    return this.fTSDate;
  }

  public void setServerUri(String paramString)
  {
    if (!"".equals(paramString))
      this.fServerUri = paramString;
  }

  public VerifyStatus validateTimestamp(String paramString)
    throws IOException, ESignDocException, XMLSignatureException, XMLSecurityException, TransformerException
  {
    ESignDocImpl localESignDocImpl;
    Document localDocument;
    try
    {
      localESignDocImpl = new ESignDocImpl();
      localDocument = Utility.CreateDocument();
    }
    catch (Exception localException)
    {
      throw new ESignDocException(22, localException);
    }
    Object localObject = this.fNode.cloneNode(true);
    localObject = localDocument.importNode((Node)localObject, true);
    localDocument.appendChild((Node)localObject);
    localESignDocImpl.loadDom(localDocument);
    if (localESignDocImpl.getSignatures().getCount() != 1)
    {
      localVerifyStatus = new VerifyStatus("SIGNATURE UNKNOWN", "Only one timestamp signature is allowed");
      this.fStatus = localVerifyStatus;
      return localVerifyStatus;
    }
    Element localElement = Utility.getNode2(localObject = Utility.getNode2(localObject = Utility.getNode2(this.fNode, "Object", "http://www.w3.org/2000/09/xmldsig#"), "TimeStampInfo", "http://www.entrust.com/schemas/timestamp-protocol-20020207"), "CreationTime", "http://www.entrust.com/schemas/timestamp-protocol-20020207");
    if (localObject == null)
    {
      localVerifyStatus = new VerifyStatus("INVALID", "Invalid signature format.");
      this.fStatus = localVerifyStatus;
      return localVerifyStatus;
    }
    String str = Utility.getTextNodeValue(localElement);
    this.fTSDate = ISODateFormat.ISO2DateTime(str);
    if ((localElement = Utility.getNode2(localElement = Utility.getNode2((Element)localObject, "Digest", "http://www.entrust.com/schemas/timestamp-protocol-20020207"), "DigestValue", "http://www.w3.org/2000/09/xmldsig#")) == null)
    {
      localVerifyStatus = new VerifyStatus("INVALID", "Invalid signature format.");
      this.fStatus = localVerifyStatus;
      return localVerifyStatus;
    }
    if (!Utility.getTextNodeValue(localElement).equals(paramString))
    {
      localVerifyStatus = new VerifyStatus("INVALID", "Timestamp digest is invalid.Expected:" + paramString);
      this.fStatus = localVerifyStatus;
      return localVerifyStatus;
    }
    VerifyStatus localVerifyStatus = localESignDocImpl.getSignatures().getItem(0).Validate();
    this.fCertificate = localESignDocImpl.getSignatures().getItem(0).getSigningCertificateB64();
    this.fStatus = localVerifyStatus;
    return (VerifyStatus)localVerifyStatus;
  }

  public void LoadXml(Element paramElement)
    throws ESignDocException
  {
    this.fStatus = null;
    this.fNode = paramElement;
    Element localElement1;
    Element localElement2;
    String str = Utility.getTextNodeValue(localElement2 = Utility.getNode2(localElement1 = Utility.getNode2(localElement1 = Utility.getNode2(this.fNode, "Object", "http://www.w3.org/2000/09/xmldsig#"), "TimeStampInfo", "http://www.entrust.com/schemas/timestamp-protocol-20020207"), "CreationTime", "http://www.entrust.com/schemas/timestamp-protocol-20020207"));
    this.fTSDate = ISODateFormat.ISO2DateTime(str);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.TimestampProviderImpl
 * JD-Core Version:    0.6.0
 */