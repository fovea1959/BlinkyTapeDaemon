package com.whirlpool.isec.blinkytape;

import java.net.*;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer implements Runnable {

  public static void main(String[] args) {
    Exception boom = null;
    try {
      TcpServer tcpServer = new TcpServer(1357);
      Thread thread = new Thread(tcpServer);
      thread.start();
      try {
        Thread.sleep(60000);
      } catch (InterruptedException ex) {
      }
      tcpServer.setDieFlag();
    } catch (Exception ex) {
      boom = ex;
    }
    if (boom != null)
      boom.printStackTrace();
  }
  
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
      ss.setSoTimeout(100);
      while (!dieFlag) {
        Socket s = null;
        try {
          s = ss.accept();
        } catch (InterruptedIOException ex) {
          // the accept timed out
        }
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
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      if (ss != null) {
        try {
          ss.close();
        } catch (Exception ex) {
        }
      }
    }

    // should add finally block to close down
  }

  public void setDieFlag() {
    dieFlag = true;
    try {
      ss.close();
    } catch (IOException ex) {

    }
  }

  class Connection implements Runnable {
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

    String process(String request) {
      return request.toUpperCase();
    }

    public void run() {
      logger.info("starting connection {}", from_name);
      long last = System.currentTimeMillis();
      try {
        while (!connectionDieFlag) {
          long now = System.currentTimeMillis();
          if (in.ready()) {
            String clientSentence = in.readLine();
            logger.info ("received {}", clientSentence);
            String result = process(clientSentence);
            out.println(result);
            out.flush();
          } else {
            try {
              Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
          }
          if (now - last > 1000) {
            out.println (new Date(now));
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
  }
}
