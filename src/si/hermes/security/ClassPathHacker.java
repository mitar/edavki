package si.hermes.security;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class ClassPathHacker
{
  private static final Class[] parameters = { URL.class };

  public static void addFile(String paramString)
    throws IOException
  {
    System.out.println("Loading: " + paramString);
    File localFile;
    addFile(localFile = new File(paramString));
  }

  public static void addFile(File paramFile)
    throws IOException
  {
    addURL(paramFile.toURI().toURL());
  }

  public static void addURL(URL paramURL)
    throws IOException
  {
    URLClassLoader localURLClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
    Class localClass = URLClassLoader.class;
    try
    {
      (localObject = localClass.getDeclaredMethod("addURL", parameters)).setAccessible(true);
      ((Method)localObject).invoke(localURLClassLoader, new Object[] { paramURL });
      return;
    }
    catch (Throwable localThrowable)
    {
      Object localObject;
      (localObject = localThrowable).printStackTrace();
      throw new IOException("Error, could not add URL to system classloader");
    }
  }

  public static void download(String paramString1, String paramString2)
  {
    BufferedOutputStream localBufferedOutputStream = null;
    URLConnection localURLConnection = null;
    InputStream localInputStream = null;
    try
    {
      localObject1 = new URL(paramString1);
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString2));
      (localURLConnection = ((URL)localObject1).openConnection()).setUseCaches(false);
      System.out.println(paramString1 + ": " + localURLConnection.getContentLength());
      localInputStream = localURLConnection.getInputStream();
      byte[] arrayOfByte = new byte[1024];
      long l = 0L;
      int i;
      while ((i = localInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
      {
        if (i == 0)
        {
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
          }
          continue;
        }
        localBufferedOutputStream.write(arrayOfByte, 0, i);
        l += i;
      }
      System.out.println(paramString2 + "\t" + l);
    }
    catch (Exception localException)
    {
      Object localObject1;
      return;
    }
    finally
    {
      try
      {
        if (localInputStream != null)
          localInputStream.close();
        if (localBufferedOutputStream != null)
          localBufferedOutputStream.close();
      }
      catch (IOException localIOException)
      {
      }
    }
  }

  private static String toHexString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfByte.length; i++)
      localStringBuffer.append(Integer.toHexString(256 + (paramArrayOfByte[i] & 0xFF)).substring(1));
    return localStringBuffer.toString();
  }

  private static String digestFile(File paramFile)
    throws NoSuchAlgorithmException, IOException
  {
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
    if (!paramFile.exists())
      return "";
    int i;
    byte[] arrayOfByte = new byte[i = (int)paramFile.length()];
    FileInputStream localFileInputStream;
    if ((localFileInputStream = new FileInputStream(paramFile)).read(arrayOfByte) != i)
      throw new IOException(i + "unavailable.");
    localFileInputStream.close();
    localMessageDigest.update(arrayOfByte);
    return toHexString(localMessageDigest.digest()).toUpperCase();
  }

  public static String getHslUserAppPath()
  {
    if (System.getProperty("os.name").startsWith("Windows"))
      return System.getProperty("user.home") + "\\Application Data\\Hermes SoftLab\\DigSig\\";
    return System.getProperty("user.home") + "/.hsldigsig/";
  }

  public static void testComponent(String paramString1, String paramString2, String paramString3, String paramString4)
    throws NoSuchAlgorithmException, IOException
  {
    String str = "";
    File localFile;
    if ((localFile = new File(paramString1 + paramString2)).exists())
      str = digestFile(localFile);
    if (!paramString3.equals(str))
    {
      System.out.println(paramString3 + " != " + str);
      download(paramString4, paramString1 + paramString2);
    }
  }

  public static void setupComponents(String paramString1, String paramString2)
    throws ESignDocException
  {
    try
    {
      localObject = System.getProperty("os.name");
      System.out.println("Setting ESignDoc2 for: " + (String)localObject + ", " + System.getProperty("os.arch"));
      new File(getHslUserAppPath()).mkdirs();
      if ("NSS".equalsIgnoreCase(paramString2))
      {
        try
        {
          Policy.setPolicy(new Policy()
          {
            public PermissionCollection getPermissions(CodeSource paramCodeSource)
            {
              Permissions localPermissions;
              (localPermissions = new Permissions()).add(new AllPermission());
              return localPermissions;
            }

            public void refresh()
            {
            }
          });
        }
        catch (Exception localException1)
        {
          System.out.println("Exception raising policy permissions: " + localException1.getMessage());
        }
        if (((String)localObject).toLowerCase().indexOf("windows") != -1)
          testComponent(getHslUserAppPath(), "jss4.dll", "785F57862F65B749CA31B4E6EFB76CF6C5ABCC05", paramString1 + "jss4.dll");
        else if (((String)localObject).toLowerCase().indexOf("mac os x") != -1)
          testComponent(getHslUserAppPath(), "libjss4.jnilib", "DE7DF47F06DA44FA1A21031772A9B76815C117EF", paramString1 + "libjss4.jnilib");
        else if (System.getProperty("os.arch").equals("i386"))
        {
          if (new File("/usr/lib/libnss3.so.1d").exists())
            testComponent(getHslUserAppPath(), "libjss4.so", "4CA6D3C7E7D47489383D1B2D5D1E68B4568A83EE", paramString1 + "libjss4_debian_x86.so");
          else
            testComponent(getHslUserAppPath(), "libjss4.so", "E2FD9DF339E2711652168E8CD473C6E9B6B09B71", paramString1 + "libjss4_x86.so");
        }
        else if ((System.getProperty("os.arch").equals("x86_64")) || (System.getProperty("os.arch").equals("amd64")))
        {
          if (new File("/usr/lib/libnss3.so.1d").exists())
            testComponent(getHslUserAppPath(), "libjss4.so", "CFB7E3A80F6A8617A68B8BB3CA604455B63EF37C", paramString1 + "libjss4_debian_x86_64.so");
          else
            testComponent(getHslUserAppPath(), "libjss4.so", "0B837101FFF442E3C93E35B32BFF769A96336188", paramString1 + "libjss4_x86_64.so");
        }
        else
          throw new Exception("Invalid OS arch: " + System.getProperty("os.arch"));
        System.setProperty("java.library.path", System.getProperty("java.library.path") + System.getProperty("path.separator") + getHslUserAppPath());
        Field localField;
        (localField = ClassLoader.class.getDeclaredField("sys_paths")).setAccessible(true);
        if (localField != null)
          localField.set(System.class.getClassLoader(), null);
        testComponent(getHslUserAppPath(), "jss4.jar", "E6B1ED0C763EF6E8F3D89F26BA6A82D0EC3BA6D2", paramString1 + "jss4.jar");
        addFile(getHslUserAppPath() + "jss4.jar");
      }
      testComponent(getHslUserAppPath(), "xercesImpl.jar", "4E647B47D0F5D9829D6ED12AE079686D2DF88CDB", paramString1 + "xercesImpl.jar");
      testComponent(getHslUserAppPath(), "xalan.jar", "10F170DA8DFBCDCC4098131BA773710F0BA7AEF1", paramString1 + "xalan.jar");
      testComponent(getHslUserAppPath(), "serializer.jar", "85DDD38E4CDBC22FB6C518F3D35744336DA6FBFD", paramString1 + "serializer.jar");
      addFile(getHslUserAppPath() + "xercesImpl.jar");
      addFile(getHslUserAppPath() + "xalan.jar");
      addFile(getHslUserAppPath() + "serializer.jar");
      return;
    }
    catch (Exception localException2)
    {
      Object localObject;
      (localObject = localException2).printStackTrace();
      throw new ESignDocException(37, "can't load jar files", (Exception)localObject);
    }
  }

  public static void testExternalComponents(String paramString)
    throws ESignDocException
  {
    Object localObject;
    try
    {
      Class.forName("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }
    catch (ClassNotFoundException localClassNotFoundException1)
    {
      (localObject = localClassNotFoundException1).printStackTrace();
      throw new ESignDocException(37, "xercesImpl.jar not found", (Exception)localObject);
    }
    try
    {
      Class.forName("org.apache.xml.serializer.DOMSerializer");
    }
    catch (ClassNotFoundException localClassNotFoundException2)
    {
      (localObject = localClassNotFoundException2).printStackTrace();
      throw new ESignDocException(37, "serializer.jar not found", (Exception)localObject);
    }
    try
    {
      Class.forName("org.apache.xalan.processor.TransformerFactoryImpl");
    }
    catch (ClassNotFoundException localClassNotFoundException3)
    {
      (localObject = localClassNotFoundException3).printStackTrace();
      throw new ESignDocException(37, "xalan.jar not found", (Exception)localObject);
    }
    try
    {
      Class.forName("javax.xml.parsers.DocumentBuilder");
    }
    catch (ClassNotFoundException localClassNotFoundException4)
    {
      (localObject = localClassNotFoundException4).printStackTrace();
      throw new ESignDocException(37, "xml-apis.jar not found", (Exception)localObject);
    }
    if ("NSS".equalsIgnoreCase(paramString))
      try
      {
        Class.forName("org.mozilla.jss.CryptoManager");
        try
        {
          (localObject = Class.forName("org.mozilla.jss.CryptoManager").getDeclaredField("JAR_JSS_VERSION")).setAccessible(true);
          String str = (String)((Field)localObject).get(null);
          System.out.println("JSS Version: " + str);
        }
        catch (Exception localException)
        {
          (localObject = localException).printStackTrace();
          return;
        }
      }
      catch (ClassNotFoundException localClassNotFoundException5)
      {
        (localObject = localClassNotFoundException5).printStackTrace();
        throw new ESignDocException(37, "jss34.jar not found", (Exception)localObject);
      }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     si.hermes.security.ClassPathHacker
 * JD-Core Version:    0.6.0
 */