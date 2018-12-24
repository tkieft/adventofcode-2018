package com.tylerkieft;

public class Point3 {

  public final long x;
  public final long y;
  public final long z;

  public Point3(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public long distanceTo(Point3 other) {
    return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
  }

  @Override
  public String toString() {
    return "<" + x + "," + y + "," + z + ">";
  }
}
