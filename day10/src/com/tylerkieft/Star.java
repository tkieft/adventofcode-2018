package com.tylerkieft;

import java.awt.*;

public class Star {

  private final Point mPosition;
  private final Point mVelocity;

  public Star(Point position, Point velocity) {
    mPosition = position;
    mVelocity = velocity;
  }

  public Point getPosition() {
    return mPosition;
  }

  public Point getVelocity() {
    return mVelocity;
  }

  @Override
  public String toString() {
    return "Star{" +
        "mPosition=" + mPosition +
        ", mVelocity=" + mVelocity +
        '}';
  }
}
