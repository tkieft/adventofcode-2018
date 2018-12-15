package com.tylerkieft;

public class Unit {

  enum Type {
    ELF,
    GOBLIN
  }

  private final Type mType;
  private final int mAttackPower;
  private int mHitPoints;
  private Location mLocation;

  public Unit(Type type, Location location, int attackPower) {
    mType = type;
    mLocation = location;
    mAttackPower = attackPower;
    mHitPoints = 200;
  }

  public Type getType() {
    return mType;
  }

  public Location getLocation() {
    return mLocation;
  }

  public void setLocation(Location location) {
    mLocation = location;
  }

  public int getAttackPower() {
    return mAttackPower;
  }

  public int getHitPoints() {
    return mHitPoints;
  }

  public void attackBy(Unit attackingUnit) {
    mHitPoints -= attackingUnit.getAttackPower();
  }

  public boolean isAlive() {
    return mHitPoints > 0;
  }

  public boolean isDead() {
    return mHitPoints <= 0;
  }
}
