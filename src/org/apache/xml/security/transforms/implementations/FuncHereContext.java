package org.apache.xml.security.transforms.implementations;

import org.apache.xml.dtm.DTMManager;
import org.apache.xml.security.utils.I18n;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPathContext;
import org.w3c.dom.Node;

public class FuncHereContext extends XPathContext
{
  private FuncHereContext()
  {
  }

  public FuncHereContext(Node paramNode)
  {
    super(paramNode);
  }

  public FuncHereContext(Node paramNode, XPathContext paramXPathContext)
  {
    super(paramNode);
    try
    {
      this.m_dtmManager = paramXPathContext.getDTMManager();
      return;
    }
    catch (IllegalAccessError localIllegalAccessError)
    {
      throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
    }
  }

  public FuncHereContext(Node paramNode, CachedXPathAPI paramCachedXPathAPI)
  {
    super(paramNode);
    try
    {
      this.m_dtmManager = paramCachedXPathAPI.getXPathContext().getDTMManager();
      return;
    }
    catch (IllegalAccessError localIllegalAccessError)
    {
      throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
    }
  }

  public FuncHereContext(Node paramNode, DTMManager paramDTMManager)
  {
    super(paramNode);
    try
    {
      this.m_dtmManager = paramDTMManager;
      return;
    }
    catch (IllegalAccessError localIllegalAccessError)
    {
      throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.implementations.FuncHereContext
 * JD-Core Version:    0.6.0
 */