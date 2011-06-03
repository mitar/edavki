package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.paramHelper;

public final class SignerRoleImpl extends PersistableImpl
  implements ISignerRole
{
  private static final long serialVersionUID = 1545004951499358416L;
  private IXMLStringList2 fCertifiedRoles = null;
  private IXMLStringList2 fClaimedRoles = null;

  public final int addCertifiedRole(String paramString)
  {
    return getCertifiedRoles().addValue(paramString);
  }

  public final int addClaimedRole(String paramString)
  {
    return getClaimedRoles().addValue(paramString);
  }

  public final IXMLStringList2 getCertifiedRoles()
  {
    if (this.fCertifiedRoles == null)
      this.fCertifiedRoles = new XMLStringList2Impl("CertifiedRoles", "http://uri.etsi.org/01903/v1.1.1#", "CertifiedRole", "http://uri.etsi.org/01903/v1.1.1#");
    return this.fCertifiedRoles;
  }

  public final IXMLStringList2 getClaimedRoles()
  {
    if (this.fClaimedRoles == null)
      this.fClaimedRoles = new XMLStringList2Impl("ClaimedRoles", "http://uri.etsi.org/01903/v1.1.1#", "ClaimedRole", "http://uri.etsi.org/01903/v1.1.1#");
    return this.fClaimedRoles;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "SignerRole", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement1 = null;
      Element localElement2 = null;
      paramHelper localparamHelper;
      localElement2 = (localparamHelper = ensureElementAfter(this.fNode, "ClaimedRoles", "http://uri.etsi.org/01903/v1.1.1#", null, (getClaimedRoles().getCount() != 0) && (!paramBoolean), (getClaimedRoles().getCount() == 0) && (!paramBoolean))).elem1;
      localElement1 = localparamHelper.elem2;
      if (paramBoolean)
        getClaimedRoles().LoadXml(localElement2);
      else if (localElement2 != null)
        getClaimedRoles().GetXml(localElement2);
      localElement2 = (localparamHelper = ensureElementAfter(this.fNode, "CertifiedRoles", "http://uri.etsi.org/01903/v1.1.1#", localElement1, (getCertifiedRoles().getCount() != 0) && (!paramBoolean), (getCertifiedRoles().getCount() == 0) && (!paramBoolean))).elem1;
      if (paramBoolean)
      {
        getCertifiedRoles().LoadXml(localElement2);
      }
      else
      {
        if (localElement2 != null)
          getCertifiedRoles().GetXml(localElement2);
        return;
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.SignerRoleImpl
 * JD-Core Version:    0.6.0
 */