package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

  private static List<String> readFile(String filename) {
    List<String> boxIDs = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        boxIDs.add(scanner.nextLine());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return boxIDs;
  }

  private static Map<Character, Integer> getLetterCounts(String boxID) {
    return Arrays
        .stream(boxID.split(""))
        .collect(Collectors.toMap(s -> s.charAt(0), v -> 1, Integer::sum));
  }

  private static int calculateChecksum(List<String> boxIDs) {
    int twoLetterCounts = 0;
    int threeLetterCounts = 0;

    for (String boxID : boxIDs) {
      Map<Character, Integer> map = getLetterCounts(boxID);

      if (map.containsValue(2)) {
        twoLetterCounts++;
      }

      if (map.containsValue(3)) {
        threeLetterCounts++;
      }
    }

    return twoLetterCounts * threeLetterCounts;
  }

  private static String findOneLetterDifference(List<String> boxIds) {
    for (int i = 0; i < boxIds.size() - 1; i++) {
      String boxId1 = boxIds.get(i);
      for (int j = i + 1; j < boxIds.size(); j++) {
        String boxId2 = boxIds.get(j);

        int errorLoc = 0;

        for (int k = 0; k < boxId1.length(); k++) {
          if (boxId1.charAt(k) != boxId2.charAt(k)) {
            if (errorLoc != 0) {
              errorLoc = 0; // sentinel
              break;
            }
            errorLoc = k;
          }
        }

        if (errorLoc != 0) {
          return boxId1.substring(0, errorLoc) + boxId1.substring(errorLoc + 1);
        }
      }
    }
    return null;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<String> boxIds = readFile(filename);

    System.out.println("Checksum: " + calculateChecksum(boxIds));
    System.out.println("Common letters: " + findOneLetterDifference(boxIds));
  }
}
