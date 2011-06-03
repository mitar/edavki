package org.apache.xml.security.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class IgnoreAllErrorHandler
  implements ErrorHandler
{
  static Log log = LogFactory.getLog(IgnoreAllErrorHandler.class.getName());
  static final boolean warnOnExceptions = System.getProperty("org.apache.xml.security.test.warn.on.exceptions", "false").equals("true");
  static final boolean throwExceptions = System.getProperty("org.apache.xml.security.test.throw.exceptions", "false").equals("true");

  public void warning(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (warnOnExceptions)
      log.warn("", paramSAXParseException);
    if (throwExceptions)
      throw paramSAXParseException;
  }

  public void error(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (warnOnExceptions)
      log.error("", paramSAXParseException);
    if (throwExceptions)
      throw paramSAXParseException;
  }

  public void fatalError(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (warnOnExceptions)
      log.warn("", paramSAXParseException);
    if (throwExceptions)
      throw paramSAXParseException;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.IgnoreAllErrorHandler
 * JD-Core Version:    0.6.0
 */