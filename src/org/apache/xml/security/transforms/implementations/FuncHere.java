package org.apache.xml.security.transforms.implementations;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class FuncHere extends Function
{
  public XObject execute(XPathContext paramXPathContext)
    throws TransformerException
  {
    Node localNode;
    if ((localNode = (Node)paramXPathContext.getOwnerObject()) == null)
      return null;
    int i = paramXPathContext.getDTMHandleFromNode(localNode);
    int j = paramXPathContext.getCurrentNode();
    DTM localDTM;
    int k = (localDTM = paramXPathContext.getDTM(j)).getDocument();
    if (-1 == k)
      error(paramXPathContext, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
    Object localObject1 = XMLUtils.getOwnerDocument(localDTM.getNode(j));
    Object localObject2 = XMLUtils.getOwnerDocument(localNode);
    if (localObject1 != localObject2)
      throw new TransformerException(I18n.translate("xpath.funcHere.documentsDiffer"));
    localObject2 = (localObject1 = new XNodeSet(paramXPathContext.getDTMManager())).mutableNodeset();
    int m = 0;
    switch (localDTM.getNodeType(i))
    {
    case 2:
      tmpTernaryOp = i;
      break;
    case 7:
      tmpTernaryOp = i;
      break;
    case 3:
      m = localDTM.getParent(i);
      ((NodeSetDTM)localObject2).addNode(m);
    }
    ((NodeSetDTM)localObject2).detach();
    return (XObject)(XObject)localObject1;
  }

  public void fixupVariables(Vector paramVector, int paramInt)
  {
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.FuncHere
 * JD-Core Version:    0.6.0
 */