package org.apache.xml.security.encryption;

import java.util.Iterator;

public abstract interface ReferenceList
{
  public static final int DATA_REFERENCE = 1;
  public static final int KEY_REFERENCE = 2;

  public abstract void add(Reference paramReference);

  public abstract void remove(Reference paramReference);

  public abstract int size();

  public abstract boolean isEmpty();

  public abstract Iterator getReferences();

  public abstract Reference newDataReference(String paramString);

  public abstract Reference newKeyReference(String paramString);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.ReferenceList
 * JD-Core Version:    0.6.0
 */