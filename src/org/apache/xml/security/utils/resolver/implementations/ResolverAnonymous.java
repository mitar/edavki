package org.apache.xml.security.utils.resolver.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.Attr;

public class ResolverAnonymous extends ResourceResolverSpi
{
  static Log log = LogFactory.getLog(ResolverAnonymous.class.getName());
  private XMLSignatureInput _input = null;

  public ResolverAnonymous(String paramString)
    throws FileNotFoundException, IOException
  {
    this._input = new XMLSignatureInput(new FileInputStream(paramString));
  }

  public ResolverAnonymous(InputStream paramInputStream)
  {
    this._input = new XMLSignatureInput(paramInputStream);
  }

  public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
  {
    return this._input;
  }

  public boolean engineCanResolve(Attr paramAttr, String paramString)
  {
    return paramAttr == null;
  }

  public String[] engineGetPropertyKeys()
  {
    return new String[0];
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.implementations.ResolverAnonymous
 * JD-Core Version:    0.6.0
 */