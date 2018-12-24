package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static ImmuneSystemSimulator createSimulator(String filename, int boost) {
    List<Group> immuneSystem = new ArrayList<>();
    List<Group> infection = new ArrayList<>();

    Pattern groupPattern = Pattern.compile("(\\d+) units each with (\\d+) hit points " +
        "(\\((immune to (.+?))?(; )?(weak to (.+))?\\) )?" +
        "with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)");

    try (Scanner scanner = new Scanner(new File(filename))) {
      // Immune System
      scanner.nextLine();

      boolean processingInfection = false;

      int groupId = 1;

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        // Infection
        if (line.isEmpty()) {
          processingInfection = true;
          groupId = 1;
          scanner.nextLine();
          continue;
        }

        Matcher matcher = groupPattern.matcher(line);

        if (matcher.matches()) {
          List<String> immunities = new ArrayList<>();
          List<String> weaknesses = new ArrayList<>();

          if (matcher.group(4) != null) {
            immunities = Arrays.asList(matcher.group(5).split(", "));
          }

          if (matcher.group(7) != null) {
            weaknesses = Arrays.asList(matcher.group(8).split(", "));
          }

          Group group = new Group(
              processingInfection ? Group.Type.INFECTION : Group.Type.IMMUNE_SYSTEM,
              groupId,
              Integer.parseInt(matcher.group(1)),
              Integer.parseInt(matcher.group(2)),
              immunities,
              weaknesses,
              Integer.parseInt(matcher.group(9)) + (processingInfection ? 0 : boost),
              matcher.group(10),
              Integer.parseInt(matcher.group(11)));

          if (processingInfection) {
            infection.add(group);
          } else {
            immuneSystem.add(group);
          }

          groupId++;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return new ImmuneSystemSimulator(immuneSystem, infection);
  }

  public static void main(String[] args) {
    String filename = args[0];
    ImmuneSystemSimulator simulator = createSimulator(filename, 0);
    System.out.println(simulator.simulate());

    int upperBound = 1000;
    int lowerBound = 0;

    while (upperBound != lowerBound) {
      int boost = lowerBound + (upperBound - lowerBound) / 2;

      ImmuneSystemSimulator boostedSimulator = createSimulator(filename, boost);
      boostedSimulator.simulate();

      if (boostedSimulator.immuneSystemWon()) {
        upperBound = boost;
      } else {
        lowerBound = boost + 1;
      }
    }

    ImmuneSystemSimulator boostedSimulator = createSimulator(filename, upperBound);
    int result = boostedSimulator.simulate();
    System.out.println("Immune system won at boost " + upperBound + " with " + result + " units remaining");
  }
}
