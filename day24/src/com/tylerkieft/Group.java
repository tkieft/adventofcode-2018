package com.tylerkieft;

import java.util.List;

public class Group {

  enum Type {
    IMMUNE_SYSTEM,
    INFECTION
  }

  private final Type mType;
  private final int mId;

  private int mUnits;

  private final int mHitPoints;
  private final List<String> mImmunities;
  private final List<String> mWeaknesses;
  private final int mAttackPower;
  private final String mAttackType;
  private final int mInitiative;

  public Group(Type type,
               int id,
               int units,
               int hitPoints,
               List<String> immunities,
               List<String> weaknesses,
               int attackPower,
               String attackType,
               int initiative) {
    mType = type;
    mId = id;
    mUnits = units;
    mHitPoints = hitPoints;
    mImmunities = immunities;
    mWeaknesses = weaknesses;
    mAttackPower = attackPower;
    mAttackType = attackType;
    mInitiative = initiative;
  }

  public int attackDamage(Group attacker) {
    int damage = attacker.getEffectivePower();

    if (mImmunities.contains(attacker.getAttackType())) {
      damage = 0;
    } else if (mWeaknesses.contains(attacker.getAttackType())) {
      damage *= 2;
    }

    return damage;
  }

  public void attackBy(Group attacker) {
    int unitsLost = attackDamage(attacker) / mHitPoints;
    mUnits = Math.max(mUnits - unitsLost, 0);
  }

  public Type getType() {
    return mType;
  }

  public int getId() {
    return mId;
  }

  public int getUnits() {
    return mUnits;
  }

  public int getEffectivePower() {
    return mUnits * mAttackPower;
  }

  public boolean isDead() {
    return mUnits == 0;
  }

  public String getAttackType() {
    return mAttackType;
  }

  public int getInitiative() {
    return mInitiative;
  }

  @Override
  public String toString() {
    return mUnits +" units each with " + mHitPoints + " hit points (" +
        (mImmunities.size() > 0 ? "immune to " + String.join(", ", mImmunities) + "; " : "") +
        (mWeaknesses.size() > 0 ? "weak to " + String.join(", ", mWeaknesses) : "") +
        ") with an attack that does " + mAttackPower + " " + mAttackType + " damage at initiative " + mInitiative;
  }
}
