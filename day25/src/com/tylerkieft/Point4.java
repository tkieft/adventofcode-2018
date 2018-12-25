package com.tylerkieft;

public class Point4 {

  private final int x;
  private final int y;
  private final int z;
  private final int t;

  public Point4(int x, int y, int z, int t) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.t = t;
  }

  public int distanceTo(Point4 other) {
    return Math.abs(other.x - x) + Math.abs(other.y - y) + Math.abs(other.z - z) + Math.abs(other.t - t);
  }
}
