package org.apache.xml.security.keys.content.keyvalues;

import java.security.PublicKey;
import org.apache.xml.security.exceptions.XMLSecurityException;

public abstract interface KeyValueContent
{
  public abstract PublicKey getPublicKey()
    throws XMLSecurityException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.keyvalues.KeyValueContent
 * JD-Core Version:    0.6.0
 */