package org.apache.xml.security.keys.storage;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.keys.storage.implementations.KeyStoreResolver;
import org.apache.xml.security.keys.storage.implementations.SingleCertificateResolver;

public class StorageResolver
{
  static Log log = LogFactory.getLog(StorageResolver.class.getName());
  Vector _storageResolvers = new Vector();
  Iterator _iterator = null;

  public StorageResolver()
  {
  }

  public StorageResolver(StorageResolverSpi paramStorageResolverSpi)
  {
    add(paramStorageResolverSpi);
  }

  public void add(StorageResolverSpi paramStorageResolverSpi)
  {
    this._storageResolvers.add(paramStorageResolverSpi);
    this._iterator = null;
  }

  public StorageResolver(KeyStore paramKeyStore)
  {
    add(paramKeyStore);
  }

  public void add(KeyStore paramKeyStore)
  {
    try
    {
      add(new KeyStoreResolver(paramKeyStore));
      return;
    }
    catch (StorageResolverException localStorageResolverException)
    {
      log.error("Could not add KeyStore because of: ", localStorageResolverException);
    }
  }

  public StorageResolver(X509Certificate paramX509Certificate)
  {
    add(paramX509Certificate);
  }

  public void add(X509Certificate paramX509Certificate)
  {
    add(new SingleCertificateResolver(paramX509Certificate));
  }

  public Iterator getIterator()
  {
    if (this._iterator == null)
      this._iterator = new StorageResolverIterator(this._storageResolvers);
    return this._iterator;
  }

  public boolean hasNext()
  {
    if (this._iterator == null)
      this._iterator = new StorageResolverIterator(this._storageResolvers);
    return this._iterator.hasNext();
  }

  public X509Certificate next()
  {
    return (X509Certificate)this._iterator.next();
  }

  class StorageResolverIterator
    implements Iterator
  {
    Vector _resolvers = null;
    int _currentResolver = 0;

    public StorageResolverIterator(Vector arg2)
    {
      Object localObject;
      this._resolvers = localObject;
      this._currentResolver = 0;
    }

    public boolean hasNext()
    {
      if (this._resolvers == null)
        return false;
      while (this._currentResolver < this._resolvers.size())
      {
        StorageResolverSpi localStorageResolverSpi;
        if ((localStorageResolverSpi = (StorageResolverSpi)this._resolvers.elementAt(this._currentResolver)) == null)
          continue;
        if (localStorageResolverSpi.getIterator().hasNext())
          return true;
        this._currentResolver += 1;
      }
      return false;
    }

    public Object next()
    {
      if (this._resolvers == null)
        return null;
      while (this._currentResolver < this._resolvers.size())
      {
        StorageResolverSpi localStorageResolverSpi;
        if ((localStorageResolverSpi = (StorageResolverSpi)this._resolvers.elementAt(this._currentResolver)) == null)
          continue;
        if (localStorageResolverSpi.getIterator().hasNext())
          return localStorageResolverSpi.getIterator().next();
        this._currentResolver += 1;
      }
      return null;
    }

    public void remove()
    {
      throw new UnsupportedOperationException("Can't remove keys from KeyStore");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.storage.StorageResolver
 * JD-Core Version:    0.6.0
 */