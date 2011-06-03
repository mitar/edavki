package si.hermes.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Vector;
import org.mozilla.jss.asn1.ANY.Template;
import org.mozilla.jss.asn1.ASN1Template;
import org.mozilla.jss.asn1.CHOICE;
import org.mozilla.jss.asn1.CHOICE.Template;
import org.mozilla.jss.asn1.IA5String;
import org.mozilla.jss.asn1.InvalidBERException;
import org.mozilla.jss.asn1.OBJECT_IDENTIFIER;
import org.mozilla.jss.asn1.OBJECT_IDENTIFIER.Template;
import org.mozilla.jss.asn1.OCTET_STRING;
import org.mozilla.jss.asn1.OCTET_STRING.Template;
import org.mozilla.jss.asn1.SEQUENCE;
import org.mozilla.jss.asn1.SEQUENCE.OF_Template;
import org.mozilla.jss.asn1.SEQUENCE.Template;
import org.mozilla.jss.asn1.Tag;
import org.mozilla.jss.pkix.cert.Certificate;
import org.mozilla.jss.pkix.cert.Certificate.Template;
import org.mozilla.jss.pkix.cert.CertificateInfo;
import org.mozilla.jss.pkix.cert.Extension;
import org.mozilla.jss.pkix.primitive.RDN.Template;

public final class CRLDistributionPoint
{
  private final Vector CRLDistPoints = new Vector();

  CRLDistributionPoint(X509Certificate paramX509Certificate)
    throws IOException, InvalidBERException, CertificateEncodingException, ESignDocException
  {
    byte[] arrayOfByte = paramX509Certificate.getExtensionValue("2.5.29.31");
    OCTET_STRING localOCTET_STRING = null;
    Object localObject2;
    if ((localObject2 = (localObject1 = (Certificate)Certificate.getTemplate().decode(new ByteArrayInputStream(paramX509Certificate.getEncoded()))).getInfo().getExtension(new OBJECT_IDENTIFIER("2.5.29.31"))) == null)
      throw new ESignDocException(34, "extension does not exists!!!");
    Object localObject1 = new ByteArrayInputStream(arrayOfByte);
    if ((localOCTET_STRING = arrayOfByte == null ? ((Extension)localObject2).getExtnValue() : (OCTET_STRING)OCTET_STRING.getTemplate().decode((InputStream)localObject1)) != null)
    {
      localObject1 = new ByteArrayInputStream(localOCTET_STRING.toByteArray());
      localObject2 = new RDN.Template();
      SEQUENCE.OF_Template localOF_Template1 = new SEQUENCE.OF_Template(new ANY.Template());
      SEQUENCE.OF_Template localOF_Template2 = new SEQUENCE.OF_Template(new ANY.Template());
      SEQUENCE.Template localTemplate1;
      (localTemplate1 = SEQUENCE.getTemplate()).addOptionalElement(new ANY.Template());
      localTemplate1.addOptionalElement(new ANY.Template());
      SEQUENCE.Template localTemplate2;
      (localTemplate2 = SEQUENCE.getTemplate()).addOptionalElement(new OBJECT_IDENTIFIER.Template());
      localTemplate2.addOptionalElement(new ANY.Template());
      localTemplate2.addOptionalElement(new ANY.Template());
      CHOICE.Template localTemplate3;
      (localTemplate3 = CHOICE.getTemplate()).addElement(new Tag(0L), localTemplate2);
      localTemplate3.addElement(new Tag(1L), IA5String.getTemplate());
      localTemplate3.addElement(new Tag(2L), IA5String.getTemplate());
      localTemplate3.addElement(new Tag(3L), localOF_Template2);
      localTemplate3.addElement(new Tag(4L), localOF_Template1);
      localTemplate3.addElement(new Tag(5L), localTemplate1);
      localTemplate3.addElement(new Tag(6L), IA5String.getTemplate());
      localTemplate3.addElement(new Tag(7L), OCTET_STRING.getTemplate());
      localTemplate3.addElement(new Tag(8L), OBJECT_IDENTIFIER.getTemplate());
      SEQUENCE.OF_Template localOF_Template3 = new SEQUENCE.OF_Template(localTemplate3);
      CHOICE.Template localTemplate4;
      (localTemplate4 = CHOICE.getTemplate()).addElement(new Tag(0L), localOF_Template3);
      localTemplate4.addElement(new Tag(1L), (ASN1Template)localObject2);
      SEQUENCE.OF_Template localOF_Template4 = new SEQUENCE.OF_Template(localTemplate4);
      SEQUENCE.Template localTemplate5;
      (localTemplate5 = new SEQUENCE.Template()).addOptionalElement(new Tag(0L), localOF_Template4);
      localTemplate5.addOptionalElement(new Tag(1L), new ANY.Template());
      localTemplate5.addOptionalElement(new Tag(2L), new ANY.Template());
      SEQUENCE.OF_Template localOF_Template5;
      SEQUENCE localSEQUENCE1 = (SEQUENCE)(localOF_Template5 = new SEQUENCE.OF_Template(localTemplate5)).decode((InputStream)localObject1);
      for (int i = 0; i < localSEQUENCE1.size(); i++)
      {
        SEQUENCE localSEQUENCE2;
        SEQUENCE localSEQUENCE3;
        if ((localSEQUENCE3 = (SEQUENCE)(localSEQUENCE2 = (SEQUENCE)localSEQUENCE1.elementAt(i)).elementWithTag(new Tag(0L))).size() != 1)
          throw new ESignDocException(34, "Format error: ChoiceEater");
        SEQUENCE localSEQUENCE4 = (SEQUENCE)((CHOICE)localSEQUENCE3.elementAt(0)).getValue();
        for (int j = 0; j < localSEQUENCE4.size(); j++)
        {
          CHOICE localCHOICE;
          switch ((int)(localCHOICE = (CHOICE)localSEQUENCE4.elementAt(j)).getTag().getNum())
          {
          case 4:
            break;
          case 6:
            this.CRLDistPoints.addElement(String.valueOf(((IA5String)localCHOICE.getValue()).toCharArray()));
            break;
          default:
            throw new ESignDocException(34, "Please report this number to the developers: " + (int)localCHOICE.getTag().getNum());
          }
        }
      }
    }
  }

  public final X509CRL GetCRLFromCertificate()
    throws ESignDocException
  {
    for (int i = 0; i < this.CRLDistPoints.size(); i++)
      try
      {
        String str;
        X509CRL localX509CRL;
        if ((localX509CRL = SimpleCacheCRLResolver.ResolverCRL(str = (String)this.CRLDistPoints.elementAt(i))) != null)
          return localX509CRL;
      }
      catch (Exception localException)
      {
      }
    throw new ESignDocException(34, "No valid CRL Distribution Point found!");
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CRLDistributionPoint
 * JD-Core Version:    0.6.0
 */