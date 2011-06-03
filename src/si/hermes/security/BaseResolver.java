package si.hermes.security;

import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class BaseResolver
  implements IResolver
{
  static Log log = LogFactory.getLog(BaseResolver.class.getName());
  private String baseURI = "";
  private Hashtable entityInfo = new Hashtable();

  public final String getBaseURI()
  {
    return this.baseURI;
  }

  public final void setBaseURI(String paramString)
  {
    this.baseURI = paramString;
  }

  public final void setEntity(String paramString, byte[] paramArrayOfByte)
  {
    new StringBuffer();
    log.info("adding entity: " + paramString + (paramArrayOfByte == null ? " with data null" : " with data not null"));
    this.entityInfo.put(paramString, paramArrayOfByte);
  }

  public final byte[] getEntity(String paramString)
  {
    return (byte[])this.entityInfo.get(paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.BaseResolver
 * JD-Core Version:    0.6.0
 */