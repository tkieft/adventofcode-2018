package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

  private static String readFile(String filename, Map<String, String> combinations) {
    String initialState = null;

    try(Scanner scanner = new Scanner(new File(filename))) {
      initialState = scanner.nextLine().substring(15);
      scanner.nextLine();

      while (scanner.hasNextLine()) {
        String[] line = scanner.nextLine().split(" => ");
        combinations.put(line[0], line[1]);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return initialState;
  }

  private static long calculateSum(long startingPot, String state) {
    long potSum = 0;
    for (int pot = 0; pot < state.length(); pot++) {
      if (state.charAt(pot) == '#') {
        potSum += pot + startingPot;
      }
    }
    return potSum;
  }

  private static long findPotSum(String initialState, Map<String, String> combinations, long generations) {
    long startingPot = 0;
    String state = initialState;

    for (long g = 0; g < generations; g++) {
      StringBuilder newState = new StringBuilder();

      startingPot -= 2;

      for (int i = -2; i < state.length() + 2; i++) {
        if (i < 2) {
          StringBuilder combination = new StringBuilder(state.substring(0, i + 3));
          while (combination.length() < 5) combination.insert(0, ".");
          newState.append(combinations.get(combination.toString()));
        } else if (i > state.length() - 3) {
          StringBuilder combination = new StringBuilder(state.substring(i - 2));
          while (combination.length() < 5) combination.append(".");
          newState.append(combinations.get(combination.toString()));
        } else {
          newState.append(combinations.get(state.substring(i - 2, i + 3)));
        }
      }

      // Trim empty pots off the ends
      int startTrim = 0;
      int endTrim = newState.length();

      while (newState.charAt(startTrim) == '.') startTrim++;
      while (newState.charAt(endTrim - 1) == '.') endTrim--;

      String lastState = state;
      state = newState.substring(startTrim, endTrim);

      startingPot += startTrim;

      // We've converged; skip the rest of the generations
      if (state.equals(lastState)) {
        startingPot = (generations - g - 1) * (startTrim - 2) + startingPot;
        return calculateSum(startingPot, state);
      }
    }

    // We didn't converge, calculate the sum
    return calculateSum(startingPot, state);
  }

  public static void main(String[] args) {
    String filename = args[0];
    Map<String, String> combinations = new HashMap<>();
    String initialState = readFile(filename, combinations);

    System.out.println(findPotSum(initialState, combinations, 20));
    System.out.println(findPotSum(initialState, combinations, 50000000000L));
  }
}
