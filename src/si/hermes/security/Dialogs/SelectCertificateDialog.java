package si.hermes.security.Dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import si.hermes.security.CertificateModel;
import si.hermes.security.CertificateProvider;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;

public final class SelectCertificateDialog extends JDialog
{
  private static final long serialVersionUID = 4152400487894589525L;
  public static final int CERTIFICATE_SELECTED = 0;
  public static final int CERTIFICATE_CANCELED = 1;
  public static final int CERTIFICATE_MANUAL = 2;
  private CertificateModel model = new CertificateModel();
  private int state = 1;
  private int selectedRow = -1;
  private JButton btnCancel;
  private JButton btnSelect;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JTable tblCertificate;
  private int dialogType = 0;

  public static X509Certificate SelectCertificateModal(X509Certificate[] paramArrayOfX509Certificate, int paramInt)
    throws ESignDocException
  {
    1DialogResult local1DialogResult;
    (local1DialogResult = new Object()
    {
      public X509Certificate fCertificate;
      public int dialogResult;
      public int dialogType;
      public X509Certificate[] certificates;
    }).certificates = paramArrayOfX509Certificate;
    local1DialogResult.dialogType = paramInt;
    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception localException2)
      {
      }
      SelectCertificateDialog localSelectCertificateDialog = new SelectCertificateDialog(new JFrame(), local1DialogResult.certificates, local1DialogResult.dialogType);
      local1DialogResult.dialogResult = localSelectCertificateDialog.doModal();
      if (local1DialogResult.dialogResult == 0)
        local1DialogResult.fCertificate = localSelectCertificateDialog.getSelectedCertificate();
    }
    catch (Exception localException1)
    {
      throw new ESignDocException(36, localException1);
    }
    switch (local1DialogResult.dialogResult)
    {
    case 0:
      return local1DialogResult.fCertificate;
    case 2:
      throw new ESignDocException(32, "<CertificateNotFound StoreType=\"" + CertificateProvider.getShortProviderName() + "\"/>");
    }
    throw new ESignDocException(8, "<UserAbortedSigning/>");
  }

  private SelectCertificateDialog(Frame paramFrame, X509Certificate[] paramArrayOfX509Certificate, int paramInt)
  {
    super(paramFrame, true);
    this.model.addCertificatesData(paramArrayOfX509Certificate);
    this.dialogType = paramInt;
    initComponents();
  }

  public final int doModal()
  {
    setVisible(true);
    return this.state;
  }

  public final X509Certificate getSelectedCertificate()
  {
    if (this.state == 0)
      return this.model.getCertificate(this.selectedRow);
    return null;
  }

  private void initComponents()
  {
    this.tblCertificate = new JTable();
    this.btnSelect = new JButton();
    this.btnCancel = new JButton();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.jLabel3 = new JLabel();
    getContentPane().setLayout(new GridBagLayout());
    getRootPane().setDefaultButton(this.btnSelect);
    setTitle(Utility.getLocalizedMessage(this.dialogType == 1 ? "selectCertificateDialog.confirmTitle" : "selectCertificateDialog.title"));
    setModal(true);
    setName("dlgSelectCertificate");
    setResizable(true);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramWindowEvent)
      {
        SelectCertificateDialog.this.closeDialog(paramWindowEvent);
      }
    });
    Utility.centerDialog(this);
    this.tblCertificate.setModel(this.model);
    this.tblCertificate.setSelectionMode(0);
    this.tblCertificate.setRowSelectionInterval(0, 0);
    this.tblCertificate.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramKeyEvent)
      {
      }

      public void keyTyped(KeyEvent paramKeyEvent)
      {
        if (paramKeyEvent.getKeyChar() == '\n')
          SelectCertificateDialog.this.btnSelectActionPerformed(null);
      }
    });
    this.jLabel1.setFont(new Font("Dialog", 0, 12));
    this.jLabel1.setHorizontalAlignment(4);
    this.jLabel1.setText(Utility.getLocalizedMessage("selectCertificateDialog.mainText"));
    GridBagConstraints localGridBagConstraints;
    (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    localGridBagConstraints.anchor = 17;
    getContentPane().add(this.jLabel1, localGridBagConstraints);
    JScrollPane localJScrollPane;
    (localJScrollPane = new JScrollPane(this.tblCertificate)).setVerticalScrollBarPolicy(20);
    (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.weighty = 1.0D;
    localGridBagConstraints.fill = 1;
    getContentPane().add(localJScrollPane, localGridBagConstraints);
    if (this.dialogType != 1)
    {
      this.jLabel2.setFont(new Font("Dialog", 0, 12));
      this.jLabel2.setHorizontalAlignment(4);
      this.jLabel2.setText(Utility.getLocalizedMessage("selectCertificateDialog.installStoreText"));
      (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridy = 2;
      localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
      localGridBagConstraints.anchor = 17;
      getContentPane().add(this.jLabel2, localGridBagConstraints);
      this.jLabel3.setFont(new Font("Dialog", 0, 12));
      this.jLabel3.setForeground(Color.BLUE);
      Map localMap;
      (localMap = new Font("", 1, 12).getAttributes()).put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      this.jLabel3.setFont(new Font(localMap));
      this.jLabel3.setHorizontalAlignment(4);
      this.jLabel3.setText(Utility.getLocalizedMessage("selectCertificateDialog.installStoreLinkText"));
      (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridy = 3;
      localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
      localGridBagConstraints.anchor = 17;
      getContentPane().add(this.jLabel3, localGridBagConstraints);
      3 local3 = new MouseAdapter()
      {
        public void mouseClicked(MouseEvent paramMouseEvent)
        {
          SelectCertificateDialog.access$102(SelectCertificateDialog.this, 2);
          SelectCertificateDialog.this.disposeDialog();
        }
      };
      this.jLabel3.addMouseListener(local3);
    }
    this.btnSelect.setText(Utility.getLocalizedMessage("selectCertificateDialog.confirmText"));
    this.btnSelect.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramActionEvent)
      {
        SelectCertificateDialog.this.btnSelectActionPerformed(paramActionEvent);
      }
    });
    this.tblCertificate.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramMouseEvent)
      {
        if (paramMouseEvent.getClickCount() == 2)
          SelectCertificateDialog.this.btnSelectActionPerformed((ActionEvent)null);
      }
    });
    (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
    localGridBagConstraints.gridy = (this.dialogType == 1 ? 2 : 4);
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    localGridBagConstraints.anchor = 13;
    getContentPane().add(this.btnSelect, localGridBagConstraints);
    this.btnCancel.setText(Utility.getLocalizedMessage("selectCertificateDialog.cancelText"));
    this.btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramActionEvent)
      {
        SelectCertificateDialog.this.btnCancelActionPerformed(paramActionEvent);
      }
    });
    (localGridBagConstraints = new GridBagConstraints()).gridx = 1;
    localGridBagConstraints.gridy = (this.dialogType == 1 ? 2 : 4);
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(4, 4, 4, 4);
    localGridBagConstraints.anchor = 17;
    getContentPane().add(this.btnCancel, localGridBagConstraints);
    setSize(527, this.dialogType == 1 ? 170 : 387);
  }

  private void btnCancelActionPerformed(ActionEvent paramActionEvent)
  {
    disposeDialog();
  }

  public final void btnSelectActionPerformed(ActionEvent paramActionEvent)
  {
    this.state = 0;
    this.selectedRow = this.tblCertificate.getSelectedRow();
    if (this.selectedRow != -1)
      disposeDialog();
  }

  private void closeDialog(WindowEvent paramWindowEvent)
  {
    disposeDialog();
  }

  private void disposeDialog()
  {
    setVisible(false);
    dispose();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Dialogs.SelectCertificateDialog
 * JD-Core Version:    0.6.0
 */