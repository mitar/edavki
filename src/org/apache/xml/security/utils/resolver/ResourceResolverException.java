package org.apache.xml.security.utils.resolver;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Attr;

public class ResourceResolverException extends XMLSecurityException
{
  Attr _uri = null;
  String _BaseURI;

  public ResourceResolverException(String paramString1, Attr paramAttr, String paramString2)
  {
    super(paramString1);
    this._uri = paramAttr;
    this._BaseURI = paramString2;
  }

  public ResourceResolverException(String paramString1, Object[] paramArrayOfObject, Attr paramAttr, String paramString2)
  {
    super(paramString1, paramArrayOfObject);
    this._uri = paramAttr;
    this._BaseURI = paramString2;
  }

  public ResourceResolverException(String paramString1, Exception paramException, Attr paramAttr, String paramString2)
  {
    super(paramString1, paramException);
    this._uri = paramAttr;
    this._BaseURI = paramString2;
  }

  public ResourceResolverException(String paramString1, Object[] paramArrayOfObject, Exception paramException, Attr paramAttr, String paramString2)
  {
    super(paramString1, paramArrayOfObject, paramException);
    this._uri = paramAttr;
    this._BaseURI = paramString2;
  }

  public void setURI(Attr paramAttr)
  {
    this._uri = paramAttr;
  }

  public Attr getURI()
  {
    return this._uri;
  }

  public void setBaseURI(String paramString)
  {
    this._BaseURI = paramString;
  }

  public String getBaseURI()
  {
    return this._BaseURI;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.ResourceResolverException
 * JD-Core Version:    0.6.0
 */