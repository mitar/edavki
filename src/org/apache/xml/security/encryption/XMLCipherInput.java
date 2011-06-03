package org.apache.xml.security.encryption;

import B;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.w3c.dom.Attr;

public class XMLCipherInput
{
  private static Log logger = LogFactory.getLog(XMLCipher.class.getName());
  private CipherData _cipherData;
  private int _mode;

  public XMLCipherInput(CipherData paramCipherData)
    throws XMLEncryptionException
  {
    this._cipherData = paramCipherData;
    this._mode = 2;
    if (this._cipherData == null)
      throw new XMLEncryptionException("CipherData is null");
  }

  public XMLCipherInput(EncryptedType paramEncryptedType)
    throws XMLEncryptionException
  {
    this._cipherData = (paramEncryptedType == null ? null : paramEncryptedType.getCipherData());
    this._mode = 2;
    if (this._cipherData == null)
      throw new XMLEncryptionException("CipherData is null");
  }

  public byte[] getBytes()
    throws XMLEncryptionException
  {
    if (this._mode == 2)
      return getDecryptBytes();
    return null;
  }

  private byte[] getDecryptBytes()
    throws XMLEncryptionException
  {
    String str = null;
    if (this._cipherData.getDataType() == 2)
    {
      logger.debug("Found a reference type CipherData");
      Attr localAttr = (localObject = this._cipherData.getCipherReference()).getURIAsAttr();
      XMLSignatureInput localXMLSignatureInput = null;
      try
      {
        ResourceResolver localResourceResolver;
        localXMLSignatureInput = (localResourceResolver = ResourceResolver.getInstance(localAttr, null)).resolve(localAttr, null);
      }
      catch (ResourceResolverException localResourceResolverException)
      {
        throw new XMLEncryptionException("empty", localResourceResolverException);
      }
      logger.debug((localXMLSignatureInput != null ? "Managed to resolve URI \"" : "Failed to resolve URI \"") + ((CipherReference)localObject).getURI() + "\"");
      Transforms localTransforms;
      logger.debug("Have transforms in cipher reference");
      org.apache.xml.security.transforms.Transforms localTransforms1;
      localXMLSignatureInput = (localTransforms1 = localTransforms.getDSTransforms()).performTransforms(localXMLSignatureInput);
      try
      {
        return localXMLSignatureInput.getBytes();
      }
      catch (IOException localIOException)
      {
        throw new XMLEncryptionException("empty", localIOException);
      }
      catch (CanonicalizationException localCanonicalizationException)
      {
        throw new XMLEncryptionException("empty", localCanonicalizationException);
      }
    }
    Object localObject = this._cipherData.getCipherValue();
    str = new String(((CipherValue)localObject).getValue());
    throw (this._cipherData.getDataType() == 1 ? logger : new XMLEncryptionException("CipherData.getDataType() returned unexpected value"));
    logger.debug("Encrypted octets:\n" + str);
    try
    {
      localObject = Base64.decode(str);
    }
    catch (Base64DecodingException localBase64DecodingException)
    {
      throw new XMLEncryptionException("empty", localBase64DecodingException);
    }
    return (B)localObject;
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.encryption.XMLCipherInput
 * JD-Core Version:    0.6.0
 */