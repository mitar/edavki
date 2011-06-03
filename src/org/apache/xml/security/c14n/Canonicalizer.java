package org.apache.xml.security.c14n;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.exceptions.AlgorithmAlreadyRegisteredException;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Canonicalizer
{
  public static final String ENCODING = "UTF8";
  public static final String XPATH_C14N_WITH_COMMENTS_SINGLE_NODE = "(.//. | .//@* | .//namespace::*)";
  public static final String ALGO_ID_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
  public static final String ALGO_ID_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
  public static final String ALGO_ID_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
  public static final String ALGO_ID_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
  static boolean _alreadyInitialized = false;
  static Map _canonicalizerHash = null;
  protected CanonicalizerSpi canonicalizerSpi = null;

  public static void init()
  {
    if (!_alreadyInitialized)
    {
      _canonicalizerHash = new HashMap(10);
      _alreadyInitialized = true;
    }
  }

  private Canonicalizer(String paramString)
    throws InvalidCanonicalizerException
  {
    try
    {
      String str = getImplementingClass(paramString);
      this.canonicalizerSpi = ((CanonicalizerSpi)Class.forName(str).newInstance());
      this.canonicalizerSpi.reset = true;
      return;
    }
    catch (Exception localException)
    {
      Object[] arrayOfObject = { paramString };
      throw new InvalidCanonicalizerException("signature.Canonicalizer.UnknownCanonicalizer", arrayOfObject);
    }
  }

  public static final Canonicalizer getInstance(String paramString)
    throws InvalidCanonicalizerException
  {
    Canonicalizer localCanonicalizer;
    return localCanonicalizer = new Canonicalizer(paramString);
  }

  public static void register(String paramString1, String paramString2)
    throws AlgorithmAlreadyRegisteredException
  {
    String str;
    if (((str = getImplementingClass(paramString1)) != null) && (str.length() != 0))
    {
      Object[] arrayOfObject = { paramString1, str };
      throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
    }
    _canonicalizerHash.put(paramString1, paramString2);
  }

  public final String getURI()
  {
    return this.canonicalizerSpi.engineGetURI();
  }

  public boolean getIncludeComments()
  {
    return this.canonicalizerSpi.engineGetIncludeComments();
  }

  public byte[] canonicalize(byte[] paramArrayOfByte)
    throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    InputSource localInputSource = new InputSource(localByteArrayInputStream);
    DocumentBuilderFactory localDocumentBuilderFactory;
    (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setNamespaceAware(true);
    localDocumentBuilderFactory.setValidating(true);
    DocumentBuilder localDocumentBuilder;
    (localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder()).setErrorHandler(new IgnoreAllErrorHandler());
    Document localDocument = localDocumentBuilder.parse(localInputSource);
    byte[] arrayOfByte;
    return arrayOfByte = canonicalizeSubtree(localDocument);
  }

  public byte[] canonicalizeSubtree(Node paramNode)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeSubTree(paramNode);
  }

  public byte[] canonicalizeSubtree(Node paramNode, String paramString)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeSubTree(paramNode, paramString);
  }

  public byte[] canonicalizeXPathNodeSet(NodeList paramNodeList)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramNodeList);
  }

  public byte[] canonicalizeXPathNodeSet(NodeList paramNodeList, String paramString)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramNodeList, paramString);
  }

  public byte[] canonicalizeXPathNodeSet(Set paramSet)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramSet);
  }

  public byte[] canonicalizeXPathNodeSet(Set paramSet, String paramString)
    throws CanonicalizationException
  {
    return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramSet, paramString);
  }

  public void setWriter(OutputStream paramOutputStream)
  {
    this.canonicalizerSpi.setWriter(paramOutputStream);
  }

  public String getImplementingCanonicalizerClass()
  {
    return this.canonicalizerSpi.getClass().getName();
  }

  private static String getImplementingClass(String paramString)
  {
    Iterator localIterator = _canonicalizerHash.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str;
      if ((str = (String)localIterator.next()).equals(paramString))
        return (String)_canonicalizerHash.get(str);
    }
    return null;
  }

  public void notReset()
  {
    this.canonicalizerSpi.reset = false;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.Canonicalizer
 * JD-Core Version:    0.6.0
 */