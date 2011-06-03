package org.apache.xml.security.c14n.implementations;

public class Canonicalizer20010315ExclWithComments extends Canonicalizer20010315Excl
{
  public Canonicalizer20010315ExclWithComments()
  {
    super(true);
  }

  public final String engineGetURI()
  {
    return "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
  }

  public final boolean engineGetIncludeComments()
  {
    return true;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.Canonicalizer20010315ExclWithComments
 * JD-Core Version:    0.6.0
 */