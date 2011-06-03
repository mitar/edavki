package si.hermes.security;

public final class VerifyStatus
{
  private String fStatus;
  private String fReason;

  public final String getStatus()
  {
    return this.fStatus;
  }

  public final String getReason()
  {
    return this.fReason;
  }

  public final void setStatus(String paramString)
  {
    this.fStatus = paramString;
  }

  public final void setReason(String paramString)
  {
    this.fReason = paramString;
  }

  public VerifyStatus()
  {
    this.fStatus = "";
    this.fReason = "";
  }

  public VerifyStatus(String paramString1, String paramString2)
  {
    this.fStatus = paramString1;
    this.fReason = paramString2;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.VerifyStatus
 * JD-Core Version:    0.6.0
 */