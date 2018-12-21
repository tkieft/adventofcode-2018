package com.tylerkieft;

public class Main {

  public static void main(String[] args) {
    String filename = args[0];
    Reservoir reservoir = Reservoir.fromFile(filename);
    new WaterFiller(reservoir).fill();
    System.out.println(reservoir.squaresTouchingWater());
    System.out.println(reservoir.retainedWater());
  }
}
