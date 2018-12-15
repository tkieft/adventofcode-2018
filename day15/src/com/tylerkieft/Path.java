package com.tylerkieft;

import java.util.List;

/**
 * A path, which uses the current location as its starting point
 */
public class Path {

  private final List<Location> mLocations;

  public Path(List<Location> locations) {
    mLocations = locations;
  }

  public int getLength() {
    return mLocations.size() - 1;
  }

  /**
   * @return The first step after the starting location
   */
  public Location getFirstStep() {
    return mLocations.get(1);
  }

}
