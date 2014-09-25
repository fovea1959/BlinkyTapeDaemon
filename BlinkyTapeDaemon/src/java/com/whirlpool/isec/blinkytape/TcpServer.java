package com.whirlpool.isec.blinkytape;

import java.net.*;
import java.awt.Color;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.tapes.ITape;

public class TcpServer implements Runnable {

  Logger logger = LoggerFactory.getLogger(getClass());

  private Vector<Connection> connectiontable = new Vector<Connection>();

  private boolean dieFlag = false;

  private int port;

  private ServerSocket ss = null;

  public TcpServer(int port) {
    this.port = port;
  }

  public void run() {
    try {
      ss = new ServerSocket(port);
      while (!dieFlag) {
        Socket s = ss.accept();
        if (dieFlag)
          break;
        if (s != null) {
          logger.info("recieved connection {}", s);
          // Connection received - create a thread
          Connection c = new Connection(s);
          Thread current = new Thread(c);
          current.setDaemon(true);
          connectiontable.addElement(c);
          current.start();
        } else {
          logger.error("huh. got a null Socket from the accept.");
        }
      }
    } catch (Exception e) {
      if (!dieFlag)
        logger.error("accept loop died!", e);
    } finally {
      if (ss != null) {
        try {
          ss.close();
        } catch (Exception ex) {
        }
      }
    }
  }

  public void setDieFlag() {
    dieFlag = true;
    try {
      ss.close();
    } catch (IOException ex) {

    }
  }

  class Connection implements Runnable, ITape {
    Socket linkto; // the socket

    PrintWriter out; // the output and input streams

    BufferedReader in;

    String from_name; // name of host connecting

    boolean connectionDieFlag = false;

    Connection(Socket from) {
      linkto = from;
      InetAddress source = linkto.getInetAddress();
      from_name = source.getHostName() + ":" + from.getPort();
      try {
        out = new PrintWriter(new OutputStreamWriter(linkto.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(linkto.getInputStream()));
      } catch (Exception e) {
      }
    }

    void process(String request) {
      if (request.equalsIgnoreCase("q")) {
        out.println ("dying");
        connectionDieFlag = true;
      }
      if (request.equalsIgnoreCase("c")) {
        out.println ("connecting");
        Config.getInstance().getTapeConfigs().get(0).addTape(this);
      }
      if (request.equalsIgnoreCase("d")) {
        out.println ("disconnecting");
        Config.getInstance().getTapeConfigs().get(0).removeTape(this);
      }
      out.flush();
    }

    public void run() {
      logger.info("starting connection {}", from_name);
      long last = System.currentTimeMillis();
      try {
        while (!connectionDieFlag) {
          long now = System.currentTimeMillis();
          if (in.ready()) {
            String clientSentence = in.readLine();
            process(clientSentence);
            logger.info("received {}", clientSentence);
          } else {
            try {
              Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
          }
          if (now - last > 1000) {
            out.println(new Date(now));
            out.flush();
            last = now;
          }
          if (out.checkError()) {
            logger.error("checkError");
            connectionDieFlag = true;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        logger.info("ending connection {}", from_name);
        
        Config.getInstance().getTapeConfigs().get(0).removeTape(this);
        
        connectiontable.removeElement(this);
        try {
          out.close(); // closes needed to terminate connection
        } catch (Exception e) {
        }
        try {
          in.close(); // otherwise user's window goes mute
        } catch (Exception e) {
        }
        try {
          linkto.close();
        } catch (Exception e) {
        }
      }
    }
    
    StringBuffer sb = new StringBuffer();

    @Override
    public void update(Color[] leds) {
      logger.info("updating LEDS");
      sb.setLength(0);
      sb.append("leds");
      for (Color c : leds) {
        sb.append(",");
        sb.append (Util.colorToHex(c));
      }
      out.println(sb.toString());
      out.flush();
    }

    @Override
    public void close() {
      // TODO Auto-generated method stub
      
    }
  }
}
