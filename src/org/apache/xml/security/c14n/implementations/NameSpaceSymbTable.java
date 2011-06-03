package org.apache.xml.security.c14n.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class NameSpaceSymbTable
{
  HashMap symb = new HashMap();
  int nameSpaces = 0;
  List level = new ArrayList();
  boolean cloned = true;
  static final String XMLNS = "xmlns";

  public NameSpaceSymbTable()
  {
    NameSpaceSymbEntry localNameSpaceSymbEntry;
    (localNameSpaceSymbEntry = new NameSpaceSymbEntry("", null, true)).lastrendered = "";
    this.symb.put("xmlns", localNameSpaceSymbEntry);
  }

  public void getUnrenderedNodes(Collection paramCollection)
  {
    Iterator localIterator = this.symb.entrySet().iterator();
    while (localIterator.hasNext())
    {
      NameSpaceSymbEntry localNameSpaceSymbEntry;
      if ((!(localNameSpaceSymbEntry = (NameSpaceSymbEntry)((Map.Entry)localIterator.next()).getValue()).rendered) && (localNameSpaceSymbEntry.n != null))
      {
        paramCollection.add(localNameSpaceSymbEntry.n);
        localNameSpaceSymbEntry.lastrendered = localNameSpaceSymbEntry.uri;
        localNameSpaceSymbEntry.rendered = true;
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
    Object localObject;
    if ((localObject = this.level.remove(i)) != null)
    {
      this.symb = ((HashMap)localObject);
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

  public Attr getMapping(String paramString)
  {
    if ((localNameSpaceSymbEntry = (NameSpaceSymbEntry)this.symb.get(paramString)) == null)
      return null;
    if (localNameSpaceSymbEntry.rendered)
      return null;
    NameSpaceSymbEntry localNameSpaceSymbEntry = (NameSpaceSymbEntry)localNameSpaceSymbEntry.clone();
    needsClone();
    this.symb.put(paramString, localNameSpaceSymbEntry);
    localNameSpaceSymbEntry.rendered = true;
    localNameSpaceSymbEntry.level = this.nameSpaces;
    localNameSpaceSymbEntry.lastrendered = localNameSpaceSymbEntry.uri;
    return localNameSpaceSymbEntry.n;
  }

  public Attr getMappingWithoutRendered(String paramString)
  {
    NameSpaceSymbEntry localNameSpaceSymbEntry;
    if ((localNameSpaceSymbEntry = (NameSpaceSymbEntry)this.symb.get(paramString)) == null)
      return null;
    if (localNameSpaceSymbEntry.rendered)
      return null;
    return localNameSpaceSymbEntry.n;
  }

  public boolean addMapping(String paramString1, String paramString2, Attr paramAttr)
  {
    NameSpaceSymbEntry localNameSpaceSymbEntry1;
    if (((localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)this.symb.get(paramString1)) != null) && (paramString2.equals(localNameSpaceSymbEntry1.uri)))
      return false;
    NameSpaceSymbEntry localNameSpaceSymbEntry2 = new NameSpaceSymbEntry(paramString2, paramAttr, false);
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntry2);
    if (localNameSpaceSymbEntry1 != null)
    {
      localNameSpaceSymbEntry2.lastrendered = localNameSpaceSymbEntry1.lastrendered;
      if ((localNameSpaceSymbEntry1.lastrendered != null) && (localNameSpaceSymbEntry1.lastrendered.equals(paramString2)))
        localNameSpaceSymbEntry2.rendered = true;
    }
    return true;
  }

  public Node addMappingAndRender(String paramString1, String paramString2, Attr paramAttr)
  {
    NameSpaceSymbEntry localNameSpaceSymbEntry1;
    if (((localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)this.symb.get(paramString1)) != null) && (paramString2.equals(localNameSpaceSymbEntry1.uri)))
    {
      if (!localNameSpaceSymbEntry1.rendered)
      {
        localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)localNameSpaceSymbEntry1.clone();
        needsClone();
        this.symb.put(paramString1, localNameSpaceSymbEntry1);
        localNameSpaceSymbEntry1.lastrendered = paramString2;
        localNameSpaceSymbEntry1.rendered = true;
        return localNameSpaceSymbEntry1.n;
      }
      return null;
    }
    NameSpaceSymbEntry localNameSpaceSymbEntry2;
    (localNameSpaceSymbEntry2 = new NameSpaceSymbEntry(paramString2, paramAttr, true)).lastrendered = paramString2;
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntry2);
    if ((localNameSpaceSymbEntry1 != null) && (localNameSpaceSymbEntry1.lastrendered != null) && (localNameSpaceSymbEntry1.lastrendered.equals(paramString2)))
    {
      localNameSpaceSymbEntry2.rendered = true;
      return null;
    }
    return localNameSpaceSymbEntry2.n;
  }

  public Node addMappingAndRenderXNodeSet(String paramString1, String paramString2, Attr paramAttr, boolean paramBoolean)
  {
    NameSpaceSymbEntry localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)this.symb.get(paramString1);
    int i = this.nameSpaces;
    if ((localNameSpaceSymbEntry1 != null) && (paramString2.equals(localNameSpaceSymbEntry1.uri)))
    {
      if (!localNameSpaceSymbEntry1.rendered)
      {
        localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)localNameSpaceSymbEntry1.clone();
        needsClone();
        this.symb.put(paramString1, localNameSpaceSymbEntry1);
        localNameSpaceSymbEntry1.rendered = true;
        localNameSpaceSymbEntry1.level = i;
        return localNameSpaceSymbEntry1.n;
      }
      localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)localNameSpaceSymbEntry1.clone();
      needsClone();
      this.symb.put(paramString1, localNameSpaceSymbEntry1);
      if ((paramBoolean) && ((i - localNameSpaceSymbEntry1.level < 2) || ("xmlns".equals(paramString1))))
      {
        localNameSpaceSymbEntry1.level = i;
        return null;
      }
      localNameSpaceSymbEntry1.level = i;
      return localNameSpaceSymbEntry1.n;
    }
    NameSpaceSymbEntry localNameSpaceSymbEntry2;
    (localNameSpaceSymbEntry2 = new NameSpaceSymbEntry(paramString2, paramAttr, true)).level = this.nameSpaces;
    localNameSpaceSymbEntry2.rendered = true;
    needsClone();
    this.symb.put(paramString1, localNameSpaceSymbEntry2);
    if (localNameSpaceSymbEntry1 != null)
    {
      localNameSpaceSymbEntry2.lastrendered = localNameSpaceSymbEntry1.lastrendered;
      if ((localNameSpaceSymbEntry1.lastrendered != null) && (localNameSpaceSymbEntry1.lastrendered.equals(paramString2)))
        localNameSpaceSymbEntry2.rendered = true;
    }
    return localNameSpaceSymbEntry2.n;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.NameSpaceSymbTable
 * JD-Core Version:    0.6.0
 */