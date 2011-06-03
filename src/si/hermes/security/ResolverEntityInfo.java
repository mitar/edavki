package si.hermes.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.w3c.dom.Attr;

public final class ResolverEntityInfo extends ResourceResolverSpi
  implements IResolverEntity
{
  static Log log = LogFactory.getLog(ResolverEntityInfo.class.getName());
  private IResolver resolver;

  public final IResolver getResolver()
  {
    return this.resolver;
  }

  public ResolverEntityInfo(String paramString)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException
  {
    this.resolver = ((IResolver)Class.forName(paramString).newInstance());
  }

  public ResolverEntityInfo()
  {
    this.resolver = new BaseResolver();
  }

  public final boolean engineCanResolve(Attr paramAttr, String paramString)
  {
    byte[] arrayOfByte;
    if ((arrayOfByte = this.resolver.getEntity(paramAttr.getValue())) != null)
    {
      log.info("Can resolve? " + paramAttr.getValue() + " true");
      return true;
    }
    log.info("Can resolve? " + paramAttr.getValue() + " false");
    return false;
  }

  public final XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    try
    {
      log.info("Resolving " + paramAttr.getValue());
      byte[] arrayOfByte;
      if ((arrayOfByte = this.resolver.getEntity(paramAttr.getValue())) == null)
        throw new Exception();
      return new XMLSignatureInput(arrayOfByte);
    }
    catch (Exception localException)
    {
    }
    throw new ResourceResolverException("generic.EmptyMessage", localException, paramAttr, paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ResolverEntityInfo
 * JD-Core Version:    0.6.0
 */