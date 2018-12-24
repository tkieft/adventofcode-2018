package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static List<Nanobot> readFile(String filename) {
    List<Nanobot> nanobots = new ArrayList<>();

    Pattern pattern = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        Matcher matcher = pattern.matcher(scanner.nextLine());
        if (matcher.matches()) {
          nanobots.add(new Nanobot(
              new Point3(
                Long.parseLong(matcher.group(1)),
                Long.parseLong(matcher.group(2)),
                Long.parseLong(matcher.group(3))),
              Long.parseLong(matcher.group(4))
          ));
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return nanobots;
  }

  private static int findNanobotsWithinRangeOfMax(List<Nanobot> nanobots) {
    Nanobot maxNanobot = nanobots.stream().max(Comparator.comparingLong(Nanobot::getSignalRadius)).get();

    return (int) nanobots
        .stream()
        .filter(nanobot -> maxNanobot.distanceTo(nanobot) <= maxNanobot.getSignalRadius())
        .count();
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<Nanobot> nanobots = readFile(filename);
    System.out.println(findNanobotsWithinRangeOfMax(nanobots));
    System.out.println(new PointFinder(nanobots).calculate());
  }
}
