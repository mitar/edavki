package si.hermes.security.Collections;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.CertificateChain;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.VerifyStatus;
import si.hermes.security.XAdES.EXAdESException;

public abstract interface ITimestamp extends IPersistable
{
  public abstract void CreateTimestamp(String paramString)
    throws NoSuchAlgorithmException, ESignDocException, ParserConfigurationException, SOAPException, IOException, XMLSecurityException, FactoryConfigurationError, SAXException, TransformerException, EXAdESException;

  public abstract Element getNode();

  public abstract ITimestampProvider getProvider();

  public abstract CertificateChain getSigningCertificateChain(boolean paramBoolean, String paramString)
    throws ESignDocException;

  public abstract VerifyStatus getStatus()
    throws XMLSignatureException, IOException, ESignDocException, XMLSecurityException, TransformerException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException;

  public abstract String getTimestampCertificate()
    throws ESignDocException;

  public abstract Date getTimestampDate()
    throws ESignDocException;

  public abstract ITransformChain getTransforms();

  public abstract String getValue();

  public abstract void setNode(Element paramElement);

  public abstract void setProvider(ITimestampProvider paramITimestampProvider);

  public abstract void setValue(String paramString);

  public abstract VerifyStatus validate()
    throws IOException, ESignDocException, XMLSignatureException, XMLSecurityException, TransformerException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException;

  public abstract int addHashDataInfo(IHashDataInfo paramIHashDataInfo)
    throws ParserConfigurationException;

  public abstract int addHashDataInfoUri(String paramString);

  public abstract IHashDataInfos getHashDataInfos();

  public abstract void setId(String paramString);

  public abstract String getId();

  public abstract String getReferenceDigest()
    throws ESignDocException, XMLSecurityException, ParserConfigurationException, EXAdESException, FactoryConfigurationError, SAXException, IOException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ITimestamp
 * JD-Core Version:    0.6.0
 */