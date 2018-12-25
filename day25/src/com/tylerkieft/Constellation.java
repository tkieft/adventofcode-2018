package com.tylerkieft;

import java.util.ArrayList;
import java.util.List;

public class Constellation {

  private final List<Point4> mPoints;

  public Constellation(Point4 point) {
    mPoints = new ArrayList<>();
    mPoints.add(point);
  }

  public void mergeWith(Constellation other) {
    mPoints.addAll(other.mPoints);
  }

  public int distanceTo(Constellation other) {
    int minDistance = Integer.MAX_VALUE;

    for (Point4 point : mPoints) {
      for (Point4 otherPoint : other.mPoints) {
        if (point.distanceTo(otherPoint) < minDistance) {
          minDistance = point.distanceTo(otherPoint);
        }
      }
    }

    return minDistance;
  }
}
