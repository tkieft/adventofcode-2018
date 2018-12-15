package com.tylerkieft;

public class Main {

  public static void main(String[] args) {
    String filename = args[0];

    // Part 1
    Combat combat = Combat.fromFile(filename, 3);
    int elfCount = combat.aliveElfCount();
    while (!combat.hasWinner()) {
      combat.doTurn();
    }

    System.out.println("Outcome: " + combat.getOutcome());

    // Part 2
    int elfAttackPower = 4;
    while (true) {
      combat = Combat.fromFile(filename, elfAttackPower);
      while (!combat.hasWinner()) {
        combat.doTurn();
      }

      if (elfCount == combat.aliveElfCount()) {
        System.out.println(combat.getOutcome());
        break;
      }

      elfAttackPower++;
    }
  }
}
