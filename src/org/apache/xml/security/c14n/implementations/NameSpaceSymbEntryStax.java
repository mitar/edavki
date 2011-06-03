package org.apache.xml.security.c14n.implementations;

class NameSpaceSymbEntryStax
  implements Cloneable
{
  int level = 0;
  String uri;
  String lastrendered = null;
  boolean rendered = false;
  AttrStream n;

  NameSpaceSymbEntryStax(String paramString, AttrStream paramAttrStream, boolean paramBoolean)
  {
    this.uri = paramString;
    this.rendered = paramBoolean;
    this.n = paramAttrStream;
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
 * Qualified Name:     org.apache.xml.security.c14n.implementations.NameSpaceSymbEntryStax
 * JD-Core Version:    0.6.0
 */