package si.hermes.security;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.xml.security.signature.XMLSignatureException;
import si.hermes.security.Dialogs.EnterPinDialog;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

public class NativeCertificateProvider
  implements ICertificateProvider
{
  private static KeyStore keystore;
  private static String pass = null;
  private static String providerName = null;
  private static String shortProviderName = null;
  private static String inputstreamstore = null;
  private static boolean isLoaded = false;
  private static boolean needPassword = false;
  private static String passwordDialogTitle = "";
  private static boolean nssinited = false;

  private void OpenCertificateStore(String paramString)
    throws ESignDocException
  {
    Object localObject;
    try
    {
      if (!isLoaded)
      {
        if (needPassword)
        {
          FileInputStream localFileInputStream;
          try
          {
            localObject = null;
            if (inputstreamstore != null)
              localObject = new FileInputStream(inputstreamstore);
            keystore.load((InputStream)localObject, paramString == null ? null : paramString.toCharArray());
            pass = paramString;
          }
          catch (Exception localException1)
          {
            localFileInputStream = null;
            if (inputstreamstore != null)
              localFileInputStream = new FileInputStream(inputstreamstore);
            pass = EnterPinDialog.SelectPasswordModal(passwordDialogTitle);
            keystore.load(localFileInputStream, pass == null ? null : pass.toCharArray());
          }
        }
        else
        {
          localObject = null;
          if (inputstreamstore != null)
            localObject = new FileInputStream(inputstreamstore);
          keystore.load((InputStream)localObject, null);
          pass = null;
        }
        isLoaded = true;
      }
      return;
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
      throw new ESignDocException(32, "<CertificateNotFound StoreType=\"" + getShortProviderName() + "\"/>");
    }
  }

  public final boolean hasCertificatePrivateKey(X509Certificate paramX509Certificate, String paramString1, String paramString2)
  {
    System.out.println("Has certificate '" + paramX509Certificate.getSubjectX500Principal().getName() + "' private key?");
    Object localObject;
    localObject = paramX509Certificate.getIssuerDN().getName();
    String str;
    if ((str = paramX509Certificate.getSerialNumber().toString(16).toUpperCase()).length() % 2 == 1)
      str = "0" + str;
    if (!(("".equals(paramString2)) || (((String)localObject).equals(paramString2)) || (Utility.getCAFromCertificateDesc((String)localObject).equals(paramString2))) && (("".equals(paramString1)) || (str.equals(paramString1.toUpperCase())) || (Utility.reverse(str).equals(paramString1.toUpperCase())))) {
    	return false;
    }
    
    try
    {
      try
      {
        if (pass == null)
          localObject = (PrivateKey)keystore.getKey(keystore.getCertificateAlias(paramX509Certificate), null);
        else
          localObject = (PrivateKey)keystore.getKey(keystore.getCertificateAlias(paramX509Certificate), pass.toCharArray());
        if (localObject != null) {
          return true;
        }
        else {
          throw new Exception();
        }
      }
      catch (Exception localException1)
      {
        String newPass = EnterPinDialog.SelectPasswordModal(passwordDialogTitle + "'" + paramX509Certificate.getSubjectX500Principal().getName() + "'");
        localObject = (PrivateKey)keystore.getKey(keystore.getCertificateAlias(paramX509Certificate), newPass == null ? null : newPass.toCharArray());
        ((PrivateKey)localObject).getAlgorithm();
        if (localObject != null) {
          pass = newPass;
          return true;
        }
      }
    }
    catch (Exception localException2)
    {
    }
    return false;
  }

  public final Key convertToPublicKey(Key paramKey)
    throws XMLSignatureException
  {
    return paramKey;
  }

  public final X509Certificate[] getAllCertificatesWithPrivateKey(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException
  {
    Object localObject;
    OpenCertificateStore(paramArrayOfChar == null ? null : new String(paramArrayOfChar));
    try
    {
      localObject = new ArrayList();
      Enumeration localEnumeration = keystore.aliases();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        X509Certificate localX509Certificate = (X509Certificate)keystore.getCertificate(str);
        if (hasCertificatePrivateKey(localX509Certificate, paramString1, paramString2)) {
          System.out.println("Adding certificate to the list of found ones: " + localX509Certificate.getSubjectX500Principal().getName());
          ((ArrayList)localObject).add(localX509Certificate);
        }
      }
      return (X509Certificate[])((ArrayList)localObject).toArray(new X509Certificate[0]);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    throw new ESignDocException(32, "<CertificateNotFound StoreType=\"" + getShortProviderName() + "\"/>");
  }

  public final X509Certificate[] getAllCertificates(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException
  {
    Object localObject;
    OpenCertificateStore(paramArrayOfChar == null ? null : new String(paramArrayOfChar));
    try
    {
      localObject = new ArrayList();
      Enumeration localEnumeration = keystore.aliases();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        ((ArrayList)localObject).add(keystore.getCertificate(str));
      }
      return (X509Certificate[])((ArrayList)localObject).toArray(new X509Certificate[((ArrayList)localObject).size()]);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    throw new ESignDocException(32, "<CertificateNotFound StoreType=\"" + getShortProviderName() + "\"/>");
  }

  private final void initProfileMSCAPI(String paramString)
    throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
  {
    System.out.println("initProfileMSCAPI");
    shortProviderName = "WIN";
    providerName = "SunMSCAPI";
    keystore = KeyStore.getInstance(paramString);
    isLoaded = true;
    keystore.load(null, null);
  }

  private final void initProfileNSS(String paramString)
    throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
  {
    System.out.println("initProfileNSS: " + paramString);
    if ((nssinited) || (Security.getProvider("SunPKCS11") != null))
      return;
    providerName = null;
    shortProviderName = "NSS";
    String str = "name=NSSSofToken\ndescription=NSS PKCS11\nnssSecmodDirectory = \"" + paramString.replaceAll("\\\\", "\\\\\\\\") + "\"\n" + "nssDbMode = readOnly\n" + "nssModule = keystore\n" + "attributes = compatibility";
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(str.getBytes());
    Constructor localConstructor;
    Provider localProvider;
    ReflectionHelper.setFinalField((localProvider = (Provider)(localConstructor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(new Class[] { InputStream.class })).newInstance(new Object[] { localByteArrayInputStream })).getClass().getDeclaredField("nssUseSecmodTrust"), localProvider, false);
    Security.insertProviderAt(localProvider, 1);
    keystore = KeyStore.getInstance("PKCS11");
    inputstreamstore = null;
    isLoaded = false;
    needPassword = true;
    passwordDialogTitle = "NSS store";
    nssinited = true;
  }

  private final void initProfileNative(String paramString)
    throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
  {
    System.out.println("initProfileNative: " + paramString);
    providerName = null;
    shortProviderName = "JAVA";
    keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    inputstreamstore = paramString;
    isLoaded = false;
    needPassword = true;
    passwordDialogTitle = "Java store";
  }

  private final void initProfileMAC()
    throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException
  {
    System.out.println("initProfileNativeMAC");
    providerName = null;
    shortProviderName = "Apple";
    keystore = KeyStore.getInstance("KeychainStore", "Apple");
    isLoaded = true;
    needPassword = true;
    keystore.load(null, "abc123".toCharArray());
  }

  public static String getUsersCertPath()
  {
    if (System.getProperty("os.name").startsWith("Windows"))
      return System.getProperty("user.home") + "\\Application Data\\Sun\\Java\\Deployment\\security\\trusted.clientcerts";
    return System.getProperty("user.home") + "/.java/deployment/security/trusted.clientcerts";
  }

  public final void initProfile(String paramString1, String paramString2)
    throws ESignDocException
  {
    try
    {
      if ("NSS".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying NSS pkcs11 store");
        initProfileNSS(paramString2);
        return;
      }
      if ("JKS".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying JKS store");
        initProfileNative(getUsersCertPath());
        return;
      }
      if ("JKSPATH".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying JKS store");
        initProfileNative(paramString2);
        return;
      }
      if ("WINMY".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying MSCAPI store");
        initProfileMSCAPI("Windows-MY");
        return;
      }
      if ("WINCA".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying MSCAPI store");
        initProfileMSCAPI("Windows-ROOT");
        return;
      }
      if ("MAC".equalsIgnoreCase(paramString1))
      {
        System.out.println("trying Apple store");
        initProfileMAC();
        return;
      }
      if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
        try
        {
          System.out.println("trying MSCAPI");
          initProfileMSCAPI("Windows-MY");
        }
        catch (Exception localException3)
        {
          Exception localException1;
          (localException1 = localException3).printStackTrace();
          System.out.println("trying NATIVE");
          initProfileNative(getUsersCertPath());
        }
      else
        try
        {
          System.out.println("trying Apple store");
          initProfileMAC();
        }
        catch (Exception localException4)
        {
          System.out.println("trying NATIVE");
          initProfileNative(getUsersCertPath());
        }
    }
    catch (Exception localException2)
    {
      throw new ESignDocException(22, localException2);
    }
    System.out.println("NativeKeyStore inited");
  }

  public CertificateHelper findCertificate(String paramString1, String paramString2, String paramString3, char[] paramArrayOfChar)
    throws ESignDocException
  {
    OpenCertificateStore(paramString3 == null ? null : new String(paramString3));
    Object localObject1;
    try
    {
      localObject1 = null;
      String localObject2 = null;
      PrivateKey localObject4 = null;
      String str1 = paramString1.toUpperCase();
      Enumeration localEnumeration = keystore.aliases();
      while (localEnumeration.hasMoreElements())
      {
        String localObject3 = (String)localEnumeration.nextElement();
        X509Certificate localX509Certificate;
        String str2 = Utility.getHexString((localX509Certificate = (X509Certificate)keystore.getCertificate((String)localObject3)).getSerialNumber()).toUpperCase();
        if (str1.equals(str2))
        {
          String str3 = Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
          if (paramString2.equals(str3))
          {
            localObject1 = localX509Certificate;
            localObject2 = localObject3;
            break;
          }
        }
      }
      if (localObject1 == null)
        throw new ESignDocException(0, "<CertificateNotFound StoreType=\"" + getShortProviderName() + "\"/>");
      try
      {
        if (pass == null)
          localObject4 = (PrivateKey)keystore.getKey(localObject2, null);
        else
          localObject4 = (PrivateKey)keystore.getKey(localObject2, pass.toCharArray());
      }
      catch (Exception localException1)
      {
        pass = EnterPinDialog.SelectPasswordModal(passwordDialogTitle);
        localObject4 = (PrivateKey)keystore.getKey(localObject2, pass == null ? null : pass.toCharArray());
      }
      return new CertificateHelper((PrivateKey)localObject4, (X509Certificate)localObject1);
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
      throw new ESignDocException(32, localException2.getLocalizedMessage());
    }
  }

  public String getProviderName()
  {
    return providerName;
  }

  public String getShortProviderName()
  {
    return shortProviderName;
  }

  public X509Certificate[] buildCertificates(String paramString, boolean paramBoolean)
    throws ESignDocException
  {
    OpenCertificateStore(paramString == null ? null : new String(paramString));
    try
    {
      ArrayList localArrayList = new ArrayList();
      Enumeration localEnumeration = keystore.aliases();
      while (localEnumeration.hasMoreElements())
      {
        String str = (String)localEnumeration.nextElement();
        X509Certificate localX509Certificate = (X509Certificate)keystore.getCertificate(str);
        localArrayList.add(localX509Certificate);
      }
      return (X509Certificate[])localArrayList.toArray(new X509Certificate[localArrayList.size()]);
    }
    catch (Exception localException)
    {
      throw new ESignDocException(3, "Error accessing certificate store", localException);
    }
  }

  public static class ReflectionHelper
  {
    private static final String MODIFIERS_FIELD = "modifiers";
    private static final ReflectionFactory reflection = ReflectionFactory.getReflectionFactory();

    public static void setFinalField(Field paramField, Object paramObject, boolean paramBoolean)
      throws NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
      paramField.setAccessible(true);
      Field localField;
      (localField = Field.class.getDeclaredField("modifiers")).setAccessible(true);
      int i = (i = localField.getInt(paramField)) & 0xFFFFFFEF;
      localField.setInt(paramField, i);
      Method localMethod;
      FieldAccessor localFieldAccessor;
      (localFieldAccessor = (FieldAccessor)(localMethod = reflection.getClass().getDeclaredMethod("newFieldAccessor", new Class[] { Field.class, Boolean.TYPE })).invoke(reflection, new Object[] { paramField, Boolean.FALSE })).setBoolean(paramObject, paramBoolean);
    }
  }

  public static class MyCallbackHandler
    implements CallbackHandler
  {
    private String prompt;

    public MyCallbackHandler(String paramString)
    {
      this.prompt = paramString;
    }

    public void handle(Callback[] paramArrayOfCallback)
      throws IOException, UnsupportedCallbackException
    {
      for (int i = 0; i < paramArrayOfCallback.length; i++)
      {
        Callback localCallback = paramArrayOfCallback[i];
        System.out.println("   " + i + ">" + localCallback.getClass().getName());
        if (!(localCallback instanceof PasswordCallback))
          continue;
        PasswordCallback localPasswordCallback = (PasswordCallback)localCallback;
        try
        {
          NativeCertificateProvider.pass = EnterPinDialog.SelectPasswordModal(this.prompt);
        }
        catch (ESignDocException localESignDocException2)
        {
          ESignDocException localESignDocException1;
          (localESignDocException1 = localESignDocException2).printStackTrace();
          NativeCertificateProvider.pass = null;
        }
        if (NativeCertificateProvider.pass == null)
          continue;
        localPasswordCallback.setPassword(NativeCertificateProvider.pass.toCharArray());
      }
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.NativeCertificateProvider
 * JD-Core Version:    0.6.0
 */
