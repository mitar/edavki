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

public abstract interface IManifest extends IPersistable
{
  public abstract String getId();

  public abstract void setId(String paramString);

  public abstract IReferences getReferences();

  public abstract int AddReference(IReference paramIReference);

  public abstract int AddReferenceUri(String paramString);

  public abstract int AddManifestReference(IManifest paramIManifest)
    throws ParserConfigurationException;

  public abstract int AddReferenceTransform(String paramString, int paramInt)
    throws ParserConfigurationException;

  public abstract int AddReferenceXPath(String paramString1, String paramString2)
    throws ESignDocException, ParserConfigurationException;

  public abstract int AddReferenceXslt(String paramString1, String paramString2)
    throws DOMException, Base64DecodingException, ESignDocException, FactoryConfigurationError, ParserConfigurationException, SAXException, IOException;

  public abstract int AddReferenceXsltDom(String paramString, Document paramDocument)
    throws DOMException, ESignDocException, ParserConfigurationException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IManifest
 * JD-Core Version:    0.6.0
 */