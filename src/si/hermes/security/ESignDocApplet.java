package si.hermes.security;

import java.applet.Applet;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Date;
import java.util.Locale;
import javax.xml.transform.TransformerException;
import netscape.javascript.JSObject;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import si.hermes.security.Collections.IHSLSignature;
import si.hermes.security.Collections.IManifest;
import si.hermes.security.Collections.IManifests;
import si.hermes.security.Collections.ISignature;
import si.hermes.security.Collections.ISignatures;

public final class ESignDocApplet extends Applet
{
  private static final long serialVersionUID = 2894201252435246284L;
  private ESignDocImpl esignDoc = null;
  private boolean isError = false;
  private String lastErrorMessage = "";

  public final void addBaseURI(String paramString)
    throws ESignDocException
  {
    notImplemented();
  }

  public final int addManifest(IManifest paramIManifest, Element paramElement)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.addManifest(paramIManifest, paramElement);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return 0;
  }

  public final int addSignature(ISignature paramISignature, Element paramElement)
  {
    return ((Integer)notImplemented()).intValue();
  }

  public final void addXMLEntity(String paramString1, String paramString2, int paramInt)
  {
    Object[] arrayOfObject = { paramString1, paramString2, new Integer(paramInt) };
    this.isError = false;
    try
    {
      if (paramString1 == null)
        throw new ESignDocException(3, "Invalid argument: null data");
      if (paramString2 == null)
        throw new ESignDocException(3, "Invalid argument: null key");
      if (paramInt < 0)
        throw new ESignDocException(3, "Invalid argument: negative index (0 is first index)");
      AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("addXMLEntity", arrayOfObject));
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final void calculateManifestReferences(boolean paramBoolean)
  {
    notImplemented();
  }

  public final String createDetachedSignature(String paramString, boolean paramBoolean)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createDetachedSignature(paramString, paramBoolean);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopedSignature(String paramString)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopedSignature(paramString);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopedSignatureDom(Document paramDocument)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopedSignatureDom(paramDocument);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopedSignatureDomNoRoot(Document paramDocument)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopedSignatureDomNoRoot(paramDocument);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopedSignatureNoRoot(String paramString)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopedSignatureNoRoot(paramString);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopingSignature(String paramString)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopingSignature(paramString);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String createEnvelopingSignatureDom(Document paramDocument)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.createEnvelopingSignatureDom(paramDocument);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String getBaseUri()
  {
    return (String)notImplemented();
  }

  public final String getId()
  {
    if (this.esignDoc != null)
      return this.esignDoc.getId();
    return "urn:?:0.0.0.0:?";
  }

  public final String getLastErrorMessage()
  {
    return this.lastErrorMessage;
  }

  public final IManifests getManifests()
  {
    return this.esignDoc.getManifests();
  }

  public final ISignatures getSignatures()
  {
    return (ISignatures)notImplemented();
  }

  public final String getToString()
  {
    this.isError = false;
    try
    {
      return this.esignDoc.getToString();
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String getXAdESCertificateDigestAlgorithm()
  {
    return this.esignDoc.getXAdESCertificateDigestAlgorithm();
  }

  public final Element GetXml()
    throws ESignDocException
  {
    return GetXml(null);
  }

  public final Element GetXml(Element paramElement)
    throws ESignDocException
  {
    this.isError = false;
    try
    {
      return this.esignDoc.GetXml(paramElement);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String hashSHA1(String paramString)
    throws ESignDocException, Base64DecodingException
  {
    return (String)notImplemented();
  }

  public final void initprofile()
    throws PrivilegedActionException
  {
    String str1 = getParameter("profile");
    String str2 = getParameter("storetype");
    this.isError = false;
    Object[] arrayOfObject = { str1, str2 };
    AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("check", arrayOfObject));
    AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("init", arrayOfObject));
  }

  public final boolean isError()
  {
    return this.isError;
  }

  public final void load(String paramString)
  {
    this.isError = false;
    try
    {
      this.esignDoc.load(paramString);
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final void loadDom(Document paramDocument)
  {
    this.isError = false;
    try
    {
      this.esignDoc.loadDom(paramDocument);
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final void LoadXml(Element paramElement)
    throws ESignDocException
  {
    this.isError = false;
    try
    {
      this.esignDoc.LoadXml(paramElement);
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  private Object notImplemented()
  {
    this.isError = true;
    this.lastErrorMessage = "NOT_IMPL";
    return null;
  }

  public final void setBaseUri(String paramString)
  {
    notImplemented();
  }

  private void setErrorCondition(Exception paramException)
  {
    paramException.printStackTrace();
    this.isError = true;
    this.lastErrorMessage = paramException.toString();
    showStatus(paramException.toString());
  }

  public final void setXAdESCertificateDigestAlgorithm(String paramString)
  {
    this.esignDoc.setXAdESCertificateDigestAlgorithm(paramString);
  }

  public final String sign(String paramString1, String paramString2, boolean paramBoolean)
  {
    Object[] arrayOfObject = { paramString1, paramString2, new Boolean(paramBoolean) };
    this.isError = false;
    try
    {
      return (String)AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("sign", arrayOfObject));
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final void signDom(String paramString)
  {
    Object[] arrayOfObject = { paramString };
    this.isError = false;
    try
    {
      AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("signDom", arrayOfObject));
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final String signWithCertificate(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4, String paramString5)
  {
    return (String)notImplemented();
  }

  public final void signWithCertificateDom(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
  {
    notImplemented();
  }

  public final void start()
  {
    JSObject localJSObject = null;
    try
    {
      initprofile();
      if ((localJSObject = JSObject.getWindow(this)) != null)
      {
        localJSObject.call("esigndoc_start", new Object[] { "" });
      }
      else
      {
        throw new ESignDocException(12);
        return;
      }
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
      if ((localJSObject = JSObject.getWindow(this)) != null)
        localJSObject.call("esigndoc_start", new String[] { localException.toString() });
    }
  }

  public final String transform(String paramString1, String paramString2)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.transform(paramString1, paramString2);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String transform(String paramString1, String paramString2, String paramString3)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.transform(paramString1, paramString2, paramString3);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String transformEx(Document paramDocument1, Document paramDocument2, String paramString)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.transformEx(paramDocument1, paramDocument2, paramString);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String transformWithVerify(String paramString1, String paramString2)
  {
    this.isError = false;
    try
    {
      return this.esignDoc.transformWithVerify(paramString1, paramString2);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final CertificateChain validateCertificateChain(String paramString1, Date paramDate, boolean paramBoolean, String paramString2)
  {
    return (CertificateChain)notImplemented();
  }

  public final void addEntity(String paramString1, String paramString2, String paramString3)
  {
    this.isError = false;
    try
    {
      this.esignDoc.addEntity(paramString1, paramString2, paramString3);
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final String calculateManifestReferenceHash(String paramString)
    throws ESignDocException, TransformerException, XMLSecurityException, IOException
  {
    this.isError = false;
    try
    {
      return this.esignDoc.calculateManifestReferenceHash(paramString);
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final String signWithCertificateUI(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    Object[] arrayOfObject = { paramString1, paramString2, paramString3, paramString4, new Boolean(paramBoolean) };
    this.isError = false;
    try
    {
      return (String)AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("signWithCertificateUI", arrayOfObject));
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
    return null;
  }

  public final void signWithCertificateUIDom(String paramString1, String paramString2, String paramString3)
  {
    Object[] arrayOfObject = { paramString1, paramString2, paramString3 };
    this.isError = false;
    try
    {
      AccessController.doPrivileged(new ESignDocPrivilegedExceptionAction("signWithCertificateUIDom", arrayOfObject));
      return;
    }
    catch (Exception localException)
    {
      setErrorCondition(localException);
    }
  }

  public final String getUILanguage()
  {
    return Utility.getLocale().getLanguage();
  }

  public final void setUILanguage(String paramString)
    throws ESignDocException
  {
    Utility.setLocale(paramString);
  }

  private class ESignDocPrivilegedExceptionAction
    implements PrivilegedExceptionAction
  {
    private Object[] arguments = null;
    private String command = null;

    ESignDocPrivilegedExceptionAction(String paramArrayOfObject, Object[] arg3)
      throws PrivilegedActionException
    {
      Object localObject;
      if ((paramArrayOfObject == null) || (localObject == null))
        throw new PrivilegedActionException(new Exception("Invalid arguments"));
      this.command = paramArrayOfObject;
      this.arguments = localObject;
    }

    private Object executeAddEntity()
      throws Exception
    {
      if (this.arguments.length != 3)
        throw new Exception("invalid arguments to AddXMLEntity");
      String str1 = (String)this.arguments[0];
      String str2 = (String)this.arguments[1];
      int i = ((Integer)this.arguments[2]).intValue();
      IResolverEntity localIResolverEntity;
      if ((localIResolverEntity = ESignDocApplet.this.esignDoc.getSignatures().getItem(i).getResolverEntity()) == null)
      {
        ESignDocApplet.this.esignDoc.getSignatures().getItem(i).setResolverEntity(new ResolverEntityInfo());
        localIResolverEntity = ESignDocApplet.this.esignDoc.getSignatures().getItem(i).getResolverEntity();
      }
      localIResolverEntity.getResolver().setEntity(str2, Base64.decode(str1));
      return null;
    }

    private Object executeInit()
      throws Exception
    {
      if (this.arguments.length != 2)
        throw new Exception("invalid arguments to init");
      if (this.arguments[0] == null)
        throw new Exception("null argument to init");
      ESignDocImpl.initProfile((String)this.arguments[0], (String)this.arguments[1], true);
      ESignDocApplet.access$002(ESignDocApplet.this, new ESignDocImpl());
      return null;
    }

    private void testExternalComponents()
      throws Exception
    {
      if (!(str = ESignDocApplet.this.getCodeBase().toString()).endsWith("/"))
        str = str + "/";
      String str = str + "Endorsed/";
      System.out.println(str);
      ClassPathHacker.setupComponents(str, (String)this.arguments[1]);
      ClassPathHacker.testExternalComponents((String)this.arguments[1]);
    }

    private Object executeSign()
      throws Exception
    {
      if (this.arguments.length != 3)
        throw new Exception("invalid arguments to sign");
      return ESignDocApplet.this.esignDoc.sign((String)this.arguments[0], (String)this.arguments[1], ((Boolean)this.arguments[2]).booleanValue());
    }

    private Object executeSignWithCertificateUI()
      throws Exception
    {
      if (this.arguments.length != 5)
        throw new Exception("invalid arguments to signWithCertificateUI");
      return ESignDocApplet.this.esignDoc.signWithCertificateUI((String)this.arguments[0], (String)this.arguments[1], (String)this.arguments[2], (String)this.arguments[3], ((Boolean)this.arguments[4]).booleanValue());
    }

    public Object run()
      throws Exception
    {
      if (this.command.toLowerCase().equals("init"))
        return executeInit();
      if (this.command.toLowerCase().equals("sign"))
        return executeSign();
      if (this.command.toLowerCase().equals("signwithcertificateui"))
        return executeSignWithCertificateUI();
      if (this.command.toLowerCase().equals("addxmlentity"))
        return executeAddEntity();
      if (this.command.toLowerCase().equals("check"))
      {
        testExternalComponents();
        return null;
      }
      throw new Exception("Command not known");
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ESignDocApplet
 * JD-Core Version:    0.6.0
 */