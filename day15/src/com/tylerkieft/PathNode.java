package com.tylerkieft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathNode {

  private final PathNode mParent;
  private final Location mLocation;

  public PathNode(PathNode parent, Location location) {
    mParent = parent;
    mLocation = location;
  }

  public Location getLocation() {
    return mLocation;
  }

  public Path path() {
    List<Location> locations = new ArrayList<>();

    PathNode node = this;
    while (node != null) {
      locations.add(node.mLocation);
      node = node.mParent;
    }

    Collections.reverse(locations);

    return new Path(locations);
  }
}
