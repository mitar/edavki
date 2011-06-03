package org.apache.xml.security.keys.keyresolver;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Vector;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.w3c.dom.Element;

public class KeyResolver
{
  static Log log = LogFactory.getLog(KeyResolver.class.getName());
  static boolean _alreadyInitialized = false;
  static Vector _resolverVector = null;
  protected KeyResolverSpi _resolverSpi = null;
  protected StorageResolver _storage = null;

  private KeyResolver(String paramString)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    this._resolverSpi = ((KeyResolverSpi)Class.forName(paramString).newInstance());
  }

  public static int length()
  {
    return _resolverVector.size();
  }

  public static KeyResolver item(int paramInt)
    throws KeyResolverException
  {
    String str = (String)_resolverVector.elementAt(paramInt);
    KeyResolver localKeyResolver = null;
    try
    {
      localKeyResolver = new KeyResolver(str);
    }
    catch (Exception localException)
    {
      throw new KeyResolverException("utils.resolver.noClass", localException);
    }
    return localKeyResolver;
  }

  public static final KeyResolver getInstance(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    for (int i = 0; i < _resolverVector.size(); i++)
    {
      String str = (String)_resolverVector.elementAt(i);
      KeyResolver localKeyResolver;
      try
      {
        localKeyResolver = new KeyResolver(str);
      }
      catch (Exception localException)
      {
        Object[] arrayOfObject2 = { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
        throw new KeyResolverException("utils.resolver.noClass", arrayOfObject2, localException);
      }
      if (log.isDebugEnabled())
        log.debug("check resolvability by class " + str);
      if (localKeyResolver.canResolve(paramElement, paramString, paramStorageResolver))
        return localKeyResolver;
    }
    Object[] arrayOfObject1 = { (paramElement != null) && (paramElement.getNodeType() == 1) ? paramElement.getTagName() : "null" };
    throw new KeyResolverException("utils.resolver.noClass", arrayOfObject1);
  }

  public static void init()
  {
    if (!_alreadyInitialized)
    {
      _resolverVector = new Vector(10);
      _alreadyInitialized = true;
    }
  }

  public static void register(String paramString)
  {
    _resolverVector.add(paramString);
  }

  public static void registerAtStart(String paramString)
  {
    _resolverVector.add(0, paramString);
  }

  public static PublicKey resolveStatic(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    KeyResolver localKeyResolver;
    return (localKeyResolver = getInstance(paramElement, paramString, paramStorageResolver)).resolvePublicKey(paramElement, paramString, paramStorageResolver);
  }

  public PublicKey resolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    return this._resolverSpi.engineResolvePublicKey(paramElement, paramString, paramStorageResolver);
  }

  public X509Certificate resolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    return this._resolverSpi.engineResolveX509Certificate(paramElement, paramString, paramStorageResolver);
  }

  public SecretKey resolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    return this._resolverSpi.engineResolveSecretKey(paramElement, paramString, paramStorageResolver);
  }

  public void setProperty(String paramString1, String paramString2)
  {
    this._resolverSpi.engineSetProperty(paramString1, paramString2);
  }

  public String getProperty(String paramString)
  {
    return this._resolverSpi.engineGetProperty(paramString);
  }

  public String[] getPropertyKeys()
  {
    return this._resolverSpi.engineGetPropertyKeys();
  }

  public boolean understandsProperty(String paramString)
  {
    return this._resolverSpi.understandsProperty(paramString);
  }

  public boolean canResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return this._resolverSpi.engineCanResolve(paramElement, paramString, paramStorageResolver);
  }

  public String resolverClassName()
  {
    return this._resolverSpi.getClass().getName();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.KeyResolver
 * JD-Core Version:    0.6.0
 */