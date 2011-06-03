package si.hermes.security;

import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.Principal;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.Constants;
import org.mozilla.jss.CryptoManager;
import org.mozilla.jss.CryptoManager.InitializationValues;
import org.mozilla.jss.CryptoManager.NotInitializedException;
import org.mozilla.jss.JSSProvider;
import org.mozilla.jss.NoSuchTokenException;
import org.mozilla.jss.crypto.AlreadyInitializedException;
import org.mozilla.jss.crypto.CryptoStore;
import org.mozilla.jss.crypto.CryptoToken;
import org.mozilla.jss.crypto.InvalidKeyFormatException;
import org.mozilla.jss.crypto.ObjectNotFoundException;
import org.mozilla.jss.crypto.PrivateKey;
import org.mozilla.jss.crypto.TokenException;
import org.mozilla.jss.pkcs11.PK11RSAPublicKey;
import org.mozilla.jss.pkcs11.PK11Token.NotInitializedException;
import org.mozilla.jss.util.IncorrectPasswordException;
import org.mozilla.jss.util.Password;
import org.mozilla.jss.util.PasswordCallback;
import org.xml.sax.SAXException;
import si.hermes.security.Dialogs.JSSEnterPinDialog;

public class JssCertificateProvider
  implements ICertificateProvider
{
  private static CryptoManager manager = null;
  private static String profileDir = null;
  public static String providerName = "Mozilla-JSS";
  public static String shortProviderName = "NSS";

  public final CryptoManager getCryptoManager()
  {
    if (manager == null)
      try
      {
        manager = CryptoManager.getInstance();
      }
      catch (CryptoManager.NotInitializedException localNotInitializedException2)
      {
        CryptoManager.NotInitializedException localNotInitializedException1;
        (localNotInitializedException1 = localNotInitializedException2).printStackTrace();
      }
    return manager;
  }

  public final Key convertToPublicKey(Key paramKey)
    throws XMLSignatureException
  {
    try
    {
      return PK11RSAPublicKey.fromSPKI(paramKey.getEncoded());
    }
    catch (InvalidKeyFormatException localInvalidKeyFormatException)
    {
    }
    throw new XMLSignatureException("empty", localInvalidKeyFormatException);
  }

  public final void initProfile(String paramString1, String paramString2)
    throws ESignDocException
  {
    System.out.println("initProfileNSS: " + paramString2);
    profileDir = paramString2;
    try
    {
      CryptoManager.InitializationValues localInitializationValues = new CryptoManager.InitializationValues(paramString2);
      if ((paramString2 == null) || (paramString2.equals("")))
        localInitializationValues.initializeJavaOnly = true;
      localInitializationValues.readOnly = true;
      CryptoManager.initialize(localInitializationValues);
    }
    catch (AlreadyInitializedException localAlreadyInitializedException)
    {
      System.out.println("JSSAlreadyInitialized");
    }
    catch (Exception localException1)
    {
      throw new ESignDocException(22, localException1);
    }
    try
    {
      Provider localProvider;
      if ((localProvider = Security.getProvider("Mozilla-JSS")) != null)
      {
        Security.removeProvider("Mozilla-JSS");
        Security.addProvider(localProvider);
      }
      else
      {
        Security.addProvider(new JSSProvider());
        return;
      }
    }
    catch (Exception localException2)
    {
      throw new ESignDocException(22, localException2);
    }
  }

  public final boolean hasCertificatePrivateKey(org.mozilla.jss.crypto.X509Certificate paramX509Certificate, String paramString1, String paramString2)
    throws TokenException, CertificateException
  {
    try
    {
      PrivateKey localPrivateKey;
      (localPrivateKey = manager.findPrivKeyByCert(paramX509Certificate)).getAlgorithm();
      String str1 = paramX509Certificate.getIssuerDN().getName();
      String str2;
      if ((str2 = paramX509Certificate.getSerialNumber().toString(16).toUpperCase()).length() % 2 == 1)
        str2 = "0" + str2;
      return (("".equals(paramString2)) || (str1.equals(paramString2)) || (Utility.getCAFromCertificateDesc(str1).equals(paramString2))) && (("".equals(paramString1)) || (str2.equals(paramString1.toUpperCase())) || (Utility.reverse(str2).equals(paramString1.toUpperCase())));
    }
    catch (ObjectNotFoundException localObjectNotFoundException)
    {
    }
    return false;
  }

  public final java.security.cert.X509Certificate[] getAllCertificatesWithPrivateKey(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException
  {
    Object localObject1;
    try
    {
      localObject1 = new ArrayList();
      CryptoToken localCryptoToken = null;
      org.mozilla.jss.crypto.X509Certificate[] arrayOfX509Certificate = null;
      Constants.setSignatureSpecNSprefix("");
      Enumeration localEnumeration = getCryptoManager().getAllTokens();
      while (localEnumeration.hasMoreElements())
      {
        if (!(localCryptoToken = (CryptoToken)localEnumeration.nextElement()).isPresent())
          continue;
        Object localObject2;
        if ((localCryptoToken == manager.getInternalKeyStorageToken()) && (paramArrayOfChar != null))
        {
          localObject2 = new Password(paramArrayOfChar);
          localCryptoToken.login((PasswordCallback)localObject2);
          ((Password)localObject2).clear();
        }
        try
        {
          if ((profileDir != null) && (!profileDir.equals("")) && (ESignDocImpl.runningInApplet))
            localCryptoToken.login(new JSSEnterPinDialog(localCryptoToken.getName()));
        }
        catch (PK11Token.NotInitializedException localNotInitializedException)
        {
        }
        continue;
        manager.setThreadToken(localCryptoToken);
        arrayOfX509Certificate = (localObject2 = localCryptoToken.getCryptoStore()).getCertificates();
        for (int i = 0; i < arrayOfX509Certificate.length; i++)
        {
          if (!hasCertificatePrivateKey(arrayOfX509Certificate[i], paramString1, paramString2))
            continue;
          try
          {
            ((ArrayList)localObject1).add(Utility.convertToJavaCert(arrayOfX509Certificate[i]));
          }
          catch (CertificateParsingException localCertificateParsingException)
          {
            System.out.println("Problem converting certificate: " + arrayOfX509Certificate[i].getSubjectDN());
            System.out.println("CertificateParsingException " + localCertificateParsingException);
            localCertificateParsingException.printStackTrace();
          }
        }
      }
      return (java.security.cert.X509Certificate[])((ArrayList)localObject1).toArray(new java.security.cert.X509Certificate[0]);
    }
    catch (Exception localException)
    {
      (localObject1 = localException).printStackTrace();
    }
    throw new ESignDocException(32, ((Exception)localObject1).getLocalizedMessage());
  }

  public final org.mozilla.jss.crypto.X509Certificate[] getAllCertificates(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      localObject1 = null;
      org.mozilla.jss.crypto.X509Certificate[] arrayOfX509Certificate = null;
      Constants.setSignatureSpecNSprefix("");
      Enumeration localEnumeration = getCryptoManager().getAllTokens();
      while (localEnumeration.hasMoreElements())
      {
        if (!(localObject1 = (CryptoToken)localEnumeration.nextElement()).isPresent())
          continue;
        Object localObject2;
        if ((localObject1 == manager.getInternalKeyStorageToken()) && (paramArrayOfChar != null))
        {
          localObject2 = new Password(paramArrayOfChar);
          ((CryptoToken)localObject1).login((PasswordCallback)localObject2);
          ((Password)localObject2).clear();
        }
        try
        {
          if (((profileDir == null) || (profileDir.equals(""))) && (ESignDocImpl.runningInApplet))
            ((CryptoToken)localObject1).login(new JSSEnterPinDialog(((CryptoToken)localObject1).getName()));
        }
        catch (PK11Token.NotInitializedException localNotInitializedException)
        {
        }
        continue;
        manager.setThreadToken((CryptoToken)localObject1);
        arrayOfX509Certificate = (localObject2 = ((CryptoToken)localObject1).getCryptoStore()).getCertificates();
        for (int i = 0; i < arrayOfX509Certificate.length; i++)
          localArrayList.add(arrayOfX509Certificate[i]);
      }
    }
    catch (Exception localException)
    {
      Object localObject1;
      (localObject1 = localException).printStackTrace();
      throw new ESignDocException(32, ((Exception)localObject1).getLocalizedMessage());
    }
    return (org.mozilla.jss.crypto.X509Certificate)(org.mozilla.jss.crypto.X509Certificate)(org.mozilla.jss.crypto.X509Certificate[])localArrayList.toArray(new org.mozilla.jss.crypto.X509Certificate[localArrayList.size()]);
  }

  public static final org.mozilla.jss.crypto.X509Certificate[] getInternalCertificates(CryptoManager paramCryptoManager, String paramString, char[] paramArrayOfChar)
    throws XMLSecurityException, NoSuchTokenException, HeadlessException, SAXException, IOException, TransformerException, ESignDocException, ObjectNotFoundException, TokenException
  {
    try
    {
      localObject1 = null;
      org.mozilla.jss.crypto.X509Certificate[] arrayOfX509Certificate = null;
      Object localObject2;
      if (!(localObject1 = paramCryptoManager.getInternalKeyStorageToken()).isLoggedIn())
        if (paramArrayOfChar != null)
        {
          localObject2 = paramString != null ? new Password(paramString.toCharArray()) : new Password(paramArrayOfChar);
          ((CryptoToken)localObject1).login((PasswordCallback)localObject2);
          ((Password)localObject2).clear();
        }
      paramCryptoManager.setThreadToken((CryptoToken)localObject1);
      return arrayOfX509Certificate = (localObject2 = ((CryptoToken)localObject1).getCryptoStore()).getCertificates();
    }
    catch (IncorrectPasswordException localIncorrectPasswordException)
    {
      Object localObject1;
      (localObject1 = localIncorrectPasswordException).printStackTrace();
    }
    return (org.mozilla.jss.crypto.X509Certificate)(org.mozilla.jss.crypto.X509Certificate)null;
  }

  public CertificateHelper findCertificate(String paramString1, String paramString2, String paramString3, char[] paramArrayOfChar)
    throws ESignDocException
  {
    Object localObject1;
    try
    {
      localObject1 = null;
      String str = paramString1.toUpperCase();
      org.mozilla.jss.crypto.X509Certificate[] arrayOfX509Certificate = getInternalCertificates(getCryptoManager(), paramString3, paramArrayOfChar);
      org.mozilla.jss.crypto.X509Certificate localX509Certificate;
      Object localObject2;
      for (int i = 0; i < arrayOfX509Certificate.length; i++)
      {
        localX509Certificate = arrayOfX509Certificate[i];
        try
        {
          getCryptoManager().findPrivKeyByCert(localX509Certificate);
        }
        catch (Exception localException1)
        {
          continue;
        }
        localObject2 = Utility.getHexString(localX509Certificate.getSerialNumber()).toUpperCase();
        if (!str.equals(localObject2))
          continue;
        localObject3 = Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
        if (!paramString2.equals(localObject3))
          continue;
        localObject1 = localX509Certificate;
        break;
      }
      if (localObject1 == null)
      {
        arrayOfX509Certificate = getAllCertificates("", "", paramArrayOfChar);
        for (i = 0; i < arrayOfX509Certificate.length; i++)
        {
          localX509Certificate = arrayOfX509Certificate[i];
          try
          {
            getCryptoManager().findPrivKeyByCert(localX509Certificate);
          }
          catch (Exception localException2)
          {
            continue;
          }
          localObject2 = Utility.getHexString(localX509Certificate.getSerialNumber()).toUpperCase();
          if (!str.equals(localObject2))
            continue;
          localObject3 = Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
          if (!paramString2.equals(localObject3))
            continue;
          localObject1 = localX509Certificate;
          break;
        }
      }
      if (localObject1 == null)
        throw new ESignDocException(0, "<CertificateNotFound StoreType=\"" + getShortProviderName() + "\"/>");
      PrivateKey localPrivateKey = getCryptoManager().findPrivKeyByCert((org.mozilla.jss.crypto.X509Certificate)localObject1);
      Object localObject3 = (java.security.cert.X509Certificate)(localObject2 = CertificateFactory.getInstance("X.509")).generateCertificate(new ByteArrayInputStream(((org.mozilla.jss.crypto.X509Certificate)localObject1).getEncoded()));
      return new CertificateHelper(localPrivateKey, (java.security.cert.X509Certificate)localObject3);
    }
    catch (Exception localException3)
    {
      (localObject1 = localException3).printStackTrace();
    }
    throw new ESignDocException(32, ((Exception)localObject1).getLocalizedMessage());
  }

  public String getProviderName()
  {
    return providerName;
  }

  public String getShortProviderName()
  {
    return shortProviderName;
  }

  public java.security.cert.X509Certificate[] buildCertificates(String paramString, boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      ArrayList localArrayList = new ArrayList();
      CryptoToken localCryptoToken = null;
      Constants.setSignatureSpecNSprefix("");
      CryptoManager localCryptoManager;
      Enumeration localEnumeration = (localCryptoManager = CryptoManager.getInstance()).getAllTokens();
      while (localEnumeration.hasMoreElements())
      {
        if ((!(localCryptoToken = (CryptoToken)localEnumeration.nextElement()).isPresent()) || ((localCryptoToken != localCryptoManager.getInternalKeyStorageToken()) && ((localCryptoToken == localCryptoManager.getInternalKeyStorageToken()) || (!paramBoolean))))
          continue;
        try
        {
          if (!localCryptoToken.isLoggedIn())
          {
            if (paramString != null)
            {
              localCryptoToken.login(new Password(paramString.toCharArray()));
              break label154;
            }
            if (ESignDocImpl.runningInApplet)
              localCryptoToken.login(new JSSEnterPinDialog(localCryptoToken.getName()));
          }
        }
        catch (PK11Token.NotInitializedException localNotInitializedException)
        {
        }
        continue;
        label154: localCryptoManager.setThreadToken(localCryptoToken);
        CryptoStore localCryptoStore;
        org.mozilla.jss.crypto.X509Certificate[] arrayOfX509Certificate = (localCryptoStore = localCryptoToken.getCryptoStore()).getCertificates();
        for (int i = 0; i < arrayOfX509Certificate.length; i++)
          try
          {
            localArrayList.add(Utility.convertToJavaCert(arrayOfX509Certificate[i]));
          }
          catch (Exception localException2)
          {
          }
      }
      return (java.security.cert.X509Certificate[])localArrayList.toArray(new java.security.cert.X509Certificate[localArrayList.size()]);
    }
    catch (Exception localException1)
    {
    }
    throw new ESignDocException(3, "Error accessing certificate store", localException1);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.JssCertificateProvider
 * JD-Core Version:    0.6.0
 */