package com.tylerkieft;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

  private static String getPolymerFromFile(String filename) {
    try {
      return new Scanner(new File(filename)).nextLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static String reducePolymer(String polymer) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < polymer.length(); i++) {
      stringBuilder.append(polymer.charAt(i));

      if (stringBuilder.length() > 1 &&
          Math.abs(
              stringBuilder.charAt(stringBuilder.length() - 1) -
                  stringBuilder.charAt(stringBuilder.length() - 2)) == 32) {
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
      }
    }

    return stringBuilder.toString();
  }

  public static void main(String[] args) {
    String filename = args[0];
    String polymer = getPolymerFromFile(filename);
    String reducedPolymer = reducePolymer(polymer);
    System.out.println("Length of reduced polymer: " + reducedPolymer.length());

    int min = Arrays
        .stream("abcdefghijklmnopqrstuvwxyz".split(""))
        .map(character -> polymer.replaceAll(character, "").replaceAll(character.toUpperCase(), ""))
        .map(Main::reducePolymer)
        .min(Comparator.comparing(String::length))
        .get()
        .length();

    System.out.println("Min string: " + min);
  }
}
