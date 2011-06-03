package si.hermes.security.Dialogs;

import org.mozilla.jss.util.Password;
import org.mozilla.jss.util.PasswordCallback;
import org.mozilla.jss.util.PasswordCallback.GiveUpException;
import org.mozilla.jss.util.PasswordCallbackInfo;
import si.hermes.security.ESignDocException;

public class JSSEnterPinDialog
  implements PasswordCallback
{
  private static final long serialVersionUID = 619401576758898828L;
  private String tokenName;

  public JSSEnterPinDialog(String paramString)
  {
    this.tokenName = paramString;
  }

  public Password getPassword()
    throws PasswordCallback.GiveUpException
  {
    String str;
    try
    {
      str = EnterPinDialog.SelectPasswordModal(this.tokenName);
    }
    catch (ESignDocException localESignDocException2)
    {
      ESignDocException localESignDocException1;
      (localESignDocException1 = localESignDocException2).printStackTrace();
      throw new PasswordCallback.GiveUpException();
    }
    if (str != null)
      return new Password(str.toCharArray());
    throw new PasswordCallback.GiveUpException();
  }

  public Password getPasswordFirstAttempt(PasswordCallbackInfo paramPasswordCallbackInfo)
    throws PasswordCallback.GiveUpException
  {
    return getPassword();
  }

  public Password getPasswordAgain(PasswordCallbackInfo paramPasswordCallbackInfo)
    throws PasswordCallback.GiveUpException
  {
    return getPassword();
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.Dialogs.JSSEnterPinDialog
 * JD-Core Version:    0.6.0
 */