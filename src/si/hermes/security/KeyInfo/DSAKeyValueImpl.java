package si.hermes.security.KeyInfo;

import org.w3c.dom.Element;
import si.hermes.security.ESignDocException;
import si.hermes.security.PersistableImpl;

public final class DSAKeyValueImpl extends PersistableImpl
  implements IDSAKeyValue
{
  private String fP;
  private String fQ;
  private String fG;
  private String fY;
  private String fJ;
  private String fSeed;
  private String fPgenCounter;
  private static final long serialVersionUID = -2894338547557508986L;

  public final String getG()
  {
    return this.fG;
  }

  public final void setG(String paramString)
  {
    this.fG = paramString;
  }

  public final String getJ()
  {
    return this.fJ;
  }

  public final void setJ(String paramString)
  {
    this.fJ = paramString;
  }

  public final String getP()
  {
    return this.fP;
  }

  public final void setP(String paramString)
  {
    this.fP = paramString;
  }

  public final String getPgenCounter()
  {
    return this.fPgenCounter;
  }

  public final void setPgenCounter(String paramString)
  {
    this.fPgenCounter = paramString;
  }

  public final String getQ()
  {
    return this.fQ;
  }

  public final void setQ(String paramString)
  {
    this.fQ = paramString;
  }

  public final String getSeed()
  {
    return this.fSeed;
  }

  public final void setSeed(String paramString)
  {
    this.fSeed = paramString;
  }

  public final String getY()
  {
    return this.fY;
  }

  public final void setY(String paramString)
  {
    this.fY = paramString;
  }

  protected final void Synchronize(boolean paramBoolean)
    throws ESignDocException
  {
    try
    {
      this.fNode = EnsureBaseElement(this.fNode, "KeyValue", "http://www.w3.org/2000/09/xmldsig#");
      Element localElement1 = EnsureElement(this.fNode, "DSAKeyValue", "http://www.w3.org/2000/09/xmldsig#");
      Element localElement2 = EnsureElement(localElement1, "P", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fP = EnsureValueCheckNode(localElement2, this.fP, paramBoolean);
      localElement2 = EnsureElement(localElement1, "Q", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fQ = EnsureValueCheckNode(localElement2, this.fQ, paramBoolean);
      localElement2 = EnsureElement(localElement1, "J", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fJ = EnsureValueCheckNode(localElement2, this.fJ, paramBoolean);
      localElement2 = EnsureElement(localElement1, "G", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fG = EnsureValueCheckNode(localElement2, this.fG, paramBoolean);
      localElement2 = EnsureElement(localElement1, "Y", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fY = EnsureValueCheckNode(localElement2, this.fY, paramBoolean);
      localElement2 = EnsureElement(localElement1, "Seed", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fSeed = EnsureValueCheckNode(localElement2, this.fSeed, paramBoolean);
      localElement2 = EnsureElement(localElement1, "PgenCounter", "http://www.w3.org/2000/09/xmldsig#", false);
      this.fPgenCounter = EnsureValueCheckNode(localElement2, this.fPgenCounter, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(15, localException);
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.KeyInfo.DSAKeyValueImpl
 * JD-Core Version:    0.6.0
 */