package si.hermes.security.XAdES;

import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.IPersistable;
import si.hermes.security.VerifyStatus;

public abstract interface IDataObjectFormat extends IPersistable
{
  public abstract String getDescription();

  public abstract String getEncoding();

  public abstract String getMimeType();

  public abstract IObjectIdentifier getObjectIdentifier();

  public abstract String getObjectReference();

  public abstract void setDescription(String paramString);

  public abstract void setEncoding(String paramString);

  public abstract void setMimeType(String paramString);

  public abstract void setObjectIdentifier(IObjectIdentifier paramIObjectIdentifier);

  public abstract void setObjectReference(String paramString);

  public abstract VerifyStatus validate(IHSLSignature paramIHSLSignature);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IDataObjectFormat
 * JD-Core Version:    0.6.0
 */