package si.hermes.security.Collections;

import java.awt.HeadlessException;
import java.io.IOException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.Manifest;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;
import si.hermes.security.CertificateChain;
import si.hermes.security.CertificateHelper;
import si.hermes.security.CertificateProvider;
import si.hermes.security.Dialogs.SelectCertificateDialog;
import si.hermes.security.DigSigLoader;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.IResolver;
import si.hermes.security.IResolverEntity;
import si.hermes.security.KeyInfo.IKeyInfo;
import si.hermes.security.KeyInfo.IKeyInfoX509Data;
import si.hermes.security.KeyInfo.KeyInfoImpl;
import si.hermes.security.PersistableImpl;
import si.hermes.security.ResolverEntityInfo;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.SignedInfo.SignedInfoImpl;
import si.hermes.security.Utility;
import si.hermes.security.VerifyStatus;
import si.hermes.security.XAdES.DataObjectFormatImpl;
import si.hermes.security.XAdES.EXAdESException;
import si.hermes.security.XAdES.IDataObjectFormat;
import si.hermes.security.XAdES.IObjectIdentifier;
import si.hermes.security.XAdES.IXAdES_A;
import si.hermes.security.XAdES.ObjectIdentifierImpl;
import si.hermes.security.XAdES.XAdES_AImpl;

public final class SignatureImpl extends PersistableImpl
  implements ISignature, IHSLSignature
{
  static Log log = LogFactory.getLog(SignatureImpl.class.getName());
  private static final long serialVersionUID = 1405654796282631825L;
  private String fBaseUri;
  private CertificateChain fCertificateChain;
  private String fCertificateIssuerDN;
  private String fCertificateSerialNumber;
  private String fCertificateSerialNumberHex;
  private String fCertificateSubjectDN;
  private String fDefaultTransform;
  private String fDisplayTransform = null;
  private String fDisplayTransformDigestEdurs;
  private String fDisplayTransformEdurs;
  private String fFriendlyName;
  private String fId;
  private boolean fIsCalculateManifestReferences = true;
  private IKeyInfo fKeyInfo = null;
  private boolean fKeyInfoPreserve;
  private IManifests fManifests = null;
  private IXmlDataObjectList fObjectList = null;
  private IResolverEntity fResolver = null;
  private String fSignatureValue = "";
  private String fSignatureValueId = "";
  private ISignedInfo fSignedInfo = null;
  private boolean fSignedInfoPreserve;
  private String fSigningCertificateB64;
  private VerifyStatus fStatus = null;
  private String fStyleSheetTransform;
  private char[] password = null;
  IXAdES_A fXAdES;
  boolean fXAdESPreserve = true;
  String fXAdESCertificateAlgorithm = "";

  public final int addObject(IXmlDataObject paramIXmlDataObject)
    throws ParserConfigurationException
  {
    return getObjectList().Add(paramIXmlDataObject);
  }

  public final void clearPassword()
  {
    this.password = "".toCharArray();
  }

  public final IXmlDataObject CreateXAdESElement()
    throws ESignDocException
  {
    XmlDataObjectImpl localXmlDataObjectImpl;
    Element localElement1 = (localXmlDataObjectImpl = new XmlDataObjectImpl()).GetXml(Utility.CreateElement(this.fNode.getOwnerDocument(), "Object", "http://www.w3.org/2000/09/xmldsig#"));
    Element localElement2 = this.fXAdES.GetXml(null);
    localElement2 = (Element)localElement1.getOwnerDocument().importNode(localElement2, true);
    this.fXAdES.LoadXml(localElement2);
    localElement1.appendChild(localElement2);
    return localXmlDataObjectImpl;
  }

  public final void emptySignature()
  {
    this.fKeyInfo = null;
    setSignatureValue("");
    for (int i = 0; i < getSignedInfo().getReferences().getCount(); i++)
      getSignedInfo().getReferences().getItem(i).setDigestValue("");
  }

  public final String getBaseUri()
  {
    if (this.fBaseUri == null)
      this.fBaseUri = "";
    return this.fBaseUri;
  }

  public final String getCertificateIssuerDN()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificateIssuerDN;
  }

  public final String getCertificateSerialNumber()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificateSerialNumber;
  }

  public final String getCertificateSerialNumberHex()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificateSerialNumberHex;
  }

  public final String getCertificateSubjectDN()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fCertificateSubjectDN;
  }

  public final boolean getDefaultDisplayTransform()
  {
    return "True".equals(this.fDefaultTransform);
  }

  public final String getDisplayTransformName()
  {
    if (this.fDisplayTransform != null)
      return this.fDisplayTransform;
    if (this.fDisplayTransformEdurs != null)
      return this.fDisplayTransformEdurs;
    if (this.fStyleSheetTransform != null)
      return this.fStyleSheetTransform;
    return "";
  }

  public final String getDisplayTransformStream()
    throws ESignDocException
  {
    String str;
    if ((str = getDisplayTransformName()) == "")
      return null;
    ManifestImpl localManifestImpl = new ManifestImpl();
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri(str);
    try
    {
      localManifestImpl.AddReference(localReferenceImpl);
    }
    catch (Exception localException1)
    {
      throw new ESignDocException(36, "Loading reference failed", localException1);
    }
    try
    {
      Manifest localManifest;
      XMLSignatureInput localXMLSignatureInput;
      return Base64.encode((localXMLSignatureInput = (localManifest = new Manifest(localManifestImpl.GetXml(), getBaseUri())).getReferencedContentBeforeTransformsItem(0)).getBytes());
    }
    catch (Exception localException2)
    {
    }
    throw new ESignDocException(36, "Getting reference failed", localException2);
  }

  public final String getFriendlyName()
  {
    return this.fFriendlyName;
  }

  public final String getId()
  {
    return this.fId;
  }

  public final IKeyInfo getKeyInfo()
  {
    if (this.fKeyInfo == null)
      this.fKeyInfo = new KeyInfoImpl();
    return this.fKeyInfo;
  }

  public final IKeyInfo getKeyInfo(boolean paramBoolean)
  {
    return this.fKeyInfo;
  }

  private final IManifests getManifests()
  {
    return this.fManifests;
  }

  public final IXmlDataObjectList getObjectList()
  {
    if (this.fObjectList == null)
      this.fObjectList = new XmlDataObjectListImpl();
    return this.fObjectList;
  }

  private final char[] getPassword()
  {
    return this.password;
  }

  public final IResolverEntity getResolverEntity()
  {
    return this.fResolver;
  }

  private final Element getSignatureProperty(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws ParserConfigurationException, ESignDocException
  {
    Element localElement = null;
    Object localObject1 = null;
    Object localObject2;
    for (int j = 0; j < getObjectList().getCount(); j++)
    {
      if (((localObject1 = Utility.getNodeList2(getObjectList().getItem(j).getData(), "SignatureProperties", "http://www.w3.org/2000/09/xmldsig#")) == null) || (((Node)localObject1).getChildNodes() == null))
        continue;
      for (int i = 0; i < ((Node)localObject1).getChildNodes().getLength(); i++)
      {
        localObject2 = ((Node)localObject1).getChildNodes().item(i);
        if (("SignatureProperty".equals(((Node)localObject2).getLocalName())) && ("http://www.w3.org/2000/09/xmldsig#".equals(((Node)localObject2).getNamespaceURI())) && ((localElement = Utility.getNode2((Element)localObject2, paramString, "urn:schemas-hermes-softlab-com:2003/09/Signatures")) != null))
          return localElement;
      }
    }
    if ((!paramBoolean1) && (paramBoolean2))
    {
      Object localObject3;
      if (localObject1 != null)
      {
        localObject2 = EnsureBaseElement(null, "SignatureProperty", "http://www.w3.org/2000/09/xmldsig#");
        if (((Node)localObject1).getOwnerDocument() != null)
          localObject2 = ((Node)localObject1).getOwnerDocument().importNode((Node)localObject2, true);
        localObject3 = EnsureElement((Element)localObject2, paramString, "urn:schemas-hermes-softlab-com:2003/09/Signatures");
        ((Node)localObject1).appendChild((Node)localObject2);
        localElement = (Element)localObject3;
        getObjectList().GetXml(this.fNode);
      }
      else
      {
        localObject3 = new XmlDataObjectImpl();
        localObject1 = EnsureBaseElement(null, "Object", "http://www.w3.org/2000/09/xmldsig#");
        if (this.fNode.getOwnerDocument() != null)
          localObject1 = this.fNode.getOwnerDocument().importNode((Node)localObject1, true);
        getObjectList().Add((IPersistable)localObject3);
        ((XmlDataObjectImpl)localObject3).LoadXml((Element)localObject1);
        localObject1 = ((XmlDataObjectImpl)localObject3).GetXml();
        getObjectList().GetXml(this.fNode);
        localObject2 = EnsureElement((Element)localObject1, "SignatureProperties", "http://www.w3.org/2000/09/xmldsig#");
        localObject2 = EnsureElement((Element)localObject2, "SignatureProperty", "http://www.w3.org/2000/09/xmldsig#");
        localElement = EnsureElement((Element)localObject2, paramString, "urn:schemas-hermes-softlab-com:2003/09/Signatures");
      }
    }
    return (Element)(Element)(Element)localElement;
  }

  public final String getSignatureValue()
  {
    return this.fSignatureValue;
  }

  public final String getSignatureValueId()
  {
    return this.fSignatureValueId;
  }

  public final ISignedInfo getSignedInfo()
  {
    if (this.fSignedInfo == null)
      this.fSignedInfo = new SignedInfoImpl();
    return this.fSignedInfo;
  }

  public final String getSigningCertificateB64()
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    return this.fSigningCertificateB64;
  }

  public final CertificateChain getSigningCertificateChain(boolean paramBoolean, String paramString)
    throws ESignDocException
  {
    if (this.fStatus == null)
      throw new ESignDocException(28);
    if (this.fCertificateChain == null)
    {
      Date localDate = (this.fXAdES != null) && (this.fXAdES.getSignatureTimeStamps().getCount() > 0) ? this.fXAdES.getSignatureTimeStamps().getItem(0).getTimestampDate() : new Date();
      this.fCertificateChain = new CertificateChain();
      this.fCertificateChain.init(getSigningCertificateB64(), localDate, paramBoolean, paramString);
    }
    return this.fCertificateChain;
  }

  public final ITimestamp getTimestamp()
  {
    if ((this.fXAdES != null) && (this.fXAdES.getSignatureTimeStamps().getCount() > 0))
      return this.fXAdES.getSignatureTimeStamps().getItem(0);
    return null;
  }

  private final Element getXAdESElement()
    throws ESignDocException
  {
    Element localElement = null;
    for (int i = 0; i < getObjectList().getCount(); i++)
      if ((localElement = Utility.getNode2(localElement = getObjectList().getItem(i).GetXml(null), "QualifyingProperties", "http://uri.etsi.org/01903/v1.1.1#")) != null)
        return localElement;
    return localElement;
  }

  protected final String getUrlFromXmlStyleSheetProcessingInstruction(String paramString)
  {
    int i;
    if ((i = paramString.indexOf("href")) != 0)
    {
      String str;
      if ((i = (str = paramString.substring(i + 4)).indexOf("\"")) == 0)
        return "";
      if ((i = (str = str.substring(i + 1)).indexOf("\"")) == 0)
        return "";
      return str = str.substring(1, i - 1);
    }
    return "";
  }

  private final boolean loadCertificateData(VerifyStatus paramVerifyStatus)
    throws ESignDocException, TransformerException, XMLSecurityException, CertificateEncodingException
  {
    this.fSigningCertificateB64 = "";
    this.fCertificateSerialNumber = "";
    this.fCertificateSerialNumberHex = "";
    this.fCertificateIssuerDN = "";
    this.fCertificateSubjectDN = "";
    IKeyInfoX509Data localIKeyInfoX509Data;
    if (((localIKeyInfoX509Data = (IKeyInfoX509Data)getKeyInfo().getClause("X509Data", false)) == null) || (localIKeyInfoX509Data.getCertificate().getCount() == 0))
    {
      paramVerifyStatus.setStatus("CREDENTIALS NOT FOUND");
      paramVerifyStatus.setReason("No X509 certificate was found");
      return false;
    }
    X509Certificate localX509Certificate = null;
    Element localElement = getKeyInfo().GetXml(null);
    KeyInfo localKeyInfo;
    if ((localX509Certificate = (localKeyInfo = new KeyInfo(localElement, getBaseUri())).getX509Certificate()) == null)
    {
      paramVerifyStatus.setStatus("CREDENTIALS NOT FOUND");
      paramVerifyStatus.setReason("No valid X509 certificate was found");
      return false;
    }
    this.fSigningCertificateB64 = Base64.encode(localX509Certificate.getEncoded());
    this.fCertificateSerialNumber = String.valueOf(localX509Certificate.getSerialNumber());
    this.fCertificateSerialNumberHex = Utility.getHexString(localX509Certificate.getSerialNumber()).toUpperCase();
    this.fCertificateSubjectDN = localX509Certificate.getSubjectDN().getName();
    this.fCertificateIssuerDN = localX509Certificate.getIssuerDN().getName();
    return true;
  }

  public final void setBaseUri(String paramString)
  {
    this.fBaseUri = paramString;
  }

  protected final void setCertificateSerialNumber(String paramString)
  {
    this.fCertificateSerialNumber = paramString;
  }

  public final void setDefaultDisplayTransform(boolean paramBoolean)
  {
    this.fDefaultTransform = (paramBoolean ? "True" : "False");
  }

  public final void setDisplayTransformName(String paramString)
  {
    if ("".equals(paramString))
      paramString = null;
    this.fDisplayTransform = paramString;
  }

  public final void setFriendlyName(String paramString)
  {
    this.fFriendlyName = paramString;
  }

  public final void setId(String paramString)
  {
    this.fId = paramString;
  }

  public final void setManifests(IManifests paramIManifests)
  {
    this.fManifests = paramIManifests;
  }

  public final void setPassword(char[] paramArrayOfChar)
  {
    this.password = paramArrayOfChar;
  }

  public final void setResolverEntity(IResolverEntity paramIResolverEntity)
  {
    this.fResolver = paramIResolverEntity;
  }

  public final void setSignatureValue(String paramString)
  {
    this.fSignatureValue = paramString;
  }

  public final void setSignatureValueId(String paramString)
  {
    this.fSignatureValueId = paramString;
  }

  public final void setSignedInfo(ISignedInfo paramISignedInfo)
  {
    this.fSignedInfo = paramISignedInfo;
    this.fSignedInfoPreserve = false;
  }

  public final void setTimestamp(ITimestamp paramITimestamp)
    throws ESignDocException, ParserConfigurationException
  {
    if (this.fXAdES == null)
      this.fXAdES = new XAdES_AImpl();
    if (this.fXAdES.getSignatureTimeStamps().getCount() == 0)
      this.fXAdES.addTimestamp(paramITimestamp);
    else
      this.fXAdES.getSignatureTimeStamps().setItem(0, paramITimestamp);
    this.fXAdESPreserve = false;
    if (this.fNode != null)
      paramITimestamp.GetXml(Utility.CreateElement(this.fNode.getOwnerDocument(), "SignatureTimeStamp", "http://uri.etsi.org/01903/v1.1.1#"));
  }

  public final void signWithCertificateUI(String paramString1, String paramString2)
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException
  {
    try
    {
      X509Certificate[] arrayOfX509Certificate;
      if ((arrayOfX509Certificate = CertificateProvider.getAllCertificatesWithPrivateKey(paramString1, paramString2, this.password)).length == 0)
        throw new ESignDocException(0, "<CertificateNotFound StoreType=\"" + CertificateProvider.getShortProviderName() + "\"/>");
      X509Certificate localX509Certificate;
      String str1 = Utility.getHexString((localX509Certificate = SelectCertificateDialog.SelectCertificateModal(arrayOfX509Certificate, 1)).getSerialNumber()).toUpperCase();
      String str2 = Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
      signWithCertificate(str1, str2, true, null);
      return;
    }
    finally
    {
      clearPassword();
    }
  }

  public final void sign()
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException
  {
    try
    {
      X509Certificate[] arrayOfX509Certificate;
      if ((arrayOfX509Certificate = CertificateProvider.getAllCertificatesWithPrivateKey("", "", this.password)).length == 0)
        throw new ESignDocException(0, "<CertificateNotFound StoreType=\"" + CertificateProvider.getShortProviderName() + "\"/>");
      X509Certificate localX509Certificate;
      String str1 = Utility.getHexString((localX509Certificate = SelectCertificateDialog.SelectCertificateModal(arrayOfX509Certificate, 0)).getSerialNumber()).toUpperCase();
      String str2 = Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
      signWithCertificate(str1, str2, true, null);
      return;
    }
    finally
    {
      clearPassword();
    }
  }

  public final void signWithCertificate(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new ESignDocException(3);
    Constants.setSignatureSpecNSprefix("");
    try
    {
      CertificateHelper localCertificateHelper = CertificateProvider.findCertificate(paramString1, paramString2, paramString3, this.password);
      if (!new String("").equals(this.fXAdESCertificateAlgorithm))
        getXAdES().createSignedData(this, Base64.encode(localCertificateHelper.certificate.getEncoded()), this.fXAdESCertificateAlgorithm, new Date());
      if ("DSA".equalsIgnoreCase(localCertificateHelper.certificate.getPublicKey().getAlgorithm()))
        getSignedInfo().setSignatureMethod("http://www.w3.org/2000/09/xmldsig#dsa-sha1");
      while (getKeyInfo().getCount() != 0)
        getKeyInfo().RemoveClause("", 0);
      Element localElement;
      if ((localElement = GetXml()).getParentNode() == null)
        throw new ESignDocException(19, "Null parent node!");
      if (this.fIsCalculateManifestReferences)
        DigSigLoader.digestManifestReferences(getSignedInfo().GetXml(), getBaseUri(), (ResourceResolverSpi)getResolverEntity(), this.fManifests);
      Manifest localManifest = new Manifest(localElement.getOwnerDocument());
      if (getResolverEntity() != null)
        localManifest.addResourceResolver((ResourceResolverSpi)getResolverEntity());
      digestReferences(this.fIsCalculateManifestReferences, localManifest);
      getSignedInfo().GetXml();
      DigSigLoader.sign(this, localCertificateHelper.privatekey, localCertificateHelper.certificate);
      if (paramBoolean)
        DigSigLoader.appendX509Data(this, localCertificateHelper.privatekey, localCertificateHelper.certificate);
      GetXml();
      if (this.fManifests != null)
        this.fManifests.LoadXml(this.fNode.getOwnerDocument().getDocumentElement());
      return;
    }
    finally
    {
      clearPassword();
    }
  }

  public final VerifyStatus status()
    throws XMLSignatureException, IOException, ESignDocException, XMLSecurityException, TransformerException
  {
    if (this.fStatus != null)
      return this.fStatus;
    return Validate();
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      if (paramBoolean)
      {
        this.fStatus = null;
        this.fCertificateChain = null;
      }
      this.fNode = EnsureBaseElement(this.fNode, "Signature", "http://www.w3.org/2000/09/xmldsig#");
      this.fId = EnsureAttribute(this.fNode, "Id", this.fId, paramBoolean, false);
      Object localObject2;
      if (paramBoolean)
      {
        localElement1 = EnsureElement(this.fNode, "SignedInfo", "http://www.w3.org/2000/09/xmldsig#");
        getSignedInfo().LoadXml(localElement1);
      }
      else
      {
        if (this.fSignedInfoPreserve)
        {
          getSignedInfo().GetXml(null);
          break label212;
        }
        RemoveChildren(this.fNode, "SignedInfo", "http://www.w3.org/2000/09/xmldsig#");
        localObject1 = this.fNode.getFirstChild();
        localObject2 = getSignedInfo().GetXml(null);
        if (this.fNode.getOwnerDocument() != ((Node)localObject2).getOwnerDocument())
        {
          localObject2 = this.fNode.getOwnerDocument().importNode((Node)localObject2, true);
          getSignedInfo().LoadXml((Element)localObject2);
        }
        this.fNode.insertBefore((Node)localObject2, (Node)localObject1);
      }
      this.fSignedInfoPreserve = true;
      label212: Element localElement1 = EnsureElement(this.fNode, "SignatureValue", "http://www.w3.org/2000/09/xmldsig#");
      this.fSignatureValue = EnsureValue(localElement1, this.fSignatureValue, paramBoolean);
      this.fSignatureValueId = EnsureAttribute(localElement1, "Id", this.fSignatureValueId, paramBoolean, false);
      if (paramBoolean)
      {
        if ((localElement1 = EnsureElement(this.fNode, "KeyInfo", "http://www.w3.org/2000/09/xmldsig#", false)) == null)
          break label442;
        getKeyInfo().LoadXml(localElement1);
      }
      else
      {
        if (this.fKeyInfo == null)
          break label442;
        if (this.fKeyInfoPreserve)
        {
          getKeyInfo().GetXml(null);
          break label442;
        }
        RemoveChildren(this.fNode, "KeyInfo", "http://www.w3.org/2000/09/xmldsig#");
        localObject1 = (localObject1 = this.fNode.getFirstChild()).getNextSibling();
        localObject2 = getKeyInfo().GetXml(null);
        if (this.fNode.getOwnerDocument() != ((Node)localObject2).getOwnerDocument())
        {
          localObject2 = this.fNode.getOwnerDocument().importNode((Node)localObject2, true);
          getKeyInfo().LoadXml((Element)localObject2);
        }
        this.fNode.insertBefore((Node)localObject2, ((Node)localObject1).getNextSibling());
      }
      this.fKeyInfoPreserve = true;
      label442: if (paramBoolean)
        getObjectList().LoadXml(this.fNode);
      else
        getObjectList().GetXml(this.fNode);
      Object localObject1 = getXAdESElement();
      if (paramBoolean)
      {
        if (localObject1 != null)
        {
          this.fXAdES = new XAdES_AImpl();
          this.fXAdES.LoadXml((Element)localObject1);
          this.fXAdESPreserve = true;
        }
        else
        {
          this.fXAdES = null;
        }
      }
      else if (localObject1 != null)
      {
        if (this.fXAdESPreserve)
        {
          this.fXAdES.GetXml(null);
        }
        else
        {
          localObject2 = (Element)((Element)localObject1).getParentNode();
          RemoveChildren((Element)localObject2, "QualifyingProperties", "http://uri.etsi.org/01903/v1.1.1#");
          if (this.fXAdES != null)
          {
            Element localElement2 = this.fXAdES.GetXml(null);
            if (((Element)localObject2).getOwnerDocument() != localElement2.getOwnerDocument())
            {
              localElement2 = (Element)((Element)localObject2).getOwnerDocument().importNode(localElement2, true);
              this.fXAdES.LoadXml(localElement2);
            }
            ((Element)localObject2).appendChild(localElement2);
          }
        }
      }
      else if (this.fXAdES != null)
      {
        getObjectList().Add(CreateXAdESElement());
        getObjectList().GetXml(this.fNode);
      }
      SynchronizeHslSignature(paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }

  protected final void SynchronizeHslSignature(boolean paramBoolean)
  {
    try
    {
      Element localElement;
      if ((localElement = getSignatureProperty("DisplayTransform", paramBoolean, this.fDisplayTransform != null)) != null)
        if ((!paramBoolean) && (this.fDisplayTransform == null))
        {
          localElement.getParentNode().getParentNode().removeChild(localElement.getParentNode());
        }
        else
        {
          this.fDisplayTransform = EnsureAttribute(localElement, "transform", this.fDisplayTransform, paramBoolean);
          this.fDefaultTransform = EnsureAttribute(localElement, "default", this.fDefaultTransform, paramBoolean, false);
        }
      if ((localElement = getSignatureProperty("FriendlyName", paramBoolean, this.fFriendlyName != null)) != null)
        if ((!paramBoolean) && (this.fFriendlyName == null))
          localElement.getParentNode().getParentNode().removeChild(localElement.getParentNode());
        else
          this.fFriendlyName = EnsureAttribute(localElement, "name", this.fFriendlyName, paramBoolean);
      Object localObject;
      this.fDisplayTransformEdurs = localElement.getAttribute("transform");
      this.fDisplayTransformEdurs = null;
      this.fDisplayTransformDigestEdurs = ((localElement = (this.fNode.getParentNode() != null) && (this.fNode.getParentNode().getNodeType() == 1) ? Utility.getNode2(localObject = Utility.getNode2((Element)this.fNode.getParentNode(), "Presentation", "urn:schemas-hermes-softlab-com:2003/09/Signatures"), "DisplayTransformDigest", "urn:schemas-hermes-softlab-com:2003/09/Signatures") : null) != null ? localElement.getAttribute("digest") : null);
      NodeList localNodeList = this.fNode.getOwnerDocument().getChildNodes();
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        String str = ((ProcessingInstruction)localObject).getData();
        this.fStyleSheetTransform = getUrlFromXmlStyleSheetProcessingInstruction(str);
      }
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public final VerifyStatus Validate()
    throws IOException, ESignDocException, XMLSignatureException, XMLSecurityException, TransformerException
  {
    Element localElement = GetXml();
    if ((this.fSignatureValue == null) || (this.fSignatureValue.equals("")))
      return new VerifyStatus("EMPTY", "");
    VerifyStatus localVerifyStatus1 = new VerifyStatus();
    try
    {
      if (!loadCertificateData(localVerifyStatus1))
      {
        log.warn("Certificate data failed.");
        return localVerifyStatus1;
      }
      log.info("Certificate data ok.");
    }
    catch (Exception localException)
    {
      localVerifyStatus1.setStatus("INVALID");
      localVerifyStatus1.setReason(localException.getLocalizedMessage());
    }
    XMLSignature localXMLSignature;
    VerifyStatus localVerifyStatus2;
    if (((localVerifyStatus2 = DigSigLoader.verify(localXMLSignature = new XMLSignature(localElement, getBaseUri()), getBaseUri(), (ResourceResolverSpi)getResolverEntity(), this.fIsCalculateManifestReferences)) != null) && (localVerifyStatus2.getStatus() == "VALID") && (this.fDisplayTransform != null) && (this.fDisplayTransformEdurs != null))
    {
      ReferenceImpl localReferenceImpl;
      (localReferenceImpl = new ReferenceImpl()).setUri(this.fDisplayTransformEdurs);
      localReferenceImpl.setDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1");
      byte[] arrayOfByte = Utility.calculateDigest(localReferenceImpl);
      localReferenceImpl.setDigestValue(Base64.encode(arrayOfByte));
      if (localReferenceImpl.getDigestValue() != this.fDisplayTransformDigestEdurs)
      {
        localVerifyStatus2.setStatus("INVALID");
        localVerifyStatus2.setReason(this.fDisplayTransformEdurs);
      }
    }
    this.fStatus = localVerifyStatus2;
    return localVerifyStatus2;
  }

  public final String getXAdESCertificateDigestAlgorithm()
  {
    return this.fXAdESCertificateAlgorithm;
  }

  public final void setXAdESCertificateDigestAlgorithm(String paramString)
  {
    this.fXAdESCertificateAlgorithm = (("sha1".equalsIgnoreCase(paramString)) || ("sha-1".equalsIgnoreCase(paramString)) ? "http://www.w3.org/2000/09/xmldsig#sha1" : paramString);
  }

  public final IXAdES_A getXAdES()
    throws ESignDocException
  {
    if (this.fXAdES == null)
    {
      this.fXAdES = new XAdES_AImpl();
      if (this.fNode != null)
        this.fXAdES.GetXml(Utility.CreateElement(this.fNode.getOwnerDocument(), "QualifyingProperties", "http://uri.etsi.org/01903/v1.1.1#"));
    }
    return this.fXAdES;
  }

  public final void setXAdES(IXAdES_A paramIXAdES_A)
    throws ESignDocException
  {
    this.fXAdES = paramIXAdES_A;
    this.fXAdESPreserve = false;
    if (this.fNode != null)
      paramIXAdES_A.GetXml(Utility.CreateElement(this.fNode.getOwnerDocument(), "QualifyingProperties", "http://uri.etsi.org/01903/v1.1.1#"));
  }

  public final void calculateManifestReferences(boolean paramBoolean)
  {
    this.fIsCalculateManifestReferences = paramBoolean;
  }

  public final void addEntity(String paramString1, String paramString2)
    throws Base64DecodingException
  {
    if (this.fResolver == null)
      this.fResolver = new ResolverEntityInfo();
    getResolverEntity().getResolver().setEntity(paramString1, Base64.decode(paramString2));
  }

  public final void addEntity(String paramString, byte[] paramArrayOfByte)
    throws Base64DecodingException
  {
    if (this.fResolver == null)
      this.fResolver = new ResolverEntityInfo();
    getResolverEntity().getResolver().setEntity(paramString, paramArrayOfByte);
  }

  private void digestReferences(boolean paramBoolean, Manifest paramManifest)
    throws TransformerException, XMLSecurityException, ESignDocException, IOException
  {
    if (this.fIsCalculateManifestReferences)
      DigSigLoader.digestManifestReferences(getSignedInfo().GetXml(), getBaseUri(), (ResourceResolverSpi)getResolverEntity(), this.fManifests);
    for (int i = 0; i < getSignedInfo().getCount(); i++)
      DigSigLoader.calculateReferenceDigest(getSignedInfo().getReferences().getItem(i), getBaseUri(), paramManifest);
  }

  public final String calculateManifestReferenceHash()
    throws TransformerException, XMLSecurityException, ESignDocException, IOException
  {
    Element localElement;
    if ((localElement = GetXml()).getParentNode() == null)
      throw new ESignDocException(19, "Null parent node!");
    Manifest localManifest = new Manifest(localElement.getOwnerDocument());
    Constants.setSignatureSpecNSprefix("");
    if (getResolverEntity() != null)
      localManifest.addResourceResolver((ResourceResolverSpi)getResolverEntity());
    digestReferences(this.fIsCalculateManifestReferences, localManifest);
    if (this.fManifests != null)
      this.fManifests.LoadXml(localElement.getOwnerDocument().getDocumentElement());
    String str1 = "";
    for (int i = 0; i < getSignedInfo().getCount(); i++)
    {
      IReference localIReference;
      String str2;
      String str3 = Utility.getFragmentURI(str2 = (localIReference = getSignedInfo().getReferences().getItem(i)).getUri());
      int j = 1;
      if ((!"".equals(str3)) && (this.fManifests != null))
        for (int k = 0; k < this.fManifests.getCount(); k++)
        {
          if (!str3.equals(this.fManifests.getItem(k).getId()))
            continue;
          for (int m = 0; m < this.fManifests.getItem(k).getReferences().getCount(); m++)
          {
            str1 = str1 + this.fManifests.getItem(k).getReferences().getItem(m).getUri() + '|' + this.fManifests.getItem(k).getReferences().getItem(m).getDigestValue() + '|';
            j = 0;
          }
        }
      if ((this.fXAdES != null) && (this.fXAdES.getSignedId() != null) && (!this.fXAdES.getSignedId().equals("")) && (this.fXAdES.getSignedId().equals(str3)))
        j = 0;
      if (j == 0)
        continue;
      str1 = str1 + str2 + '|' + getSignedInfo().getReferences().getItem(i).getDigestValue() + '|';
    }
    return Utility.hashSHA1(str1.getBytes());
  }

  public final void addAttachmentUri(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws ESignDocException
  {
    if ("".equals(this.fXAdESCertificateAlgorithm))
      throw new ESignDocException(6, "XAdESCertificateAlgorithm");
    int i = 0;
    for (int j = 0; j < getSignedInfo().getReferences().getCount(); j++)
    {
      if (!paramString1.equals(getSignedInfo().getReferences().getItem(j).getUri()))
        continue;
      i = 1;
    }
    if (i == 0)
      throw new ESignDocException(31, paramString1);
    DataObjectFormatImpl localDataObjectFormatImpl;
    (localDataObjectFormatImpl = new DataObjectFormatImpl()).setDescription(paramString2);
    localDataObjectFormatImpl.setMimeType(paramString5);
    localDataObjectFormatImpl.setEncoding(paramString3);
    localDataObjectFormatImpl.setObjectIdentifier(new ObjectIdentifierImpl());
    localDataObjectFormatImpl.getObjectIdentifier().setIdentifier(paramString4);
    localDataObjectFormatImpl.setObjectReference(paramString1);
    getXAdES().addDataObjectFormat(localDataObjectFormatImpl);
  }

  public final void addAttachment(String paramString1, String paramString2, String paramString3, String paramString4)
    throws ESignDocException, TransformerException, ParserConfigurationException
  {
    if ("".equals(this.fXAdESCertificateAlgorithm))
      throw new ESignDocException(6, "XAdESCertificateAlgorithm");
    XmlDataObjectImpl localXmlDataObjectImpl;
    (localXmlDataObjectImpl = new XmlDataObjectImpl()).setId("Object-" + Utility.generateRandom(30));
    localXmlDataObjectImpl.setEncoding("http://www.w3.org/2000/09/xmldsig#base64");
    localXmlDataObjectImpl.setMimeType(paramString4);
    localXmlDataObjectImpl.setDataNode(Utility.CreateDocument().createTextNode(paramString1));
    addObject(localXmlDataObjectImpl);
    getSignedInfo().AddReferenceUri("#" + localXmlDataObjectImpl.getId());
    addAttachmentUri("#" + localXmlDataObjectImpl.getId(), paramString2, localXmlDataObjectImpl.getEncoding(), paramString3, localXmlDataObjectImpl.getMimeType());
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.SignatureImpl
 * JD-Core Version:    0.6.0
 */