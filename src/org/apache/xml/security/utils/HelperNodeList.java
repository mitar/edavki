package org.apache.xml.security.utils;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HelperNodeList
  implements NodeList
{
  static Log log = LogFactory.getLog(HelperNodeList.class.getName());
  ArrayList nodes = new ArrayList(20);
  boolean _allNodesMustHaveSameParent = false;

  public HelperNodeList()
  {
    this(false);
  }

  public HelperNodeList(boolean paramBoolean)
  {
    this._allNodesMustHaveSameParent = paramBoolean;
  }

  public Node item(int paramInt)
  {
    return (Node)this.nodes.get(paramInt);
  }

  public int getLength()
  {
    return this.nodes.size();
  }

  public void appendChild(Node paramNode)
    throws IllegalArgumentException
  {
    if ((this._allNodesMustHaveSameParent) && (getLength() > 0) && (item(0).getParentNode() != paramNode.getParentNode()))
      throw new IllegalArgumentException("Nodes have not the same Parent");
    this.nodes.add(paramNode);
  }

  public Document getOwnerDocument()
  {
    if (getLength() == 0)
      return null;
    return XMLUtils.getOwnerDocument(item(0));
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.HelperNodeList
 * JD-Core Version:    0.6.0
 */