package com.tylerkieft;

import java.time.LocalDateTime;

public class LogEntry {

  public enum Type {
    BEGINS_SHIFT,
    FALLS_ASLEEP,
    WAKES_UP;

    public static Type fromString(String string) {
      return string.equals("falls asleep") ? FALLS_ASLEEP :
          string.equals("wakes up") ? WAKES_UP :
              BEGINS_SHIFT;
    }
  }

  private final LocalDateTime mDateTime;
  private final String mId;
  private final Type mType;

  public LogEntry(LocalDateTime dateTime, String id, Type type) {
    mDateTime = dateTime;
    mId = id;
    mType = type;
  }

  public LocalDateTime getDateTime() {
    return mDateTime;
  }

  public String getId() {
    return mId;
  }

  public Type getType() {
    return mType;
  }

  @Override
  public String toString() {
    return "LogEntry{" +
        "mDateTime=" + mDateTime +
        ", mId='" + mId + '\'' +
        ", mType=" + mType +
        '}';
  }
}
