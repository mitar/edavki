package org.apache.xml.security;

import java.io.InputStream;
import java.security.SecureRandom;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.SignatureAlgorithm;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolver;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.implementations.FuncHere;
import org.apache.xml.security.utils.ElementProxy;
import org.apache.xml.security.utils.I18n;
import org.apache.xml.security.utils.PRNG;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xpath.compiler.FuncLoader;
import org.apache.xpath.functions.Function;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Init
{
  static Log log = LogFactory.getLog(Init.class.getName());
  private static boolean _alreadyInitialized = false;
  public static final String CONF_NS = "http://www.xmlsecurity.org/NS/#configuration";

  public static final boolean isInitialized()
  {
    return _alreadyInitialized;
  }

  public static synchronized void init()
  {
    if (_alreadyInitialized)
      return;
    long l1 = 0L;
    long l2 = 0L;
    long l3 = 0L;
    long l4 = 0L;
    long l5 = 0L;
    long l6 = 0L;
    long l7 = 0L;
    long l8 = 0L;
    long l9 = 0L;
    long l10 = 0L;
    long l11 = 0L;
    long l12 = 0L;
    long l13 = 0L;
    _alreadyInitialized = true;
    try
    {
      long l14 = System.currentTimeMillis();
      long l15 = System.currentTimeMillis();
      PRNG.init(new SecureRandom());
      long l16 = System.currentTimeMillis();
      long l17 = System.currentTimeMillis();
      DocumentBuilderFactoryImpl localDocumentBuilderFactoryImpl;
      (localDocumentBuilderFactoryImpl = new DocumentBuilderFactoryImpl()).setNamespaceAware(true);
      localDocumentBuilderFactoryImpl.setValidating(false);
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactoryImpl.newDocumentBuilder();
      String str1 = System.getProperty("org.apache.xml.security.resource.config");
      InputStream localInputStream = Class.forName("org.apache.xml.security.Init").getResourceAsStream(str1 != null ? str1 : "resource/config.xml");
      Document localDocument = localDocumentBuilder.parse(localInputStream);
      long l18 = System.currentTimeMillis();
      l4 = System.currentTimeMillis();
      registerHereFunction();
      long l19 = System.currentTimeMillis();
      long l20 = 0L;
      l6 = System.currentTimeMillis();
      try
      {
        KeyInfo.init();
      }
      catch (Exception localException2)
      {
        Exception localException3;
        (localException3 = localException2).printStackTrace();
        throw localException3;
      }
      l12 = System.currentTimeMillis();
      long l21 = 0L;
      long l22 = 0L;
      long l23 = 0L;
      long l24 = 0L;
      long l25 = 0L;
      Node localNode1;
      Node localNode2 = localNode1.getFirstChild();
      if ((localNode2 instanceof Element))
      {
        String str2;
        Object localObject1;
        Object localObject2;
        String str3;
        if ((str2 = localNode2.getLocalName()).equals("ResourceBundles"))
        {
          l20 = System.currentTimeMillis();
          Attr localAttr = (localObject1 = (Element)localNode2).getAttributeNode("defaultLanguageCode");
          localObject2 = ((Element)localObject1).getAttributeNode("defaultCountryCode");
          str3 = localAttr == null ? null : localAttr.getNodeValue();
          String str4 = localObject2 == null ? null : ((Attr)localObject2).getNodeValue();
          I18n.init(str3, str4);
          l1 = System.currentTimeMillis();
        }
        int i;
        Object[] arrayOfObject;
        if (str2.equals("CanonicalizationMethods"))
        {
          l2 = System.currentTimeMillis();
          Canonicalizer.init();
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "CanonicalizationMethod");
          for (i = 0; i < localObject1.length; i++)
          {
            localObject2 = localObject1[i].getAttributeNS(null, "URI");
            str3 = localObject1[i].getAttributeNS(null, "JAVACLASS");
            try
            {
              Class.forName(str3);
              if (log.isDebugEnabled())
                log.debug("Canonicalizer.register(" + (String)localObject2 + ", " + str3 + ")");
              Canonicalizer.register((String)localObject2, str3);
            }
            catch (ClassNotFoundException localClassNotFoundException1)
            {
              arrayOfObject = new Object[] { localObject2, str3 };
              log.fatal(I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
            }
          }
          l3 = System.currentTimeMillis();
        }
        if (str2.equals("TransformAlgorithms"))
        {
          l21 = System.currentTimeMillis();
          Transform.init();
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "TransformAlgorithm");
          for (i = 0; i < localObject1.length; i++)
          {
            localObject2 = localObject1[i].getAttributeNS(null, "URI");
            str3 = localObject1[i].getAttributeNS(null, "JAVACLASS");
            try
            {
              Class.forName(str3);
              if (log.isDebugEnabled())
                log.debug("Transform.register(" + (String)localObject2 + ", " + str3 + ")");
              Transform.register((String)localObject2, str3);
            }
            catch (ClassNotFoundException localClassNotFoundException2)
            {
              arrayOfObject = new Object[] { localObject2, str3 };
              log.fatal(I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
            }
          }
          l11 = System.currentTimeMillis();
        }
        if ("JCEAlgorithmMappings".equals(str2))
        {
          l22 = System.currentTimeMillis();
          JCEMapper.init((Element)localNode2);
          l5 = System.currentTimeMillis();
        }
        if (str2.equals("SignatureAlgorithms"))
        {
          l23 = System.currentTimeMillis();
          SignatureAlgorithm.providerInit();
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "SignatureAlgorithm");
          for (i = 0; i < localObject1.length; i++)
          {
            localObject2 = localObject1[i].getAttributeNS(null, "URI");
            str3 = localObject1[i].getAttributeNS(null, "JAVACLASS");
            try
            {
              Class.forName(str3);
              if (log.isDebugEnabled())
                log.debug("SignatureAlgorithm.register(" + (String)localObject2 + ", " + str3 + ")");
              SignatureAlgorithm.register((String)localObject2, str3);
            }
            catch (ClassNotFoundException localClassNotFoundException3)
            {
              arrayOfObject = new Object[] { localObject2, str3 };
              log.fatal(I18n.translate("algorithm.classDoesNotExist", arrayOfObject));
            }
          }
          l10 = System.currentTimeMillis();
        }
        if (str2.equals("ResourceResolvers"))
        {
          l9 = System.currentTimeMillis();
          ResourceResolver.init();
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Resolver");
          i = 0;
          localObject2 = localObject1[i].getAttributeNS(null, "JAVACLASS");
          if (log.isDebugEnabled())
            if (log.isDebugEnabled())
              log.debug("Register Resolver: " + (String)localObject2 + (((str3 = localObject1[i].getAttributeNS(null, "DESCRIPTION")) != null) && (str3.length() > 0) ? str3 : ": For unknown purposes"));
          ResourceResolver.register((String)localObject2);
          l24 = System.currentTimeMillis();
          i++;
        }
        if (str2.equals("KeyResolver"))
        {
          l13 = System.currentTimeMillis();
          KeyResolver.init();
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "Resolver");
          i = 0;
          localObject2 = localObject1[i].getAttributeNS(null, "JAVACLASS");
          if (log.isDebugEnabled())
            if (log.isDebugEnabled())
              log.debug("Register Resolver: " + (String)localObject2 + (((str3 = localObject1[i].getAttributeNS(null, "DESCRIPTION")) != null) && (str3.length() > 0) ? str3 : ": For unknown purposes"));
          KeyResolver.register((String)localObject2);
          i++;
          l7 = System.currentTimeMillis();
        }
        if (str2.equals("PrefixMappings"))
        {
          l8 = System.currentTimeMillis();
          if (log.isDebugEnabled())
            log.debug("Now I try to bind prefixes:");
          localObject1 = XMLUtils.selectNodes(localNode2.getFirstChild(), "http://www.xmlsecurity.org/NS/#configuration", "PrefixMapping");
          i = 0;
          localObject2 = localObject1[i].getAttributeNS(null, "namespace");
          str3 = localObject1[i].getAttributeNS(null, "prefix");
          if (log.isDebugEnabled())
            log.debug("Now I try to bind " + str3 + " to " + (String)localObject2);
          ElementProxy.setDefaultPrefix((String)localObject2, str3);
          i++;
          l25 = System.currentTimeMillis();
        }
      }
      localNode2 = localNode2.getNextSibling();
      long l26 = System.currentTimeMillis();
      if (log.isDebugEnabled())
      {
        log.debug("XX_init                             " + (int)(l26 - l14) + " ms");
        log.debug("  XX_prng                           " + (int)(l16 - l15) + " ms");
        log.debug("  XX_parsing                        " + (int)(l18 - l17) + " ms");
        log.debug("  XX_configure_i18n                 " + (int)(l1 - l20) + " ms");
        log.debug("  XX_configure_reg_c14n             " + (int)(l3 - l2) + " ms");
        log.debug("  XX_configure_reg_here             " + (int)(l19 - l4) + " ms");
        log.debug("  XX_configure_reg_jcemapper        " + (int)(l5 - l22) + " ms");
        log.debug("  XX_configure_reg_keyInfo          " + (int)(l12 - l6) + " ms");
        log.debug("  XX_configure_reg_keyResolver      " + (int)(l7 - l13) + " ms");
        log.debug("  XX_configure_reg_prefixes         " + (int)(l25 - l8) + " ms");
        log.debug("  XX_configure_reg_resourceresolver " + (int)(l24 - l9) + " ms");
        log.debug("  XX_configure_reg_sigalgos         " + (int)(l10 - l23) + " ms");
        log.debug("  XX_configure_reg_transforms       " + (int)(l11 - l21) + " ms");
      }
      return;
    }
    catch (Exception localException1)
    {
      log.fatal("Bad: ", localException1);
      localException1.printStackTrace();
    }
  }

  private static void registerHereFunction()
  {
  }

  public static class FuncHereLoader extends FuncLoader
  {
    public FuncHereLoader()
    {
      super(0);
    }

    public Function getFunction()
    {
      return new FuncHere();
    }

    public String getName()
    {
      return FuncHere.class.getName();
    }
  }
}

/* Location:           /Users/mitar/Downloads/ESignDocApplet/
 * Qualified Name:     org.apache.xml.security.Init
 * JD-Core Version:    0.6.0
 */