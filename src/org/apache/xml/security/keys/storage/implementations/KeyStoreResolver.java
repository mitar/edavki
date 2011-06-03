package org.apache.xml.security.keys.storage.implementations;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.xml.security.keys.content.x509.XMLX509SKI;
import org.apache.xml.security.keys.storage.StorageResolverException;
import org.apache.xml.security.keys.storage.StorageResolverSpi;
import org.apache.xml.security.utils.Base64;

public class KeyStoreResolver extends StorageResolverSpi
{
  KeyStore _keyStore = null;
  Iterator _iterator = null;

  public KeyStoreResolver(KeyStore paramKeyStore)
    throws StorageResolverException
  {
    this._keyStore = paramKeyStore;
    this._iterator = new KeyStoreIterator(this, this._keyStore);
  }

  public Iterator getIterator()
  {
    return this._iterator;
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    KeyStore localKeyStore;
    (localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())).load(new FileInputStream("data/org/apache/xml/security/samples/input/keystore.jks"), "xmlsecurity".toCharArray());
    KeyStoreResolver localKeyStoreResolver;
    Iterator localIterator = (localKeyStoreResolver = new KeyStoreResolver(localKeyStore)).getIterator();
    while (localIterator.hasNext())
    {
      X509Certificate localX509Certificate;
      byte[] arrayOfByte = XMLX509SKI.getSKIBytesFromCert(localX509Certificate = (X509Certificate)localIterator.next());
      System.out.println(Base64.encode(arrayOfByte));
    }
  }

  class KeyStoreIterator
    implements Iterator
  {
    KeyStore _keyStore = null;
    Enumeration _aliases = null;

    public KeyStoreIterator(KeyStoreResolver paramKeyStore, KeyStore arg3)
      throws StorageResolverException
    {
      try
      {
        Object localObject;
        this._keyStore = localObject;
        this._aliases = this._keyStore.aliases();
        return;
      }
      catch (KeyStoreException localKeyStoreException)
      {
        throw new StorageResolverException("generic.EmptyMessage", localKeyStoreException);
      }
    }

    public boolean hasNext()
    {
      return this._aliases.hasMoreElements();
    }

    public Object next()
    {
      String str = (String)this._aliases.nextElement();
      try
      {
        return this._keyStore.getCertificate(str);
      }
      catch (KeyStoreException localKeyStoreException)
      {
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
 * Qualified Name:     org.apache.xml.security.keys.storage.implementations.KeyStoreResolver
 * JD-Core Version:    0.6.0
 */