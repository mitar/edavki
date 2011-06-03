package org.apache.xml.security.keys.storage.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.keys.content.x509.XMLX509SKI;
import org.apache.xml.security.keys.storage.StorageResolverException;
import org.apache.xml.security.keys.storage.StorageResolverSpi;
import org.apache.xml.security.utils.Base64;

public class CertsInFilesystemDirectoryResolver extends StorageResolverSpi
{
  static Log log = LogFactory.getLog(CertsInFilesystemDirectoryResolver.class.getName());
  String _merlinsCertificatesDir = null;
  private Vector _certs = new Vector();
  Iterator _iterator = null;

  public CertsInFilesystemDirectoryResolver(String paramString)
    throws StorageResolverException
  {
    this._merlinsCertificatesDir = paramString;
    readCertsFromHarddrive();
    this._iterator = new FilesystemIterator(this._certs);
  }

  private void readCertsFromHarddrive()
    throws StorageResolverException
  {
    File localFile1 = new File(this._merlinsCertificatesDir);
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = localFile1.list();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      String str1;
      if (!(str1 = arrayOfString[i]).endsWith(".crt"))
        continue;
      localArrayList.add(arrayOfString[i]);
    }
    CertificateFactory localCertificateFactory = null;
    try
    {
      localCertificateFactory = CertificateFactory.getInstance("X.509");
    }
    catch (CertificateException localCertificateException1)
    {
      throw new StorageResolverException("empty", localCertificateException1);
    }
    if (localCertificateFactory == null)
      throw new StorageResolverException("empty");
    int j = 0;
    String str2 = localFile1.getAbsolutePath() + File.separator + (String)localArrayList.get(j);
    File localFile2 = new File(str2);
    int k = 0;
    String str3 = null;
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(localFile2);
      X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(localFileInputStream);
      localFileInputStream.close();
      localX509Certificate.checkValidity();
      this._certs.add(localX509Certificate);
      str3 = localX509Certificate.getSubjectDN().getName();
      k = 1;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      tmpTernaryOp = new StringBuffer();
    }
    catch (IOException localIOException)
    {
      tmpTernaryOp = new StringBuffer();
    }
    catch (CertificateNotYetValidException localCertificateNotYetValidException)
    {
      tmpTernaryOp = new StringBuffer();
    }
    catch (CertificateExpiredException localCertificateExpiredException)
    {
      tmpTernaryOp = new StringBuffer();
    }
    catch (CertificateException localCertificateException2)
    {
    }
    log.debug("Could not add certificate from file " + str2, localCertificateException2);
    if ((k != 0) && (log.isDebugEnabled()))
      log.debug("Added certificate: " + str3);
    j++;
  }

  public Iterator getIterator()
  {
    return this._iterator;
  }

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    CertsInFilesystemDirectoryResolver localCertsInFilesystemDirectoryResolver;
    Iterator localIterator = (localCertsInFilesystemDirectoryResolver = new CertsInFilesystemDirectoryResolver("data/ie/baltimore/merlin-examples/merlin-xmldsig-eighteen/certs")).getIterator();
    while (localIterator.hasNext())
    {
      X509Certificate localX509Certificate;
      byte[] arrayOfByte = XMLX509SKI.getSKIBytesFromCert(localX509Certificate = (X509Certificate)localIterator.next());
      System.out.println();
      System.out.println("Base64(SKI())=                 \"" + Base64.encode(arrayOfByte) + "\"");
      System.out.println("cert.getSerialNumber()=        \"" + localX509Certificate.getSerialNumber().toString() + "\"");
      System.out.println("cert.getSubjectDN().getName()= \"" + localX509Certificate.getSubjectDN().getName() + "\"");
      System.out.println("cert.getIssuerDN().getName()=  \"" + localX509Certificate.getIssuerDN().getName() + "\"");
    }
  }

  class FilesystemIterator
    implements Iterator
  {
    Vector _certs = null;
    int _i;

    public FilesystemIterator(Vector arg2)
    {
      Object localObject;
      this._certs = localObject;
      this._i = 0;
    }

    public boolean hasNext()
    {
      return this._i < this._certs.size();
    }

    public Object next()
    {
      return this._certs.elementAt(this._i++);
    }

    public void remove()
    {
      throw new UnsupportedOperationException("Can't remove keys from KeyStore");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.storage.implementations.CertsInFilesystemDirectoryResolver
 * JD-Core Version:    0.6.0
 */