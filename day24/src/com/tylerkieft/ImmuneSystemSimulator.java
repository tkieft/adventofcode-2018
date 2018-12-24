package com.tylerkieft;

import java.util.*;

public class ImmuneSystemSimulator {

  private final List<Group> mImmuneSystem;
  private final List<Group> mInfection;

  public ImmuneSystemSimulator(List<Group> immuneSystem, List<Group> infection) {
    mImmuneSystem = immuneSystem;
    mInfection = infection;
  }

  private void doTurn() {
    // ----------------
    // TARGET SELECTION
    // ----------------

    Map<Group, Group> targets = new HashMap<>();

    // Each group can only be targeted by at most one other group, keep track of that here
    List<Group> immuneSystemTargetsAvailable = new ArrayList<>(mImmuneSystem);
    List<Group> infectionTargetsAvailable = new ArrayList<>(mInfection);


    List<Group> groups = new ArrayList<>();
    groups.addAll(mImmuneSystem);
    groups.addAll(mInfection);

    // Sort descending by type, effective power, initiative
    groups.sort((g1, g2) -> {
      if (g1.getType() != g2.getType()) return g2.getType() == Group.Type.IMMUNE_SYSTEM ? -1 : 1;
      int effectivePowerDifference = g2.getEffectivePower() - g1.getEffectivePower();
      return effectivePowerDifference == 0 ? g2.getInitiative() - g1.getInitiative() : effectivePowerDifference;
    });

    // Choose a target
    for (Group attacker : groups) {
      List<Group> defenders = attacker.getType() == Group.Type.IMMUNE_SYSTEM ? infectionTargetsAvailable : immuneSystemTargetsAvailable;

      // get max of attack damage, effective power, initiative
      Optional<Group> optionalDefender = defenders.stream().max(
          (g1, g2) -> {
            int attackDamageDifference = g1.attackDamage(attacker) - g2.attackDamage(attacker);
            if (attackDamageDifference != 0) return attackDamageDifference;

            int effectivePowerDifference = g1.getEffectivePower() - g2.getEffectivePower();
            if (effectivePowerDifference != 0) return effectivePowerDifference;

            return g1.getInitiative() - g2.getInitiative();
          });

      for (Group defender : defenders) {
//        System.out.println((attacker.getType() == Group.Type.IMMUNE_SYSTEM ? "Immune system " : "Infection ") +
//            "group " + attacker.getId() + " would deal defending group " + defender.getId() + " " +
//            defender.attackDamage(attacker) + " damage");
      }

      if (optionalDefender.isPresent() && optionalDefender.get().attackDamage(attacker) > 0) {
        targets.put(attacker, optionalDefender.get());
        defenders.remove(optionalDefender.get());
      }
    }

    //System.out.println();

    // ------
    // ATTACK
    // ------

    // Sort descending by initiative
    groups.sort((g1, g2) -> g2.getInitiative() - g1.getInitiative());

    // Attack!
    for (Group attacker : groups) {
      Group defender = targets.get(attacker);

      if (defender != null) {
        int unitsBefore = defender.getUnits();
        targets.get(attacker).attackBy(attacker);

//        System.out.println((attacker.getType() == Group.Type.IMMUNE_SYSTEM ? "Immune system " : "Infection ") +
//            "group " + attacker.getId() + " attacks defending group " + targets.get(attacker).getId() +
//            ", killing " + (unitsBefore - defender.getUnits()) + " units");
      }
    }
//    System.out.println();

    // Remove dead groups

    Iterator<Group> groupIterator = mImmuneSystem.iterator();
    while (groupIterator.hasNext()) {
      Group group = groupIterator.next();
      if (group.isDead()) {
        groupIterator.remove();
      }
    }

    groupIterator = mInfection.iterator();
    while (groupIterator.hasNext()) {
      Group group = groupIterator.next();
      if (group.isDead()) {
        groupIterator.remove();
      }
    }
  }

  private static int totalUnitsRemaining(List<Group> army) {
    return army.stream().mapToInt(Group::getUnits).sum();
  }

  public int simulate() {
    while (mInfection.size() > 0 && mImmuneSystem.size() > 0) {
      int totalUnits =  totalUnitsRemaining(mInfection) + totalUnitsRemaining(mImmuneSystem);
      // System.out.println(this);
      doTurn();

      if (totalUnitsRemaining(mInfection) + totalUnitsRemaining(mImmuneSystem) == totalUnits) {
        // we're at a draw
        return 0;
      }
    }

    return totalUnitsRemaining(mInfection.size() > 0 ? mInfection : mImmuneSystem);
  }

  public boolean immuneSystemWon() {
    return mInfection.size() == 0 && mImmuneSystem.size() > 0;
  }

  public String startState() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Immune System:\n");
    for (Group group : mImmuneSystem) {
      stringBuilder.append(group + "\n");
    }
    stringBuilder.append("\nInfection:\n");
    for (Group group : mInfection) {
      stringBuilder.append(group + "\n");
    }
    return stringBuilder.toString();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Immune System:\n");
    for (Group group : mImmuneSystem) {
      stringBuilder.append("Group " + group.getId() + " contains " + group.getUnits() + " units\n");
    }
    stringBuilder.append("\nInfection:\n");
    for (Group group : mInfection) {
      stringBuilder.append("Group " + group.getId() + " contains " + group.getUnits() + " units\n");
    }
    return stringBuilder.toString();
  }
}

// 4087 = too low
// 18589 = too low
// 19322 = too high
// 19293 = not right