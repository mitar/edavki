package si.hermes.security.XAdES;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;
import si.hermes.security.paramHelper;

public final class SignatureProductionPlaceImpl extends PersistableImpl
  implements ISignatureProductionPlace
{
  private static final long serialVersionUID = -5040353326061108076L;
  private String fCity;
  private String fCountryName;
  private String fPostalCode;
  private String fStateOrProvince;

  public final String getCity()
  {
    return this.fCity;
  }

  public final String getCountryName()
  {
    return this.fCountryName;
  }

  public final String getPostalCode()
  {
    return this.fPostalCode;
  }

  public final String getStateOrProvince()
  {
    return this.fStateOrProvince;
  }

  public final void setCity(String paramString)
  {
    this.fCity = paramString;
  }

  public final void setCountryName(String paramString)
  {
    this.fCountryName = paramString;
  }

  public final void setPostalCode(String paramString)
  {
    this.fPostalCode = paramString;
  }

  public final void setStateOrProvince(String paramString)
  {
    this.fStateOrProvince = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "SignatureProductionPlace", "http://uri.etsi.org/01903/v1.1.1#");
      Element localElement = null;
      paramHelper localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "City", "http://uri.etsi.org/01903/v1.1.1#", null, this.fCity, paramBoolean);
      this.fCity = localparamHelper.str1;
      localElement = localparamHelper.elem1;
      localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "StateOrProvince", "http://uri.etsi.org/01903/v1.1.1#", localElement, this.fStateOrProvince, paramBoolean);
      this.fStateOrProvince = localparamHelper.str1;
      localElement = localparamHelper.elem1;
      localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "PostalCode", "http://uri.etsi.org/01903/v1.1.1#", localElement, this.fPostalCode, paramBoolean);
      this.fPostalCode = localparamHelper.str1;
      localElement = localparamHelper.elem1;
      localparamHelper = ensureOptionalElementAfterWithValue(this.fNode, "CountryName", "http://uri.etsi.org/01903/v1.1.1#", localElement, this.fCountryName, paramBoolean);
      this.fCountryName = localparamHelper.str1;
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.SignatureProductionPlaceImpl
 * JD-Core Version:    0.6.0
 */