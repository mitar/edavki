package org.apache.xml.security.encryption;

import org.w3c.dom.Attr;

public abstract interface CipherReference
{
  public abstract String getURI();

  public abstract Attr getURIAsAttr();

  public abstract Transforms getTransforms();

  public abstract void setTransforms(Transforms paramTransforms);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.CipherReference
 * JD-Core Version:    0.6.0
 */