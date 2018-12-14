package com.tylerkieft;

import java.awt.Point;
import java.util.List;

public class Car {

  private final Point mLocation;
  private Direction mDirection;
  private Turn mNextTurn;

  public static boolean isCarCharacter(char c) {
    return c == '^' || c == '>' || c == 'v' || c == '<';
  }

  private static Direction characterToDirection(char c) {
    switch (c) {
      case '^': return Direction.NORTH;
      case '>': return Direction.EAST;
      case 'v': return Direction.SOUTH;
      case '<': return Direction.WEST;
    }
    throw new RuntimeException("Not a car character: " + c);
  }

  public static Car from(Character character, Point location) {
    return new Car(location, characterToDirection(character));
  }

  public Car(Point location, Direction direction) {
    mLocation = location;
    mDirection = direction;
    mNextTurn = Turn.LEFT;
  }

  public void advance(List<List<Character>> map) {
    mDirection.advance(mLocation);
    char c = map.get(mLocation.y).get(mLocation.x);
    switch (c) {
      case '\\':
      case '/':
        mDirection = mDirection.corner(c);
        break;
      case '-':
      case '|':
        // nothing to do
        break;
      case '+':
        mDirection = mDirection.turn(mNextTurn);
        mNextTurn = mNextTurn.nextTurn();
        break;
    }
  }

  public Point getLocation() {
    return mLocation;
  }

  public Direction getDirection() {
    return mDirection;
  }

}
