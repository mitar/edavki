package si.hermes.security.Collections;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public abstract interface IReference extends IPersistable
{
  public abstract int AddTransform(ITransform paramITransform)
    throws ParserConfigurationException;

  public abstract int AddTransformAlgorithm(int paramInt)
    throws ParserConfigurationException;

  public abstract int AddTransformXPath(String paramString)
    throws ESignDocException, ParserConfigurationException;

  public abstract int AddTransformXslt(String paramString)
    throws DOMException, Base64DecodingException, ESignDocException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException;

  public abstract int AddTransformXsltDom(Document paramDocument)
    throws DOMException, ESignDocException, ParserConfigurationException;

  public abstract String getDigestMethod();

  public abstract String getDigestValue();

  public abstract String getId();

  public abstract ITransformChain getTransformChain();

  public abstract String getType();

  public abstract String getUri();

  public abstract void setDigestMethod(String paramString);

  public abstract void setDigestValue(String paramString);

  public abstract void setId(String paramString);

  public abstract void setType(String paramString);

  public abstract void setUri(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IReference
 * JD-Core Version:    0.6.0
 */