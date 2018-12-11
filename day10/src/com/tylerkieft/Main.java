package com.tylerkieft;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static List<Star> readFile(String filename) {
    List<Star> stars = new ArrayList<>();

    Pattern pattern = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          stars.add(new Star(
              new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
              new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))));
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return stars;
  }

  private static long findArea(List<Star> stars) {
    long minX = stars.stream().map(star -> star.getPosition().x).min(Comparator.naturalOrder()).get();
    long minY = stars.stream().map(star -> star.getPosition().y).min(Comparator.naturalOrder()).get();
    long maxX = stars.stream().map(star -> star.getPosition().x).max(Comparator.naturalOrder()).get();
    long maxY = stars.stream().map(star -> star.getPosition().y).max(Comparator.naturalOrder()).get();

    return (Math.abs(maxY - minY) + 1) * (Math.abs(maxX - minX) + 1);
  }

  private static void plotStars(List<Star> stars) {
    stars.sort(Comparator.comparingInt((Star a) -> (a.getPosition().y)).thenComparingInt(a -> a.getPosition().x));

    // Calculating the min x and max x is a little more difficult than calculating min and max y
    int minX = stars.stream().map(star -> star.getPosition().x).min(Comparator.naturalOrder()).get();
    int maxX = stars.stream().map(star -> star.getPosition().x).max(Comparator.naturalOrder()).get();
    int minY = stars.get(0).getPosition().y;
    int maxY = stars.get(stars.size() - 1).getPosition().y;

    int starIndex = 0;

    // blank line before
    System.out.println();

    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {
        if (starIndex < stars.size()) {
          Point position = stars.get(starIndex).getPosition();
          if (position.x == x && position.y == y) {
            System.out.print('*');

            do {
              starIndex++;
            } while (starIndex < stars.size() && stars.get(starIndex).getPosition().x == x && stars.get(starIndex).getPosition().y == y);

            continue;
          }
        }
        System.out.print(' ');
      }
      // end of row
      System.out.println();
    }

    // blank line after
    System.out.println();
  }

  private static void advanceStars(List<Star> stars, boolean direction) {
    for (Star star : stars) {
      Point velocity = star.getVelocity();

      if (!direction) {
        velocity = new Point(velocity.x * -1, velocity.y * -1);
      }

      star.getPosition().translate(velocity.x, velocity.y);
    }
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<Star> stars = readFile(filename);

    long minArea = Long.MAX_VALUE;
    int minAreaIteration = 0;
    int i = 0;

    // Our strategy is to find when the stars cover the smallest area. We'll know when this occurs
    // after we have gone one past (when the area starts increasing again).
    while (true) {
      long area = findArea(stars);
      if (area <= minArea) {
        minArea = area;
        minAreaIteration = i;
      } else {
        break;
      }
      advanceStars(stars, true);
      i++;
    }

    // go back one step to return to the min area
    advanceStars(stars, false);

    plotStars(stars);

    System.out.println("Constellation achieved after " + minAreaIteration + " seconds.");
  }
}
