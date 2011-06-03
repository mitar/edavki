package si.hermes.security;

import java.net.URI;
import java.security.cert.X509CRL;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SimpleCacheCRLResolver
{
  static Log log = LogFactory.getLog(SimpleCacheCRLResolver.class.getName());
  private static ICRLResolver fCRLResolver = new CRLResolverImpl();
  private static Hashtable fInternalCRLCache = new Hashtable();
  private static int fTimeout = 86400;

  public static int getCacheTimeout()
  {
    return fTimeout;
  }

  public static ICRLResolver getCRLResolver()
  {
    return fCRLResolver;
  }

  public static X509CRL ResolverCRL(String paramString)
  {
    if (log.isDebugEnabled())
      log.debug("SimpleCacheCRLResolver.ResolverCRL ENTER (" + paramString + ")");
    SimpleObject localSimpleObject;
    if (((localSimpleObject = (SimpleObject)fInternalCRLCache.get(paramString)) == null) || (localSimpleObject.Expired(fTimeout)))
      synchronized (fInternalCRLCache)
      {
        if (((localSimpleObject = (SimpleObject)fInternalCRLCache.get(paramString)) == null) || (localSimpleObject.Expired(fTimeout)))
        {
          try
          {
            URI localURI = new URI(paramString);
            if ("ldap".equalsIgnoreCase(localURI.getScheme()))
              localSimpleObject = new SimpleObject(fCRLResolver.ResolveLDAP(localURI));
            else
              localSimpleObject = new SimpleObject(fCRLResolver.ResolveURL(localURI));
          }
          catch (Exception localException)
          {
            localSimpleObject = new SimpleObject(null);
          }
          fInternalCRLCache.put(paramString, localSimpleObject);
        }
      }
    if (log.isDebugEnabled())
      log.debug("SimpleCacheCRLResolver.ResolverCRL EXIT");
    return localSimpleObject.fInput;
  }

  public static void setCacheTimeout(int paramInt)
  {
    fTimeout = paramInt;
  }

  public static void setCRLResolver(ICRLResolver paramICRLResolver)
  {
    fCRLResolver = paramICRLResolver;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.SimpleCacheCRLResolver
 * JD-Core Version:    0.6.0
 */