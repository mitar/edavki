package si.hermes.security;

import java.io.PrintStream;
import java.security.Key;
import java.security.cert.X509Certificate;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.signature.XMLSignatureException;

public class CertificateProvider
{
  public static ICertificateProvider provider = null;
  private static boolean inited = false;

  public static final void initProfile(String paramString1, String paramString2)
    throws ESignDocException
  {
    if (inited)
      return;
    inited = true;
    System.out.println("initProfile: " + paramString1 + ", " + paramString2);
    if ("NSS".equalsIgnoreCase(paramString2))
      try
      {
        if (Utility.getJavaVersion() < 160)
        {
          provider = new JssCertificateProvider();
          provider.initProfile(paramString2, paramString1);
          break label180;
        }
        try
        {
          provider = new NativeCertificateProvider();
          provider.initProfile(paramString2, paramString1);
        }
        catch (ESignDocException localESignDocException2)
        {
          (localESignDocException1 = localESignDocException2).printStackTrace();
          provider = new JssCertificateProvider();
          provider.initProfile(paramString2, paramString1);
        }
      }
      catch (ESignDocException localESignDocException3)
      {
        ESignDocException localESignDocException1;
        (localESignDocException1 = localESignDocException3).printStackTrace();
        provider = new NativeCertificateProvider();
        tmpTernaryOp = "?";
      }
    else
      provider = new NativeCertificateProvider();
    provider.initProfile(paramString2, paramString1);
    label180: JCEMapper.setProviderId(getProviderName());
  }

  public static final String getProviderName()
  {
    return provider.getProviderName();
  }

  public static final String getShortProviderName()
  {
    return provider.getShortProviderName();
  }

  public static final Key convertToPublicKey(Key paramKey)
    throws XMLSignatureException
  {
    return provider.convertToPublicKey(paramKey);
  }

  public static final X509Certificate[] getAllCertificatesWithPrivateKey(String paramString1, String paramString2, char[] paramArrayOfChar)
    throws ESignDocException
  {
    return provider.getAllCertificatesWithPrivateKey(paramString1, paramString2, paramArrayOfChar);
  }

  public static final X509Certificate[] buildCertificates(String paramString, boolean paramBoolean)
    throws ESignDocException
  {
    return provider.buildCertificates(paramString, paramBoolean);
  }

  public static CertificateHelper findCertificate(String paramString1, String paramString2, String paramString3, char[] paramArrayOfChar)
    throws ESignDocException
  {
    return provider.findCertificate(paramString1, paramString2, paramString3, paramArrayOfChar);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CertificateProvider
 * JD-Core Version:    0.6.0
 */