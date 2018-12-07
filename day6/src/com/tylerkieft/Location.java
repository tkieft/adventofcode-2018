package com.tylerkieft;

import java.awt.*;
import java.util.Objects;

public class Location {

  private final int mId;
  private final Point mPoint;

  public Location(int id, Point point) {
    mId = id;
    mPoint = point;
  }

  public int getId() {
    return mId;
  }

  public Point getPoint() {
    return mPoint;
  }

  @Override
  public String toString() {
    return "[" + mId + ": " + mPoint + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return mId == location.mId &&
        mPoint.equals(location.mPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mPoint);
  }
}
