package si.hermes.security;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class CertificateModel extends DefaultTableModel
{
  private static final long serialVersionUID = 1L;
  Vector certificates = new Vector();

  public CertificateModel()
  {
    String[] arrayOfString = { Utility.getLocalizedMessage("certificateModel.col1"), Utility.getLocalizedMessage("certificateModel.col2"), Utility.getLocalizedMessage("certificateModel.col3") };
    for (int i = 0; i < arrayOfString.length; i++)
      addColumn(arrayOfString[i]);
  }

  public boolean isCellEditable(int paramInt1, int paramInt2)
  {
    return false;
  }

  public void addCertificatesData(X509Certificate[] paramArrayOfX509Certificate)
  {
    for (int i = 0; i < paramArrayOfX509Certificate.length; i++)
      addCertificateData(paramArrayOfX509Certificate[i]);
  }

  public void addCertificateData(X509Certificate paramX509Certificate)
  {
    Vector localVector;
    (localVector = new Vector()).add(Utility.getCAFromCertificateDesc(paramX509Certificate.getSubjectDN().getName()));
    localVector.add(Utility.getCAFromCertificateDesc(paramX509Certificate.getIssuerDN().getName()));
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");
    localVector.add(localSimpleDateFormat.format(paramX509Certificate.getNotAfter()));
    addRow(localVector);
    this.certificates.add(paramX509Certificate);
  }

  public X509Certificate getCertificate(int paramInt)
  {
    return (X509Certificate)this.certificates.get(paramInt);
  }

  public int getCertificateCount()
  {
    return this.certificates.size();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.CertificateModel
 * JD-Core Version:    0.6.0
 */