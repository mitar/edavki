package si.hermes.security.Collections;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import si.hermes.security.ESignDocException;
import si.hermes.security.VerifyStatus;

public abstract interface ITimestampProvider
{
  public abstract String CreateTimestamp(String paramString)
    throws ESignDocException, NoSuchAlgorithmException, ParserConfigurationException, SOAPException, IOException, SAXException, TransformerException;

  public abstract boolean getIsEncapsulated();

  public abstract void setTestSignatureData(String paramString1, String paramString2, String paramString3);

  public abstract void setServerUri(String paramString);

  public abstract VerifyStatus validateTimestamp(String paramString)
    throws IOException, ESignDocException, XMLSignatureException, XMLSecurityException, TransformerException;

  public abstract String getTimestampCertificate()
    throws ESignDocException;

  public abstract Date getTimestampDate()
    throws ESignDocException;

  public abstract void LoadXml(Element paramElement)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.ITimestampProvider
 * JD-Core Version:    0.6.0
 */