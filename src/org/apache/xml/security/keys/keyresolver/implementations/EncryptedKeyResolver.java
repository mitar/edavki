package org.apache.xml.security.keys.keyresolver.implementations;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class EncryptedKeyResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(RSAKeyValueResolver.class.getName());
  Key _key = null;
  Key _kek;
  String _algorithm;

  public EncryptedKeyResolver(String paramString)
  {
    this._kek = null;
    this._algorithm = paramString;
  }

  public EncryptedKeyResolver(String paramString, Key paramKey)
  {
    this._algorithm = paramString;
    this._kek = paramKey;
  }

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (log.isDebugEnabled())
      log.debug("EncryptedKeyResolver - Can I resolve " + paramElement.getTagName());
    if (paramElement == null)
      return false;
    boolean bool;
    if ((bool = XMLUtils.elementIsInEncryptionSpace(paramElement, "EncryptedKey")))
    {
      log.debug("Passed an Encrypted Key");
      try
      {
        XMLCipher localXMLCipher;
        (localXMLCipher = XMLCipher.getInstance()).init(4, this._kek);
        EncryptedKey localEncryptedKey = localXMLCipher.loadEncryptedKey(paramElement);
        this._key = localXMLCipher.decryptKey(localEncryptedKey, this._algorithm);
      }
      catch (Exception localException)
      {
      }
    }
    return this._key != null;
  }

  public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }

  public X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return null;
  }

  public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    return (SecretKey)this._key;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.EncryptedKeyResolver
 * JD-Core Version:    0.6.0
 */