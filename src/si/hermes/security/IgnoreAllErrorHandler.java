package si.hermes.security;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class IgnoreAllErrorHandler
  implements ErrorHandler
{
  public void warning(SAXParseException paramSAXParseException)
    throws SAXException
  {
  }

  public void error(SAXParseException paramSAXParseException)
    throws SAXException
  {
    throw paramSAXParseException;
  }

  public void fatalError(SAXParseException paramSAXParseException)
    throws SAXException
  {
    throw paramSAXParseException;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.IgnoreAllErrorHandler
 * JD-Core Version:    0.6.0
 */