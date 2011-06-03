package org.apache.xml.security.encryption;

public abstract interface XMLCipherParameters
{
  public static final String AES_128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
  public static final String AES_256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
  public static final String AES_192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
  public static final String RSA_1_5 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
  public static final String RSA_OAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
  public static final String DIFFIE_HELLMAN = "http://www.w3.org/2001/04/xmlenc#dh";
  public static final String TRIPLEDES_KEYWRAP = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
  public static final String AES_128_KEYWRAP = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
  public static final String AES_256_KEYWRAP = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
  public static final String AES_192_KEYWRAP = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
  public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
  public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
  public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";
  public static final String RIPEMD_160 = "http://www.w3.org/2001/04/xmlenc#ripemd160";
  public static final String XML_DSIG = "http://www.w3.org/2000/09/xmldsig#";
  public static final String N14C_XML = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String N14C_XML_CMMNTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String EXCL_XML_N14C = "http://www.w3.org/2001/10/xml-exc-c14n#";
  public static final String EXCL_XML_N14C_CMMNTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.XMLCipherParameters
 * JD-Core Version:    0.6.0
 */