package si.hermes.security.Dialogs;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import si.hermes.security.ESignDocException;
import si.hermes.security.Utility;

public class EnterPinDialog extends JDialog
{
  private static final long serialVersionUID = -3764678795596218284L;
  public static final int PIN_ENTERED = 0;
  public static final int PIN_CANCELED = 1;
  private JButton btnCancel;
  private JButton btnSubmit;
  private JPasswordField edtPin;
  private JLabel lblEnterPin;
  private String pin = null;
  private String tokenName = "";
  private int state = 1;

  public static String SelectPasswordModal(String paramString)
    throws ESignDocException
  {
    1DialogResult local1DialogResult;
    (local1DialogResult = new Object()
    {
      public String fPassword;
      public String fTokenName;
      public int dialogResult;
    }).fTokenName = paramString;
    try
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception localException2)
      {
      }
      EnterPinDialog localEnterPinDialog = new EnterPinDialog(new JFrame(), local1DialogResult.fTokenName);
      local1DialogResult.dialogResult = localEnterPinDialog.doModal();
      if (local1DialogResult.dialogResult == 0)
        local1DialogResult.fPassword = localEnterPinDialog.getPin();
    }
    catch (Exception localException1)
    {
      throw new ESignDocException(36, localException1);
    }
    switch (local1DialogResult.dialogResult)
    {
    case 0:
      return local1DialogResult.fPassword;
    }
    return null;
  }

  private EnterPinDialog(Frame paramFrame, String paramString)
  {
    super(paramFrame, true);
    this.tokenName = paramString;
    initComponents();
  }

  public String getPin()
  {
    return this.pin;
  }

  public int doModal()
  {
    setVisible(true);
    return this.state;
  }

  private void initComponents()
  {
    this.lblEnterPin = new JLabel();
    this.btnSubmit = new JButton();
    this.edtPin = new JPasswordField();
    this.btnCancel = new JButton();
    getContentPane().setLayout(new GridBagLayout());
    getRootPane().setDefaultButton(this.btnSubmit);
    Utility.centerDialog(this);
    setResizable(false);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramWindowEvent)
      {
        EnterPinDialog.this.closeDialog(paramWindowEvent);
      }
    });
    this.lblEnterPin.setText(Utility.getLocalizedMessage("enterPinDialog.maintext") + " " + this.tokenName + ":");
    GridBagConstraints localGridBagConstraints;
    (localGridBagConstraints = new GridBagConstraints()).insets = new Insets(12, 6, 6, 6);
    getContentPane().add(this.lblEnterPin, localGridBagConstraints);
    this.btnSubmit.setText(Utility.getLocalizedMessage("enterPinDialog.confirmText"));
    this.btnSubmit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramActionEvent)
      {
        EnterPinDialog.this.btnSubmitActionPerformed(paramActionEvent);
      }
    });
    (localGridBagConstraints = new GridBagConstraints()).gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.insets = new Insets(12, 6, 6, 6);
    localGridBagConstraints.anchor = 13;
    getContentPane().add(this.btnSubmit, localGridBagConstraints);
    this.edtPin.setHorizontalAlignment(2);
    (localGridBagConstraints = new GridBagConstraints()).fill = 2;
    localGridBagConstraints.insets = new Insets(12, 6, 6, 6);
    getContentPane().add(this.edtPin, localGridBagConstraints);
    this.btnCancel.setText(Utility.getLocalizedMessage("enterPinDialog.cancelText"));
    this.btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramActionEvent)
      {
        EnterPinDialog.this.btnCancelActionPerformed(paramActionEvent);
      }
    });
    (localGridBagConstraints = new GridBagConstraints()).gridx = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.insets = new Insets(12, 6, 6, 6);
    getContentPane().add(this.btnCancel, localGridBagConstraints);
    pack();
  }

  private void btnCancelActionPerformed(ActionEvent paramActionEvent)
  {
    disposeDialog();
  }

  private void btnSubmitActionPerformed(ActionEvent paramActionEvent)
  {
    this.state = 0;
    this.pin = new String(this.edtPin.getPassword());
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
 * Qualified Name:     si.hermes.security.Dialogs.EnterPinDialog
 * JD-Core Version:    0.6.0
 */