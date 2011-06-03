package si.hermes.security.XAdES;

import si.hermes.security.IPersistable;

public abstract interface IObjectIdentifier extends IPersistable
{
  public abstract int addDocumentationReference(String paramString);

  public abstract String getDescription();

  public abstract IXMLStringList2 getDocumentationReferences();

  public abstract String getIdentifier();

  public abstract String getQualifier();

  public abstract void setDescription(String paramString);

  public abstract void setIdentifier(String paramString);

  public abstract void setQualifier(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.IObjectIdentifier
 * JD-Core Version:    0.6.0
 */