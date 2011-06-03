package org.apache.xml.security.c14n;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class CanonicalizerSpi
{
  protected boolean reset = false;

  public byte[] engineCanonicalize(byte[] paramArrayOfByte)
    throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    InputSource localInputSource = new InputSource(localByteArrayInputStream);
    DocumentBuilderFactory localDocumentBuilderFactory;
    (localDocumentBuilderFactory = DocumentBuilderFactoryImpl.newInstance()).setNamespaceAware(true);
    DocumentBuilder localDocumentBuilder;
    Document localDocument = (localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder()).parse(localInputSource);
    byte[] arrayOfByte;
    return arrayOfByte = engineCanonicalizeSubTree(localDocument);
  }

  public byte[] engineCanonicalizeXPathNodeSet(NodeList paramNodeList)
    throws CanonicalizationException
  {
    return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(paramNodeList));
  }

  public byte[] engineCanonicalizeXPathNodeSet(NodeList paramNodeList, String paramString)
    throws CanonicalizationException
  {
    return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(paramNodeList), paramString);
  }

  public abstract String engineGetURI();

  public abstract boolean engineGetIncludeComments();

  public abstract byte[] engineCanonicalizeXPathNodeSet(Set paramSet)
    throws CanonicalizationException;

  public abstract byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
    throws CanonicalizationException;

  public abstract byte[] engineCanonicalizeSubTree(Node paramNode)
    throws CanonicalizationException;

  public abstract byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
    throws CanonicalizationException;

  public abstract void setWriter(OutputStream paramOutputStream);
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.c14n.CanonicalizerSpi
 * JD-Core Version:    0.6.0
 */