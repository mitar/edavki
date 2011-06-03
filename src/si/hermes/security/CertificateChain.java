package si.hermes.security;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.apache.xml.security.utils.Base64;

public final class CertificateChain
{
  public static int CERT_CHAIN_CYCLIC = 101;
  public static int CERT_CHAIN_INCOMPLETE = 104;
  public static int CERT_CHAIN_INVALID = 102;
  public static int CERT_CHAIN_VALID = 0;
  public static int CERT_EXPIRED = 2;
  public static int CERT_NOT_YET_VALID = 1;
  public static int CERT_REVOKED = 4;
  public static int CERT_REVOKED_UNKNOWN = 8;
  public static int CERT_VALID = 0;
  private Vector certchain = new Vector();
  private Hashtable certlist = new Hashtable();
  private boolean checkCRL;
  private TreeSet cyclic_check = new TreeSet();
  private Date date;
  private boolean fAllStores = false;
  private int fChainStatus;

  private void addCertificateToChain(X509Certificate paramX509Certificate)
    throws ESignDocException
  {
    try
    {
      this.certchain.add(paramX509Certificate);
      return;
    }
    catch (Exception localException2)
    {
      Exception localException1;
      (localException1 = localException2).printStackTrace();
      throw new ESignDocException(3, "Error converting certificate", localException1);
    }
  }

  private void addCertificateToMap(X509Certificate paramX509Certificate)
  {
    X509Certificate[] arrayOfX509Certificate;
    if ((arrayOfX509Certificate = (X509Certificate[])this.certlist.put(parseSubjectDN(paramX509Certificate.getSubjectDN().toString()), new X509Certificate[] { paramX509Certificate })) != null)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < arrayOfX509Certificate.length; i++)
        localArrayList.add(arrayOfX509Certificate[i]);
      localArrayList.add(paramX509Certificate);
      this.certlist.put(parseSubjectDN(paramX509Certificate.getSubjectDN().toString()), (X509Certificate[])localArrayList.toArray(new X509Certificate[localArrayList.size()]));
    }
  }

  private void buildCertificates(String paramString)
    throws ESignDocException
  {
    X509Certificate[] arrayOfX509Certificate = CertificateProvider.buildCertificates(paramString, this.fAllStores);
    for (int i = 0; i < arrayOfX509Certificate.length; i++)
      addCertificateToMap(arrayOfX509Certificate[i]);
  }

  private int checkCRL(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
    throws ESignDocException
  {
    if (this.checkCRL)
    {
      X509CRL localX509CRL;
      try
      {
        CRLDistributionPoint localCRLDistributionPoint;
        localX509CRL = (localCRLDistributionPoint = new CRLDistributionPoint(paramX509Certificate1)).GetCRLFromCertificate();
        if (paramX509Certificate2 != null)
          localX509CRL.verify(paramX509Certificate2.getPublicKey());
      }
      catch (Exception localException)
      {
        return 2;
      }
      if (localX509CRL.isRevoked(paramX509Certificate1))
        return 1;
      return 0;
    }
    return 0;
  }

  private void buildCyclic(X509Certificate paramX509Certificate)
  {
    this.cyclic_check.add(paramX509Certificate.getSubjectDN().toString());
  }

  private boolean checkCylic(X509Certificate paramX509Certificate)
  {
    return this.cyclic_check.contains(paramX509Certificate.getSubjectDN().toString());
  }

  private void clearCyclicCheck()
  {
    this.cyclic_check.clear();
  }

  private X509Certificate findCertificate(X509Certificate paramX509Certificate)
    throws ESignDocException
  {
    X509Certificate[] arrayOfX509Certificate;
    if ((arrayOfX509Certificate = (X509Certificate[])this.certlist.get(parseSubjectDN(paramX509Certificate.getIssuerDN().toString()))) == null)
      return null;
    int i = 0;
    while (i < arrayOfX509Certificate.length)
      try
      {
        paramX509Certificate.verify(arrayOfX509Certificate[i].getPublicKey());
        return arrayOfX509Certificate[i];
      }
      catch (Exception localException)
      {
        i++;
      }
    return null;
  }

  public final String getCertCN(int paramInt)
  {
    X509Certificate localX509Certificate = (X509Certificate)this.certchain.get(paramInt);
    return Utility.extractFromDistinguishedName("CN", localX509Certificate.getIssuerDN().toString());
  }

  public final int getCertCount()
  {
    return this.certchain.size();
  }

  public final String getCertIssuerDN(int paramInt)
  {
    X509Certificate localX509Certificate;
    return (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getIssuerDN().toString();
  }

  public final String getCertSerial(int paramInt)
  {
    X509Certificate localX509Certificate;
    return Utility.getHexString((localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getSerialNumber()).toUpperCase();
  }

  public final String getCertSerialAsNumber(int paramInt)
  {
    X509Certificate localX509Certificate;
    return (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getSerialNumber().toString();
  }

  public final int getCertStatus(int paramInt)
  {
    try
    {
      getCertStatusEx(paramInt);
    }
    catch (CertificateNotYetValidException localCertificateNotYetValidException)
    {
      return CERT_NOT_YET_VALID;
    }
    catch (CertificateExpiredException localCertificateExpiredException)
    {
      return CERT_EXPIRED;
    }
    return CERT_VALID;
  }

  public final void getCertStatusEx(int paramInt)
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    X509Certificate localX509Certificate;
    (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).checkValidity(this.date);
  }

  public final String getCertSubjectDN(int paramInt)
  {
    X509Certificate localX509Certificate;
    return (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getSubjectDN().toString();
  }

  public final Date getCertValidFrom(int paramInt)
  {
    X509Certificate localX509Certificate;
    return (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getNotBefore();
  }

  public final Date getCertValidTo(int paramInt)
  {
    X509Certificate localX509Certificate;
    return (localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getNotAfter();
  }

  public final String getCertB64(int paramInt)
    throws CertificateEncodingException
  {
    X509Certificate localX509Certificate;
    return Base64.encode((localX509Certificate = (X509Certificate)this.certchain.get(paramInt)).getEncoded());
  }

  public final String getCRLB64(int paramInt)
    throws ESignDocException
  {
    if (paramInt == getCertCount() - 1)
      return null;
    X509Certificate localX509Certificate = (X509Certificate)this.certchain.get(paramInt);
    try
    {
      CRLDistributionPoint localCRLDistributionPoint;
      return Base64.encode((localCRLDistributionPoint = new CRLDistributionPoint(localX509Certificate)).GetCRLFromCertificate().getEncoded());
    }
    catch (Exception localException)
    {
    }
    throw new ESignDocException(34, "getCRL", localException);
  }

  public final int getChainStatus()
  {
    return this.fChainStatus;
  }

  public final boolean getCheckAllStores()
  {
    return this.fAllStores;
  }

  public final void init(String paramString1, Date paramDate, boolean paramBoolean, String paramString2)
    throws ESignDocException
  {
    X509Certificate localX509Certificate2;
    if ((localX509Certificate2 = parseCertificate(paramString1)) == null)
      throw new ESignDocException(3, "No certificates found");
    this.checkCRL = paramBoolean;
    this.date = (paramDate != null ? paramDate : new Date());
    if (this.certlist.size() == 0)
      buildCertificates(paramString2);
    clearCyclicCheck();
    int i = 0;
    X509Certificate localX509Certificate1;
    do
    {
      setChainStatus(CERT_CHAIN_INCOMPLETE);
      if (checkCylic(localX509Certificate1))
      {
        setChainStatus(CERT_CHAIN_CYCLIC);
        break;
      }
      buildCyclic(localX509Certificate1);
      addCertificateToChain(localX509Certificate1);
      if ((localX509Certificate2 = findCertificate(localX509Certificate1)) != null)
        i = 1;
      int j;
      switch (j = isOver(localX509Certificate1) ? 0 : checkCRL(localX509Certificate1, localX509Certificate2))
      {
      case 0:
        tmpTernaryOp = CERT_VALID;
        break;
      case 1:
        tmpTernaryOp = CERT_REVOKED;
        break;
      case 2:
        setChainStatus(getCertStatus(this.certchain.size() - 1) != CERT_VALID ? CERT_CHAIN_INVALID : CERT_REVOKED_UNKNOWN);
      }
    }
    while (((localX509Certificate1 = localX509Certificate2) == null ? this : isOver(localX509Certificate1)) == 0);
    if (i == 0)
      setChainStatus(CERT_CHAIN_INVALID);
    clearCyclicCheck();
  }

  private boolean isOver(X509Certificate paramX509Certificate)
  {
    if (paramX509Certificate == null)
      return true;
    return isOver(paramX509Certificate.getSubjectDN().toString(), paramX509Certificate.getIssuerDN().toString());
  }

  private boolean isOver(String paramString1, String paramString2)
  {
    return paramString1.equals(paramString2);
  }

  private X509Certificate parseCertificate(String paramString)
    throws ESignDocException
  {
    try
    {
      return Utility.createCertificateFromString(paramString);
    }
    catch (Exception localException)
    {
    }
    throw new ESignDocException(3, "Error parsing certificate", localException);
  }

  private String parseSubjectDN(String paramString)
  {
    String str;
    return (str = paramString.replaceAll(", ", ",")).replaceAll("EMAILADDRESS=", "E=");
  }

  public final void refresh()
  {
    this.certlist.clear();
  }

  private void setChainStatus(int paramInt)
  {
    int i;
    if (((i = this.fChainStatus - paramInt) != CERT_CHAIN_VALID) && (i != CERT_CHAIN_CYCLIC) && (i != CERT_CHAIN_INVALID) && (i != CERT_CHAIN_INCOMPLETE))
      this.fChainStatus += paramInt;
  }

  public final void setCheckAllStores(boolean paramBoolean)
  {
    this.fAllStores = paramBoolean;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CertificateChain
 * JD-Core Version:    0.6.0
 */