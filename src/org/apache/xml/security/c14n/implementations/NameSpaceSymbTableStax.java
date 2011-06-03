package org.apache.xml.security.c14n.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class NameSpaceSymbTableStax
{
  HashMap symb = new HashMap();
  int nameSpaces = 0;
  List level = new ArrayList();
  boolean cloned = true;
  static final String XMLNS = "xmlns";

  public NameSpaceSymbTableStax()
  {
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax;
    (localNameSpaceSymbEntryStax = new NameSpaceSymbEntryStax("", null, true)).lastrendered = "";
    this.symb.put("xmlns", localNameSpaceSymbEntryStax);
  }

  public void getUnrenderedNodes(Collection paramCollection)
  {
    Iterator localIterator = this.symb.entrySet().iterator();
    while (localIterator.hasNext())
    {
      NameSpaceSymbEntryStax localNameSpaceSymbEntryStax;
      if ((!(localNameSpaceSymbEntryStax = (NameSpaceSymbEntryStax)((Map.Entry)localIterator.next()).getValue()).rendered) && (localNameSpaceSymbEntryStax.n != null))
      {
        paramCollection.add(localNameSpaceSymbEntryStax.n);
        localNameSpaceSymbEntryStax.lastrendered = localNameSpaceSymbEntryStax.uri;
        localNameSpaceSymbEntryStax.rendered = true;
      }
    }
  }

  public void outputNodePush()
  {
    this.nameSpaces += 1;
    push();
  }

  public void outputNodePop()
  {
    this.nameSpaces -= 1;
    pop();
  }

  public void push()
  {
    this.level.add(null);
    this.cloned = false;
  }

  public void pop()
  {
    int i = this.level.size() - 1;
    HashMap localHashMap;
    if ((localHashMap = (HashMap)this.level.remove(i)) != null)
    {
      this.symb = localHashMap;
      if (i != 0)
      {
        this.cloned = (this.level.get(i - 1) != this.symb);
        return;
      }
    }
    this.cloned = false;
  }

  final void needsClone()
  {
    if (!this.cloned)
    {
      this.level.remove(this.level.size() - 1);
      this.level.add(this.symb);
      this.symb = ((HashMap)this.symb.clone());
      this.cloned = true;
    }
  }

  public AttrStream getMapping(String paramString)
  {
    if ((localNameSpaceSymbEntryStax = (NameSpaceSymbEntryStax)this.symb.get(paramString)) == null)
      return null;
    if (localNameSpaceSymbEntryStax.rendered)
      return null;
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax = (NameSpaceSymbEntryStax)localNameSpaceSymbEntryStax.clone();
    needsClone();
    this.symb.put(paramString, localNameSpaceSymbEntryStax);
    localNameSpaceSymbEntryStax.rendered = true;
    localNameSpaceSymbEntryStax.level = this.nameSpaces;
    localNameSpaceSymbEntryStax.lastrendered = localNameSpaceSymbEntryStax.uri;
    return localNameSpaceSymbEntryStax.n;
  }

  public AttrStream getMappingWithoutRendered(String paramString)
  {
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax;
    if ((localNameSpaceSymbEntryStax = (NameSpaceSymbEntryStax)this.symb.get(paramString)) == null)
      return null;
    if (localNameSpaceSymbEntryStax.rendered)
      return null;
    return localNameSpaceSymbEntryStax.n;
  }

  public boolean addMapping(String paramString1, String paramString2, AttrStream paramAttrStream)
  {
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax1;
    if (((localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)this.symb.get(paramString1)) != null) && (paramString2.equals(localNameSpaceSymbEntryStax1.uri)))
      return false;
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax2 = new NameSpaceSymbEntryStax(paramString2, paramAttrStream, false);
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntryStax2);
    if (localNameSpaceSymbEntryStax1 != null)
    {
      localNameSpaceSymbEntryStax2.lastrendered = localNameSpaceSymbEntryStax1.lastrendered;
      if ((localNameSpaceSymbEntryStax1.lastrendered != null) && (localNameSpaceSymbEntryStax1.lastrendered.equals(paramString2)))
        localNameSpaceSymbEntryStax2.rendered = true;
    }
    return true;
  }

  public AttrStream addMappingAndRender(String paramString1, String paramString2, AttrStream paramAttrStream)
  {
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax1;
    if (((localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)this.symb.get(paramString1)) != null) && (paramString2.equals(localNameSpaceSymbEntryStax1.uri)))
    {
      if (!localNameSpaceSymbEntryStax1.rendered)
      {
        localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)localNameSpaceSymbEntryStax1.clone();
        needsClone();
        this.symb.put(paramString1, localNameSpaceSymbEntryStax1);
        localNameSpaceSymbEntryStax1.lastrendered = paramString2;
        localNameSpaceSymbEntryStax1.rendered = true;
        return localNameSpaceSymbEntryStax1.n;
      }
      return null;
    }
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax2;
    (localNameSpaceSymbEntryStax2 = new NameSpaceSymbEntryStax(paramString2, paramAttrStream, true)).lastrendered = paramString2;
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntryStax2);
    if ((localNameSpaceSymbEntryStax1 != null) && (localNameSpaceSymbEntryStax1.lastrendered != null) && (localNameSpaceSymbEntryStax1.lastrendered.equals(paramString2)))
    {
      localNameSpaceSymbEntryStax2.rendered = true;
      return null;
    }
    return localNameSpaceSymbEntryStax2.n;
  }

  public AttrStream addMappingAndRenderXNodeSet(String paramString1, String paramString2, AttrStream paramAttrStream, boolean paramBoolean)
  {
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)this.symb.get(paramString1);
    int i = this.nameSpaces;
    if ((localNameSpaceSymbEntryStax1 != null) && (paramString2.equals(localNameSpaceSymbEntryStax1.uri)))
    {
      if (!localNameSpaceSymbEntryStax1.rendered)
      {
        localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)localNameSpaceSymbEntryStax1.clone();
        needsClone();
        this.symb.put(paramString1, localNameSpaceSymbEntryStax1);
        localNameSpaceSymbEntryStax1.rendered = true;
        localNameSpaceSymbEntryStax1.level = i;
        return localNameSpaceSymbEntryStax1.n;
      }
      localNameSpaceSymbEntryStax1 = (NameSpaceSymbEntryStax)localNameSpaceSymbEntryStax1.clone();
      needsClone();
      this.symb.put(paramString1, localNameSpaceSymbEntryStax1);
      if ((paramBoolean) && ((i - localNameSpaceSymbEntryStax1.level < 2) || ("xmlns".equals(paramString1))))
      {
        localNameSpaceSymbEntryStax1.level = i;
        return null;
      }
      localNameSpaceSymbEntryStax1.level = i;
      return localNameSpaceSymbEntryStax1.n;
    }
    NameSpaceSymbEntryStax localNameSpaceSymbEntryStax2;
    (localNameSpaceSymbEntryStax2 = new NameSpaceSymbEntryStax(paramString2, paramAttrStream, true)).level = this.nameSpaces;
    localNameSpaceSymbEntryStax2.rendered = true;
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntryStax2);
    if (localNameSpaceSymbEntryStax1 != null)
    {
      localNameSpaceSymbEntryStax2.lastrendered = localNameSpaceSymbEntryStax1.lastrendered;
      if ((localNameSpaceSymbEntryStax1.lastrendered != null) && (localNameSpaceSymbEntryStax1.lastrendered.equals(paramString2)))
        localNameSpaceSymbEntryStax2.rendered = true;
    }
    return localNameSpaceSymbEntryStax2.n;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.NameSpaceSymbTableStax
 * JD-Core Version:    0.6.0
 */