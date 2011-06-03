package si.hermes.security.XAdES;

import si.hermes.security.IPersistable;

public abstract interface ISignerRole extends IPersistable
{
  public abstract int addCertifiedRole(String paramString);

  public abstract int addClaimedRole(String paramString);

  public abstract IXMLStringList2 getCertifiedRoles();

  public abstract IXMLStringList2 getClaimedRoles();
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.ISignerRole
 * JD-Core Version:    0.6.0
 */