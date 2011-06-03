package si.hermes.security;

import java.util.Hashtable;

public class ESignDocException extends Exception
{
  private static final long serialVersionUID = -2777644526952600265L;
  public static final int NO_CERT_FOUND = 0;
  private static final String NO_CERT_FOUND_MSG = "No certificate was found";
  public static final int NO_PRIVATE_KEY_FOUND = 1;
  private static final String NO_PRIVATE_KEY_FOUND_MSG = "No private key was found";
  public static final int XPATH_EMPTY = 2;
  private static final String XPATH_EMPTY_MSG = "XPath did not produce any results";
  public static final int INVALID_ARG = 3;
  private static final String INVALID_ARG_MSG = "Invalid arguments";
  public static final int ALGO_NOT_FOUND = 4;
  private static final String ALGO_NOT_FOUND_MSG = "Algorithm not found or supported";
  public static final int SERIALIZATION_FAILED = 5;
  private static final String SERIALIZATION_FAILED_MSG = "Serialization failed";
  public static final int INIT_NOT_CALLED = 6;
  private static final String INIT_NOT_CALLED_MSG = "Initialization method was not called";
  public static final int NOT_IMPL = 7;
  private static final String NOT_IMPL_MSG = "Method not implemented";
  public static final int USER_ABORTED_SIGNING = 8;
  private static final String USER_ABORTED_SIGNING_MSG = "User aborted signing";
  public static final int INVALID_NUMOFSIGNATURES = 9;
  private static final String INVALID_NUMOFSIGNATURES_MSG = "Invalid number of signatures found.";
  public static final int SIGNATURE_NOT_VALID = 10;
  private static final String SIGNATURE_NOT_VALID_MSG = "Signature is not valid.";
  public static final int CERTIFICATESUBJECT_MISMATCH = 11;
  private static final String CERTIFICATESUBJECT_MISMATCH_MSG = "Certificate subject mismatch.";
  public static final int MAINWINDOW_NULL = 12;
  private static final String MAINWINDOW_NULL_MSG = "Cannot get main window focus.";
  public static final int INDEX_BOUNDS = 13;
  private static final String INDEX_BOUNDS_MSG = "Index out of bounds.";
  public static final int XML_ELEMENT = 14;
  private static final String XML_ELEMENT_MSG = "Wrong base element";
  public static final int SYNCHRONIZATION = 15;
  private static final String SYNCHRONIZATION_MSG = "Error while synchronizing data: ";
  public static final int XML_DOCUMENT_FAILED = 16;
  private static final String XML_DOCUMENT_FAILED_MSG = "Error creating DOM Document.";
  public static final int UNKNOWN_NODE = 17;
  private static final String UNKNOWN_NODE_MSG = "Unknown node.";
  public static final int INVALID_XML = 18;
  private static final String INVALID_XML_MSG = "Invalid xml data.";
  public static final int PARENT_NODE_NOT_SET = 19;
  private static final String PARENT_NODE_NOT_SET_MSG = "Parent node not set.";
  public static final int SOAP_CALL_FAILED = 20;
  private static final String SOAP_CALL_FAILED_MSG = "Soap call failed.";
  public static final int BASE_URI_NOT_SET = 21;
  private static final String BASE_URI_NOT_SET_MSG = "Base uri not set.";
  public static final int INITIALIZATION = 22;
  private static final String INITIALIZATION_MSG = "Initialization error.";
  public static final int SIGNATURE_NOT_FOUND = 23;
  private static final String SIGNATURE_NOT_FOUND_MSG = "Signature not found.";
  public static final int ZIP_FAILED = 24;
  private static final String ZIP_FAILED_MSG = "Failed to zip string.";
  public static final int TRIAL_ERROR = 25;
  private static final String TRIAL_ERROR_MSG = "Error setting trial code.";
  public static final int PROVIDER_VALIDATION_ERROR = 26;
  private static final String PROVIDER_VALIDATION_ERROR_MSG = "This provider is for validation only.";
  public static final int MANIFEST_EMPTY_ID = 27;
  private static final String MANIFEST_EMPTY_ID_MSG = "Empty manifest id.";
  public static final int VALIDATE_NOT_CALLED = 28;
  private static final String VALIDATE_NOT_CALLED_MSG = "Validate not called to initialize value.";
  public static final int ERROR_ADDING_OBJECT = 29;
  private static final String ERROR_ADDING_OBJECT_MSG = "Error adding object.";
  public static final int INVALID_CERTIFICATE_BLOB = 30;
  private static final String INVALID_CERTIFICATE_BLOB_MSG = "Invalid certificate blob.";
  public static final int DIGEST_REFERENCE_ERROR = 31;
  private static final String DIGEST_REFERENCE_ERROR_MSG = "Invalid digest reference.";
  public static final int CERTIFICATE_NOT_FOUND = 32;
  private static final String CERTIFICATE_NOT_FOUND_MSG = "Certificate not found.";
  public static final int CERTIFICATE_NOT_VALID = 33;
  private static final String CERTIFICATE_NOT_VALID_MSG = "Certificate not valid.";
  public static final int CRLDP_CERTIFICATE_NOT_VALID = 34;
  private static final String CRLDP_CERTIFICATE_NOT_VALID_MSG = "Certificate's CRL not valid.";
  public static final int CRL_SERIALNUMBER_ERROR = 35;
  private static final String CRL_SERIALNUMBER_ERROR_MSG = "CRL Serial number error.";
  public static final int INTERNAL_ERROR = 36;
  private static final String INTERNAL_ERROR_MSG = "Internal error.";
  public static final int EXTERNAL_CLASSES_MISSING = 37;
  private static final String EXTERNAL_CLASSES_MISSING_MSG = "External classes missing.";
  private int type = -1;
  private static Hashtable mapping = new Hashtable();

  public ESignDocException(int paramInt, String paramString)
  {
    this(paramInt, paramString, null);
  }

  public ESignDocException(int paramInt)
  {
    this(paramInt, null, null);
  }

  public ESignDocException(int paramInt, Exception paramException)
  {
    this(paramInt, null, paramException);
  }

  public ESignDocException(int paramInt, String paramString, Exception paramException)
  {
    super((String)mapping.get(new Integer(paramInt)) + (paramString != null ? ": " + paramString : "") + (paramException != null ? " (" + paramException.getClass().getName() + (paramException.getMessage() != null ? ": " + paramException.getMessage() : "") + ")" : ""));
    if (paramException != null)
      setStackTrace(paramException.getStackTrace());
    this.type = paramInt;
  }

  public int getErrorType()
  {
    return this.type;
  }

  public String getErrorText()
  {
    return (String)mapping.get(new Integer(this.type));
  }

  static
  {
    mapping.put(new Integer(0), "No certificate was found");
    mapping.put(new Integer(1), "No private key was found");
    mapping.put(new Integer(2), "XPath did not produce any results");
    mapping.put(new Integer(3), "Invalid arguments");
    mapping.put(new Integer(4), "Algorithm not found or supported");
    mapping.put(new Integer(5), "Serialization failed");
    mapping.put(new Integer(6), "Initialization method was not called");
    mapping.put(new Integer(7), "Method not implemented");
    mapping.put(new Integer(8), "User aborted signing");
    mapping.put(new Integer(9), "Invalid number of signatures found.");
    mapping.put(new Integer(10), "Signature is not valid.");
    mapping.put(new Integer(11), "Certificate subject mismatch.");
    mapping.put(new Integer(12), "Cannot get main window focus.");
    mapping.put(new Integer(13), "Index out of bounds.");
    mapping.put(new Integer(14), "Wrong base element");
    mapping.put(new Integer(15), "Error while synchronizing data: ");
    mapping.put(new Integer(16), "Error creating DOM Document.");
    mapping.put(new Integer(17), "Unknown node.");
    mapping.put(new Integer(18), "Invalid xml data.");
    mapping.put(new Integer(19), "Parent node not set.");
    mapping.put(new Integer(20), "Soap call failed.");
    mapping.put(new Integer(21), "Base uri not set.");
    mapping.put(new Integer(22), "Initialization error.");
    mapping.put(new Integer(23), "Signature not found.");
    mapping.put(new Integer(24), "Failed to zip string.");
    mapping.put(new Integer(25), "Error setting trial code.");
    mapping.put(new Integer(26), "This provider is for validation only.");
    mapping.put(new Integer(27), "Empty manifest id.");
    mapping.put(new Integer(28), "Validate not called to initialize value.");
    mapping.put(new Integer(29), "Error adding object.");
    mapping.put(new Integer(30), "Invalid certificate blob.");
    mapping.put(new Integer(31), "Invalid digest reference.");
    mapping.put(new Integer(32), "Certificate not found.");
    mapping.put(new Integer(33), "Certificate not valid.");
    mapping.put(new Integer(34), "Certificate's CRL not valid.");
    mapping.put(new Integer(35), "CRL Serial number error.");
    mapping.put(new Integer(36), "Internal error.");
    mapping.put(new Integer(37), "External classes missing.");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ESignDocException
 * JD-Core Version:    0.6.0
 */