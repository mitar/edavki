package org.apache.xml.security.keys.content.x509;

import java.security.Principal;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.RFC2253Parser;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLX509SubjectName extends SignatureElementProxy
  implements XMLX509DataContent
{
  static Log log = LogFactory.getLog(XMLX509SubjectName.class.getName());

  public XMLX509SubjectName(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    super(paramElement, paramString);
  }

  public XMLX509SubjectName(Document paramDocument, String paramString)
  {
    super(paramDocument);
    addText(paramString);
  }

  public XMLX509SubjectName(Document paramDocument, X509Certificate paramX509Certificate)
  {
    this(paramDocument, RFC2253Parser.normalize(paramX509Certificate.getSubjectDN().getName()));
  }

  public String getSubjectName()
  {
    return RFC2253Parser.normalize(getTextFromTextChild());
  }

  public boolean equals(Object paramObject)
  {
    if (!paramObject.getClass().getName().equals(getClass().getName()))
      return false;
    XMLX509SubjectName localXMLX509SubjectName;
    String str1 = (localXMLX509SubjectName = (XMLX509SubjectName)paramObject).getSubjectName();
    String str2 = getSubjectName();
    return str1.equals(str2);
  }

  public String getBaseLocalName()
  {
    return "X509SubjectName";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.content.x509.XMLX509SubjectName
 * JD-Core Version:    0.6.0
 */