package com.tylerkieft;

public class Nanobot {

  private final Point3 mPoint;
  private final long mSignalRadius;

  public Nanobot(Point3 point, long signalRadius) {
    mPoint = point;
    mSignalRadius = signalRadius;
  }

  public Point3 getPoint() {
    return mPoint;
  }

  public long getSignalRadius() {
    return mSignalRadius;
  }

  public long distanceTo(Point3 point) {
    return mPoint.distanceTo(point);
  }

  public long distanceTo(Nanobot other) {
    return distanceTo(other.getPoint());
  }
}
