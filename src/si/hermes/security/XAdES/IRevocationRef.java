package si.hermes.security.XAdES;

import org.apache.xml.security.exceptions.Base64DecodingException;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;
import si.hermes.security.VerifyStatus;

public abstract interface IRevocationRef extends IPersistable
{
  public abstract void createRevocationRefFromB64(String paramString1, String paramString2)
    throws ESignDocException, EXAdESException, Base64DecodingException;

  public abstract String getCRL();

  public abstract String getDigestMethod();

  public abstract String getDigestValue();

  public abstract String getIssuer();

  public abstract String getIssueTime();

  public abstract String getNumber();

  public abstract String getUri();

  public abstract void setCRL(String paramString);

  public abstract void setDigestMethod(String paramString);

  public abstract void setDigestValue(String paramString);

  public abstract void setIssuer(String paramString);

  public abstract void setIssueTime(String paramString);

  public abstract void setNumber(String paramString);

  public abstract void setUri(String paramString);

  public abstract VerifyStatus validateRevocationRef(String paramString)
    throws EXAdESException, ESignDocException, Base64DecodingException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IRevocationRef
 * JD-Core Version:    0.6.0
 */