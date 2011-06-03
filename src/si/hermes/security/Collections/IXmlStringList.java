package si.hermes.security.Collections;

import org.apache.xml.utils.PrefixResolver;
import si.hermes.security.ESignDocException;
import si.hermes.security.IPersistable;

public abstract interface IXmlStringList extends IPersistable
{
  public abstract int getCount();

  public abstract String getItem(int paramInt)
    throws ESignDocException;

  public abstract void setItem(int paramInt, String paramString)
    throws ESignDocException;

  public abstract void Remove(int paramInt);

  public abstract void SetFilterName(String paramString1, String paramString2);

  public abstract void SetXPath(String paramString, PrefixResolver paramPrefixResolver);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Collections.IXmlStringList
 * JD-Core Version:    0.6.0
 */