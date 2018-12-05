package com.tylerkieft;

import java.time.LocalDateTime;

public class LogEntryUnparsed {

  private final LocalDateTime mDateTime;
  private final String mAction;

  public LogEntryUnparsed(LocalDateTime dateTime, String action) {
    mDateTime = dateTime;
    mAction = action;
  }

  public LocalDateTime getDateTime() {
    return mDateTime;
  }

  public String getAction() {
    return mAction;
  }
}
