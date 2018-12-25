package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

  private static List<Constellation> readFile(String filename) {
    List<Constellation> constellations = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] coords = line.split(",");
        constellations.add(new Constellation(new Point4(
            Integer.parseInt(coords[0]),
            Integer.parseInt(coords[1]),
            Integer.parseInt(coords[2]),
            Integer.parseInt(coords[3]))));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return constellations;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<Constellation> constellations = readFile(filename);
    int numConstellations;

    do {
      numConstellations = constellations.size();

      for (int i = 0; i < constellations.size() - 1; i++) {
        Constellation c1 = constellations.get(i);
        for (int j = i + 1; j < constellations.size(); j++) {
          Constellation c2 = constellations.get(j);

          if (c1.distanceTo(c2) <= 3) {
            c1.mergeWith(c2);
            constellations.remove(j--);
          }
        }
      }
    } while (constellations.size() != numConstellations);

    System.out.println(constellations.size());
  }
}
