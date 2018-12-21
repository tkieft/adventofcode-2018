package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

  private static final int NUM_ITERATIONS = 1000000000;

  private static List<List<Character>> readFile(String filename) {
    List<List<Character>> area = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        area.add(scanner.nextLine().chars().mapToObj(e -> (char)e).collect(Collectors.toList()));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return area;
  }

  private static int adjacentCount(List<List<Character>> area, int y, int x, char testChar) {
    int adjacentCount = 0;

    for (int i = Math.max(0, y - 1); i <= Math.min(y + 1, area.size() - 1); i++) {
      for (int j = Math.max(0, x - 1); j <= Math.min(x + 1, area.get(i).size() - 1); j++) {
        if (i == y && j == x) {
          continue;
        }
        if (area.get(i).get(j) == testChar) {
          adjacentCount++;
        }
      }
    }

    return adjacentCount;
  }

  private static List<List<Character>> doIteration(List<List<Character>> area) {
    List<List<Character>> newArea = new ArrayList<>();

    for (int y = 0; y < area.size(); y++) {
      List<Character> row = area.get(y);
      List<Character> newRow = new ArrayList<>();
      for (int x = 0; x < area.size(); x++) {
        if (row.get(x) == '.') {
          newRow.add(adjacentCount(area, y, x, '|') >= 3 ? '|' : '.');
        } else if (row.get(x) == '|') {
          newRow.add(adjacentCount(area, y, x, '#') >= 3 ? '#' : '|');
        } else if (row.get(x) == '#') {
          newRow.add(adjacentCount(area, y, x, '#') >= 1 && adjacentCount(area, y, x, '|') >= 1 ? '#' : '.');
        }
      }
      newArea.add(newRow);
    }

    return newArea;
  }

  private static int getValue(List<List<Character>> area) {
    int lumberyards = 0;
    int trees = 0;

    for (List<Character> row : area) {
      for (Character character : row) {
        if (character == '|') trees++;
        if (character == '#') lumberyards++;
      }
    }

    return lumberyards * trees;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<List<Character>> area = readFile(filename);

    // Part 1
    int i = 0;

    while (i < 10) {
      area = doIteration(area);
      i++;
    }

    System.out.println(getValue(area));

    // Part 2
    Map<String, Integer> cache = new HashMap<>();

    while (i < NUM_ITERATIONS) {
      area = doIteration(area);

      String areaString = areaToString(area);

      if (cache.get(areaString) != null) {
        int difference = i - cache.get(areaString);
        i += ((NUM_ITERATIONS - i) / difference) * difference;
      } else {
        cache.put(areaString, i);
      }
      i++;
    }

    System.out.println(getValue(area));
  }

  private static String areaToString(List<List<Character>> area) {
    StringBuilder stringBuilder = new StringBuilder();
    for (List<Character> row : area) {
      for (Character character : row) {
        stringBuilder.append(character);
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }
}
