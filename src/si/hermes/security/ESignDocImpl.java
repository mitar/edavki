package si.hermes.security;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PrivilegedActionException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IManifest;
import si.hermes.security.Collections.IManifests;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.ISignature;
import si.hermes.security.Collections.ISignatures;
import si.hermes.security.Collections.ITransform;
import si.hermes.security.Collections.IXmlDataObject;
import si.hermes.security.Collections.ManifestsImpl;
import si.hermes.security.Collections.ReferenceImpl;
import si.hermes.security.Collections.SignatureImpl;
import si.hermes.security.Collections.SignaturesImpl;
import si.hermes.security.Collections.Transform;
import si.hermes.security.Collections.XmlDataObjectImpl;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.XAdES.EXAdESException;

public final class ESignDocImpl extends PersistableImpl
  implements Serializable, IESignDoc2
{
  private static final String NAME_TAG = "ProductName";
  private static final String RESOURCELOCATION = "/si/hermes/security/resource";
  private static final long serialVersionUID = -1050217478018354195L;
  private static final String TIMESTAMP_TAG = "ReleaseTimestamp";
  private static final String VERSION_TAG = "FileVersion";
  protected String fBaseUri = "";
  private Document fDoc = null;
  private int fIsCalculateManifestReferences = 0;
  private IManifests fManifests;
  private ISignatures fSignatures;
  private static String name;
  private static String timestamp;
  private static String version;
  private String fXAdESCertificateAlgorithm = "";
  public static boolean runningInApplet;

  public final int addSignature(ISignature paramISignature, Element paramElement)
    throws ESignDocException, ParserConfigurationException
  {
    if ((paramElement == null) || (this.fDoc == null))
      this.fDoc = Utility.CreateDocument();
    if (paramElement == null)
    {
      this.fNode = paramISignature.GetXml(null);
      this.fDoc = this.fNode.getOwnerDocument();
      this.fDoc.removeChild(this.fDoc.getDocumentElement());
      this.fDoc.appendChild(this.fNode);
      return getSignatures().Add(paramISignature, paramElement, true);
    }
    return getSignatures().Add(paramISignature, paramElement, true);
  }

  public final String createEnvelopedSignature(String paramString)
    throws IOException, Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, DOMException, ESignDocException
  {
    return createEnvelopedSignatureDom(Utility.createDomDocumentFromB64String(paramString));
  }

  public final String createEnvelopedSignatureDom(Document paramDocument)
    throws ParserConfigurationException, DOMException, ESignDocException
  {
    getSignatures().Clear();
    SignatureImpl localSignatureImpl = new SignatureImpl();
    String str = "EnvelopedSignature-" + Utility.generateRandom(20);
    localSignatureImpl.setId(str);
    ReferenceImpl localReferenceImpl = new ReferenceImpl();
    Transform localTransform;
    (localTransform = new Transform()).setAlgorithm("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
    localReferenceImpl.AddTransform(localTransform);
    localSignatureImpl.getSignedInfo().AddReference(localReferenceImpl);
    Element localElement1 = paramDocument.createElement("Envelope");
    Element localElement2 = paramDocument.getDocumentElement();
    paramDocument.removeChild(localElement2);
    localElement1.appendChild(localElement2);
    paramDocument.appendChild(localElement1);
    localElement1 = localSignatureImpl.GetXml(null);
    if (paramDocument.getOwnerDocument() != localElement1.getOwnerDocument())
      localElement1 = (Element)paramDocument.importNode(localElement1, true);
    paramDocument.getDocumentElement().appendChild(localElement1);
    loadDom(paramDocument);
    return str;
  }

  public final String createEnvelopedSignatureNoRoot(String paramString)
    throws IOException, Base64DecodingException, SAXException, ESignDocException, ParserConfigurationException, FactoryConfigurationError
  {
    return createEnvelopedSignatureDomNoRoot(Utility.createDomDocumentFromB64String(paramString));
  }

  public final String createEnvelopedSignatureDomNoRoot(Document paramDocument)
    throws ESignDocException, ParserConfigurationException
  {
    getSignatures().Clear();
    SignatureImpl localSignatureImpl = new SignatureImpl();
    String str = "EnvelopedSignature-" + Utility.generateRandom(20);
    localSignatureImpl.setId(str);
    ReferenceImpl localReferenceImpl = new ReferenceImpl();
    Transform localTransform;
    (localTransform = new Transform()).setAlgorithm("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
    localReferenceImpl.AddTransform(localTransform);
    localSignatureImpl.getSignedInfo().AddReference(localReferenceImpl);
    Element localElement = localSignatureImpl.GetXml(null);
    if (paramDocument.getOwnerDocument() != localElement.getOwnerDocument())
      localElement = (Element)paramDocument.importNode(localElement, true);
    paramDocument.getDocumentElement().appendChild(localElement);
    loadDom(paramDocument);
    return str;
  }

  public final String createEnvelopingSignature(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException
  {
    return createEnvelopingSignatureDom(Utility.createDomDocumentFromB64String(paramString));
  }

  public final String createEnvelopingSignatureDom(Document paramDocument)
    throws ParserConfigurationException, ESignDocException
  {
    getSignatures().Clear();
    String str = "Object-" + Utility.generateRandom(20);
    SignatureImpl localSignatureImpl;
    (localSignatureImpl = new SignatureImpl()).setId("EnvelopingSignature-" + Utility.generateRandom(20));
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri("#" + str);
    localSignatureImpl.getSignedInfo().AddReference(localReferenceImpl);
    XmlDataObjectImpl localXmlDataObjectImpl;
    (localXmlDataObjectImpl = new XmlDataObjectImpl()).setId(str);
    localXmlDataObjectImpl.setData(paramDocument.getChildNodes());
    localSignatureImpl.addObject(localXmlDataObjectImpl);
    addSignature(localSignatureImpl, null);
    return localSignatureImpl.getId();
  }

  public final String createDetachedSignature(String paramString, boolean paramBoolean)
    throws ParserConfigurationException, ESignDocException
  {
    getSignatures().Clear();
    SignatureImpl localSignatureImpl;
    (localSignatureImpl = new SignatureImpl()).setId("DetachedSignature-" + Utility.generateRandom(20));
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setUri(paramString);
    if (paramBoolean)
    {
      Transform localTransform;
      (localTransform = new Transform()).setAlgorithm("http://www.w3.org/2000/09/xmldsig#base64");
      localReferenceImpl.AddTransform(localTransform);
    }
    localSignatureImpl.getSignedInfo().AddReference(localReferenceImpl);
    addSignature(localSignatureImpl, null);
    return localSignatureImpl.getId();
  }

  public final String getBaseUri()
  {
    return this.fBaseUri;
  }

  private final String getDefaultBaseUri()
  {
    return "";
  }

  public final String getId()
  {
    return "urn:" + name + ":" + version + ":" + timestamp;
  }

  public final IManifests getManifests()
  {
    if (this.fManifests == null)
      this.fManifests = new ManifestsImpl();
    return this.fManifests;
  }

  public final IHSLSignature getSignature(String paramString)
    throws ESignDocException
  {
    return (IHSLSignature)getSignatureById(paramString);
  }

  private final ISignature getSignatureById(String paramString)
    throws ESignDocException
  {
    try
    {
      for (int i = 0; i < getSignatures().getCount(); i++)
      {
        IHSLSignature localIHSLSignature = getSignatures().getItem(i);
        if (paramString.equals(localIHSLSignature.getId()))
          return localIHSLSignature;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(23, "Error searching for signature. Id:" + paramString, localException);
    }
    return null;
  }

  public final ISignatures getSignatures()
  {
    if (this.fSignatures == null)
      this.fSignatures = new SignaturesImpl();
    return this.fSignatures;
  }

  public final String hashSHA1(String paramString)
    throws ESignDocException, Base64DecodingException
  {
    if (paramString == null)
      throw new ESignDocException(3);
    return Utility.hashSHA1(Base64.decode(paramString));
  }

  public static final void initProfile(String paramString)
    throws GeneralSecurityException
  {
    initProfile(paramString, "NSS");
  }

  public static final void initProfile(String paramString1, String paramString2)
    throws GeneralSecurityException
  {
    try
    {
      CertificateProvider.initProfile(paramString1, paramString2);
      loadVersionInfo();
      return;
    }
    catch (Exception localException2)
    {
      Exception localException1;
      (localException1 = localException2).printStackTrace();
    }
  }

  public static final void initProfile(String paramString1, String paramString2, boolean paramBoolean)
    throws GeneralSecurityException, ESignDocException
  {
    runningInApplet = paramBoolean;
    initProfile(paramString1, paramString2);
  }

  public final void load(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException
  {
    loadDom(Utility.createDomDocumentFromB64String(paramString));
  }

  public final void loadDom(Document paramDocument)
    throws ESignDocException
  {
    LoadXml(paramDocument.getDocumentElement());
  }

  public final void loadZip(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException
  {
    load(Utility.unzipString(paramString, "data.xml"));
  }

  private static final void loadVersionInfo()
  {
    InputStream localInputStream = null;
    try
    {
      if ((localInputStream = ESignDocImpl.class.getResourceAsStream("/si/hermes/security/resource/SebBuildVersion.xml")) == null)
      {
        System.out.println("warning: version info not found!");
        jsr 148;
      }
      DocumentBuilderFactoryImpl localDocumentBuilderFactoryImpl;
      DocumentBuilder localDocumentBuilder;
      String str1 = Utility.getNodeValue(localObject1 = (localDocumentBuilder = (localDocumentBuilderFactoryImpl = new DocumentBuilderFactoryImpl()).newDocumentBuilder()).parse(new InputSource(localInputStream)), "ProductName");
      String str2 = Utility.getNodeValue((Document)localObject1, "FileVersion");
      String str3 = Utility.getNodeValue((Document)localObject1, "ReleaseTimestamp");
      if ((str1 != null) && (str2 != null) && (str3 != null))
      {
        name = str1;
        version = str2;
        timestamp = str3;
      }
      else
      {
        System.out.println("Warning: Missing version info: Name=" + str1 + " version=" + str2 + " timestamp=" + str3);
      }
    }
    catch (Exception localException)
    {
      Object localObject1;
      return;
    }
    finally
    {
      if (localInputStream != null)
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException2)
        {
          IOException localIOException1;
          (localIOException1 = localIOException2).printStackTrace();
        }
    }
  }

  public final void setBaseUri(String paramString)
  {
    this.fBaseUri = paramString;
  }

  public final void calculateManifestReferences(boolean paramBoolean)
  {
    this.fIsCalculateManifestReferences = (paramBoolean ? 1 : 2);
  }

  public final String sign(String paramString1, String paramString2, boolean paramBoolean)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, TransformerException, EXAdESException
  {
    if (paramString1 == null)
      throw new ESignDocException(3);
    signDom(paramString1);
    String str = Utility.createB64StringFromDomDocument(GetXml().getOwnerDocument(), paramString2);
    if (paramBoolean)
      str = Utility.zipString(str, null);
    return str;
  }

  public final void signDom(String paramString)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, IOException, TransformerException, EXAdESException
  {
    if (paramString == null)
      throw new ESignDocException(3);
    Synchronize(false);
    ISignature localISignature;
    if ((localISignature = getSignatureById(paramString)) == null)
      throw new ESignDocException(23);
    if (localISignature.getResolverEntity() != null)
      localISignature.getResolverEntity().getResolver().setBaseURI(!"".equals(this.fBaseUri) ? this.fBaseUri : getDefaultBaseUri());
    localISignature.setBaseUri(this.fBaseUri);
    localISignature.setManifests(this.fManifests);
    if (this.fIsCalculateManifestReferences == 2)
      localISignature.calculateManifestReferences(this.fIsCalculateManifestReferences == 1);
    if (IsNullOrEmpty(localISignature.getXAdESCertificateDigestAlgorithm()))
      localISignature.setXAdESCertificateDigestAlgorithm(this.fXAdESCertificateAlgorithm);
    localISignature.sign();
  }

  public final String signWithCertificate(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4, String paramString5)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, TransformerException, EXAdESException
  {
    signWithCertificateDom(paramString1, paramString2, paramString3, paramBoolean, paramString5);
    return Utility.createB64StringFromDomDocument(this.fDoc, paramString4);
  }

  public final void signWithCertificateDom(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, IOException, TransformerException, EXAdESException
  {
    if (paramString1 == null)
      throw new ESignDocException(3);
    Synchronize(false);
    ISignature localISignature;
    if ((localISignature = getSignatureById(paramString1)) == null)
      throw new ESignDocException(23);
    if (localISignature.getResolverEntity() != null)
      localISignature.getResolverEntity().getResolver().setBaseURI(!"".equals(this.fBaseUri) ? this.fBaseUri : getDefaultBaseUri());
    localISignature.setBaseUri(this.fBaseUri);
    localISignature.setManifests(this.fManifests);
    if (this.fIsCalculateManifestReferences == 2)
      localISignature.calculateManifestReferences(this.fIsCalculateManifestReferences == 1);
    if (IsNullOrEmpty(localISignature.getXAdESCertificateDigestAlgorithm()))
      localISignature.setXAdESCertificateDigestAlgorithm(this.fXAdESCertificateAlgorithm);
    localISignature.signWithCertificate(paramString2, paramString3, paramBoolean, paramString4);
  }

  public final String getToString()
    throws IOException, ESignDocException
  {
    this.fNode = this.fDoc.getDocumentElement();
    return Utility.createStringFromDomDocument(this.fDoc);
  }

  public final Element GetXml(Element paramElement)
    throws ESignDocException
  {
    this.fNode = this.fDoc.getDocumentElement();
    return super.GetXml(paramElement);
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      if (this.fNode == null)
        this.fNode = EnsureBaseElement(this.fNode, "Envelope", "");
      this.fDoc = this.fNode.getOwnerDocument();
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      throw new ESignDocException(16, localParserConfigurationException);
    }
    if (paramBoolean)
    {
      getSignatures().LoadXml(this.fNode);
      getManifests().LoadXml(this.fNode);
      return;
    }
    getSignatures().GetXml(this.fNode);
    getManifests().GetXml(this.fNode);
  }

  public final String transform(String paramString1, String paramString2)
    throws TransformerException, ESignDocException, IOException, Base64DecodingException
  {
    return transform(paramString1, paramString2, null);
  }

  public final String transform(String paramString1, String paramString2, String paramString3)
    throws TransformerException, ESignDocException, IOException, Base64DecodingException
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new ESignDocException(3);
    byte[] arrayOfByte1 = Base64.decode(paramString2);
    byte[] arrayOfByte2;
    return Utility.transform(arrayOfByte2 = Base64.decode(paramString1), arrayOfByte1, paramString3);
  }

  public final String transformEx(Document paramDocument1, Document paramDocument2, String paramString)
    throws TransformerException, ESignDocException, IOException, PrivilegedActionException, ParserConfigurationException
  {
    if ((paramDocument1 == null) || (paramDocument2 == null))
      throw new ESignDocException(3);
    return Utility.transform(paramDocument1, paramDocument2, paramString);
  }

  public final String transformWithVerify(String paramString1, String paramString2)
    throws ESignDocException, PrivilegedActionException, FactoryConfigurationError, XMLSecurityException, TransformerException, SAXException, IOException, ParserConfigurationException
  {
    ESignDocImpl localESignDocImpl;
    (localESignDocImpl = new ESignDocImpl()).load(paramString2);
    SignaturesImpl localSignaturesImpl;
    if ((localSignaturesImpl = (SignaturesImpl)localESignDocImpl.getSignatures()).getCount() != 1)
      throw new ESignDocException(9);
    VerifyStatus localVerifyStatus;
    if (!(localVerifyStatus = localSignaturesImpl.getItem(0).status()).getStatus().equals("VALID"))
      throw new ESignDocException(10, localVerifyStatus.getStatus() + " " + localVerifyStatus.getReason());
    return transform(paramString1, paramString2);
  }

  public final CertificateChain validateCertificateChain(String paramString1, Date paramDate, boolean paramBoolean, String paramString2)
    throws ESignDocException
  {
    CertificateChain localCertificateChain;
    (localCertificateChain = new CertificateChain()).init(paramString1, paramDate, paramBoolean, paramString2);
    return localCertificateChain;
  }

  public final int addManifest(IManifest paramIManifest, Element paramElement)
    throws ESignDocException, ParserConfigurationException
  {
    if (paramElement == null)
      throw new ESignDocException(3, "Parent is null!");
    return getManifests().Add(paramIManifest, paramElement, true);
  }

  public final void setXAdESCertificateDigestAlgorithm(String paramString)
  {
    this.fXAdESCertificateAlgorithm = paramString;
  }

  public final String getXAdESCertificateDigestAlgorithm()
  {
    return this.fXAdESCertificateAlgorithm;
  }

  public final void addEntity(String paramString1, String paramString2, String paramString3)
    throws ESignDocException, Base64DecodingException
  {
    ISignature localISignature;
    if ((localISignature = getSignatureById(paramString1)) == null)
      throw new ESignDocException(23);
    localISignature.addEntity(paramString2, paramString3);
  }

  public final String calculateManifestReferenceHash(String paramString)
    throws ESignDocException, TransformerException, XMLSecurityException, IOException
  {
    ISignature localISignature;
    if ((localISignature = getSignatureById(paramString)) == null)
      throw new ESignDocException(23);
    if (localISignature.getResolverEntity() != null)
      localISignature.getResolverEntity().getResolver().setBaseURI(!"".equals(this.fBaseUri) ? this.fBaseUri : getDefaultBaseUri());
    localISignature.setBaseUri(this.fBaseUri);
    localISignature.setManifests(this.fManifests);
    if (this.fIsCalculateManifestReferences == 2)
      localISignature.calculateManifestReferences(this.fIsCalculateManifestReferences == 1);
    return localISignature.calculateManifestReferenceHash();
  }

  public final String signWithCertificateUI(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, TransformerException, EXAdESException
  {
    signWithCertificateUIDom(paramString1, paramString2, paramString3);
    String str = Utility.createB64StringFromDomDocument(this.fDoc, paramString4);
    if (paramBoolean)
      str = Utility.zipString(str, null);
    return str;
  }

  public final void signWithCertificateUIDom(String paramString1, String paramString2, String paramString3)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, ParserConfigurationException, SAXException, IOException, TransformerException, EXAdESException
  {
    if (paramString1 == null)
      throw new ESignDocException(3);
    Synchronize(false);
    ISignature localISignature;
    if ((localISignature = getSignatureById(paramString1)) == null)
      throw new ESignDocException(23);
    if (localISignature.getResolverEntity() != null)
      localISignature.getResolverEntity().getResolver().setBaseURI(!"".equals(this.fBaseUri) ? this.fBaseUri : getDefaultBaseUri());
    localISignature.setBaseUri(this.fBaseUri);
    localISignature.setManifests(this.fManifests);
    if (this.fIsCalculateManifestReferences == 2)
      localISignature.calculateManifestReferences(this.fIsCalculateManifestReferences == 1);
    if (IsNullOrEmpty(localISignature.getXAdESCertificateDigestAlgorithm()))
      localISignature.setXAdESCertificateDigestAlgorithm(this.fXAdESCertificateAlgorithm);
    localISignature.signWithCertificateUI(paramString2, paramString3);
  }

  public final String getUILanguage()
  {
    return Utility.getLocale().getLanguage();
  }

  public final void setUILanguage(String paramString)
    throws ESignDocException
  {
    Utility.setLocale(paramString);
  }

  static
  {
    try
    {
      Init.init();
    }
    catch (Exception localException)
    {
    }
    name = "";
    timestamp = "";
    version = "";
    runningInApplet = false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ESignDocImpl
 * JD-Core Version:    0.6.0
 */