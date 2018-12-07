package com.tylerkieft;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Main {

  private static int MAX_DIMEN = 400;

  private static Point[] MANHATTAN_DELTAS = new Point[] {
      new Point(0, -1),
      new Point(0, 1),
      new Point(-1, 0),
      new Point(1, 0) };

  private static List<Location> readFile(String filename) {
    List<Location> points = new ArrayList<>();

    int id = 0;

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String[] coordinates = scanner.nextLine().split(", ");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        points.add(new Location(id++, new Point(x, y)));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return points;
  }

  private static void addNextPointsToVisit(Queue<Location> queue, Set<Location> visited, Location location, int maxX, int maxY) {
    Point point = location.getPoint();

    for (Point delta : MANHATTAN_DELTAS) {
      Point newPoint = new Point(point.x + delta.x, point.y + delta.y);
      if (newPoint.x < maxX && newPoint.x >= 0 && newPoint.y < maxY && newPoint.y >= 0) {
        Location newLocation = new Location(location.getId(), newPoint);
        if (!visited.contains(newLocation)) {
          visited.add(newLocation);
          queue.add(newLocation);
        }
      }
    }
  }

  private static int findLargestArea(List<Location> locations) {
    int[][] idMap = new int[MAX_DIMEN][MAX_DIMEN];       // store which location id each point is closest to
    int[][] distanceMap = new int[MAX_DIMEN][MAX_DIMEN]; // store the distance to the nearest location id

    for (int i = 0; i < MAX_DIMEN; i++) {
      Arrays.fill(distanceMap[i], Integer.MAX_VALUE);
    }

    int distance = 0;
    Queue<Location> toVisit = new ArrayDeque<>(locations);
    Set<Location> visited = new HashSet<>();

    while (!toVisit.isEmpty()) {
      Queue<Location> nextToVisit = new ArrayDeque<>();

      while (!toVisit.isEmpty()) {
        Location location = toVisit.poll();

        Point point = location.getPoint();

        if (distanceMap[point.x][point.y] > distance) {
          distanceMap[point.x][point.y] = distance;
          idMap[point.x][point.y] = location.getId();

          addNextPointsToVisit(nextToVisit, visited, location, MAX_DIMEN, MAX_DIMEN);
        } else if (distanceMap[point.x][point.y] == distance) {
          // Tied
          idMap[point.x][point.y] = Integer.MIN_VALUE;

          addNextPointsToVisit(nextToVisit, visited, location, MAX_DIMEN, MAX_DIMEN);
        }
      }
      toVisit = nextToVisit;
      distance++;
    }

    Map<Integer, Integer> frequency = new HashMap<>();

    for (int i = 0; i < MAX_DIMEN; i++) {
      for (int j = 0; j < MAX_DIMEN; j++) {
        if (i == 0 || i == MAX_DIMEN - 1 || j == 0 || j == MAX_DIMEN - 1) {
          // Infinite areas are not under consideration
          frequency.put(idMap[i][j], Integer.MIN_VALUE);
        } else {
          frequency.merge(idMap[i][j], 1, Integer::sum);
        }
      }
    }

    return frequency.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
  }


  private static int findRegionWithinTotalDistance(final List<Location> locations, final int distance) {
    int regionSize = 0;

    for (int i = 0; i < 400; i++) {
      for (int j = 0; j < 400; j++) {
        int currentDistance = 0;

        for (Location location : locations) {
          Point point = location.getPoint();
          currentDistance += Math.abs(i - point.x) + Math.abs(j - point.y);

          if (currentDistance > distance) break;
        }

        if (currentDistance < distance) {
          regionSize++;
        }
      }
    }

    return regionSize;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<Location> locations = readFile(filename);

    System.out.println(findLargestArea(locations));
    System.out.print(findRegionWithinTotalDistance(locations, 10000));
  }
}
