package com.tylerkieft;

public enum Turn {
  LEFT,
  STRAIGHT,
  RIGHT;

  public Turn nextTurn() {
    if (this == LEFT) {
      return STRAIGHT;
    } else if (this == STRAIGHT) {
      return RIGHT;
    } else {
      return LEFT;
    }
  }
}
