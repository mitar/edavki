package si.hermes.security;

import java.security.cert.X509CRL;
import java.util.Date;

public final class SimpleObject
{
  Date fDate = new Date();
  X509CRL fInput;

  public final boolean Expired(int paramInt)
  {
    return new Date().getTime() - this.fDate.getTime() > paramInt * 1000;
  }

  public SimpleObject(X509CRL paramX509CRL)
  {
    this.fInput = paramX509CRL;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.SimpleObject
 * JD-Core Version:    0.6.0
 */