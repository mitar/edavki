package org.apache.xml.security.transforms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.AlgorithmAlreadyRegisteredException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.HelperNodeList;
import org.apache.xml.security.utils.SignatureElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class Transform extends SignatureElementProxy
{
  static Log log = LogFactory.getLog(Transform.class.getName());
  static boolean _alreadyInitialized = false;
  static HashMap _transformHash = null;
  protected TransformSpi transformSpi = null;

  public Transform(Document paramDocument, String paramString, NodeList paramNodeList)
    throws InvalidTransformException
  {
    super(paramDocument);
    try
    {
      this._constructionElement.setAttributeNS(null, "Algorithm", paramString);
      String str = getImplementingClass(paramString);
      if (log.isDebugEnabled())
      {
        log.debug("Create URI \"" + paramString + "\" class \"" + str + "\"");
        log.debug("The NodeList is " + paramNodeList);
      }
      this.transformSpi = ((TransformSpi)Class.forName(str).newInstance());
      this.transformSpi.setTransform(this);
      if (paramNodeList != null)
        for (int i = 0; i < paramNodeList.getLength(); i++)
          this._constructionElement.appendChild(paramNodeList.item(i).cloneNode(true));
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      arrayOfObject = new Object[] { paramString };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject, localClassNotFoundException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      arrayOfObject = new Object[] { paramString };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject, localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      Object[] arrayOfObject = { paramString };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject, localInstantiationException);
    }
  }

  public Transform(Element paramElement, String paramString)
    throws InvalidTransformException, TransformationException, XMLSecurityException
  {
    super(paramElement, paramString);
    String str;
    Object localObject;
    if (((str = paramElement.getAttributeNS(null, "Algorithm")) == null) || (str.length() == 0))
    {
      localObject = new Object[] { "Algorithm", "Transform" };
      throw new TransformationException("xml.WrongContent", localObject);
    }
    try
    {
      localObject = (String)_transformHash.get(str);
      this.transformSpi = ((TransformSpi)Class.forName((String)localObject).newInstance());
      this.transformSpi.setTransform(this);
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      arrayOfObject = new Object[] { str };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      arrayOfObject = new Object[] { str };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject);
    }
    catch (InstantiationException localInstantiationException)
    {
      Object[] arrayOfObject = { str };
      throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject);
    }
  }

  public static final Transform getInstance(Document paramDocument, String paramString)
    throws InvalidTransformException
  {
    return getInstance(paramDocument, paramString, (NodeList)null);
  }

  public static final Transform getInstance(Document paramDocument, String paramString, Element paramElement)
    throws InvalidTransformException
  {
    HelperNodeList localHelperNodeList;
    (localHelperNodeList = new HelperNodeList()).appendChild(paramDocument.createTextNode("\n"));
    localHelperNodeList.appendChild(paramElement);
    localHelperNodeList.appendChild(paramDocument.createTextNode("\n"));
    return getInstance(paramDocument, paramString, localHelperNodeList);
  }

  public static final Transform getInstance(Document paramDocument, String paramString, NodeList paramNodeList)
    throws InvalidTransformException
  {
    return new Transform(paramDocument, paramString, paramNodeList);
  }

  public static void init()
  {
    if (!_alreadyInitialized)
    {
      _transformHash = new HashMap(10);
      _alreadyInitialized = true;
    }
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
    _transformHash.put(paramString1, paramString2);
  }

  public final String getURI()
  {
    return this._constructionElement.getAttributeNS(null, "Algorithm");
  }

  public final XMLSignatureInput performTransform(XMLSignatureInput paramXMLSignatureInput)
    throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException
  {
    XMLSignatureInput localXMLSignatureInput = null;
    try
    {
      localXMLSignatureInput = this.transformSpi.enginePerformTransform(paramXMLSignatureInput);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      arrayOfObject = new Object[] { getURI(), "ParserConfigurationException" };
      throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localParserConfigurationException);
    }
    catch (SAXException localSAXException)
    {
      Object[] arrayOfObject = { getURI(), "SAXException" };
      throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localSAXException);
    }
    return localXMLSignatureInput;
  }

  public final XMLSignatureInput performTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
    throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException
  {
    XMLSignatureInput localXMLSignatureInput = null;
    try
    {
      localXMLSignatureInput = this.transformSpi.enginePerformTransform(paramXMLSignatureInput, paramOutputStream);
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      arrayOfObject = new Object[] { getURI(), "ParserConfigurationException" };
      throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localParserConfigurationException);
    }
    catch (SAXException localSAXException)
    {
      Object[] arrayOfObject = { getURI(), "SAXException" };
      throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localSAXException);
    }
    return localXMLSignatureInput;
  }

  private static String getImplementingClass(String paramString)
  {
    try
    {
      Iterator localIterator = _transformHash.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str;
        if ((str = (String)localIterator.next()).equals(paramString))
          return (String)_transformHash.get(str);
      }
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    return null;
  }

  public final String getBaseLocalName()
  {
    return "Transform";
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.transforms.Transform
 * JD-Core Version:    0.6.0
 */