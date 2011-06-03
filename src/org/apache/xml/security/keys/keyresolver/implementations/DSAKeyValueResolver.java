package org.apache.xml.security.keys.keyresolver.implementations;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.keyvalues.DSAKeyValue;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

public class DSAKeyValueResolver extends KeyResolverSpi
{
  private Element _dsaKeyElement = null;

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (paramElement == null)
      return false;
    boolean bool1 = XMLUtils.elementIsInSignatureSpace(paramElement, "KeyValue");
    boolean bool2 = XMLUtils.elementIsInSignatureSpace(paramElement, "DSAKeyValue");
    if (bool1)
    {
      this._dsaKeyElement = XMLUtils.selectDsNode(paramElement.getFirstChild(), "DSAKeyValue", 0);
      if (this._dsaKeyElement != null)
        return true;
    }
    else if (bool2)
    {
      this._dsaKeyElement = paramElement;
      return true;
    }
    return false;
  }

  public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    boolean bool;
    if ((this._dsaKeyElement == null) && ((!(bool = engineCanResolve(paramElement, paramString, paramStorageResolver))) || (this._dsaKeyElement == null)))
      return null;
    try
    {
      DSAKeyValue localDSAKeyValue;
      PublicKey localPublicKey;
      return localPublicKey = (localDSAKeyValue = new DSAKeyValue(this._dsaKeyElement, paramString)).getPublicKey();
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
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
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.DSAKeyValueResolver
 * JD-Core Version:    0.6.0
 */