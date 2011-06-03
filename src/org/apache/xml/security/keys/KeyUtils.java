package org.apache.xml.security.keys;

import java.io.PrintStream;
import java.security.PublicKey;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.KeyName;
import org.apache.xml.security.keys.content.KeyValue;
import org.apache.xml.security.keys.content.MgmtData;
import org.apache.xml.security.keys.content.X509Data;

public class KeyUtils
{
  public static void prinoutKeyInfo(KeyInfo paramKeyInfo, PrintStream paramPrintStream)
    throws XMLSecurityException
  {
    Object localObject;
    for (int i = 0; i < paramKeyInfo.lengthKeyName(); i++)
    {
      localObject = paramKeyInfo.itemKeyName(i);
      paramPrintStream.println("KeyName(" + i + ")=\"" + ((KeyName)localObject).getKeyName() + "\"");
    }
    for (i = 0; i < paramKeyInfo.lengthKeyValue(); i++)
    {
      PublicKey localPublicKey = (localObject = paramKeyInfo.itemKeyValue(i)).getPublicKey();
      paramPrintStream.println("KeyValue Nr. " + i);
      paramPrintStream.println(localPublicKey);
    }
    for (i = 0; i < paramKeyInfo.lengthMgmtData(); i++)
    {
      localObject = paramKeyInfo.itemMgmtData(i);
      paramPrintStream.println("MgmtData(" + i + ")=\"" + ((MgmtData)localObject).getMgmtData() + "\"");
    }
    for (i = 0; i < paramKeyInfo.lengthX509Data(); i++)
    {
      localObject = paramKeyInfo.itemX509Data(i);
      paramPrintStream.println("X509Data(" + i + ")=\"" + (((X509Data)localObject).containsCertificate() ? "Certificate " : "") + (((X509Data)localObject).containsIssuerSerial() ? "IssuerSerial " : "") + "\"");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.keys.KeyUtils
 * JD-Core Version:    0.6.0
 */