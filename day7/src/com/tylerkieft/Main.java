package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static Map<String, String> readFile(String filename) {
    Map<String, String> dependencies = new HashMap<>();

    Pattern pattern = Pattern.compile("Step (\\w) .*? step (\\w) .*?");

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          dependencies.merge(matcher.group(2), matcher.group(1), String::concat);
          dependencies.merge(matcher.group(1), "", String::concat); // make sure all steps are present in map keys
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return dependencies;
  }

  private static Optional<String> findNextStep(Map<String, String> dependencies) {
    return dependencies.entrySet().stream()
        .filter(entry -> entry.getValue().isEmpty())
        .map(Map.Entry::getKey)
        .sorted()
        .findFirst();
  }

  private static void markStepInProgress(String step, Map<String, String> dependencies) {
    dependencies.remove(step);
  }

  private static void markStepDone(String step, Map<String, String> dependencies) {
    for (Map.Entry<String, String> entry : dependencies.entrySet()) {
      entry.setValue(entry.getValue().replaceAll(step, ""));
    }
  }

  private static String findCompletionOrder(Map<String, String> dependencies) {
    StringBuilder sb = new StringBuilder();

    while (!dependencies.isEmpty()) {
      String nextStep = findNextStep(dependencies).get();
      sb.append(nextStep);
      markStepInProgress(nextStep, dependencies);
      markStepDone(nextStep, dependencies);
    }

    return sb.toString();
  }

  private static int costForStep(String step, int baseCost, int incrementalCost) {
    return baseCost + (step.charAt(0) - 'A' + 1) * incrementalCost;
  }

  private static int findCompletionTime(
      Map<String, String> dependencies,
      int workerCount,
      int baseCost,
      int incrementalCost) {

    int totalTime = 0;

    String[] workers = new String[workerCount];
    int[] workerTimes = new int[workerCount];
    boolean working = false;

    while (!dependencies.isEmpty() || working) {
      working = false;

      for (int worker = 0; worker < workerCount; worker++) {
        // fill slots
        if (workers[worker] == null) {
          Optional<String> optionalNextStep = findNextStep(dependencies);
          if (optionalNextStep.isPresent()) {
            String nextStep = optionalNextStep.get();
            workers[worker] = nextStep;
            workerTimes[worker] = costForStep(nextStep, baseCost, incrementalCost);
            markStepInProgress(nextStep, dependencies);
          }
        }
      }

      // time elapses...
      totalTime++;

      // count down, remove done steps
      for (int worker = 0; worker < workerCount; worker++) {
        if (--workerTimes[worker] == 0) {
          markStepDone(workers[worker], dependencies);
          workers[worker] = null;
        }

        if (workers[worker] != null) {
          working = true;
        }
      }
    }

    return totalTime;
  }

  public static void main(String[] args) {
    String filename = args[0];
    Map<String, String> dependencies = readFile(filename);
    String completionOrder = findCompletionOrder(dependencies);
    System.out.println(completionOrder);

    // re-generate the list as we modified it in the first section
    dependencies = readFile(filename);
    System.out.println(findCompletionTime(dependencies, 5, 60, 1));
  }
}
