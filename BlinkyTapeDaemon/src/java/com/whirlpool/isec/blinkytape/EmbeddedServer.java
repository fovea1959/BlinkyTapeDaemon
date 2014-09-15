package com.whirlpool.isec.blinkytape;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.xml.sax.SAXException;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.config.TapeConfig;

public class EmbeddedServer {
  public static Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

  private final static Config config = Config.getInstance();

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    // Optionally remove existing handlers attached to j.u.l root logger
    SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

    // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
    // the initialization phase of your application
    SLF4JBridgeHandler.install();

    // not sure if this is necessary
    java.util.logging.Logger.getLogger("org.glassfish.jersey.servlet").setLevel(
        java.util.logging.Level.ALL);

    Util.setupConverters();

    Throwable boom = null;
    try {
      EmbeddedServer embeddedServer = new EmbeddedServer();
      embeddedServer.go();
    } catch (Exception ex) {
      boom = ex;
    }

    if (boom != null) {
      logger.error("Died!", boom);
      System.exit(1);
    } else {
      System.exit(0);
    }
  }

  void go() throws Exception {

    config();

    // System.exit(0);

    Server server = new Server();

    ServerConnector connector = new ServerConnector(server);
    connector.setPort(9999);

    HttpConfiguration https = new HttpConfiguration();
    https.addCustomizer(new SecureRequestCustomizer());
    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(EmbeddedServer.class.getResource("/keystore.jks")
        .toExternalForm());
    sslContextFactory.setKeyStorePassword("123456");
    sslContextFactory.setKeyManagerPassword("123456");
    ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(
        sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
    sslConnector.setPort(9998);

    server.setConnectors(new Connector[] { connector, sslConnector });

    WebAppContext context = new WebAppContext();
    context.setServer(server);
    context.setContextPath("/");

    ProtectionDomain protectionDomain = EmbeddedServer.class.getProtectionDomain();
    URL location = protectionDomain.getCodeSource().getLocation();
    context.setWar(location.toExternalForm());

    server.setHandler(context);
    while (true) {
      try {
        server.start();
        break;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // Need to test this

    TapeConfig tapeConfig = config.getTapeConfigs().get(0);

    // need to look at configuration and make the proper tapes from config.
    // Perhaps methods in tapeconfig
    // get moved to AbstractTape?

    Tape tape = new Tape(tapeConfig);

    Thread tapeThread = new Thread(tape);

    tapeThread.start();

    while (!config.isShutdownRequested()) {
      // TODO figure out shutdown here
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {
        //
      }
    }

    tape.setDieFlag(true);
    tapeThread.join();
    tape.close();
    server.stop();
    server.join();
  }

  public static void config() throws IOException, SAXException {
    // ConvertUtils.register(new ColorConverter(), Color.class);
    Config config = Config.getInstance();

    DigesterLoader loader = DigesterLoader.newLoader(new MyRulesModule());
    Digester digester = loader.newDigester();

    digester.push(config);
    digester.parse(new File("config.xml"));

    config.postParse();

    System.out.println(config);

  }

  static class MyRulesModule extends FromXmlRulesModule {

    @Override
    protected void loadRules() {
      loadXMLRules(EmbeddedServer.class.getResourceAsStream("/config-rules.xml"));
    }

  }
}
