package com.tylerkieft;

import java.awt.*;

public enum Direction {
  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  private final int mX;
  private final int mY;

  Direction(int x, int y) {
    mX = x;
    mY = y;
  }

  public void advance(Point point) {
    point.translate(mX, mY);
  }

  public Direction corner(char c) {
    switch (this) {
      case NORTH: return c == '/' ? EAST : WEST;
      case SOUTH: return c == '/' ? WEST : EAST;
      case WEST:  return c == '/' ? SOUTH : NORTH;
      case EAST:  return c == '/' ? NORTH : SOUTH;
    }
    throw new RuntimeException();
  }

  public Direction turn(Turn turn) {
    switch (this) {
      case NORTH: return turn == Turn.STRAIGHT ? NORTH : (turn == Turn.RIGHT ? EAST  : WEST);
      case SOUTH: return turn == Turn.STRAIGHT ? SOUTH : (turn == Turn.RIGHT ? WEST  : EAST);
      case WEST:  return turn == Turn.STRAIGHT ? WEST  : (turn == Turn.RIGHT ? NORTH : SOUTH);
      case EAST:  return turn == Turn.STRAIGHT ? EAST  : (turn == Turn.RIGHT ? SOUTH : NORTH);
    }
    throw new RuntimeException();
  }
}
