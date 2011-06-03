package si.hermes.security;

public final class HslConstants
{
  public static final int TransformAlgorithm_CanonicalXml = 1;
  public static final int TransformAlgorithm_CanonicalXmlWithComments = 2;
  public static final int TransformAlgorithm_XPathFiltering = 3;
  public static final int TransformAlgorithm_EnvelopedSignature = 4;
  public static final int TransformAlgorithm_XSLTTransform = 5;
  public static final int TransformAlgorithm_Base64Transform = 6;
  public static final String NS_DIGSIG = "http://www.w3.org/2000/09/xmldsig#";
  public static final String NS_XADES = "http://uri.etsi.org/01903/v1.1.1#";
  public static final String NS_ENTRUST = "http://www.entrust.com/schemas/timestamp-protocol-20020207";
  public static final String NS_HERMES = "urn:schemas-hermes-softlab-com:2003/09/Signatures";
  public static final String SELECTION_DIGSIG = "xmlns:dsig=\"http://www.w3.org/2000/09/xmldsig#\"";
  public static final String SELECTION_HSL_DIGSIG = "xmlns:dsig=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:hslsig=\"urn:schemas-hermes-softlab-com:2003/09/Signatures\"";
  public static final String XML_DIGEST_SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
  public static final String XML_SIGN_RSA = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
  public static final String XML_SIGN_DSA = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
  public static final String XML_CANONIZATION_C14N = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String XML_CANONIZATION_WITHCOM = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String XML_TRANSFORM_ENV = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
  public static final String XML_TRANSFORM_XSLT = "http://www.w3.org/TR/1999/REC-xslt-19991116";
  public static final String XML_TRANSFORM_XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
  public static final String XML_TRANSFORM_B64 = "http://www.w3.org/2000/09/xmldsig#base64";
  public static final String SIGNATURE_UNKNOWN = "SIGNATURE UNKNOWN";
  public static final String SIGNATURE_CREDENTIALMISSING = "CREDENTIALS NOT FOUND";
  public static final String SIGNATURE_CREDENTIALINVALID = "CREDENTIALS ARE INVALID";
  public static final String SIGNATURE_CREDENTIALDIGESTINVALID = "CREDENTIALS DIGEST IS INVALID";
  public static final String SIGNATURE_CREDENTIALVALID = "VALID CREDENTIALS";
  public static final String SIGNATURE_EMPTY = "EMPTY";
  public static final String SIGNATURE_INVALID = "INVALID";
  public static final String SIGNATURE_VALID = "VALID";
  public static final String UNKNOWN_DATA = "UNKNOWN";
  public static final String STRUCTURE_INVALID = "STRUCTINVALID";
  public static final String CRL_VALID = "REVOCATION_LIST_VALID";
  public static final String CRL_INVALID = "REVOCATION_LIST_INVALID";
  public static final String CRL_NOT_FOUND = "REVOCATION_LIST_NOT_FOUND";
  public static final String CRL_DIGEST_INVALID = "REVOCATION_LIST_DIGEST_INVALID";
  public static final String TS_DATE_NOT_FOUND = "TIMESTAMP_DATE_NOT_FOUND";
  public static final String TS_DIGEST_NOT_FOUND = "TIMESTAMP_DIGEST_NOT_FOUND";
  public static final String TS_DIGEST_INVALID = "TIMESTAMP_DIGEST_INVALID";
  public static final String TS_SIGNATURE_NOT_FOUND = "TIMESTAMP_SIGNATURE_NOT_FOUND";
  public static final String REFS_EMPTY = "CERTIFICATE_REFERENCES_EMPTY";
  public static final String REFS_NOT_VALID = "REFERENCES_NOT_VALID";
  public static final String VALUES_EMPTY = "CERTIFICATE_VALUES_EMPTY";
  public static final String VALUES_NOT_VALID = "VALUES_NOT_VALID";
  public static final String UNKNOWN_ROLE = "UNKNOWN";
  public static final String NUBER_OF_REVOCATION_LIST_INCORRECT = "NUBER_OF_REVOCATION_LIST_INCORRECT";
  public static final String NUMBER_OF_CERTIFICATES_INCORRECT = "NUMBER_OF_CERTIFICATES_INCORRECT";
  public static final String DISPLAY_TRANSFORM_NOT_SIGNED = "DISPLAY_TRANSFORM_NOT_SIGNED";
  public static final String _TAG_XADES_TIMESTAMP = "SignatureTimeStamp";
  public static final String _TAG_XADES_HASHDATAINFO = "HashDataInfo";
  public static final String _TAG_XADES_ENCAPSULATED_TIMESTAMP = "EncapsulatedTimeStamp";
  public static final String _TAG_XADES_XMLTIMESTAMP = "XMLTimeStamp";
  public static final String _TAG_XADES_OBJECT = "Object";
  public static final String _TAG_XADES_TIMESTAMPINFO = "TimeStampInfo";
  public static final String _TAG_XADES_CREATIONTIME = "CreationTime";
  public static final String _ATTR_XADES_URI = "uri";
  public static final String _TAG_XADES_QUALIFYING_PROPERTIES = "QualifyingProperties";
  public static final String _TAG_XADES_UNSIGNED_PROPERTIES = "UnsignedProperties";
  public static final String _TAG_XADES_UNSIGNED_SIGNATURE_PROPERTIES = "UnsignedSignatureProperties";
  public static final String _TAG_XADES_COUNTER_SIGNATURE = "CounterSignature";
  public static final String _TAG_XADES_CRLREFS = "CRLRefs";
  public static final String _TAG_XADES_CRLREF = "CRLRef";
  public static final String _TAG_XADES_CERTREFS = "CertRefs";
  public static final String _TAG_XADES_CERT = "Cert";
  public static final String _TAG_XADES_REFSONLYTIMESTAMP = "RefsOnlyTimeStamp";
  public static final String _TAG_XADES_SIGNATURE_TIMESTAMP = "SignatureTimeStamp";
  public static final String _TAG_XADES_COMPLETECERTIFICATEREFS = "CompleteCertificateRefs";
  public static final String _TAG_XADES_COMPLETEREVOCATIONREFS = "CompleteRevocationRefs";
  public static final String _TAG_XADES_SIGANDREFSTIMESTAMP = "SigAndRefsTimeStamp";
  public static final String _TAG_XADES_CERTIFICATEVALUES = "CertificateValues";
  public static final String _TAG_XADES_ENCAPSULATEDX509CERTIFICATE = "EncapsulatedX509Certificate";
  public static final String _TAG_XADES_CRLVALUES = "CRLValues";
  public static final String _TAG_XADES_ENCAPSULATEDCRLVALUE = "EncapsulatedCRLValue";
  public static final String _TAG_XADES_SIGNINGCERTIFICATE = "SigningCertificate";
  public static final String _TAG_XADES_ARCHIVETIMESTAMP = "ArchiveTimeStamp";
  public static final String _TAG_XADES_REVOCATIONVALUES = "RevocationValues";
  public static final String _TAG_XADES_SIGNEDDATAOBJECTPROPERTIES = "SignedDataObjectProperties";
  public static final String _TAG_XADES_DATAOBJECTFORMAT = "DataObjectFormat";
  public static final int XAdES = 0;
  public static final int XAdES_T = 1;
  public static final int XAdES_C = 2;
  public static final int XAdES_X = 3;
  public static final int XAdES_X_L = 4;
  public static final int XAdES_A = 5;
  public static final String _TAG_ENVELOPE = "Envelope";
  public static final String TIMESTAMPSERVER_SLO_SIGOV = "https://ts.si-tsa.sigov.si/verificationserver/timestamp";
  public static final String TIMESTAMPSERVER_SLO_SIGOVTEST = "http://tsa-test.gov.si/verificationserver/timestamp";
  public static final String TIMESTAMPSERVER_SLO_POSTARCA = "https://tsa.posta.si/CasovniZig.asmx";
  public static final String TIMESTAMPSERVER_TEST = "TEST";
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.HslConstants
 * JD-Core Version:    0.6.0
 */