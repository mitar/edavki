package org.apache.xml.security.utils;

import java.io.PrintStream;

public class Version
{
  public static String fVersion = "@@VERSION@@";

  public static final String getVersion()
  {
    return fVersion;
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println(getVersion());
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.Version
 * JD-Core Version:    0.6.0
 */