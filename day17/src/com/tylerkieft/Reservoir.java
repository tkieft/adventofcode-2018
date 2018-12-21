package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reservoir {

  private final char[][] mStorage;

  private final int mMinY;

  public static Reservoir fromFile(String filename) {
    Pattern pattern = Pattern.compile("(\\w)=(\\d+), (\\w)=(\\d+)\\.\\.(\\d+)");

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    List<Clay> clays = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          int value1 = Integer.parseInt(matcher.group(2));
          int value2 = Integer.parseInt(matcher.group(4));
          int value3 = Integer.parseInt(matcher.group(5));

          if (matcher.group(1).equals("x")) {
            if (value1 < minX) minX = value1;
            if (value1 > maxX) maxX = value1;
            if (value2 < minY) minY = value2;
            if (value3 > maxY) maxY = value3;
            clays.add(new Clay(Clay.Type.VERTICAL, value1, value2, value3));
          } else {
            if (value1 < minY) minY = value1;
            if (value1 > maxY) maxY = value1;
            if (value2 < minX) minX = value2;
            if (value3 > maxX) maxX = value3;
            clays.add(new Clay(Clay.Type.HORIZONTAL, value1, value2, value3));
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // Allow one extra column on each side
    char[][] reservoir = new char[maxY + 1][maxX - minX + 1 + 2];
    for (char[] row : reservoir) {
      Arrays.fill(row, '.');
    }

    // water source
    reservoir[0][500 - minX + 1] = '+';

    for (Clay clay : clays) {
      for (int i = clay.mDimen2; i <= clay.mDimen3; i++) {
        if (clay.mType == Clay.Type.VERTICAL) {
          reservoir[i][clay.mDimen1 - minX + 1] = '#';
        } else {
          reservoir[clay.mDimen1][i - minX + 1] = '#';
        }
      }
    }

    return new Reservoir(reservoir, minY);
  }

  private Reservoir(char[][] storage, int minY) {
    mStorage = storage;
    mMinY = minY;
  }

  public char get(int y, int x) {
    return mStorage[y][x];
  }

  public void set(int y, int x, char c) {
    mStorage[y][x] = c;
  }

  public int rows() {
    return mStorage.length;
  }

  public int columns() {
    return mStorage[0].length;
  }

  public int squaresTouchingWater() {
    int squaresTouchingWater = 0;

    for (int y = mMinY; y < mStorage.length; y++) {
      for (char c : mStorage[y]) {
        if (c == '|' || c == '~') {
          squaresTouchingWater++;
        }
      }
    }

    return squaresTouchingWater;
  }

  public int retainedWater() {
    int retainedWater = 0;

    for (int y = mMinY; y < mStorage.length; y++) {
      for (char c : mStorage[y]) {
        if (c == '~') {
          retainedWater++;
        }
      }
    }

    return retainedWater;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n");
    for (char[] row : mStorage) {
      for (char c : row) {
        stringBuilder.append(c);
      }
      stringBuilder.append("\n");
    }
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
}
