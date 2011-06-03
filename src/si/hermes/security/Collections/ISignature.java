package si.hermes.security.Collections;

import java.awt.HeadlessException;
import java.io.IOException;
import java.security.cert.CertificateException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.xml.sax.SAXException;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.IResolverEntity;
import si.hermes.security.KeyInfo.IKeyInfo;
import si.hermes.security.SignedInfo.ISignedInfo;
import si.hermes.security.VerifyStatus;
import si.hermes.security.XAdES.EXAdESException;

public abstract interface ISignature extends IPersistable
{
  public abstract void setManifests(IManifests paramIManifests);

  public abstract ISignedInfo getSignedInfo();

  public abstract void setSignedInfo(ISignedInfo paramISignedInfo);

  public abstract String getSignatureValue();

  public abstract void setSignatureValue(String paramString);

  public abstract IKeyInfo getKeyInfo();

  public abstract IXmlDataObjectList getObjectList();

  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract void setResolverEntity(IResolverEntity paramIResolverEntity);

  public abstract IResolverEntity getResolverEntity();

  public abstract int addObject(IXmlDataObject paramIXmlDataObject)
    throws ParserConfigurationException;

  public abstract VerifyStatus Validate()
    throws IOException, ESignDocException, XMLSignatureException, XMLSignatureException, XMLSecurityException, TransformerException;

  public abstract String getBaseUri();

  public abstract void setBaseUri(String paramString);

  public abstract void setSignatureValueId(String paramString);

  public abstract VerifyStatus status()
    throws XMLSignatureException, IOException, ESignDocException, XMLSecurityException, TransformerException;

  public abstract String getSignatureValueId();

  public abstract void sign()
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException;

  public abstract void signWithCertificate(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException;

  public abstract void signWithCertificateUI(String paramString1, String paramString2)
    throws XMLSecurityException, HeadlessException, CertificateException, ParserConfigurationException, SAXException, IOException, TransformerException, ESignDocException, EXAdESException;

  public abstract void emptySignature();

  public abstract void setPassword(char[] paramArrayOfChar);

  public abstract void clearPassword();

  public abstract String getXAdESCertificateDigestAlgorithm();

  public abstract void setXAdESCertificateDigestAlgorithm(String paramString);

  public abstract void calculateManifestReferences(boolean paramBoolean);

  public abstract void addEntity(String paramString1, String paramString2)
    throws Base64DecodingException;

  public abstract void addEntity(String paramString, byte[] paramArrayOfByte)
    throws Base64DecodingException;

  public abstract String calculateManifestReferenceHash()
    throws TransformerException, XMLSecurityException, ESignDocException, IOException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ISignature
 * JD-Core Version:    0.6.0
 */