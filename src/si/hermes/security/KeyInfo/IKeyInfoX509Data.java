package si.hermes.security.KeyInfo;

import javax.xml.parsers.ParserConfigurationException;
import si.hermes.security.Collections.IXmlStringList;
import si.hermes.security.ESignDocException;

public abstract interface IKeyInfoX509Data extends IKeyInfoClause
{
  public abstract IXmlStringList getIssuerSerial()
    throws ESignDocException;

  public abstract IXmlStringList getSubjectName()
    throws ESignDocException;

  public abstract IXmlStringList getSKI()
    throws ESignDocException;

  public abstract IXmlStringList getCertificate()
    throws ESignDocException;

  public abstract IXmlStringList getCRL()
    throws ESignDocException;

  public abstract IXmlStringList getIssuerName()
    throws ESignDocException;

  public abstract void AddCertificate(String paramString)
    throws ESignDocException, ParserConfigurationException;

  public abstract void AddIssuerSerial(String paramString1, String paramString2)
    throws ESignDocException, ParserConfigurationException;

  public abstract void AddSubjectName(String paramString)
    throws ESignDocException, ParserConfigurationException;

  public abstract void AddSKI(String paramString)
    throws ESignDocException, ParserConfigurationException;

  public abstract void AddCRL(String paramString)
    throws ESignDocException, ParserConfigurationException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.IKeyInfoX509Data
 * JD-Core Version:    0.6.0
 */