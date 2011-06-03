package si.hermes.security;

public abstract interface IResolver
{
  public abstract String getBaseURI();

  public abstract void setBaseURI(String paramString);

  public abstract byte[] getEntity(String paramString);

  public abstract void setEntity(String paramString, byte[] paramArrayOfByte);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.IResolver
 * JD-Core Version:    0.6.0
 */