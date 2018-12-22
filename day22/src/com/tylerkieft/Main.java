package com.tylerkieft;

import java.util.*;

public class Main {

//  private static final int TARGET_X = 10;
//  private static final int TARGET_Y = 10;
//  private static final int DEPTH = 510;

  private static final int TARGET_X = 7;
  private static final int TARGET_Y = 701;
  private static final int DEPTH = 11394;

  private static final int BUFFER_X = 50;
  private static final int BUFFER_Y = 120;

  private static final Long[][] sGeologicIndexCache = new Long[TARGET_X + 1 + BUFFER_X][TARGET_Y + 1 + BUFFER_Y];
  private static final Integer[][] sRiskLevelCache = new Integer[TARGET_X + 1 + BUFFER_X][TARGET_Y + 1 + BUFFER_Y];

  private static long geologicIndexCalculate(int x, int y) {
    if (x == 0 && y == 0) return 0;
    if (x == TARGET_X && y == TARGET_Y) return 0;
    if (y == 0) return (long)x * 16807;
    if (x == 0) return (long)y * 48271;
    return erosionLevel(x - 1, y) * erosionLevel(x, y - 1);
  }

  private static long geologicIndex(int x, int y) {
    if (sGeologicIndexCache[x][y] != null) {
      return sGeologicIndexCache[x][y];
    }

    long geologicIndex = geologicIndexCalculate(x, y);
    sGeologicIndexCache[x][y] = geologicIndex;
    return geologicIndex;
  }

  private static long erosionLevel(int x, int y) {
    return (geologicIndex(x, y) + DEPTH) % 20183;
  }

  private static int riskLevelCalculate(int x, int y) {
    return (int)(erosionLevel(x, y) % 3);
  }

  private static int riskLevel(int x, int y) {
    if (sRiskLevelCache[x][y] != null) {
      return sRiskLevelCache[x][y];
    }
    int riskLevel = riskLevelCalculate(x, y);
    sRiskLevelCache[x][y] = riskLevel;
    return riskLevel;
  }

  private static void addEdges(List<Vertex> v1s, List<Vertex> v2s) {
    for (Vertex v1 : v1s) {
      for (Vertex v2 : v2s) {
        if (v2.equipment == v1.equipment) {
          v1.edges.add(new Edge(v2, 1));
        }
      }
    }
  }

  public static void main(String[] args) {
    // Part 1
    int riskLevel = 0;

    for (int x = 0; x <= TARGET_X; x++) {
      for (int y = 0; y <= TARGET_Y; y++) {
        riskLevel += riskLevel(x, y);
      }
    }

    System.out.println(riskLevel);

    // Part 2

    // Generate the map
    List<Vertex>[][] map = new ArrayList[TARGET_X + 1 + BUFFER_X][TARGET_Y + 1 + BUFFER_Y];

    int xLimit = TARGET_X + BUFFER_X;
    int yLimit = TARGET_Y + BUFFER_Y;

    // Generate vertices
    for (int x = 0; x <= xLimit; x++) {
      for (int y = 0; y <= yLimit; y++) {
        map[x][y] = Vertex.create(riskLevel(x, y));
      }
    }

    // Generate edges
    for (int x = 0; x <= xLimit; x++) {
      for (int y = 0; y <= yLimit; y++) {
        if (x + 1 <= xLimit) {
          addEdges(map[x][y], map[x + 1][y]);
        }

        if (y + 1 <= yLimit) {
          addEdges(map[x][y], map[x][y + 1]);
        }

        if (x - 1 >= 0) {
          addEdges(map[x][y], map[x - 1][y]);
        }

        if (y - 1 >= 0) {
          addEdges(map[x][y], map[x][y - 1]);
        }
      }
    }

    // Do Djikstra's
    Map<Vertex, Integer> distance = new HashMap<>();
    Queue<Vertex> queue = new ArrayDeque<>();

    for (int x = 0; x <= xLimit; x++) {
      for (int y = 0; y <= yLimit; y++) {
        for (Vertex vertex : map[x][y]) {
          distance.put(vertex, Integer.MAX_VALUE);
          queue.add(vertex);
        }
      }
    }

    Vertex startVertex = map[0][0].get(0);
    Vertex endVertex = map[TARGET_X][TARGET_Y].get(0);

    distance.put(startVertex, 0);

    while (!queue.isEmpty()) {
      Vertex min = queue.stream().min(Comparator.comparingInt(distance::get)).get();

      if (endVertex == min) {
        break;
      }

      queue.remove(min);
      for (Edge edge : min.edges) {
        if (distance.get(edge.v) > distance.get(min) + edge.cost) {
          distance.put(edge.v, distance.get(min) + edge.cost);
        }
      }
    }

    System.out.println(distance.get(endVertex));
  }
}
