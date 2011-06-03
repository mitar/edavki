package si.hermes.security.XAdES;

import java.io.IOException;
import java.util.Date;
import javax.xml.transform.TransformerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.ISignatures;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.VerifyStatus;

public abstract interface IXAdES extends IPersistable
{
  public abstract int addCounterSignature(IHSLSignature paramIHSLSignature);

  public abstract int addDataObjectFormat(IDataObjectFormat paramIDataObjectFormat);

  public abstract int addSigningCertificate(ICertificate paramICertificate);

  public abstract void createSignedData(IHSLSignature paramIHSLSignature, String paramString1, String paramString2, Date paramDate)
    throws ESignDocException, EXAdESException;

  public abstract ISignatures getCounterSignatures();

  public abstract IDataObjectFormats getDataObjectFormats();

  public abstract String getId();

  public abstract ISignaturePolicyIdentifier getSignaturePolicyIdentifier();

  public abstract ISignatureProductionPlace getSignatureProductionPlace();

  public abstract String getSignedDataId();

  public abstract String getSignedId();

  public abstract ISignerRole getSignerRole();

  public abstract ICertificateList getSigningCertificates();

  public abstract Date getSigningTime()
    throws ESignDocException;

  public abstract String getTarget();

  public abstract void setId(String paramString);

  public abstract void setSignaturePolicyIdentifier(ISignaturePolicyIdentifier paramISignaturePolicyIdentifier);

  public abstract void setSignatureProductionPlace(ISignatureProductionPlace paramISignatureProductionPlace);

  public abstract void setSignedDataId(String paramString);

  public abstract void setSignedId(String paramString);

  public abstract void setSignerRole(ISignerRole paramISignerRole);

  public abstract void setSigningTime(Date paramDate);

  public abstract void setTarget(String paramString);

  public abstract VerifyStatus validateSignedData(IHSLSignature paramIHSLSignature)
    throws ESignDocException, XMLSignatureException, IOException, XMLSecurityException, TransformerException;

  public abstract VerifyStatus status(int paramInt)
    throws ESignDocException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IXAdES
 * JD-Core Version:    0.6.0
 */