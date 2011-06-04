package si.hermes.security;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.keys.content.x509.XMLX509IssuerSerial;
import org.apache.xml.security.keys.content.x509.XMLX509SubjectName;
import org.apache.xml.security.signature.Manifest;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.SignedInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.InvalidTransformException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.DigesterOutputStream;
import org.apache.xml.security.utils.SignerOutputStream;
import org.apache.xml.security.utils.UnsyncBufferedOutputStream;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IManifests;
import si.hermes.security.Collections.IReference;
import si.hermes.security.KeyInfo.IKeyInfo;
import si.hermes.security.KeyInfo.KeyInfoX509DataImpl;
import si.hermes.security.SignedInfo.ISignedInfo;

public final class DigSigLoader
{
  private static PrefixResolver digSigPrefixResolver = null;

  public static byte[] calculateReferenceDigest(Reference paramReference)
    throws XMLSignatureException, IOException
  {
    MessageDigestAlgorithm localMessageDigestAlgorithm;
    (localMessageDigestAlgorithm = paramReference.getMessageDigestAlgorithm()).reset();
    DigesterOutputStream localDigesterOutputStream = new DigesterOutputStream(localMessageDigestAlgorithm);
    UnsyncBufferedOutputStream localUnsyncBufferedOutputStream;
    (localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localDigesterOutputStream)).write(paramReference.getReferencedBytes());
    localUnsyncBufferedOutputStream.flush();
    return localDigesterOutputStream.getDigestValue();
  }

  public static void calculateReferenceDigest(IReference paramIReference, String paramString, Manifest paramManifest)
    throws IOException, XMLSecurityException, ESignDocException
  {
    byte[] arrayOfByte = calculateReferenceDigest(new ReferenceHsl(paramIReference.GetXml(), paramString, paramManifest));
    paramIReference.setDigestValue(Base64.encode(arrayOfByte));
  }

  public static void sign(IHSLSignature paramIHSLSignature, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
    throws XMLSecurityException, ESignDocException
  {
    SignatureAlgorithm localSignatureAlgorithm;
    (localSignatureAlgorithm = new SignatureAlgorithm(paramIHSLSignature.GetXml().getOwnerDocument(), paramIHSLSignature.getSignedInfo().getSignatureMethod())).initSign(paramPrivateKey);
    SignedInfo localSignedInfo = new SignedInfo(paramIHSLSignature.getSignedInfo().GetXml(), paramIHSLSignature.getBaseUri());
    UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(new SignerOutputStream(localSignatureAlgorithm));
    try
    {
      localUnsyncBufferedOutputStream.close();
    }
    catch (IOException localIOException)
    {
    }
    localSignedInfo.signInOctectStream(localUnsyncBufferedOutputStream);
    byte[] arrayOfByte = localSignatureAlgorithm.sign();
    paramIHSLSignature.setSignatureValue(Base64.encode(arrayOfByte));
  }

  public static void appendX509Data(XMLSignature paramXMLSignature, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    X509Data localX509Data;
    (localX509Data = new X509Data(paramXMLSignature.getDocument())).addCertificate(paramX509Certificate);
    localX509Data.add(new XMLX509IssuerSerial(paramXMLSignature.getDocument(), paramX509Certificate.getIssuerDN().toString(), paramX509Certificate.getSerialNumber()));
    localX509Data.add(new XMLX509SubjectName(paramXMLSignature.getDocument(), paramX509Certificate.getSubjectDN().toString()));
    paramXMLSignature.getKeyInfo().add(localX509Data);
  }

  public static void appendX509Data(IHSLSignature paramIHSLSignature, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate)
    throws XMLSecurityException, CertificateEncodingException, ESignDocException, ParserConfigurationException
  {
    KeyInfoX509DataImpl localKeyInfoX509DataImpl;
    (localKeyInfoX509DataImpl = new KeyInfoX509DataImpl()).AddCertificate(Base64.encode(paramX509Certificate.getEncoded()));
    localKeyInfoX509DataImpl.AddIssuerSerial(paramX509Certificate.getIssuerDN().toString(), paramX509Certificate.getSerialNumber().toString());
    localKeyInfoX509DataImpl.AddSubjectName(paramX509Certificate.getSubjectDN().toString());
    paramIHSLSignature.getKeyInfo().AddClause(localKeyInfoX509DataImpl);
  }

  public static void digestManifestReferences(Element paramElement, String paramString, ResourceResolverSpi paramResourceResolverSpi, IManifests paramIManifests)
    throws TransformerException, XMLSecurityException, ESignDocException
  {
    NodeList localNodeList = null;
    Element localElement = null;
    String str = null;
    Node localNode = null;
    localNodeList = selectNodes(paramElement, ".//dsig:Reference");
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      if (((str = Utility.getFragmentURI((localElement = (Element)localNodeList.item(i)).getAttribute("URI"))) == null) || ((localNode = selectSingleNode(localElement.getOwnerDocument(), "//*[@Id='" + str + "']")) == null) || (!localNode.getNodeName().equals("Manifest")) || (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
        continue;
      Manifest localManifest = loadManifest((Element)localNode, paramString);
      if (paramResourceResolverSpi != null)
        localManifest.addResourceResolver(paramResourceResolverSpi);
      localManifest.generateDigestValues();
    }
  }

  public static VerifyStatus verify(XMLSignature paramXMLSignature, String paramString, ResourceResolverSpi paramResourceResolverSpi, boolean paramBoolean)
    throws TransformerException, XMLSecurityException, ESignDocException
  {
    NodeList localNodeList = null;
    Element localElement = null;
    String str = null;
    Node localNode = null;
    SignedInfo localSignedInfo = null;
    localSignedInfo = paramXMLSignature.getSignedInfo();
    if (paramResourceResolverSpi != null)
      localSignedInfo.addResourceResolver(paramResourceResolverSpi);
    localNodeList = selectNodes(paramXMLSignature.getSignedInfo().getElement(), ".//dsig:Reference");
    Object localObject;
    if (paramBoolean)
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        if (((str = Utility.getFragmentURI((localElement = (Element)localNodeList.item(i)).getAttribute("URI"))) == null) || ((localNode = selectSingleNode(localElement.getOwnerDocument(), "//*[@Id='" + str + "']")) == null) || (!localNode.getNodeName().equals("Manifest")) || (!localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
          continue;
        localObject = new Manifest((Element)localNode, paramString);
        if (paramResourceResolverSpi != null)
          ((Manifest)localObject).addResourceResolver(paramResourceResolverSpi);
        if (((Manifest)localObject).verifyReferences())
          continue;
        for (int k = 0; k < ((Manifest)localObject).getLength(); k++)
          if (!((Manifest)localObject).getVerificationResult(k))
            return new VerifyStatus("INVALID", ((Manifest)localObject).getId() + ";" + ((Manifest)localObject).item(k).getURI());
      }
    X509Certificate localX509Certificate = null;
    if ((paramXMLSignature.getKeyInfo().containsX509Data()) && ((localObject = paramXMLSignature.getKeyInfo().itemX509Data(0)).containsCertificate()))
      localX509Certificate = ((X509Data)localObject).itemCertificate(0).getX509Certificate();
    if (localX509Certificate == null)
      return new VerifyStatus("CREDENTIALS NOT FOUND", paramXMLSignature.getId());
    if (!paramXMLSignature.checkSignatureValue(localX509Certificate))
    {
      for (int j = 0; j < localSignedInfo.getLength(); j++)
        if (!localSignedInfo.getVerificationResult(j))
          return new VerifyStatus("INVALID", localSignedInfo.getId() + ";" + localSignedInfo.item(j).getURI());
      return new VerifyStatus("INVALID", "signature value is invalid");
    }
    return (VerifyStatus)new VerifyStatus("VALID", paramXMLSignature.getId());
  }

  public static PrefixResolver getDigSigPrefixResolver()
  {
    if (digSigPrefixResolver == null)
      digSigPrefixResolver = new DigSigPrefixResolver(null);
    return digSigPrefixResolver;
  }

  public static Node selectSingleNode(Node paramNode, String paramString)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = XPathAPI.eval(paramNode, paramString, getDigSigPrefixResolver())).nodeset().nextNode();
  }

  public static NodeList selectNodes(Node paramNode, String paramString)
    throws TransformerException
  {
    XObject localXObject;
    return (localXObject = XPathAPI.eval(paramNode, paramString, getDigSigPrefixResolver())).nodelist();
  }

  public static Manifest loadManifest(Node paramNode, String paramString1, String paramString2)
    throws TransformerException, XMLSecurityException
  {
    Node localNode;
    if ((localNode = selectSingleNode(paramNode, ".//dsig:Manifest[@Id='" + paramString2 + "']")) != null)
      return loadManifest((Element)localNode, paramString1);
    return null;
  }

  public static Manifest loadManifest(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    Manifest localManifest1 = new Manifest(paramElement, paramString);
    Manifest localManifest2 = new Manifest(paramElement.getOwnerDocument());
    copyManifest(localManifest1, localManifest2);
    Element localElement1 = localManifest1.getElement();
    Element localElement2 = localManifest2.getElement();
    localElement1.getParentNode().replaceChild(localElement2, localElement1);
    return localManifest2;
  }

  private static void copyManifest(Manifest paramManifest1, Manifest paramManifest2)
    throws XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
  {
    if ((paramManifest1.getId() != null) && (paramManifest1.getId().length() > 0))
      paramManifest2.setId(paramManifest1.getId());
    for (int i = 0; i < paramManifest1.getLength(); i++)
    {
      Reference localReference = paramManifest1.item(i);
      String str1 = "http://www.w3.org/2000/09/xmldsig#sha1";
      if (localReference.getMessageDigestAlgorithm() != null)
        str1 = localReference.getMessageDigestAlgorithm().getAlgorithmURI();
      String str2;
      if ((str2 = localReference.getType()).length() == 0)
        str2 = null;
      String str3;
      if ((str3 = localReference.getId()).length() == 0)
        str3 = null;
      paramManifest2.addDocument(paramManifest1.getBaseURI(), localReference.getURI(), localReference.getTransforms(), str1, str3, str2);
    }
  }

  private static class DigSigPrefixResolver
    implements PrefixResolver
  {
    private DigSigPrefixResolver()
    {
    }

    public boolean handlesNullPrefixes()
    {
      return true;
    }

    public String getBaseIdentifier()
    {
      return null;
    }

    public String getNamespaceForPrefix(String paramString)
    {
      if (paramString.equals("dsig"))
        return "http://www.w3.org/2000/09/xmldsig#";
      if (paramString.equals("xds"))
        return "http://uri.etsi.org/01903/v1.1.1#";
      return null;
    }

    public String getNamespaceForPrefix(String paramString, Node paramNode)
    {
      return null;
    }

    DigSigPrefixResolver(DigSigLoader param1)
    {
      this();
    }
  }

  public static class ReferenceHsl extends Reference
  {
    protected ReferenceHsl(Element paramElement, String paramString, Manifest paramManifest)
      throws XMLSecurityException
    {
      super(paramString, paramManifest);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.DigSigLoader
 * JD-Core Version:    0.6.0
 */
