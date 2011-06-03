package org.apache.xml.security.keys.keyresolver.implementations;

import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.x509.XMLX509SKI;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class X509SKIResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(X509SKIResolver.class.getName());
  private Element[] _x509childNodes = null;
  private XMLX509SKI[] _x509childObject = null;

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (log.isDebugEnabled())
      log.debug("Can I resolve " + paramElement.getTagName() + "?");
    if (!XMLUtils.elementIsInSignatureSpace(paramElement, "X509Data"))
    {
      log.debug("I can't");
      return false;
    }
    this._x509childNodes = XMLUtils.selectDsNodes(paramElement, "X509SKI");
    if ((this._x509childNodes != null) && (this._x509childNodes.length > 0))
    {
      log.debug("Yes Sir, I can");
      return true;
    }
    log.debug("I can't");
    return false;
  }

  public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    X509Certificate localX509Certificate;
    if ((localX509Certificate = engineResolveX509Certificate(paramElement, paramString, paramStorageResolver)) != null)
      return localX509Certificate.getPublicKey();
    return null;
  }

  public X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException
  {
    try
    {
      boolean bool;
      if ((this._x509childNodes == null) && ((!(bool = engineCanResolve(paramElement, paramString, paramStorageResolver))) || (this._x509childNodes == null)))
        return null;
      Object localObject;
      if (paramStorageResolver == null)
      {
        Object[] arrayOfObject = { "X509SKI" };
        localObject = new KeyResolverException("KeyResolver.needStorageResolver", arrayOfObject);
        log.info("", (Throwable)localObject);
        throw ((Throwable)localObject);
      }
      this._x509childObject = new XMLX509SKI[this._x509childNodes.length];
      for (int i = 0; i < this._x509childNodes.length; i++)
        this._x509childObject[i] = new XMLX509SKI(this._x509childNodes[i], paramString);
      while (paramStorageResolver.hasNext())
      {
        X509Certificate localX509Certificate = paramStorageResolver.next();
        localObject = new XMLX509SKI(paramElement.getOwnerDocument(), localX509Certificate);
        for (int j = 0; j < this._x509childObject.length; j++)
        {
          if (!((XMLX509SKI)localObject).equals(this._x509childObject[j]))
            continue;
          log.debug("Return PublicKey from " + localX509Certificate.getSubjectDN().getName());
          return localX509Certificate;
        }
      }
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      throw new KeyResolverException("empty", localXMLSecurityException);
    }
    return (X509Certificate)null;
  }

  public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.X509SKIResolver
 * JD-Core Version:    0.6.0
 */