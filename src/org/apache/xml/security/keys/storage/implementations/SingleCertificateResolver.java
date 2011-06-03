package org.apache.xml.security.keys.storage.implementations;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import org.apache.xml.security.keys.storage.StorageResolverSpi;

public class SingleCertificateResolver extends StorageResolverSpi
{
  X509Certificate _certificate = null;
  Iterator _iterator = null;

  public SingleCertificateResolver(X509Certificate paramX509Certificate)
  {
    this._certificate = paramX509Certificate;
    this._iterator = new InternalIterator(this._certificate);
  }

  public Iterator getIterator()
  {
    return this._iterator;
  }

  class InternalIterator
    implements Iterator
  {
    boolean _alreadyReturned = false;
    X509Certificate _certificate = null;

    public InternalIterator(X509Certificate arg2)
    {
      Object localObject;
      this._certificate = localObject;
    }

    public boolean hasNext()
    {
      return !this._alreadyReturned;
    }

    public Object next()
    {
      this._alreadyReturned = true;
      return this._certificate;
    }

    public void remove()
    {
      throw new UnsupportedOperationException("Can't remove keys from KeyStore");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.storage.implementations.SingleCertificateResolver
 * JD-Core Version:    0.6.0
 */