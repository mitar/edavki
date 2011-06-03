package org.apache.xml.security.algorithms;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class SignatureAlgorithmSpi
{
  static Log log = LogFactory.getLog(SignatureAlgorithmSpi.class.getName());
  Document _doc = null;
  Element _constructionElement = null;

  protected abstract String engineGetURI();

  protected abstract String engineGetJCEAlgorithmString();

  protected abstract String engineGetJCEProviderName();

  protected abstract void engineUpdate(byte[] paramArrayOfByte)
    throws XMLSignatureException;

  protected abstract void engineUpdate(byte paramByte)
    throws XMLSignatureException;

  protected abstract void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws XMLSignatureException;

  protected abstract void engineInitSign(Key paramKey)
    throws XMLSignatureException;

  protected abstract void engineInitSign(Key paramKey, SecureRandom paramSecureRandom)
    throws XMLSignatureException;

  protected abstract void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException;

  protected abstract byte[] engineSign()
    throws XMLSignatureException;

  protected abstract void engineInitVerify(Key paramKey)
    throws XMLSignatureException;

  protected abstract boolean engineVerify(byte[] paramArrayOfByte)
    throws XMLSignatureException;

  protected abstract void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws XMLSignatureException;

  protected void engineSetDocument(Document paramDocument)
  {
    this._doc = paramDocument;
  }

  protected void engineGetContextFromElement(Element paramElement)
  {
    this._constructionElement = paramElement;
  }

  protected void engineAddContextToElement(Element paramElement)
  {
  }

  protected abstract void engineSetHMACOutputLength(int paramInt)
    throws XMLSignatureException;
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.algorithms.SignatureAlgorithmSpi
 * JD-Core Version:    0.6.0
 */