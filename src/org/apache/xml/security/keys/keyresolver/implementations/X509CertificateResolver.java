package org.apache.xml.security.keys.keyresolver.implementations;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class X509CertificateResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(X509CertificateResolver.class.getName());
  Element[] _x509CertKeyElements = null;
  XMLX509Certificate[] _x509certObject = null;

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (log.isDebugEnabled())
      log.debug("Can I resolve " + paramElement.getTagName() + "?");
    if (!XMLUtils.elementIsInSignatureSpace(paramElement, "X509Data"))
    {
      log.debug("I can't");
      return false;
    }
    this._x509CertKeyElements = XMLUtils.selectDsNodes(paramElement.getFirstChild(), "X509Certificate");
    if ((this._x509CertKeyElements != null) && (this._x509CertKeyElements.length > 0))
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
      if (((this._x509CertKeyElements == null) || (this._x509CertKeyElements.length == 0)) && ((!(i = engineCanResolve(paramElement, paramString, paramStorageResolver))) || (this._x509CertKeyElements == null) || (this._x509CertKeyElements.length == 0)))
        return null;
      this._x509certObject = new XMLX509Certificate[this._x509CertKeyElements.length];
      for (int i = 0; i < this._x509CertKeyElements.length; i++)
        this._x509certObject[i] = new XMLX509Certificate(this._x509CertKeyElements[i], paramString);
      for (int j = 0; j < this._x509certObject.length; j++)
      {
        X509Certificate localX509Certificate;
        if ((localX509Certificate = this._x509certObject[j].getX509Certificate()) != null)
          return localX509Certificate;
      }
      return null;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      log.debug("XMLSecurityException", localXMLSecurityException);
    }
    throw new KeyResolverException("generic.EmptyMessage", localXMLSecurityException);
  }

  public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.X509CertificateResolver
 * JD-Core Version:    0.6.0
 */