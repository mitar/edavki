package si.hermes.security.Collections;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Manifest;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.InvalidTransformException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.CertificateChain;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;
import si.hermes.security.XAdES.EXAdESException;

public class BaseTimestampImpl extends PersistableImpl
  implements ITimestamp
{
  private static final long serialVersionUID = -2466778818199050423L;
  private ITimestampProvider fProvider;
  protected String fBaseElement;
  private String fTSEncapsulated;
  private String fId;
  private IHashDataInfos fHashDataInfos;
  private Element fTSSignature;
  private CertificateChain fCertificateChain = null;
  private Date fDatetime;
  private String fCertificate;
  private ITransformChain fTransforms;
  private VerifyStatus fStatus = null;
  public static ITimestamp tsInternal = null;

  public void CreateTimestamp(String paramString)
    throws NoSuchAlgorithmException, ESignDocException, ParserConfigurationException, SOAPException, IOException, XMLSecurityException, FactoryConfigurationError, SAXException, TransformerException, EXAdESException
  {
    getProvider().setServerUri(paramString);
    tsInternal = this;
    String str = getProvider().CreateTimestamp(getReferenceDigest());
    if (getProvider().getIsEncapsulated())
    {
      this.fTSEncapsulated = str;
      return;
    }
    Element localElement = (Element)Utility.createDomDocumentFromString(str).getDocumentElement().cloneNode(true);
    if ((this.fNode != null) && (this.fNode.getOwnerDocument() != null))
      localElement = (Element)this.fNode.getOwnerDocument().importNode(localElement, true);
    this.fTSSignature = localElement;
    getProvider().LoadXml(this.fTSSignature);
  }

  public CertificateChain getSigningCertificateChain(boolean paramBoolean, String paramString)
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    if (this.fCertificateChain == null)
    {
      this.fCertificateChain = new CertificateChain();
      this.fCertificateChain.init(getTimestampCertificate(), getTimestampDate(), paramBoolean, paramString);
    }
    return this.fCertificateChain;
  }

  private static byte[] calculateDigest(Element paramElement, IHashDataInfo paramIHashDataInfo)
    throws ESignDocException, XMLSecurityException
  {
    TransformXades localTransformXades = null;
    if (paramIHashDataInfo.getTransforms().getCount() > 0)
      localTransformXades = new TransformXades(paramIHashDataInfo.getTransforms().GetXml(), "");
    Manifest localManifest;
    (localManifest = new Manifest(paramElement.getOwnerDocument())).addDocument("", paramIHashDataInfo.getUri(), localTransformXades, "http://www.w3.org/2000/09/xmldsig#sha1", null, null);
    Reference localReference;
    return (localReference = localManifest.item(0)).getReferencedBytes();
  }

  public String getReferenceDigest()
    throws ESignDocException, XMLSecurityException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException, IOException
  {
    if (getHashDataInfos().getCount() == 0)
      throw new ESignDocException(21);
    MessageDigest localMessageDigest;
    try
    {
      String str;
      if ((str = JCEMapper.getProviderId()) == null)
        localMessageDigest = MessageDigest.getInstance("SHA-1");
      else
        localMessageDigest = MessageDigest.getInstance("SHA-1", str);
    }
    catch (Exception localException)
    {
      throw new ESignDocException(4, localException);
    }
    for (int i = 0; i < getHashDataInfos().getCount(); i++)
      localMessageDigest.update(calculateDigest(this.fNode, getHashDataInfos().getItem(i)));
    byte[] arrayOfByte;
    return Base64.encode(arrayOfByte = localMessageDigest.digest());
  }

  public Element getNode()
  {
    return this.fTSSignature;
  }

  public ITimestampProvider getProvider()
  {
    if (this.fProvider == null)
      this.fProvider = new TimestampProviderImpl();
    return this.fProvider;
  }

  public Date getTimestampDate()
    throws ESignDocException
  {
    return this.fDatetime;
  }

  public String getTimestampCertificate()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificate;
  }

  public ITransformChain getTransforms()
  {
    if (this.fTransforms == null)
      this.fTransforms = new TransformChainHashDataInfoImpl();
    return this.fTransforms;
  }

  public String getValue()
  {
    return this.fTSEncapsulated;
  }

  public void setNode(Element paramElement)
  {
    this.fTSSignature = paramElement;
  }

  public void setProvider(ITimestampProvider paramITimestampProvider)
  {
    this.fProvider = paramITimestampProvider;
  }

  public void setValue(String paramString)
  {
    this.fTSEncapsulated = paramString;
  }

  protected void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      if (paramBoolean)
      {
        this.fStatus = null;
        this.fCertificateChain = null;
      }
      this.fNode = EnsureBaseElement(this.fNode, this.fBaseElement, "http://uri.etsi.org/01903/v1.1.1#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean);
      if (paramBoolean)
        getHashDataInfos().LoadXml(this.fNode);
      else
        getHashDataInfos().GetXml(this.fNode);
      Element localElement2;
      Element localElement1;
      if (((localElement2 = EnsureElement(this.fNode, "EncapsulatedTimeStamp", "http://uri.etsi.org/01903/v1.1.1#", false)) != null) || ((this.fTSEncapsulated != null) && (!"".equals(this.fTSEncapsulated))))
      {
        RemoveChildren(this.fNode, "XMLTimeStamp", "http://uri.etsi.org/01903/v1.1.1#");
        localElement1 = EnsureElement(this.fNode, "EncapsulatedTimeStamp", "http://uri.etsi.org/01903/v1.1.1#");
        this.fTSEncapsulated = EnsureValue(localElement1, this.fTSEncapsulated, paramBoolean);
      }
      else
      {
        localElement1 = EnsureElement(this.fNode, "XMLTimeStamp", "http://uri.etsi.org/01903/v1.1.1#");
        if ((paramBoolean) || (this.fTSSignature != null))
          this.fTSSignature = EnsureChildNode(localElement1, this.fTSSignature, paramBoolean);
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  public VerifyStatus getStatus()
    throws XMLSignatureException, IOException, ESignDocException, XMLSecurityException, TransformerException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException
  {
    if (this.fStatus == null)
      return validate();
    return this.fStatus;
  }

  public VerifyStatus validate()
    throws IOException, ESignDocException, XMLSignatureException, XMLSecurityException, TransformerException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException
  {
    getProvider().LoadXml(this.fTSSignature);
    this.fStatus = getProvider().validateTimestamp(getReferenceDigest());
    this.fCertificate = getProvider().getTimestampCertificate();
    this.fDatetime = getProvider().getTimestampDate();
    return this.fStatus;
  }

  public int addHashDataInfo(IHashDataInfo paramIHashDataInfo)
  {
    return getHashDataInfos().Add(paramIHashDataInfo);
  }

  public int addHashDataInfoUri(String paramString)
  {
    HashDataInfoImpl localHashDataInfoImpl;
    (localHashDataInfoImpl = new HashDataInfoImpl()).setUri(paramString);
    return addHashDataInfo(localHashDataInfoImpl);
  }

  public IHashDataInfos getHashDataInfos()
  {
    if (this.fHashDataInfos == null)
      this.fHashDataInfos = new HashDataInfosImpl(this.fBaseElement);
    return this.fHashDataInfos;
  }

  public void setId(String paramString)
  {
    this.fId = paramString;
  }

  public String getId()
  {
    return this.fId;
  }

  static class TransformXades extends Transforms
  {
    public TransformXades(Element paramElement, String paramString)
      throws DOMException, XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
    {
      super(paramString);
    }

    public String getBaseNamespace()
    {
      return "http://uri.etsi.org/01903/v1.1.1#";
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.BaseTimestampImpl
 * JD-Core Version:    0.6.0
 */