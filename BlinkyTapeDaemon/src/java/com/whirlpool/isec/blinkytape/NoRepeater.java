package com.whirlpool.isec.blinkytape;

import java.util.*;

public class NoRepeater {
  private long period = 60000; // one minute 
  String f = null;
  public NoRepeater (long period, String f) {
    this.period = period;
    this.f = f;
  }
  
  Set<String> seenAtAll = new HashSet<String>();
  Map<String, NoRepeaterResult> status = new HashMap<String, NoRepeaterResult>();
  
  public NoRepeaterResult somethingHappened (String name) {
    long now = System.currentTimeMillis();
    
    boolean needToReport = false;
    
    if (!seenAtAll.contains(name)) needToReport = true;
    seenAtAll.add(name);
    
    NoRepeaterResult r = status.get(name);
    if (r == null) {
      r = new NoRepeaterResult(name);
    }
    if (r.getSince() == null) {
      r.setSince(now);
    }
    r.bumpCount();
    r.setLast(now);
    
    if ((r.getLast() - r.getSince()) > period) {
      needToReport = true;
    }
    
    if (needToReport) {
      NoRepeaterResult replacement = new NoRepeaterResult(name);
      status.put(name, replacement);
    } else {
      r = null;
    }
    
    return r;
  }
  
  public class NoRepeaterResult {
    Long since, last;
    int count = 0;
    String fSpecific;
    public NoRepeaterResult (String n) {
      fSpecific = String.format (f, n);
    }
    public Long getSince() {
      return since;
    }
    public void setSince(Long since) {
      this.since = since;
    }
    public int getCount() {
      return count;
    }
    public int bumpCount() {
      return ++count;
    }
    public int zapCount() {
      return count = 0;
    }
    public Long getLast() {
      return last;
    }
    public void setLast(Long last) {
      this.last = last;
    }
    public String message() {
      if (count == 1) {
        return fSpecific; 
      } else {
        return String.format ("%s %d times since %s", fSpecific, count, new Date(since));
      }
    }
  }

}
