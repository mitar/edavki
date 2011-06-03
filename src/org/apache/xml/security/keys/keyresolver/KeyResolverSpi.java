package org.apache.xml.security.keys.keyresolver;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.w3c.dom.Element;

public abstract class KeyResolverSpi
{
  static Log log = LogFactory.getLog(KeyResolverSpi.class.getName());
  protected Map _properties = new HashMap(10);

  public abstract boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver);

  public abstract PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException;

  public abstract X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException;

  public abstract SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
    throws KeyResolverException;

  public void engineSetProperty(String paramString1, String paramString2)
  {
    Iterator localIterator = this._properties.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals(paramString1))
      {
        paramString1 = str;
        break;
      }
    }
    this._properties.put(paramString1, paramString2);
  }

  public String engineGetProperty(String paramString)
  {
    Iterator localIterator = this._properties.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals(paramString))
      {
        paramString = str;
        break;
      }
    }
    return (String)this._properties.get(paramString);
  }

  public String[] engineGetPropertyKeys()
  {
    return new String[0];
  }

  public boolean understandsProperty(String paramString)
  {
    String[] arrayOfString;
    if ((arrayOfString = engineGetPropertyKeys()) != null)
      for (int i = 0; i < arrayOfString.length; i++)
        if (arrayOfString[i].equals(paramString))
          return true;
    return false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.KeyResolverSpi
 * JD-Core Version:    0.6.0
 */