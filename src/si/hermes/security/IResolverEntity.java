package si.hermes.security;

import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.w3c.dom.Attr;

public abstract interface IResolverEntity
{
  public abstract IResolver getResolver();

  public abstract boolean engineCanResolve(Attr paramAttr, String paramString);

  public abstract XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.IResolverEntity
 * JD-Core Version:    0.6.0
 */