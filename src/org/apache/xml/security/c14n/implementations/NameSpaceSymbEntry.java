package org.apache.xml.security.c14n.implementations;

import org.w3c.dom.Attr;

class NameSpaceSymbEntry
  implements Cloneable
{
  int level = 0;
  String uri;
  String lastrendered = null;
  boolean rendered = false;
  Attr n;

  NameSpaceSymbEntry(String paramString, Attr paramAttr, boolean paramBoolean)
  {
    this.uri = paramString;
    this.rendered = paramBoolean;
    this.n = paramAttr;
  }

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    return null;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.NameSpaceSymbEntry
 * JD-Core Version:    0.6.0
 */