package com.tylerkieft;

import java.awt.*;

public class Location {

  enum Type {
    OPEN,
    WALL;
  }

  private final Type mType;
  private final int mX;
  private final int mY;
  private Unit mUnit;

  public Location(Type type, int x, int y) {
    mType = type;
    mX = x;
    mY = y;
  }

  public Type getType() {
    return mType;
  }

  public int getX() {
    return mX;
  }

  public int getY() {
    return mY;
  }

  public boolean hasUnit() {
    return mUnit != null;
  }

  public Unit getUnit() {
    return mUnit;
  }

  public void setUnit(Unit unit) {
    mUnit = unit;
  }

  @Override
  public String toString() {
    if (mType == Type.WALL) {
      return "#";
    }
    if (mUnit == null) {
      return ".";
    }
    return mUnit.getType() == Unit.Type.ELF ? "E" : "G";
  }
}
