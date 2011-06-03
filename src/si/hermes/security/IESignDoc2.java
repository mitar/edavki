package si.hermes.security;

import java.awt.HeadlessException;
import java.io.IOException;
import java.security.PrivilegedActionException;
import java.security.cert.CertificateException;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.mozilla.jss.CryptoManager.NotInitializedException;
import org.mozilla.jss.NoSuchTokenException;
import org.mozilla.jss.crypto.ObjectNotFoundException;
import org.mozilla.jss.crypto.TokenException;
import org.mozilla.jss.util.IncorrectPasswordException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IManifest;
import si.hermes.security.Collections.IManifests;
import si.hermes.security.Collections.ISignature;
import si.hermes.security.Collections.ISignatures;
import si.hermes.security.XAdES.EXAdESException;

public abstract interface IESignDoc2 extends IPersistable
{
  public abstract int addManifest(IManifest paramIManifest, Element paramElement)
    throws ESignDocException, ParserConfigurationException;

  public abstract int addSignature(ISignature paramISignature, Element paramElement)
    throws ESignDocException, ParserConfigurationException;

  public abstract void calculateManifestReferences(boolean paramBoolean);

  public abstract String createDetachedSignature(String paramString, boolean paramBoolean)
    throws ParserConfigurationException, ESignDocException;

  public abstract String createEnvelopedSignature(String paramString)
    throws IOException, Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, DOMException, ESignDocException;

  public abstract String createEnvelopedSignatureDom(Document paramDocument)
    throws ParserConfigurationException, DOMException, ESignDocException;

  public abstract String createEnvelopedSignatureDomNoRoot(Document paramDocument)
    throws ParserConfigurationException, ESignDocException;

  public abstract String createEnvelopedSignatureNoRoot(String paramString)
    throws IOException, Base64DecodingException, ParserConfigurationException, SAXException, ESignDocException;

  public abstract String createEnvelopingSignature(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException;

  public abstract String createEnvelopingSignatureDom(Document paramDocument)
    throws ParserConfigurationException, ESignDocException;

  public abstract String getBaseUri();

  public abstract String getUILanguage();

  public abstract String getId();

  public abstract IManifests getManifests();

  public abstract ISignatures getSignatures();

  public abstract IHSLSignature getSignature(String paramString)
    throws ESignDocException;

  public abstract String getXAdESCertificateDigestAlgorithm();

  public abstract String hashSHA1(String paramString)
    throws ESignDocException, Base64DecodingException;

  public abstract void load(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException;

  public abstract void loadZip(String paramString)
    throws Base64DecodingException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException, ESignDocException;

  public abstract void loadDom(Document paramDocument)
    throws ESignDocException;

  public abstract void setBaseUri(String paramString);

  public abstract void setUILanguage(String paramString)
    throws ESignDocException;

  public abstract void setXAdESCertificateDigestAlgorithm(String paramString);

  public abstract String sign(String paramString1, String paramString2, boolean paramBoolean)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract void signDom(String paramString)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, IOException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract String signWithCertificate(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4, String paramString5)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract void signWithCertificateDom(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, IOException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract String signWithCertificateUI(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
    throws ESignDocException, IOException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract void signWithCertificateUIDom(String paramString1, String paramString2, String paramString3)
    throws ESignDocException, HeadlessException, CertificateException, XMLSecurityException, CryptoManager.NotInitializedException, NoSuchTokenException, IncorrectPasswordException, ParserConfigurationException, SAXException, IOException, TransformerException, ObjectNotFoundException, TokenException, EXAdESException;

  public abstract String transform(String paramString1, String paramString2)
    throws TransformerException, ESignDocException, IOException, Base64DecodingException, PrivilegedActionException;

  public abstract String transform(String paramString1, String paramString2, String paramString3)
    throws TransformerException, ESignDocException, IOException, Base64DecodingException, PrivilegedActionException;

  public abstract String transformEx(Document paramDocument1, Document paramDocument2, String paramString)
    throws TransformerException, ESignDocException, IOException, ParserConfigurationException, PrivilegedActionException;

  public abstract String transformWithVerify(String paramString1, String paramString2)
    throws ESignDocException, PrivilegedActionException, FactoryConfigurationError, XMLSecurityException, TransformerException, SAXException, IOException, ParserConfigurationException;

  public abstract CertificateChain validateCertificateChain(String paramString1, Date paramDate, boolean paramBoolean, String paramString2)
    throws ESignDocException;

  public abstract void addEntity(String paramString1, String paramString2, String paramString3)
    throws ESignDocException, Base64DecodingException;

  public abstract String calculateManifestReferenceHash(String paramString)
    throws ESignDocException, TransformerException, XMLSecurityException, IOException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.IESignDoc2
 * JD-Core Version:    0.6.0
 */