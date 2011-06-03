package org.apache.xml.security.keys.keyresolver.implementations;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.keyvalues.RSAKeyValue;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class RSAKeyValueResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(RSAKeyValueResolver.class.getName());
  private Element _rsaKeyElement = null;

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (log.isDebugEnabled())
      log.debug("Can I resolve " + paramElement.getTagName());
    if (paramElement == null)
      return false;
    boolean bool1 = XMLUtils.elementIsInSignatureSpace(paramElement, "KeyValue");
    boolean bool2 = XMLUtils.elementIsInSignatureSpace(paramElement, "RSAKeyValue");
    if (bool1)
    {
      this._rsaKeyElement = XMLUtils.selectDsNode(paramElement.getFirstChild(), "RSAKeyValue", 0);
      if (this._rsaKeyElement != null)
        return true;
    }
    else if (bool2)
    {
      this._rsaKeyElement = paramElement;
      return true;
    }
    return false;
  }

  public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    boolean bool;
    if ((this._rsaKeyElement == null) && ((!(bool = engineCanResolve(paramElement, paramString, paramStorageResolver))) || (this._rsaKeyElement == null)))
      return null;
    try
    {
      RSAKeyValue localRSAKeyValue;
      return (localRSAKeyValue = new RSAKeyValue(this._rsaKeyElement, paramString)).getPublicKey();
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      log.debug("XMLSecurityException", localXMLSecurityException);
    }
    return null;
  }

  public X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }

  public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.RSAKeyValueResolver
 * JD-Core Version:    0.6.0
 */