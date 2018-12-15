package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Combat {

  private final List<List<Location>> mBoard;
  private final List<Unit> mUnits;

  private int mTurns = 0;

  private static final Comparator<Location> sXYComparator = (l1, l2) -> {
    int yDiff = l1.getY() - l2.getY();
    int xDiff = l1.getX() - l2.getX();
    return yDiff == 0 ? xDiff : yDiff;
  };

  private Combat(List<List<Location>> board, List<Unit> units) {
    mBoard = board;
    mUnits = units;
  }

  public static Combat fromFile(String filename, int elfAttackPower) {
    List<List<Location>> board = new ArrayList<>();
    List<Unit> units = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      int y = 0;
      while (scanner.hasNextLine()) {
        List<Location> row = new ArrayList<>();
        char[] line = scanner.nextLine().toCharArray();

        for (int x = 0; x < line.length; x++) {
          char c = line[x];
          if (c == '#') {
            row.add(new Location(Location.Type.WALL, x, y));
          } else if (c == '.') {
            row.add(new Location(Location.Type.OPEN, x, y));
          } else if (c == 'G') {
            Location location = new Location(Location.Type.OPEN, x, y);
            row.add(location);
            Unit goblin = new Unit(Unit.Type.GOBLIN, location, 3);
            units.add(goblin);
            location.setUnit(goblin);
          } else if (c == 'E') {
            Location location = new Location(Location.Type.OPEN, x, y);
            row.add(location);
            Unit elf = new Unit(Unit.Type.ELF, location, elfAttackPower);
            units.add(elf);
            location.setUnit(elf);
          }
        }

        board.add(row);
        y++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return new Combat(board, units);
  }

  private List<Location> generateAdjacentLocations(Location location) {
    int y = location.getY();
    int x = location.getX();

    List<Location> adjacentLocations = new ArrayList<>();
    if (y > 0) adjacentLocations.add(mBoard.get(y - 1).get(x));
    if (x > 0) adjacentLocations.add(mBoard.get(y).get(x - 1));
    if (x < mBoard.get(y).size() - 1) adjacentLocations.add(mBoard.get(y).get(x + 1));
    if (y < mBoard.size() - 1) adjacentLocations.add(mBoard.get(y + 1).get(x));

    return adjacentLocations;
  }

  private List<Location> generateAdjacentLocations(PathNode pathNode) {
    return generateAdjacentLocations(pathNode.getLocation());
  }

  private List<PathNode> findShortestLengthPathsToEnemy(Location location) {
    Set<Location> visitedLocations = new HashSet<>();

    List<PathNode> paths = new ArrayList<>();

    PathNode startingPath = new PathNode(null, location);
    paths.add(startingPath);

    List<PathNode> terminatingPaths = new ArrayList<>();

    while (terminatingPaths.isEmpty() && !paths.isEmpty()) {
      List<PathNode> newPaths = new ArrayList<>();

      for (PathNode pathNode : paths) {
        for (Location adjacentLocation : generateAdjacentLocations(pathNode)) {
          if (visitedLocations.contains(adjacentLocation)) {
            continue;
          }

          if (adjacentLocation.hasUnit() && adjacentLocation.getUnit().getType() != location.getUnit().getType()) {
            terminatingPaths.add(new PathNode(pathNode, adjacentLocation));
            visitedLocations.add(adjacentLocation);
          } else if (adjacentLocation.getType() == Location.Type.OPEN && !adjacentLocation.hasUnit()) {
            newPaths.add(new PathNode(pathNode, adjacentLocation));
            visitedLocations.add(adjacentLocation);
          }
        }
      }

      paths = newPaths;
    }

    return terminatingPaths;
  }

  private void maybeAttack(final Unit unit) {
    // Find all adjacent enemies
    Location location = unit.getLocation();

    Optional<Location> optionalEnemyLocation = generateAdjacentLocations(location).stream()
        .filter(adjacentLocation -> adjacentLocation.hasUnit() &&
            adjacentLocation.getUnit().getType() != location.getUnit().getType())
        .min((l1, l2) -> {
          int hitPointsDiff = l1.getUnit().getHitPoints() - l2.getUnit().getHitPoints();
          return hitPointsDiff == 0 ? sXYComparator.compare(l1, l2) : hitPointsDiff;
        });

    if (optionalEnemyLocation.isPresent()) {
      Location enemyLocation = optionalEnemyLocation.get();
      Unit enemyUnit = enemyLocation.getUnit();
      enemyUnit.attackBy(location.getUnit());

      if (enemyUnit.isDead()) {
        enemyLocation.setUnit(null);
      }
    }
  }

  private void doTurn(Unit unit) {
    Location location = unit.getLocation();

    List<Path> paths = findShortestLengthPathsToEnemy(location)
        .stream().map(PathNode::path).collect(Collectors.toList());

    if (paths.isEmpty()) {
      // Nowhere to move, bail
      return;
    }

    if (paths.get(0).getLength() > 1) {
      // Move. Sort by reading order of first step
      paths.sort((p1, p2) -> sXYComparator.compare(p1.getFirstStep(), p2.getFirstStep()));

      // Move the unit
      unit.getLocation().setUnit(null);
      Location newLocation = paths.get(0).getFirstStep();
      unit.setLocation(newLocation);
      newLocation.setUnit(unit);
    }

    if (paths.get(0).getLength() <= 2) {
      maybeAttack(unit);
    }
  }

  public void doTurn() {
    mUnits.sort((u1, u2) -> sXYComparator.compare(u1.getLocation(), u2.getLocation()));

    for (Unit unit : mUnits) {
      if (unit.isDead()) {
        continue;
      }

      // break out once there's a winner, don't count this turn
      if (hasWinner()) {
        return;
      }

      doTurn(unit);
    }

    mTurns++;
  }

  public boolean hasWinner() {
    return mUnits.stream()
        .filter(unit -> !unit.isDead())
        .collect(Collectors.groupingBy(Unit::getType))
        .size() == 1;
  }

  public int aliveElfCount() {
    return (int) mUnits.stream()
        .filter(unit -> unit.getType() == Unit.Type.ELF && unit.isAlive())
        .count();
  }

  public int getOutcome() {
    return mTurns * mUnits.stream()
        .filter(Unit::isAlive)
        .mapToInt(Unit::getHitPoints)
        .sum();
  }

  public void printBoard() {
    if (mTurns == 0) {
      System.out.println("Initially");
    } else {
      System.out.println("After " + mTurns + (mTurns == 1 ? " round" : " rounds"));
    }

    for (List<Location> row : mBoard) {
      List<Unit> unitsInRow = new ArrayList<>();
      for (Location location : row) {
        System.out.print(location);
        if (location.hasUnit()) {
          unitsInRow.add(location.getUnit());
        }
      }
      System.out.println(" " + unitsInRow.stream()
          .map(unit -> (unit.getType() == Unit.Type.ELF ? "E" : "G") + "(" + unit.getHitPoints() + ")")
          .collect(Collectors.joining(", "))
      );
    }
    System.out.println();
  }
}
