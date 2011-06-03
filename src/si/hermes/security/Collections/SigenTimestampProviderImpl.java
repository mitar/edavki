package si.hermes.security.Collections;

import javax.xml.parsers.ParserConfigurationException;
import si.hermes.security.ESignDocException;

public final class SigenTimestampProviderImpl extends TimestampProviderImpl
  implements ITimestampProvider
{
  private static final long serialVersionUID = 3258297317993193572L;

  protected final String generateRequest(String paramString1, String paramString2)
    throws ParserConfigurationException, ESignDocException
  {
    return "<?xml version='1.0' encoding='UTF-8'?>\n<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n  <SOAP-ENV:Body>\n    <tsa:service xmlns:tsa=\"urn:Entrust-TSA\"><ts:TimeStampRequest xmlns:ts=\"http://www.entrust.com/schemas/timestamp-protocol-20020207\"><ts:Digest><ds:DigestMethod xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><ds:DigestValue xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" + paramString1 + "</ds:DigestValue></ts:Digest><ts:Nonce>" + paramString2 + "</ts:Nonce></ts:TimeStampRequest></tsa:service>\n" + "  </SOAP-ENV:Body>\n" + "</SOAP-ENV:Envelope>";
  }

  public SigenTimestampProviderImpl()
  {
    setServerUri("https://ts.si-tsa.sigov.si/verificationserver/timestamp");
    setSoapAction("urn:this-is-the-action-uri");
    addNamespace("ds", "http://www.w3.org/2000/09/xmldsig#");
    addNamespace("ts", "http://www.entrust.com/schemas/timestamp-protocol-20020207");
    addNamespace("tsa", "urn:Entrust-TSA");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.SigenTimestampProviderImpl
 * JD-Core Version:    0.6.0
 */