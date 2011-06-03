package org.apache.xml.security.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import org.apache.xml.security.utils.I18n;

public class XMLSecurityException extends Exception
{
  protected Exception originalException = null;
  protected String msgID;

  public XMLSecurityException()
  {
    super("Missing message string");
    this.msgID = null;
    this.originalException = null;
  }

  public XMLSecurityException(String paramString)
  {
    super(I18n.getExceptionMessage(paramString));
    this.msgID = paramString;
    this.originalException = null;
  }

  public XMLSecurityException(String paramString, Object[] paramArrayOfObject)
  {
    super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
    this.msgID = paramString;
    this.originalException = null;
  }

  public XMLSecurityException(Exception paramException)
  {
    super("Missing message ID to locate message string in resource bundle \"org/apache/xml/security/resource/xmlsecurity\". Original Exception was a " + paramException.getClass().getName() + " and message " + paramException.getMessage());
    this.originalException = paramException;
  }

  public XMLSecurityException(String paramString, Exception paramException)
  {
    super(I18n.getExceptionMessage(paramString, paramException));
    this.msgID = paramString;
    this.originalException = paramException;
  }

  public XMLSecurityException(String paramString, Object[] paramArrayOfObject, Exception paramException)
  {
    super(MessageFormat.format(I18n.getExceptionMessage(paramString), paramArrayOfObject));
    this.msgID = paramString;
    this.originalException = paramException;
  }

  public String getMsgID()
  {
    if (this.msgID == null)
      return "Missing message ID";
    return this.msgID;
  }

  public String toString()
  {
    String str1 = getClass().getName();
    String str2 = (str2 = super.getLocalizedMessage()) != null ? str1 + ": " + str2 : str1;
    if (this.originalException != null)
      str2 = str2 + "\nOriginal Exception was " + this.originalException.toString();
    return str2;
  }

  public void printStackTrace()
  {
    synchronized (System.err)
    {
      super.printStackTrace(System.err);
      if (this.originalException != null)
        this.originalException.printStackTrace(System.err);
      return;
    }
  }

  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    super.printStackTrace(paramPrintWriter);
    if (this.originalException != null)
      this.originalException.printStackTrace(paramPrintWriter);
  }

  public void printStackTrace(PrintStream paramPrintStream)
  {
    super.printStackTrace(paramPrintStream);
    if (this.originalException != null)
      this.originalException.printStackTrace(paramPrintStream);
  }

  public Exception getOriginalException()
  {
    return this.originalException;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.exceptions.XMLSecurityException
 * JD-Core Version:    0.6.0
 */