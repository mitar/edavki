package org.apache.xml.security.c14n.implementations;

public class Canonicalizer20010315WithComments extends Canonicalizer20010315
{
  public Canonicalizer20010315WithComments()
  {
    super(true);
  }

  public final String engineGetURI()
  {
    return "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  }

  public final boolean engineGetIncludeComments()
  {
    return true;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.implementations.Canonicalizer20010315WithComments
 * JD-Core Version:    0.6.0
 */