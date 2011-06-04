package si.hermes.security;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JDialog;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xml.security.Init;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Manifest;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.ReferenceNotInitializedException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import si.hermes.security.Collections.IReference;
import si.hermes.security.Collections.ReferenceImpl;
import si.hermes.security.XAdES.EXAdESException;

public final class Utility
{
  private static final String LANGUAGE_FILENAME = "resource/MessagesBundle";
  private static Locale locale = Locale.getDefault();
  private static Properties properties = null;
  private static final String[] supportedLanguages = { "sl", "en", "de", "hr", "bs-Latn-BA", "sr-SP-Latn", "sr-Latn-CS" };
  private static Random random;

  private static byte[] calculateDigest(Reference paramReference)
    throws ReferenceNotInitializedException, XMLSignatureException
  {
    try
    {
      byte[] arrayOfByte1 = paramReference.getReferencedBytes();
      MessageDigestAlgorithm localMessageDigestAlgorithm;
      (localMessageDigestAlgorithm = paramReference.getMessageDigestAlgorithm()).reset();
      localMessageDigestAlgorithm.update(arrayOfByte1);
      byte[] arrayOfByte2;
      return arrayOfByte2 = localMessageDigestAlgorithm.digest();
    }
    catch (XMLSecurityException localXMLSecurityException)
    {
    }
    throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
  }

  public static byte[] calculateDigest(IReference paramIReference)
    throws ESignDocException, XMLSecurityException
  {
    Element localElement1;
    Element localElement2;
    (localElement2 = CreateElement((localElement1 = ((ReferenceImpl)paramIReference).GetXml()).getOwnerDocument(), "Manifest", "http://www.w3.org/2000/09/xmldsig#")).appendChild(localElement1);
    Manifest localManifest;
    Reference localReference;
    return calculateDigest(localReference = (localManifest = new Manifest(localElement2, "")).item(0));
  }

  public static byte[] calculateDigest(String paramString1, String paramString2)
    throws ESignDocException
  {
    return calculateDigest(paramString1.getBytes(), paramString2);
  }

  public static byte[] calculateDigest(byte[] paramArrayOfByte, String paramString)
    throws ESignDocException
  {
    ReferenceImpl localReferenceImpl;
    (localReferenceImpl = new ReferenceImpl()).setDigestMethod(paramString);
    String str = "tmp.txt";
    localReferenceImpl.setUri(str);
    try
    {
      Element localElement1;
      Element localElement2;
      (localElement2 = CreateElement((localElement1 = localReferenceImpl.GetXml(null)).getOwnerDocument(), "Manifest", "http://www.w3.org/2000/09/xmldsig#")).appendChild(localElement1);
      Manifest localManifest = new Manifest(localElement2, "");
      ResolverEntityInfo localResolverEntityInfo;
      (localResolverEntityInfo = new ResolverEntityInfo()).getResolver().setBaseURI("");
      localResolverEntityInfo.getResolver().setEntity(str, paramArrayOfByte);
      localManifest.addResourceResolver(localResolverEntityInfo);
      Reference localReference;
      return calculateDigest(localReference = localManifest.item(0));
    }
    catch (Exception localException)
    {
    }
    throw new ESignDocException(36, "Error calculation digest", localException);
  }

  public static void centerDialog(JDialog paramJDialog)
  {
    int i = Toolkit.getDefaultToolkit().getScreenSize().height;
    int j = Toolkit.getDefaultToolkit().getScreenSize().width;
    int k = paramJDialog.getSize().height;
    int m = paramJDialog.getSize().width;
    paramJDialog.setLocation(j / 2 - m / 2, i / 2 - k / 2);
  }

  public static java.security.cert.X509Certificate convertToJavaCert(org.mozilla.jss.crypto.X509Certificate paramX509Certificate)
    throws CertificateException
  {
    CertificateFactory localCertificateFactory;
    return (java.security.cert.X509Certificate)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCertificate(new ByteArrayInputStream(paramX509Certificate.getEncoded()));
  }

  // ERROR //
  public static String createB64StringFromDomDocument(Document paramDocument, String paramString)
    throws ESignDocException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 69	org/apache/xml/serialize/OutputFormat
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 101	org/apache/xml/serialize/OutputFormat:<init>	(Lorg/w3c/dom/Document;)V
    //   12: dup
    //   13: astore 4
    //   15: aload_1
    //   16: invokevirtual 220	org/apache/xml/serialize/OutputFormat:setEncoding	(Ljava/lang/String;)V
    //   19: aload 4
    //   21: iconst_1
    //   22: invokevirtual 226	org/apache/xml/serialize/OutputFormat:setPreserveSpace	(Z)V
    //   25: new 6	java/io/ByteArrayOutputStream
    //   28: dup
    //   29: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   32: astore_2
    //   33: new 4	java/io/BufferedOutputStream
    //   36: dup
    //   37: aload_2
    //   38: invokespecial 103	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   41: astore_3
    //   42: new 70	org/apache/xml/serialize/XMLSerializer
    //   45: dup
    //   46: aload_3
    //   47: aload 4
    //   49: invokespecial 104	org/apache/xml/serialize/XMLSerializer:<init>	(Ljava/io/OutputStream;Lorg/apache/xml/serialize/OutputFormat;)V
    //   52: astore 5
    //   54: aload 5
    //   56: aload_0
    //   57: invokevirtual 217	org/apache/xml/serialize/XMLSerializer:serialize	(Lorg/w3c/dom/Document;)V
    //   60: aload_3
    //   61: invokevirtual 162	java/io/BufferedOutputStream:flush	()V
    //   64: aload_2
    //   65: invokevirtual 231	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   68: invokestatic 158	org/apache/xml/security/utils/Base64:encode	([B)Ljava/lang/String;
    //   71: astore 6
    //   73: jsr +27 -> 100
    //   76: aload 6
    //   78: areturn
    //   79: astore 6
    //   81: new 80	si/hermes/security/ESignDocException
    //   84: dup
    //   85: iconst_5
    //   86: aload 6
    //   88: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   91: athrow
    //   92: astore 7
    //   94: jsr +6 -> 100
    //   97: aload 7
    //   99: athrow
    //   100: astore 8
    //   102: aload_3
    //   103: ifnull +7 -> 110
    //   106: aload_3
    //   107: invokevirtual 142	java/io/BufferedOutputStream:close	()V
    //   110: aload_2
    //   111: ifnull +7 -> 118
    //   114: aload_2
    //   115: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   118: ret 8
    //
    // Exception table:
    //   from	to	target	type
    //   54	73	79	java/lang/Exception
    //   4	76	92	finally
    //   79	97	92	finally
  }

  // ERROR //
  public static String createB64StringFromDomDocumentUsingUTF8(Document paramDocument)
    throws ESignDocException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: new 6	java/io/ByteArrayOutputStream
    //   7: dup
    //   8: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   11: astore_1
    //   12: new 4	java/io/BufferedOutputStream
    //   15: dup
    //   16: aload_1
    //   17: invokespecial 103	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   20: astore_2
    //   21: aload_0
    //   22: invokeinterface 251 1 0
    //   27: aload_2
    //   28: iconst_1
    //   29: invokestatic 208	org/apache/xml/security/utils/XMLUtils:outputDOM	(Lorg/w3c/dom/Node;Ljava/io/OutputStream;Z)V
    //   32: aload_2
    //   33: invokevirtual 162	java/io/BufferedOutputStream:flush	()V
    //   36: aload_1
    //   37: invokevirtual 231	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   40: invokestatic 158	org/apache/xml/security/utils/Base64:encode	([B)Ljava/lang/String;
    //   43: astore_3
    //   44: jsr +24 -> 68
    //   47: aload_3
    //   48: areturn
    //   49: astore_3
    //   50: new 80	si/hermes/security/ESignDocException
    //   53: dup
    //   54: iconst_5
    //   55: aload_3
    //   56: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   59: athrow
    //   60: astore 4
    //   62: jsr +6 -> 68
    //   65: aload 4
    //   67: athrow
    //   68: astore 5
    //   70: aload_2
    //   71: ifnull +7 -> 78
    //   74: aload_2
    //   75: invokevirtual 142	java/io/BufferedOutputStream:close	()V
    //   78: aload_1
    //   79: ifnull +7 -> 86
    //   82: aload_1
    //   83: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   86: ret 5
    //
    // Exception table:
    //   from	to	target	type
    //   21	44	49	java/lang/Exception
    //   4	47	60	finally
    //   49	65	60	finally
  }

  // ERROR //
  public static byte[] createByteArrayFromDomDocument(Document paramDocument)
    throws ESignDocException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: new 69	org/apache/xml/serialize/OutputFormat
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 101	org/apache/xml/serialize/OutputFormat:<init>	(Lorg/w3c/dom/Document;)V
    //   12: dup
    //   13: astore_3
    //   14: iconst_1
    //   15: invokevirtual 226	org/apache/xml/serialize/OutputFormat:setPreserveSpace	(Z)V
    //   18: new 6	java/io/ByteArrayOutputStream
    //   21: dup
    //   22: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   25: astore_1
    //   26: new 4	java/io/BufferedOutputStream
    //   29: dup
    //   30: aload_1
    //   31: invokespecial 103	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   34: astore_2
    //   35: new 70	org/apache/xml/serialize/XMLSerializer
    //   38: dup
    //   39: aload_2
    //   40: aload_3
    //   41: invokespecial 104	org/apache/xml/serialize/XMLSerializer:<init>	(Ljava/io/OutputStream;Lorg/apache/xml/serialize/OutputFormat;)V
    //   44: astore 4
    //   46: aload 4
    //   48: aload_0
    //   49: invokevirtual 217	org/apache/xml/serialize/XMLSerializer:serialize	(Lorg/w3c/dom/Document;)V
    //   52: aload_2
    //   53: invokevirtual 162	java/io/BufferedOutputStream:flush	()V
    //   56: aload_1
    //   57: invokevirtual 231	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   60: astore 5
    //   62: jsr +27 -> 89
    //   65: aload 5
    //   67: areturn
    //   68: astore 5
    //   70: new 80	si/hermes/security/ESignDocException
    //   73: dup
    //   74: iconst_5
    //   75: aload 5
    //   77: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   80: athrow
    //   81: astore 6
    //   83: jsr +6 -> 89
    //   86: aload 6
    //   88: athrow
    //   89: astore 7
    //   91: aload_2
    //   92: ifnull +7 -> 99
    //   95: aload_2
    //   96: invokevirtual 142	java/io/BufferedOutputStream:close	()V
    //   99: aload_1
    //   100: ifnull +7 -> 107
    //   103: aload_1
    //   104: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   107: ret 7
    //
    // Exception table:
    //   from	to	target	type
    //   46	62	68	java/lang/Exception
    //   4	65	81	finally
    //   68	86	81	finally
  }

  public static Document CreateDocument()
    throws ParserConfigurationException
  {
    DocumentBuilderFactoryImpl localDocumentBuilderFactoryImpl;
    (localDocumentBuilderFactoryImpl = new DocumentBuilderFactoryImpl()).setNamespaceAware(true);
    localDocumentBuilderFactoryImpl.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
    DocumentBuilder localDocumentBuilder;
    Document localDocument;
    return localDocument = (localDocumentBuilder = localDocumentBuilderFactoryImpl.newDocumentBuilder()).newDocument();
  }

  public static Document createDomDocumentFromB64String(String paramString)
    throws FactoryConfigurationError, ParserConfigurationException, Base64DecodingException, SAXException, IOException
  {
    return createDomDocumentFromString(Base64.decode(paramString));
  }

  public static Document createDomDocumentFromString(String paramString)
    throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
  {
    return createDomDocumentFromString(paramString.getBytes("UTF-8"));
  }

  public static Document createDomDocumentFromString(byte[] paramArrayOfByte)
    throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactoryImpl localDocumentBuilderFactoryImpl;
    (localDocumentBuilderFactoryImpl = new DocumentBuilderFactoryImpl()).setNamespaceAware(true);
    localDocumentBuilderFactoryImpl.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
    DocumentBuilder localDocumentBuilder = localDocumentBuilderFactoryImpl.newDocumentBuilder();
    IgnoreAllErrorHandler localIgnoreAllErrorHandler = new IgnoreAllErrorHandler();
    localDocumentBuilder.setErrorHandler(localIgnoreAllErrorHandler);
    Document localDocument = null;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    return localDocument = localDocumentBuilder.parse(new InputSource(localByteArrayInputStream));
  }

  public static Document createDomDocumentFromStream(InputStream paramInputStream)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactoryImpl localDocumentBuilderFactoryImpl;
    (localDocumentBuilderFactoryImpl = new DocumentBuilderFactoryImpl()).setNamespaceAware(true);
    localDocumentBuilderFactoryImpl.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
    DocumentBuilder localDocumentBuilder = localDocumentBuilderFactoryImpl.newDocumentBuilder();
    IgnoreAllErrorHandler localIgnoreAllErrorHandler = new IgnoreAllErrorHandler();
    localDocumentBuilder.setErrorHandler(localIgnoreAllErrorHandler);
    return localDocumentBuilder.parse(new InputSource(paramInputStream));
  }

  public static Element CreateElement(Document paramDocument, String paramString)
  {
    return paramDocument.createElement(paramString);
  }

  public static Element CreateElement(Document paramDocument, String paramString1, String paramString2)
  {
    Element localElement;
    (localElement = paramDocument.createElementNS(paramString2, paramString1)).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", paramString2);
    return localElement;
  }

  public static Element CreateElement(String paramString)
    throws ParserConfigurationException, ESignDocException
  {
    return CreateElement(paramString, null);
  }

  public static Element CreateElement(String paramString1, String paramString2)
    throws ParserConfigurationException, ESignDocException
  {
    Document localDocument;
    if ((localDocument = CreateDocument()) != null)
    {
      if (paramString2 == null)
        return localDocument.createElement(paramString1);
      return CreateElement(localDocument, paramString1, paramString2);
    }
    throw new ESignDocException(16);
  }

  public static void createFileFromDomDocument(Document paramDocument, String paramString1, String paramString2)
    throws ESignDocException, IOException
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      OutputFormat localOutputFormat;
      (localOutputFormat = new OutputFormat(paramDocument)).setPreserveSpace(true);
      localOutputFormat.setEncoding(paramString2);
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString1));
      XMLSerializer localXMLSerializer = new XMLSerializer(localBufferedOutputStream, localOutputFormat);
      try
      {
        localXMLSerializer.serialize(paramDocument);
        localBufferedOutputStream.flush();
      }
      catch (Exception localException)
      {
        throw new ESignDocException(5, localException);
      }
      localBufferedOutputStream.close();
      return;
    }
    finally
    {
      if (localBufferedOutputStream != null)
        localBufferedOutputStream.close();
    }
  }

  public static InputStream createInputStreamFromFile(String paramString)
    throws FileNotFoundException
  {
    return new BufferedInputStream(new FileInputStream(paramString));
  }

  // ERROR //
  public static String createStringFromDomDocument(Document paramDocument)
    throws ESignDocException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: new 69	org/apache/xml/serialize/OutputFormat
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 101	org/apache/xml/serialize/OutputFormat:<init>	(Lorg/w3c/dom/Document;)V
    //   12: dup
    //   13: astore_3
    //   14: iconst_1
    //   15: invokevirtual 226	org/apache/xml/serialize/OutputFormat:setPreserveSpace	(Z)V
    //   18: aload_3
    //   19: ldc_w 293
    //   22: invokevirtual 220	org/apache/xml/serialize/OutputFormat:setEncoding	(Ljava/lang/String;)V
    //   25: new 6	java/io/ByteArrayOutputStream
    //   28: dup
    //   29: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   32: astore_1
    //   33: new 4	java/io/BufferedOutputStream
    //   36: dup
    //   37: aload_1
    //   38: invokespecial 103	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   41: astore_2
    //   42: new 70	org/apache/xml/serialize/XMLSerializer
    //   45: dup
    //   46: aload_2
    //   47: aload_3
    //   48: invokespecial 104	org/apache/xml/serialize/XMLSerializer:<init>	(Ljava/io/OutputStream;Lorg/apache/xml/serialize/OutputFormat;)V
    //   51: astore 4
    //   53: aload 4
    //   55: aload_0
    //   56: invokevirtual 217	org/apache/xml/serialize/XMLSerializer:serialize	(Lorg/w3c/dom/Document;)V
    //   59: aload_2
    //   60: invokevirtual 162	java/io/BufferedOutputStream:flush	()V
    //   63: aload_1
    //   64: ldc_w 293
    //   67: invokevirtual 232	java/io/ByteArrayOutputStream:toString	(Ljava/lang/String;)Ljava/lang/String;
    //   70: astore 5
    //   72: jsr +27 -> 99
    //   75: aload 5
    //   77: areturn
    //   78: astore 5
    //   80: new 80	si/hermes/security/ESignDocException
    //   83: dup
    //   84: iconst_5
    //   85: aload 5
    //   87: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   90: athrow
    //   91: astore 6
    //   93: jsr +6 -> 99
    //   96: aload 6
    //   98: athrow
    //   99: astore 7
    //   101: aload_2
    //   102: ifnull +7 -> 109
    //   105: aload_2
    //   106: invokevirtual 142	java/io/BufferedOutputStream:close	()V
    //   109: aload_1
    //   110: ifnull +7 -> 117
    //   113: aload_1
    //   114: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   117: ret 7
    //
    // Exception table:
    //   from	to	target	type
    //   53	72	78	java/lang/Exception
    //   4	75	91	finally
    //   78	96	91	finally
  }

  // ERROR //
  public static String createStringFromDomElement(Element paramElement)
    throws ESignDocException, IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: new 69	org/apache/xml/serialize/OutputFormat
    //   7: dup
    //   8: aload_0
    //   9: invokeinterface 262 1 0
    //   14: invokespecial 101	org/apache/xml/serialize/OutputFormat:<init>	(Lorg/w3c/dom/Document;)V
    //   17: dup
    //   18: astore_3
    //   19: iconst_1
    //   20: invokevirtual 226	org/apache/xml/serialize/OutputFormat:setPreserveSpace	(Z)V
    //   23: new 6	java/io/ByteArrayOutputStream
    //   26: dup
    //   27: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   30: astore_1
    //   31: new 4	java/io/BufferedOutputStream
    //   34: dup
    //   35: aload_1
    //   36: invokespecial 103	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   39: astore_2
    //   40: new 70	org/apache/xml/serialize/XMLSerializer
    //   43: dup
    //   44: aload_2
    //   45: aload_3
    //   46: invokespecial 104	org/apache/xml/serialize/XMLSerializer:<init>	(Ljava/io/OutputStream;Lorg/apache/xml/serialize/OutputFormat;)V
    //   49: astore 4
    //   51: aload 4
    //   53: iconst_1
    //   54: invokevirtual 225	org/apache/xml/serialize/XMLSerializer:setNamespaces	(Z)V
    //   57: aload 4
    //   59: aload_0
    //   60: invokevirtual 218	org/apache/xml/serialize/XMLSerializer:serialize	(Lorg/w3c/dom/Element;)V
    //   63: aload_2
    //   64: invokevirtual 162	java/io/BufferedOutputStream:flush	()V
    //   67: aload_1
    //   68: ldc_w 293
    //   71: invokevirtual 232	java/io/ByteArrayOutputStream:toString	(Ljava/lang/String;)Ljava/lang/String;
    //   74: astore 5
    //   76: jsr +27 -> 103
    //   79: aload 5
    //   81: areturn
    //   82: astore 5
    //   84: new 80	si/hermes/security/ESignDocException
    //   87: dup
    //   88: iconst_5
    //   89: aload 5
    //   91: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   94: athrow
    //   95: astore 6
    //   97: jsr +6 -> 103
    //   100: aload 6
    //   102: athrow
    //   103: astore 7
    //   105: aload_2
    //   106: ifnull +7 -> 113
    //   109: aload_2
    //   110: invokevirtual 142	java/io/BufferedOutputStream:close	()V
    //   113: aload_1
    //   114: ifnull +7 -> 121
    //   117: aload_1
    //   118: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   121: ret 7
    //
    // Exception table:
    //   from	to	target	type
    //   51	76	82	java/lang/Exception
    //   4	79	95	finally
    //   82	100	95	finally
  }

  public static String createStringFromDomElement(Element paramElement, boolean paramBoolean)
    throws ESignDocException, IOException
  {
    String str = createStringFromDomElement(paramElement);
    if (paramBoolean)
    {
      int i = str.indexOf(">");
      str = str.substring(i + 1);
    }
    return str;
  }

  public static String extractFromDistinguishedName(String paramString1, String paramString2)
  {
    String str = null;
    StringBuffer localStringBuffer = new StringBuffer();
    int j = paramString2.length();
    int k = (str = paramString1 + "=").length();
    int i;
    if ((i = paramString2.indexOf(str)) != -1)
      for (int m = i + k; (m < j) && (paramString2.charAt(m) != ','); m++)
        localStringBuffer.append(paramString2.charAt(m));
    return localStringBuffer.toString();
  }

  public static String generateSecureRandom(int paramInt, String paramString)
    throws NoSuchAlgorithmException
  {
    SecureRandom localSecureRandom = SecureRandom.getInstance(paramString == null ? "SHA1PRNG" : paramString);
    String str = "";
    for (int i = 0; i < paramInt; i++)
      str = str + String.valueOf(localSecureRandom.nextInt(8));
    return str;
  }

  public static String generateRandomString(int paramInt, String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
    SecureRandom localSecureRandom = SecureRandom.getInstance(paramString2 == null ? "SHA1PRNG" : paramString2);
    String str = "";
    byte[] arrayOfByte = new byte[paramInt];
    localSecureRandom.nextBytes(arrayOfByte);
    for (int i = 0; i < paramInt; i++)
    {
      int j = Math.abs(arrayOfByte[i] % paramString1.length());
      str = str + paramString1.substring(j, j + 1);
    }
    return str;
  }

  public static String generateRandom(int paramInt)
  {
    String str;
    int i = Math.abs(random.nextInt() % 10);
    return str;
  }

  public static String getFragmentURI(String paramString)
  {
    String str = null;
    if ((paramString.length() >= 2) && (paramString.charAt(0) == '#'))
      str = paramString.substring(1, paramString.length());
    return str;
  }

  public static String getHexString(BigInteger paramBigInteger)
  {
    return paramBigInteger.toString(16);
  }

  public static Locale getLocale()
  {
    return locale;
  }

  public static String getLocalizedMessage(String paramString)
  {
    if (properties == null)
    {
      System.out.println("Warning: resource string " + paramString + " not found");
      return paramString;
    }
    String str;
    if ((str = properties.getProperty(paramString)) == null)
    {
      System.out.println("Warning: resource string " + paramString + " not found");
      return "dummy";
    }
    return str;
  }

  public static void setLocale(String paramString)
    throws ESignDocException
  {
    try
    {
      InputStream localInputStream = null;
      String str = null;
      int i = 0;
      for (int j = 0; j < supportedLanguages.length; j++)
      {
        if (!supportedLanguages[j].equals(paramString))
          continue;
        i = 1;
        break;
      }
      str = i != 0 ? "resource/MessagesBundle_" + paramString + ".properties" : "resource/MessagesBundle.properties";
      if ((localInputStream = Class.forName("si.hermes.security.Utility").getResourceAsStream(str)) != null)
      {
        properties = new Properties();
        properties.load(localInputStream);
        localInputStream.close();
      }
      return;
    }
    catch (Exception localException)
    {
      throw new ESignDocException(4, localException);
    }
  }

  public static Element getNode2(Element paramElement, String paramString1, String paramString2)
    throws ESignDocException
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new ESignDocException(3, "missing nodename or namespace");
    if (paramElement != null)
      for (int i = 0; i < paramElement.getChildNodes().getLength(); i++)
        if ((paramString1.equals(paramElement.getChildNodes().item(i).getLocalName())) && (paramString2.equals(paramElement.getChildNodes().item(i).getNamespaceURI())))
          return (Element)paramElement.getChildNodes().item(i);
    return null;
  }

  public static Element getNodeList2(NodeList paramNodeList, String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null))
      return null;
    if (paramNodeList != null)
      for (int i = 0; i < paramNodeList.getLength(); i++)
      {
        Node localNode = paramNodeList.item(i);
        if ((paramString1.equals(localNode.getLocalName())) && (paramString2.equals(localNode.getNamespaceURI())))
          return (Element)localNode;
      }
    return null;
  }

  protected static String getNodeValue(Document paramDocument, String paramString)
  {
    if (paramDocument == null)
      return null;
    NodeList localNodeList;
    if (((localNodeList = paramDocument.getElementsByTagName(paramString)) == null) || (localNodeList.getLength() == 0))
      return null;
    if (localNodeList.item(0).getFirstChild() == null)
      return null;
    return localNodeList.item(0).getFirstChild().getNodeValue();
  }

  public static String getTextNodeValue(Element paramElement)
    throws ESignDocException
  {
    if (paramElement == null)
      throw new ESignDocException(3, "null input node");
    Node localNode = paramElement.getFirstChild();
    String str = null;
    while (localNode != null)
    {
      if (localNode.getNodeType() == 3)
        str = str + localNode.getNodeValue();
      localNode = localNode.getNextSibling();
    }
    return str;
  }

  public static String hashSHA1(byte[] paramArrayOfByte)
    throws ESignDocException, Base64DecodingException
  {
    MessageDigest localMessageDigest;
    try
    {
      String str = JCEMapper.getProviderId();
      try
      {
        if (str == null)
          localMessageDigest = MessageDigest.getInstance("SHA-1");
        else
          localMessageDigest = MessageDigest.getInstance("SHA-1", str);
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        localMessageDigest = MessageDigest.getInstance("SHA-1");
      }
    }
    catch (Exception localException)
    {
      throw new ESignDocException(4, localException);
    }
    localMessageDigest.update(paramArrayOfByte);
    byte[] arrayOfByte;
    return Base64.encode(arrayOfByte = localMessageDigest.digest());
  }

  public static void setTextNodeValue(Element paramElement, String paramString)
    throws ESignDocException
  {
    if (paramElement == null)
      throw new ESignDocException(3, "invalid input parameter - node");
    Object localObject;
    paramElement.removeChild((Node)localObject);
    if (paramString != null)
    {
      localObject = paramElement.getOwnerDocument().createTextNode(paramString);
      paramElement.appendChild((Node)localObject);
    }
  }

  protected static String transform(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString)
    throws TransformerException, IOException, ESignDocException
  {
    ByteArrayInputStream localByteArrayInputStream1 = null;
    BufferedInputStream localBufferedInputStream1 = null;
    ByteArrayInputStream localByteArrayInputStream2 = null;
    BufferedInputStream localBufferedInputStream2 = null;
    ByteArrayOutputStream localByteArrayOutputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localByteArrayInputStream1 = new ByteArrayInputStream(paramArrayOfByte1);
      localBufferedInputStream1 = new BufferedInputStream(localByteArrayInputStream1);
      localByteArrayInputStream2 = new ByteArrayInputStream(paramArrayOfByte2);
      localBufferedInputStream2 = new BufferedInputStream(localByteArrayInputStream2);
      localByteArrayOutputStream = new ByteArrayOutputStream();
      localBufferedOutputStream = new BufferedOutputStream(localByteArrayOutputStream);
      TransformerFactoryImpl localTransformerFactoryImpl = new TransformerFactoryImpl();
      SAXSource localSAXSource = null;
      Object localObject2;
      try
      {
        (localObject1 = new SAXParserFactoryImpl()).setNamespaceAware(true);
        localObject2 = ((SAXParserFactoryImpl)localObject1).newSAXParser();
        localSAXSource = new SAXSource(((SAXParser)localObject2).getXMLReader(), new InputSource(localBufferedInputStream2));
      }
      catch (Exception localException1)
      {
        (localObject1 = localException1).printStackTrace();
        throw new ESignDocException(36, (Exception)localObject1);
      }
      Object localObject1 = localTransformerFactoryImpl.newTransformer(localSAXSource);
      Object localObject3;
      try
      {
        (localObject3 = new SAXParserFactoryImpl()).setNamespaceAware(true);
        localObject4 = ((SAXParserFactoryImpl)localObject3).newSAXParser();
        localObject2 = new SAXSource(((SAXParser)localObject4).getXMLReader(), new InputSource(localBufferedInputStream1));
      }
      catch (Exception localException2)
      {
        (localObject3 = localException2).printStackTrace();
        throw new ESignDocException(36, (Exception)localObject3);
      }
      ((Transformer)localObject1).transform((Source)localObject2, new StreamResult(localBufferedOutputStream));
      localBufferedOutputStream.flush();
      localObject4 = localObject3 = paramString != null ? new String(localByteArrayOutputStream.toByteArray(), paramString) : new String(localByteArrayOutputStream.toByteArray());
    }
    finally
    {
      Object localObject4;
      if (localBufferedInputStream1 != null)
        localBufferedInputStream1.close();
      if (localByteArrayInputStream1 != null)
        localByteArrayInputStream1.close();
      if (localBufferedInputStream2 != null)
        localBufferedInputStream2.close();
      if (localByteArrayInputStream2 != null)
        localByteArrayInputStream2.close();
      if (localBufferedOutputStream != null)
        localBufferedOutputStream.close();
      if (localByteArrayOutputStream != null)
        localByteArrayOutputStream.close();
    }
  }

  public static String transform(Document paramDocument1, Document paramDocument2, String paramString)
    throws TransformerException, ParserConfigurationException, IOException, ESignDocException
  {
    ByteArrayOutputStream localByteArrayOutputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    ByteArrayInputStream localByteArrayInputStream = null;
    BufferedInputStream localBufferedInputStream = null;
    try
    {
      DOMSource localDOMSource = new DOMSource(paramDocument2);
      TransformerFactory localTransformerFactory;
      Transformer localTransformer = (localTransformerFactory = TransformerFactoryImpl.newInstance()).newTransformer(localDOMSource);
      byte[] arrayOfByte = createByteArrayFromDomDocument(paramDocument1);
      localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      localBufferedInputStream = new BufferedInputStream(localByteArrayInputStream);
      localByteArrayOutputStream = new ByteArrayOutputStream();
      localBufferedOutputStream = new BufferedOutputStream(localByteArrayOutputStream);
      SAXSource localSAXSource = null;
      try
      {
        (localObject1 = new SAXParserFactoryImpl()).setNamespaceAware(true);
        SAXParser localSAXParser = ((SAXParserFactoryImpl)localObject1).newSAXParser();
        localSAXSource = new SAXSource(localSAXParser.getXMLReader(), new InputSource(localBufferedInputStream));
      }
      catch (Exception localException)
      {
        (localObject1 = localException).printStackTrace();
        throw new ESignDocException(36, (Exception)localObject1);
      }
      localTransformer.transform(localSAXSource, new StreamResult(localBufferedOutputStream));
      localBufferedOutputStream.flush();
      if (paramString != null)
      {
        localObject1 = localByteArrayOutputStream.toString(paramString);
      }
      localObject1 = localByteArrayOutputStream.toString();
    }
    finally
    {
      Object localObject1;
      if (localBufferedOutputStream != null)
        localBufferedOutputStream.close();
      if (localBufferedInputStream != null)
        localBufferedInputStream.close();
    }
  }

  public static String zipString(String paramString1, String paramString2)
    throws ESignDocException
  {
    if ((paramString2 == null) || (paramString2 == ""))
      paramString2 = "data.xml";
    try
    {
      ZipOutputStream localZipOutputStream = null;
      try
      {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        localZipOutputStream = new ZipOutputStream(localByteArrayOutputStream);
        ZipEntry localZipEntry = new ZipEntry(paramString2);
        localZipOutputStream.putNextEntry(localZipEntry);
        localZipOutputStream.write(paramString1.getBytes());
        localZipOutputStream.closeEntry();
        localZipOutputStream.flush();
        String str1;
        str2 = str1 = Base64.encode(localByteArrayOutputStream.toByteArray());
      }
      finally
      {
        String str2;
        if (localZipOutputStream != null)
          localZipOutputStream.close();
      }
    }
    catch (Exception localException)
    {
    }
    throw new ESignDocException(24, localException);
  }

  // ERROR //
  public static String unzipString(String paramString1, String paramString2)
    throws ESignDocException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +10 -> 11
    //   4: aload_1
    //   5: ldc_w 274
    //   8: if_acmpne +7 -> 15
    //   11: ldc_w 300
    //   14: astore_1
    //   15: new 41	java/util/zip/ZipInputStream
    //   18: dup
    //   19: new 5	java/io/ByteArrayInputStream
    //   22: dup
    //   23: aload_0
    //   24: invokevirtual 168	java/lang/String:getBytes	()[B
    //   27: invokespecial 100	java/io/ByteArrayInputStream:<init>	([B)V
    //   30: invokespecial 126	java/util/zip/ZipInputStream:<init>	(Ljava/io/InputStream;)V
    //   33: astore_2
    //   34: aload_2
    //   35: invokevirtual 179	java/util/zip/ZipInputStream:getNextEntry	()Ljava/util/zip/ZipEntry;
    //   38: astore_3
    //   39: aload_3
    //   40: ifnull +111 -> 151
    //   43: aload_1
    //   44: aload_3
    //   45: invokevirtual 178	java/util/zip/ZipEntry:getName	()Ljava/lang/String;
    //   48: invokevirtual 159	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   51: ifeq +88 -> 139
    //   54: new 6	java/io/ByteArrayOutputStream
    //   57: dup
    //   58: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   61: astore 4
    //   63: sipush 8192
    //   66: newarray byte
    //   68: astore 6
    //   70: aload_2
    //   71: aload 6
    //   73: iconst_0
    //   74: sipush 8192
    //   77: invokevirtual 213	java/util/zip/ZipInputStream:read	([BII)I
    //   80: dup
    //   81: istore 5
    //   83: iconst_m1
    //   84: if_icmpeq +16 -> 100
    //   87: aload 4
    //   89: aload 6
    //   91: iconst_0
    //   92: iload 5
    //   94: invokevirtual 243	java/io/ByteArrayOutputStream:write	([BII)V
    //   97: goto -27 -> 70
    //   100: aload 4
    //   102: invokevirtual 231	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   105: invokestatic 158	org/apache/xml/security/utils/Base64:encode	([B)Ljava/lang/String;
    //   108: astore 7
    //   110: aload_2
    //   111: invokevirtual 150	java/util/zip/ZipInputStream:closeEntry	()V
    //   114: aload 4
    //   116: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   119: jsr +53 -> 172
    //   122: aload 7
    //   124: areturn
    //   125: astore 8
    //   127: aload_2
    //   128: invokevirtual 150	java/util/zip/ZipInputStream:closeEntry	()V
    //   131: aload 4
    //   133: invokevirtual 143	java/io/ByteArrayOutputStream:close	()V
    //   136: aload 8
    //   138: athrow
    //   139: aload_2
    //   140: invokevirtual 150	java/util/zip/ZipInputStream:closeEntry	()V
    //   143: aload_2
    //   144: invokevirtual 179	java/util/zip/ZipInputStream:getNextEntry	()Ljava/util/zip/ZipEntry;
    //   147: astore_3
    //   148: goto -109 -> 39
    //   151: new 80	si/hermes/security/ESignDocException
    //   154: dup
    //   155: bipush 24
    //   157: ldc_w 294
    //   160: invokespecial 116	si/hermes/security/ESignDocException:<init>	(ILjava/lang/String;)V
    //   163: athrow
    //   164: astore 9
    //   166: jsr +6 -> 172
    //   169: aload 9
    //   171: athrow
    //   172: astore 10
    //   174: aload_2
    //   175: invokevirtual 148	java/util/zip/ZipInputStream:close	()V
    //   178: ret 10
    //   180: astore_2
    //   181: new 80	si/hermes/security/ESignDocException
    //   184: dup
    //   185: bipush 24
    //   187: aload_2
    //   188: invokespecial 105	si/hermes/security/ESignDocException:<init>	(ILjava/lang/Exception;)V
    //   191: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   63	110	125	finally
    //   125	127	125	finally
    //   34	122	164	finally
    //   125	169	164	finally
    //   15	122	180	java/lang/Exception
    //   125	180	180	java/lang/Exception
  }

  public static java.security.cert.X509Certificate createCertificateFromString(String paramString)
    throws CertificateException, IOException
  {
    java.security.cert.X509Certificate localX509Certificate = null;
    byte[] arrayOfByte = !paramString.startsWith("-----BEGIN") ? ("-----BEGIN CERTIFICATE-----\n" + paramString + '\n' + "-----END CERTIFICATE-----").getBytes("UTF-8") : paramString.getBytes();
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
    CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
    localByteArrayInputStream.reset();
    Collection localCollection;
    Iterator localIterator;
    if ((localIterator = (localCollection = localCertificateFactory.generateCertificates(localByteArrayInputStream)).iterator()).hasNext())
      localX509Certificate = (java.security.cert.X509Certificate)localIterator.next();
    return localX509Certificate;
  }

  public static boolean UtilCheckCertRevocation(String paramString1, String paramString2, String paramString3, Date paramDate)
  {
    try
    {
      java.security.cert.X509Certificate localX509Certificate1 = createCertificateFromString(paramString1);
      java.security.cert.X509Certificate localX509Certificate2 = createCertificateFromString(paramString2);
      X509CRL localX509CRL;
      try
      {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Base64.decode(paramString3));
        CertificateFactory localCertificateFactory;
        localX509CRL = (X509CRL)(localCertificateFactory = CertificateFactory.getInstance("X.509")).generateCRL(localByteArrayInputStream);
        localByteArrayInputStream.close();
      }
      catch (Exception localException1)
      {
        throw new EXAdESException(3, localException1);
      }
      localX509CRL.verify(localX509Certificate2.getPublicKey());
      X509CRLEntry localX509CRLEntry;
      if ((localX509CRLEntry = localX509CRL.getRevokedCertificate(localX509Certificate1.getSerialNumber())) == null)
        return true;
      return paramDate.before(localX509CRLEntry.getRevocationDate());
    }
    catch (Exception localException2)
    {
    }
    return false;
  }

  private static int findNextChar(char[] paramArrayOfChar, String paramString)
  {
    int i = 32;
    for (int j = 0; j < paramString.length(); j++)
    {
      for (int k = 0; k < paramArrayOfChar.length; k++)
        if ((paramString.charAt(j) == paramArrayOfChar[k]) && (i != 92))
          return j;
      i = paramString.charAt(j);
    }
    return -1;
  }

  private static String ltrim(String paramString)
  {
    return paramString.replaceAll("^\\s+", "");
  }

  public static String normalizeDN(String paramString)
  {
    TreeSet localTreeSet = new TreeSet();
    if ((paramString == null) || ("".equals(paramString)))
      return "";
    paramString = (paramString = paramString.replaceAll("E=", "EMAILADDRESS=")).replaceAll("S=", "ST=");
    int i;
    String str = paramString.substring(0, i);
    localTreeSet.add(ltrim(str));
    localTreeSet.add(ltrim(paramString));
    str = "";
    Iterator localIterator = localTreeSet.iterator();
    while (localIterator.hasNext())
      str = str + (String)localIterator.next() + '\n';
    return str;
  }

  public static String getCAFromCertificateDesc(String paramString)
  {
    String str = extractFromDistinguishedName("CN", paramString);
    if ("".equals(str))
      str = extractFromDistinguishedName("OU", paramString);
    if ("".equals(str))
      str = extractFromDistinguishedName("O", paramString);
    return str;
  }

  public static String reverse(String paramString)
  {
    String str = "";
    for (int i = 0; i < paramString.length() / 2; i++)
      str = paramString.substring(2 * i, 2 * i + 2) + str;
    return str;
  }

  public static int getJavaVersion()
  {
    String[] arrayOfString;
    if ((arrayOfString = System.getProperty("java.version").split("\\.")).length < 2)
      return 0;
    return Integer.valueOf(arrayOfString[0]).intValue() * 100 + Integer.valueOf(arrayOfString[1]).intValue() * 10;
  }

  static
  {
    Object localObject;
    try
    {
      Init.init();
    }
    catch (Exception localException1)
    {
      (localObject = localException1).printStackTrace();
    }
    try
    {
      localObject = null;
      String str1 = locale.getLanguage();
      String str2 = null;
      int i = 0;
      for (int j = 0; j < supportedLanguages.length; j++)
      {
        if (!supportedLanguages[j].equals(str1))
          continue;
        i = 1;
        break;
      }
      str2 = i != 0 ? "resource/MessagesBundle_" + str1 + ".properties" : "resource/MessagesBundle.properties";
      if ((localObject = Class.forName("si.hermes.security.Utility").getResourceAsStream(str2)) != null)
      {
        properties = new Properties();
        properties.load((InputStream)localObject);
        ((InputStream)localObject).close();
      }
    }
    catch (Exception localException2)
    {
      (localObject = localException2).printStackTrace();
    }
    random = new Random();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Utility
 * JD-Core Version:    0.6.0
 */
