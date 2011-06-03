package org.apache.xml.security.c14n.implementations;

public class AttrStream
{
  private String prefix;
  private String localName;
  private String namespaceUri;
  private String value;

  public AttrStream(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.prefix = paramString1;
    this.localName = paramString2;
    this.namespaceUri = paramString3;
    this.value = paramString4;
  }

  public String getNodeName()
  {
    if ((this.prefix == null) || (this.prefix == ""))
      return this.localName;
    return this.prefix + ":" + this.localName;
  }

  public String getName()
  {
    if ((this.prefix == null) || (this.prefix == ""))
      return this.localName;
    return this.prefix + ":" + this.localName;
  }

  public String getNodeValue()
  {
    return this.value;
  }

  public void setNodeValue(String paramString)
  {
    this.value = paramString;
  }

  public String getPrefix()
  {
    return this.prefix;
  }

  public String getNamespaceURI()
  {
    return this.namespaceUri;
  }

  public String getLocalName()
  {
    return this.localName;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.AttrStream
 * JD-Core Version:    0.6.0
 */