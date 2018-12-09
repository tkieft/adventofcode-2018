package com.tylerkieft;

import java.util.Arrays;

public class Main {

  private static int PLAYER_COUNT = 448;
  private static int HIGHEST_MARBLE = 7162800;

  private static long findHighScore() {
    long[] scores = new long[PLAYER_COUNT];

    int currentPlayer = 0;

    MarbleList marbles = new MarbleList();
    marbles.add(0);

    for (int marble = 1; marble <= HIGHEST_MARBLE; marble++) {
      if (marble % 23 == 0) {
        marbles.move(-7);
        scores[currentPlayer] += marble + marbles.remove();
      } else {
        marbles.move(2);
        marbles.add(marble);
      }
      currentPlayer = (currentPlayer + 1) % PLAYER_COUNT;
    }

    return Arrays.stream(scores).max().getAsLong();
  }

  public static void main(String[] args) {
    System.out.println(findHighScore());
  }
}
