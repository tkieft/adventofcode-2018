package com.tylerkieft;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

public class PointFinder {

  private static final Point3 ME = new Point3(0, 0, 0);

  private final List<Nanobot> mNanobots;

  private static class Candidate implements Comparable<Candidate> {
    private final Cube cube;
    private final int nanobotsInRange;

    public Candidate(Cube cube, int nanobotsInRange) {
      this.cube = cube;
      this.nanobotsInRange = nanobotsInRange;
    }

    public int compareTo(Candidate o) {
      int nanobotDiff = o.nanobotsInRange - this.nanobotsInRange;
      return nanobotDiff == 0 ?
          (int) (Math.min(Math.abs(cube.minX), Math.abs(cube.maxX)) +
           Math.min(Math.abs(cube.minY), Math.abs(cube.maxY)) +
           Math.min(Math.abs(cube.minZ), Math.abs(cube.maxZ)) -
          (Math.min(Math.abs(o.cube.minX), Math.abs(o.cube.maxX)) +
           Math.min(Math.abs(o.cube.minY), Math.abs(o.cube.maxY)) +
           Math.min(Math.abs(o.cube.minZ), Math.abs(o.cube.maxZ)))) :
          nanobotDiff;
    }
  }

  private static class Cube {
    private final long minX;
    private final long maxX;
    private final long minY;
    private final long maxY;
    private final long minZ;
    private final long maxZ;

    public Cube(long minX, long maxX, long minY, long maxY, long minZ, long maxZ) {
      this.minX = minX;
      this.maxX = maxX;
      this.minY = minY;
      this.maxY = maxY;
      this.minZ = minZ;
      this.maxZ = maxZ;
    }

    @Override
    public String toString() {
      return "<" + minX + "," + minY + "," + minZ + ">,<" + maxX + "," + maxY + "," + maxZ + ">";
    }
  }

  public PointFinder(List<Nanobot> nanobots) {
    mNanobots = nanobots;
  }

  private static long clamp(long value, long min, long max) {
    return value < min ?
        min :
        value > max ? max : value;
  }

  private static boolean intersects(Nanobot nanobot, Cube cube) {
    // Find the closest point to the sphere within the cube
    long closestX = clamp(nanobot.getPoint().x, cube.minX, cube.maxX);
    long closestY = clamp(nanobot.getPoint().y, cube.minY, cube.maxY);
    long closestZ = clamp(nanobot.getPoint().z, cube.minZ, cube.maxZ);

    // If that closest point is within the radius, we good
    return nanobot.distanceTo(new Point3(closestX, closestY, closestZ)) <= nanobot.getSignalRadius();
  }

  private long getDimen(ToLongFunction<Nanobot> accessor, boolean max, long precision) {
    Comparator<Nanobot> nanobotComparator = Comparator.comparingLong(accessor);
    Stream<Nanobot> stream = mNanobots.stream();
    long value = accessor.applyAsLong((max ? stream.max(nanobotComparator) : stream.min(nanobotComparator)).get());
    value = value / precision;
    value = value < 0 ? value - 1 : value + 1;
    return value * precision;
  }

  private void subdivideCandidate(Candidate startCandidate, PriorityQueue<Candidate> candidates) {
    Cube startCube = startCandidate.cube;
    long precision = (startCube.maxX - startCube.minX + 1) / 10;

    if (precision < 1) {
      precision = 1L;
    }

    subdivideCandidate(startCandidate, precision, candidates);
  }

  private void subdivideCandidate(Candidate candidate, long precision, PriorityQueue<Candidate> priorityQueue) {
    Cube cube = candidate.cube;

    int xSize = (int) ((cube.maxX - cube.minX + 1) / precision);
    int ySize = (int) ((cube.maxY - cube.minY + 1) / precision);
    int zSize = (int) ((cube.maxZ - cube.minZ + 1) / precision);

    Cube[][][] cubes = new Cube[xSize][ySize][zSize];

    for (int x = 0; x < xSize; x++) {
      for (int y = 0; y < ySize; y++) {
        for (int z = 0; z < zSize; z++) {
          cubes[x][y][z] = new Cube(
              cube.minX + (x * precision),
              cube.minX + ((x + 1) * precision) - 1,
              cube.minY + (y * precision),
              cube.minY + ((y + 1) * precision) - 1,
              cube.minZ + (z * precision),
              cube.minZ + ((z + 1) * precision) - 1);
        }
      }
    }

    int[][][] counts = new int[xSize][ySize][zSize];

    for (Nanobot nanobot : mNanobots) {
      for (int x = 0; x < xSize; x++) {
        for (int y = 0; y < ySize; y++) {
          for (int z = 0; z < zSize; z++) {
            if (intersects(nanobot, cubes[x][y][z])) {
              counts[x][y][z]++;
            }
          }
        }
      }
    }

    for (int x = 0; x < xSize; x++) {
      for (int y = 0; y < ySize; y++) {
        for (int z = 0; z < zSize; z++) {
          priorityQueue.add(new Candidate(cubes[x][y][z], counts[x][y][z]));
        }
      }
    }
  }

  public long calculate() {
    PriorityQueue<Candidate> priorityQueue = new PriorityQueue<>();

    long precision = 10000000L;

    long minX = getDimen(value -> value.getPoint().x, false, precision);
    long maxX = getDimen(value -> value.getPoint().x, true, precision);
    long minY = getDimen(value -> value.getPoint().y, false, precision);
    long maxY = getDimen(value -> value.getPoint().y, true, precision);
    long minZ = getDimen(value -> value.getPoint().z, false, precision);
    long maxZ = getDimen(value -> value.getPoint().z, true, precision);

    Cube space = new Cube(minX, maxX, minY, maxY, minZ, maxZ);
    subdivideCandidate(new Candidate(space, 0), 10000000L, priorityQueue);

    while (!priorityQueue.isEmpty()) {
      Candidate candidateUnderTest = priorityQueue.remove();
      long size = candidateUnderTest.cube.maxX - candidateUnderTest.cube.minX + 1;

      if (size == 1) {
        Cube cube = candidateUnderTest.cube;
        Point3 point = new Point3(cube.minX, cube.minY, cube.minZ);
        System.out.println("Found cube of size 1 @ " + point + " with " +
            candidateUnderTest.nanobotsInRange + " nanobots in range.");
        return point.distanceTo(ME);
      }

      subdivideCandidate(candidateUnderTest, priorityQueue);
    }

    return 0;
  }
}
