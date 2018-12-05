package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static final int FABRIC_SIZE = 1000;

  private static List<Claim> readClaims(String filename) {
    List<Claim> claims = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String claim = scanner.nextLine();

        Pattern pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(claim);

        if (matcher.matches()) {
          claims.add(new Claim(
              matcher.group(1),
              Integer.parseInt(matcher.group(2)),
              Integer.parseInt(matcher.group(3)),
              Integer.parseInt(matcher.group(4)),
              Integer.parseInt(matcher.group(5))));
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return claims;
  }

  private static void plot(int[][] fabric, Claim claim) {
    for (int x = claim.getX(); x < claim.getX() + claim.getWidth(); x++) {
      for (int y = claim.getY(); y < claim.getY() + claim.getHeight(); y++) {
        fabric[x][y] += 1;
      }
    }
  }

  private static int countOverlappingSquares(int[][] fabric) {
    int overlappingSquares = 0;

    for (int i = 0; i < fabric.length; i++) {
      for (int j = 0; j < fabric[i].length; j++) {
        if (fabric[i][j] > 1) {
          overlappingSquares++;
        }
      }
    }

    return overlappingSquares;
  }

  private static Claim findNonOverlappingClaim(int[][] fabric, List<Claim> claims) {
    for (Claim claim : claims) {
      boolean overlaps = false;

      for (int i = claim.getX(); i < claim.getX() + claim.getWidth(); i++) {
        for (int j = claim.getY(); j < claim.getY() + claim.getHeight(); j++) {
          if (fabric[i][j] != 1) {
            overlaps = true;
            break;
          }
        }
        if (overlaps) {
          break;
        }
      }

      if (!overlaps) {
        return claim;
      }
    }
    return null;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<Claim> claims = readClaims(filename);

    int[][] fabric = new int[FABRIC_SIZE][FABRIC_SIZE];
    for (Claim claim : claims) {
      plot(fabric, claim);
    }

    System.out.println("Overlapping squares: " + countOverlappingSquares(fabric));
    System.out.println("Nonoverlapping claim ID: " + findNonOverlappingClaim(fabric, claims).getId());
  }
}
