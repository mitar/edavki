package org.apache.xml.security.utils;

import java.math.BigInteger;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class ElementProxy
{
  static Log log = LogFactory.getLog(ElementProxy.class.getName());
  public static final int MODE_CREATE = 0;
  public static final int MODE_PROCESS = 1;
  public static final int MODE_UNKNOWN = 2;
  public static final int MODE_SIGN = 0;
  public static final int MODE_VERIFY = 1;
  public static final int MODE_ENCRYPT = 0;
  public static final int MODE_DECRYPT = 1;
  protected int _state = 2;
  protected Element _constructionElement = null;
  protected String _baseURI = null;
  protected Document _doc = null;
  static HashMap _prefixMappings = new HashMap();

  public abstract String getBaseNamespace();

  public abstract String getBaseLocalName();

  public ElementProxy()
  {
  }

  public ElementProxy(Document paramDocument)
  {
    this();
    if (paramDocument == null)
      throw new RuntimeException("Document is null");
    this._doc = paramDocument;
    this._state = 0;
    this._constructionElement = createElementForFamily(this._doc, getBaseNamespace(), getBaseLocalName());
  }

  public static Element createElementForFamily(Document paramDocument, String paramString1, String paramString2)
  {
    Element localElement = null;
    String str = getDefaultPrefix(paramString1);
    if (paramString1 == null)
    {
      localElement = paramDocument.createElementNS(null, paramString2);
    }
    else
    {
      localElement = paramDocument.createElementNS(paramString1, paramString2);
      (localElement = paramDocument.createElementNS(paramString1, str + ":" + paramString2)).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, paramString1);
    }
    return localElement;
  }

  public void setElement(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    if (paramElement == null)
      throw new XMLSecurityException("ElementProxy.nullElement");
    log.isDebugEnabled();
    if (log.isDebugEnabled())
      log.debug("setElement(" + paramElement.getTagName() + ", \"" + paramString + "\"");
    this._doc = paramElement.getOwnerDocument();
    this._state = 1;
    this._constructionElement = paramElement;
    this._baseURI = paramString;
  }

  public ElementProxy(Element paramElement, String paramString)
    throws XMLSecurityException
  {
    this();
    if (paramElement == null)
      throw new XMLSecurityException("ElementProxy.nullElement");
    if (log.isDebugEnabled())
      log.debug("setElement(\"" + paramElement.getTagName() + "\", \"" + paramString + "\")");
    this._doc = paramElement.getOwnerDocument();
    this._state = 1;
    this._constructionElement = paramElement;
    this._baseURI = paramString;
    guaranteeThatElementInCorrectSpace();
  }

  public final Element getElement()
  {
    return this._constructionElement;
  }

  public final NodeList getElementPlusReturns()
  {
    HelperNodeList localHelperNodeList;
    (localHelperNodeList = new HelperNodeList()).appendChild(this._doc.createTextNode("\n"));
    localHelperNodeList.appendChild(getElement());
    localHelperNodeList.appendChild(this._doc.createTextNode("\n"));
    return localHelperNodeList;
  }

  public Document getDocument()
  {
    return this._doc;
  }

  public String getBaseURI()
  {
    return this._baseURI;
  }

  public void guaranteeThatElementInCorrectSpace()
    throws XMLSecurityException
  {
    String str1 = getBaseLocalName();
    String str2 = getBaseNamespace();
    String str3 = this._constructionElement.getLocalName();
    String str4 = this._constructionElement.getNamespaceURI();
    if ((!str1.equals(str3)) || (!str2.equals(str4)))
    {
      Object[] arrayOfObject = { str4 + ":" + str3, str2 + ":" + str1 };
      throw new XMLSecurityException("xml.WrongElement", arrayOfObject);
    }
  }

  public void addBigIntegerElement(BigInteger paramBigInteger, String paramString)
  {
    if (paramBigInteger != null)
    {
      Element localElement;
      Base64.fillElementWithBigInteger(localElement = XMLUtils.createElementInSignatureSpace(this._doc, paramString), paramBigInteger);
      this._constructionElement.appendChild(localElement);
      XMLUtils.addReturnToElement(this._constructionElement);
    }
  }

  public void addBase64Element(byte[] paramArrayOfByte, String paramString)
  {
    if (paramArrayOfByte != null)
    {
      Element localElement = Base64.encodeToElement(this._doc, paramString, paramArrayOfByte);
      this._constructionElement.appendChild(localElement);
      this._constructionElement.appendChild(this._doc.createTextNode("\n"));
    }
  }

  public void addTextElement(String paramString1, String paramString2)
  {
    Element localElement = XMLUtils.createElementInSignatureSpace(this._doc, paramString2);
    Text localText = this._doc.createTextNode(paramString1);
    localElement.appendChild(localText);
    this._constructionElement.appendChild(localElement);
    XMLUtils.addReturnToElement(this._constructionElement);
  }

  public void addBase64Text(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      Text localText = this._doc.createTextNode("\n" + Base64.encode(paramArrayOfByte) + "\n");
      this._constructionElement.appendChild(localText);
    }
  }

  public void addText(String paramString)
  {
    if (paramString != null)
    {
      Text localText = this._doc.createTextNode(paramString);
      this._constructionElement.appendChild(localText);
    }
  }

  public BigInteger getBigIntegerFromChildElement(String paramString1, String paramString2)
    throws Base64DecodingException
  {
    return Base64.decodeBigIntegerFromText(XMLUtils.selectNodeText(this._constructionElement.getFirstChild(), paramString2, paramString1, 0));
  }

  public byte[] getBytesFromChildElement(String paramString1, String paramString2)
    throws XMLSecurityException
  {
    Element localElement;
    return Base64.decode(localElement = XMLUtils.selectNode(this._constructionElement.getFirstChild(), paramString2, paramString1, 0));
  }

  public String getTextFromChildElement(String paramString1, String paramString2)
  {
    Text localText;
    return (localText = (Text)XMLUtils.selectNode(this._constructionElement.getFirstChild(), paramString2, paramString1, 0).getFirstChild()).getData();
  }

  public byte[] getBytesFromTextChild()
    throws XMLSecurityException
  {
    Text localText;
    return Base64.decode((localText = (Text)this._constructionElement.getFirstChild()).getData());
  }

  public String getTextFromTextChild()
  {
    return XMLUtils.getFullTextChildrenFromElement(this._constructionElement);
  }

  public int length(String paramString1, String paramString2)
  {
    int i = 0;
    Node localNode;
    if ((paramString2.equals(localNode.getLocalName())) && (paramString1.equals(localNode.getNamespaceURI())))
      i++;
    return i;
  }

  public void setXPathNamespaceContext(String paramString1, String paramString2)
    throws XMLSecurityException
  {
    if ((paramString1 == null) || (paramString1.length() == 0))
      throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
    if (paramString1.equals("xmlns"))
      throw new XMLSecurityException("defaultNamespaceCannotBeSetHere");
    String str = "xmlns:" + paramString1;
    Attr localAttr;
    if ((localAttr = this._constructionElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", str)) != null)
    {
      if (!localAttr.getNodeValue().equals(paramString2))
      {
        Object[] arrayOfObject = { str, this._constructionElement.getAttributeNS(null, str) };
        throw new XMLSecurityException("namespacePrefixAlreadyUsedByOtherURI", arrayOfObject);
      }
      return;
    }
    this._constructionElement.setAttributeNS("http://www.w3.org/2000/xmlns/", str, paramString2);
  }

  public static void setDefaultPrefix(String paramString1, String paramString2)
    throws XMLSecurityException
  {
    Object localObject;
    if ((_prefixMappings.containsValue(paramString2)) && (!(localObject = _prefixMappings.get(paramString1)).equals(paramString2)))
    {
      Object[] arrayOfObject = { paramString2, paramString1, localObject };
      throw new XMLSecurityException("prefix.AlreadyAssigned", arrayOfObject);
    }
    _prefixMappings.put(paramString1, paramString2);
  }

  public static String getDefaultPrefix(String paramString)
  {
    String str;
    return str = (String)_prefixMappings.get(paramString);
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.utils.ElementProxy
 * JD-Core Version:    0.6.0
 */