package org.apache.xml.security.keys.keyresolver.implementations;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.keys.content.x509.XMLX509IssuerSerial;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Element;

public class X509IssuerSerialResolver extends KeyResolverSpi
{
  static Log log = LogFactory.getLog(X509IssuerSerialResolver.class.getName());

  public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
  {
    if (log.isDebugEnabled())
      log.debug("Can I resolve " + paramElement.getTagName() + "?");
    X509Data localX509Data = null;
    try
    {
      localX509Data = new X509Data(paramElement, paramString);
    }
    catch (XMLSignatureException localXMLSignatureException)
    {
      log.debug("I can't");
      return false;
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
      log.debug("I can't");
      return false;
    }
    if (localX509Data.containsIssuerSerial())
      return true;
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
      Object localObject;
      if (paramStorageResolver == null)
      {
        localObject = new Object[] { "X509IssuerSerial" };
        KeyResolverException localKeyResolverException = new KeyResolverException("KeyResolver.needStorageResolver", localObject);
        log.info("", localKeyResolverException);
        throw localKeyResolverException;
      }
      int i = (localObject = new X509Data(paramElement, paramString)).lengthIssuerSerial();
      while (paramStorageResolver.hasNext())
      {
        X509Certificate localX509Certificate = paramStorageResolver.next();
        XMLX509IssuerSerial localXMLX509IssuerSerial1 = new XMLX509IssuerSerial(paramElement.getOwnerDocument(), localX509Certificate);
        if (log.isDebugEnabled())
        {
          log.debug("Found Certificate Issuer: " + localXMLX509IssuerSerial1.getIssuerName());
          log.debug("Found Certificate Serial: " + localXMLX509IssuerSerial1.getSerialNumber().toString());
        }
        for (int j = 0; j < i; j++)
        {
          XMLX509IssuerSerial localXMLX509IssuerSerial2 = ((X509Data)localObject).itemIssuerSerial(j);
          if (log.isDebugEnabled())
          {
            log.debug("Found Element Issuer:     " + localXMLX509IssuerSerial2.getIssuerName());
            log.debug("Found Element Serial:     " + localXMLX509IssuerSerial2.getSerialNumber().toString());
          }
          if (localXMLX509IssuerSerial1.equals(localXMLX509IssuerSerial2))
          {
            log.debug("match !!! ");
            return localX509Certificate;
          }
          log.debug("no match...");
        }
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
 * Qualified Name:     org.apache.xml.security.keys.keyresolver.implementations.X509IssuerSerialResolver
 * JD-Core Version:    0.6.0
 */