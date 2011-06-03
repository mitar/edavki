package si.hermes.security.Collections;

import javax.xml.parsers.ParserConfigurationException;
import si.hermes.security.ESignDocException;

public final class PostarcaTimestampProviderImpl extends TimestampProviderImpl
  implements ITimestampProvider
{
  public PostarcaTimestampProviderImpl()
  {
    setServerUri("https://tsa.posta.si:444/CasovniZig.asmx");
    setSoapAction("http://Timestamp/CasovniZigWS/GetTimeStampCertificate");
    addNamespace("ds", "http://www.w3.org/2000/09/xmldsig#");
    addNamespace("s", "http://Timestamp/CasovniZigWS");
  }

  protected final String generateRequest(String paramString1, String paramString2)
    throws ParserConfigurationException, ESignDocException
  {
    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n  <soap:Body>\n    <GetTimeStampCertificate xmlns=\"http://Timestamp/CasovniZigWS\"><inHash>" + paramString1 + "</inHash></GetTimeStampCertificate>\n" + "  </soap:Body>\n" + "</soap:Envelope>";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.PostarcaTimestampProviderImpl
 * JD-Core Version:    0.6.0
 */