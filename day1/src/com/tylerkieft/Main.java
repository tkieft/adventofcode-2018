package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

  private static List<Integer> readInput(String pathname) {
    List<Integer> list = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(pathname))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        list.add(Integer.parseInt(line));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return list;
  }

  private static int frequencySum(List<Integer> frequencyChanges) {
    return frequencyChanges.stream().mapToInt(Integer::intValue).sum();
  }

  private static int firstRepeatedFrequency(List<Integer> frequencyChanges) {
    Set<Integer> frequencies = new HashSet<>();

    int sum = 0;
    int i = 0;

    while (true) {
      sum += frequencyChanges.get(i);

      if (frequencies.contains(sum)) {
        return sum;
      } else {
        frequencies.add(sum);
      }

      if (++i == frequencyChanges.size()) {
        i = 0;
      }
    }
  }

  public static void main(String[] args) {
    List<Integer> frequencyChanges = readInput(args[0]);

    System.out.println("Sum of frequency changes: " + frequencySum(frequencyChanges));
    System.out.println("First repeated frequency: " + firstRepeatedFrequency(frequencyChanges));
  }
}
