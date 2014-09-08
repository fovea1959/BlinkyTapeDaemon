package com.whirlpool.isec.blinkytape.eventqueue;

public abstract class EventQueueEntryImpl implements
    Comparable<EventQueueEntryImpl> {
  private Long eventTime;

  public EventQueueEntryImpl(long t0) {
    super();
    this.eventTime = t0;
  }

  @Override
  public final int compareTo(EventQueueEntryImpl o) {
    return eventTime.compareTo(o.eventTime);
  }

}
