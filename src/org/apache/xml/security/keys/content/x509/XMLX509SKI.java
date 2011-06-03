package org.apache.xml.security.keys.content.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.JavaUtils;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.security.util.DerValue;

public class XMLX509SKI extends SignatureElementProxy
  implements XMLX509DataContent
{
  static Log log = LogFactory.getLog(XMLX509SKI.class.getName());
  public static final String SKI_OID = "2.5.29.14";

  public XMLX509SKI(Document paramDocument, byte[] paramArrayOfByte)
  {
    super(paramDocument);
    addBase64Text(paramArrayOfByte);
  }

  public XMLX509SKI(Document paramDocument, X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    super(paramDocument);
    addBase64Text(getSKIBytesFromCert(paramX509Certificate));
  }

  public XMLX509SKI(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public byte[] getSKIBytes()
    throws XMLSecurityException
  {
    return getBytesFromTextChild();
  }

  public static byte[] getSKIBytesFromCert(X509Certificate paramX509Certificate)
    throws XMLSecurityException
  {
    try
    {
      byte[] arrayOfByte = paramX509Certificate.getExtensionValue("2.5.29.14");
      if (paramX509Certificate.getVersion() < 3)
      {
        localObject1 = new Object[] { new Integer(paramX509Certificate.getVersion()) };
        throw new XMLSecurityException("certificate.noSki.lowVersion", localObject1);
      }
      Object localObject1 = null;
      try
      {
        if (XMLUtils.classForName("sun.security.util.DerValue") != null)
        {
          if ((localObject2 = new DerValue(arrayOfByte)).tag != 4)
            throw new XMLSecurityException("certificate.noSki.notOctetString");
          localObject1 = ((DerValue)localObject2).getOctetString();
        }
      }
      catch (NoClassDefFoundError localNoClassDefFoundError)
      {
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
      }
      if (localObject1 == null)
        try
        {
          if ((localObject2 = XMLUtils.classForName("org.bouncycastle.asn1.DERInputStream")) != null)
          {
            Constructor localConstructor;
            InputStream localInputStream = (InputStream)(localConstructor = ((Class)localObject2).getConstructor(new Class[] { InputStream.class })).newInstance(new Object[] { new ByteArrayInputStream(arrayOfByte) });
            Method localMethod1;
            Object localObject3;
            if ((localObject3 = (localMethod1 = ((Class)localObject2).getMethod("readObject", new Class[0])).invoke(localInputStream, new Object[0])) == null)
              throw new XMLSecurityException("certificate.noSki.null");
            Class localClass;
            if (!(localClass = XMLUtils.classForName("org.bouncycastle.asn1.ASN1OctetString")).isInstance(localObject3))
              throw new XMLSecurityException("certificate.noSki.notOctetString");
            Method localMethod2;
            localObject1 = (byte[])(localMethod2 = localClass.getMethod("getOctets", new Class[0])).invoke(localObject3, new Object[0]);
          }
        }
        catch (Throwable localThrowable)
        {
        }
      Object localObject2 = new byte[localObject1.length - 2];
      System.arraycopy(localObject1, 2, localObject2, 0, localObject2.length);
      if (log.isDebugEnabled())
        log.debug("Base64 of SKI is " + Base64.encode(localObject2));
      return localObject2;
    }
    catch (IOException localIOException)
    {
    }
    throw new XMLSecurityException("generic.EmptyMessage", localIOException);
  }

  public boolean equals(Object paramObject)
  {
    if (!paramObject.getClass().getName().equals(getClass().getName()))
      return false;
    XMLX509SKI localXMLX509SKI = (XMLX509SKI)paramObject;
    try
    {
      return JavaUtils.binaryCompare(localXMLX509SKI.getSKIBytes(), getSKIBytes());
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    return false;
  }

  public String getBaseLocalName()
  {
    return "X509SKI";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.x509.XMLX509SKI
 * JD-Core Version:    0.6.0
 */