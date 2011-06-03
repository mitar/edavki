package si.hermes.security.XAdES;

import java.util.Hashtable;

public final class EXAdESException extends Exception
{
  public static final int CERT_ISSUER_NOT_TRUSTED = 0;
  private static final String CERT_ISSUER_NOT_TRUSTED_MSG = "Certificate issuer is not trusted";
  public static final int CERTIFICATE_AND_REVOCATION_REFS_MISSING = 5;
  private static final String CERTIFICATE_AND_REVOCATION_REFS_MISSING_MSG = "Certificate and revocation refs missing";
  public static final int EMPTY_SIGNATURE_PARAMETER = 6;
  private static final String EMPTY_SIGNATURE_PARAMETER_MSG = "Empty signature parameter";
  public static final int INTERNAL_ERROR = 2;
  private static final String INTERNAL_ERROR_MSG = "Internal error";
  public static final int INVALID_CRL_BLOB = 3;
  public static final int INVALID_CRL_BLOB_ISSUE_TIME_NOT_VALID = 4;
  private static final String INVALID_CRL_BLOB_ISSUE_TIME_NOT_VALID_MSG = "Invalid CRL blob issue time not valid";
  private static final String INVALID_CRL_BLOB_MSG = "Invalid CRL blob";
  private static Hashtable mapping = new Hashtable();
  private static final long serialVersionUID = 1483091244251687812L;
  public static final int SIGNATURE_SIGNEDINFO_ID_NOT_SET = 9;
  private static final String SIGNATURE_SIGNEDINFO_ID_NOT_SET_MSG = "Signature SignedInfo Id not set";
  public static final int SIGNATURE_TIMESTAMP_MISSING = 7;
  private static final String SIGNATURE_TIMESTAMP_MISSING_MSG = "Signature timestamp missing";
  public static final int SIGNER_ROLE_ALREADY_SET = 1;
  private static final String SIGNER_ROLE_ALREADY_SET_MSG = "Signer role already set";
  public static final int TIMESTAMP_INVALID_HASHDATAINFO_URI = 10;
  private static final String TIMESTAMP_INVALID_HASHDATAINFO_URI_MSG = "Timestamp invalid HashDataInfo Uri";
  public static final int TRANSFORMS_IN_HASHDATAINFO_NOT_SUPPORTED = 8;
  private static final String TRANSFORMS_IN_HASHDATAINFO_NOT_SUPPORTED_MSG = "Transforms in HashDataInfo not supported";
  private int type = -1;

  public EXAdESException(int paramInt, String paramString)
  {
    this(paramInt, paramString, null);
  }

  public EXAdESException(int paramInt)
  {
    this(paramInt, null, null);
  }

  public EXAdESException(int paramInt, Exception paramException)
  {
    this(paramInt, null, paramException);
  }

  public EXAdESException(int paramInt, String paramString, Exception paramException)
  {
    super((String)mapping.get(new Integer(paramInt)) + (paramString != null ? ": " + paramString : "") + (paramException != null ? " (" + paramException.getClass().getName() + (paramException.getMessage() != null ? ": " + paramException.getMessage() : "") + ")" : ""));
    if (paramException != null)
      setStackTrace(paramException.getStackTrace());
    this.type = paramInt;
  }

  public final String getErrorText()
  {
    return (String)mapping.get(new Integer(this.type));
  }

  public final int getErrorType()
  {
    return this.type;
  }

  static
  {
    mapping.put(new Integer(0), "Certificate issuer is not trusted");
    mapping.put(new Integer(1), "Signer role already set");
    mapping.put(new Integer(2), "Internal error");
    mapping.put(new Integer(3), "Invalid CRL blob");
    mapping.put(new Integer(4), "Invalid CRL blob issue time not valid");
    mapping.put(new Integer(5), "Certificate and revocation refs missing");
    mapping.put(new Integer(6), "Empty signature parameter");
    mapping.put(new Integer(7), "Signature timestamp missing");
    mapping.put(new Integer(8), "Transforms in HashDataInfo not supported");
    mapping.put(new Integer(9), "Signature SignedInfo Id not set");
    mapping.put(new Integer(10), "Timestamp invalid HashDataInfo Uri");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.XAdES.EXAdESException
 * JD-Core Version:    0.6.0
 */