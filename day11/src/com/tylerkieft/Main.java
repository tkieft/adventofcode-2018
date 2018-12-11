package com.tylerkieft;

import java.util.Arrays;

public class Main {

  private static final int SERIAL_NUMBER = 9445;
  private static final int GRID_SIZE = 300;

  private static int calculatePowerLevel(int x, int y) {
    int rackID = (x + 10);
    int powerLevel = rackID * y;
    powerLevel += SERIAL_NUMBER;
    powerLevel *= rackID;
    powerLevel = (powerLevel / 100) % 10;
    powerLevel -= 5;
    return powerLevel;
  }

  private static int grid(int coord) {
    return coord - 1;
  }

  private static int[][] calculatePowerLevels() {
    int[][] powerLevels = new int[GRID_SIZE][GRID_SIZE];
    for (int y = 1; y <= GRID_SIZE; y++) {
      for (int x = 1; x <= GRID_SIZE; x++) {
        powerLevels[grid(x)][grid(y)] = calculatePowerLevel(x, y);
      }
    }
    return powerLevels;
  }

  /**
   * Brute force: calculate max power for square size
   */
  private static int[] findMaxPower(int[][] powerLevels, int squareSize) {
    int maxPowerLevelSum = Integer.MIN_VALUE;
    int xLocation = 0;
    int yLocation = 0;

    for (int y = 1; y <= GRID_SIZE - squareSize + 1; y++) {
      for (int x = 1; x <= GRID_SIZE - squareSize + 1; x++) {
        int powerLevelSum = 0;
        for (int i = 0; i < squareSize; i++) {
          for (int j = 0; j < squareSize; j++) {
            powerLevelSum += powerLevels[grid(x) + i][grid(y) + j];
          }
        }

        if (powerLevelSum > maxPowerLevelSum) {
          maxPowerLevelSum = powerLevelSum;
          xLocation = x;
          yLocation = y;
        }
      }
    }
    return new int[] {maxPowerLevelSum, xLocation, yLocation, squareSize};
  }

  /**
   * Running sum method: For square size n, start with previously calculated power levels for square size n - 1
   */
  private static int[] findMaxPowerRunningSum(int[][] powerLevels, int[][] powerLevelsRunningSum, int squareSize) {
    int maxPowerLevelSum = Integer.MIN_VALUE;
    int xLocation = 0;
    int yLocation = 0;

    for (int y = 1; y <= GRID_SIZE - squareSize + 1; y++) {
      for (int x = 1; x <= GRID_SIZE - squareSize + 1; x++) {
        for (int i = 0; i < squareSize - 1; i++) {
          powerLevelsRunningSum[grid(x)][grid(y)] += powerLevels[grid(x) + i][grid(y) + squareSize - 1] + powerLevels[grid(x) + squareSize - 1][grid(y) + i];
        }
        if (squareSize != 1) {
          powerLevelsRunningSum[grid(x)][grid(y)] += powerLevels[grid(x) + (squareSize - 1)][grid(y) + (squareSize - 1)];
        }

        if (powerLevelsRunningSum[grid(x)][grid(y)] > maxPowerLevelSum) {
          maxPowerLevelSum = powerLevelsRunningSum[grid(x)][grid(y)];
          xLocation = x;
          yLocation = y;
        }
      }
    }
    return new int[] {maxPowerLevelSum, xLocation, yLocation, squareSize};
  }

  public static void main(String[] args) {
    int[][] powerLevels = calculatePowerLevels();

    // Part 1

    int[] maxPowerInfo = findMaxPower(powerLevels, 3);
    System.out.println("Max power for 3x3 grid is " + maxPowerInfo[0] +
        " at (" + maxPowerInfo[1] + "," + maxPowerInfo[2] + ")");

    // Part 2: O(N^3.5)-ish?
    // look at N square sizes, for each square size i it's (N-i)(N-i)(2i + 1) operations

    int[][] powerLevelsRunningSum = new int[GRID_SIZE][GRID_SIZE];
    for (int i = 0; i < GRID_SIZE; i++) {
      powerLevelsRunningSum[i] = Arrays.copyOf(powerLevels[i], GRID_SIZE);
    }

    for (int squareSize = 1; squareSize <= GRID_SIZE; squareSize++) {
      int[] maxPowerForSquare = findMaxPowerRunningSum(powerLevels, powerLevelsRunningSum, squareSize);
      if (maxPowerForSquare[0] > maxPowerInfo[0]) {
        maxPowerInfo = maxPowerForSquare;
      }
    }

    System.out.println("Max power for any size grid is " + maxPowerInfo[0] +
        " at (" + maxPowerInfo[1] + "," + maxPowerInfo[2] + "," + maxPowerInfo[3] + ")");
  }
}
