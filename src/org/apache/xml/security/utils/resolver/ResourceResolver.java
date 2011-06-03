package org.apache.xml.security.utils.resolver;

import java.util.Map;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.w3c.dom.Attr;

public class ResourceResolver
{
  static Log log = LogFactory.getLog(ResourceResolver.class.getName());
  static boolean _alreadyInitialized = false;
  static Vector _resolverVector = null;
  Vector _individualResolverVector = null;
  protected ResourceResolverSpi _resolverSpi = null;

  private ResourceResolver(String paramString)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException
  {
    this._resolverSpi = ((ResourceResolverSpi)Class.forName(paramString).newInstance());
  }

  public ResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
  {
    this._resolverSpi = paramResourceResolverSpi;
  }

  public static final ResourceResolver getInstance(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    for (int i = 0; i < _resolverVector.size(); i++)
    {
      String str = (String)_resolverVector.elementAt(i);
      ResourceResolver localResourceResolver;
      try
      {
        localResourceResolver = new ResourceResolver(str);
      }
      catch (Exception localException)
      {
        Object[] arrayOfObject2 = { paramAttr != null ? paramAttr.getNodeValue() : "null", paramString };
        throw new ResourceResolverException("utils.resolver.noClass", arrayOfObject2, localException, paramAttr, paramString);
      }
      if (log.isDebugEnabled())
        log.debug("check resolvability by class " + str);
      if (localResourceResolver.canResolve(paramAttr, paramString))
        return localResourceResolver;
    }
    Object[] arrayOfObject1 = { paramAttr != null ? paramAttr.getNodeValue() : "null", paramString };
    throw new ResourceResolverException("utils.resolver.noClass", arrayOfObject1, paramAttr, paramString);
  }

  public static final ResourceResolver getInstance(Attr paramAttr, String paramString, Vector paramVector)
    throws ResourceResolverException
  {
    if (log.isDebugEnabled())
    {
      log.debug("I was asked to create a ResourceResolver and got " + paramVector.size());
      log.debug(" extra resolvers to my existing " + _resolverVector.size() + " system-wide resolvers");
    }
    Object localObject1;
    Object localObject2;
    if ((paramVector != null) && (paramVector.size() > 0))
      for (i = 0; i < paramVector.size(); i++)
      {
        if ((localObject1 = (ResourceResolver)paramVector.elementAt(i)) == null)
          continue;
        localObject2 = ((ResourceResolver)localObject1)._resolverSpi.getClass().getName();
        if (log.isDebugEnabled())
          log.debug("check resolvability by class " + (String)localObject2);
        if (((ResourceResolver)localObject1).canResolve(paramAttr, paramString))
          return localObject1;
      }
    for (int i = 0; i < _resolverVector.size(); i++)
    {
      localObject1 = (String)_resolverVector.elementAt(i);
      try
      {
        localObject2 = new ResourceResolver((String)localObject1);
      }
      catch (Exception localException)
      {
        Object[] arrayOfObject2 = { paramAttr != null ? paramAttr.getNodeValue() : "null", paramString };
        throw new ResourceResolverException("utils.resolver.noClass", arrayOfObject2, localException, paramAttr, paramString);
      }
      if (log.isDebugEnabled())
        log.debug("check resolvability by class " + (String)localObject1);
      if (((ResourceResolver)localObject2).canResolve(paramAttr, paramString))
        return localObject2;
    }
    Object[] arrayOfObject1 = { paramAttr != null ? paramAttr.getNodeValue() : "null", paramString };
    throw new ResourceResolverException("utils.resolver.noClass", arrayOfObject1, paramAttr, paramString);
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

  public static XMLSignatureInput resolveStatic(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    ResourceResolver localResourceResolver;
    return (localResourceResolver = getInstance(paramAttr, paramString)).resolve(paramAttr, paramString);
  }

  public XMLSignatureInput resolve(Attr paramAttr, String paramString)
    throws ResourceResolverException
  {
    return this._resolverSpi.engineResolve(paramAttr, paramString);
  }

  public void setProperty(String paramString1, String paramString2)
  {
    this._resolverSpi.engineSetProperty(paramString1, paramString2);
  }

  public String getProperty(String paramString)
  {
    return this._resolverSpi.engineGetProperty(paramString);
  }

  public void addProperties(Map paramMap)
  {
    this._resolverSpi.engineAddProperies(paramMap);
  }

  public String[] getPropertyKeys()
  {
    return this._resolverSpi.engineGetPropertyKeys();
  }

  public boolean understandsProperty(String paramString)
  {
    return this._resolverSpi.understandsProperty(paramString);
  }

  private boolean canResolve(Attr paramAttr, String paramString)
  {
    return this._resolverSpi.engineCanResolve(paramAttr, paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.resolver.ResourceResolver
 * JD-Core Version:    0.6.0
 */