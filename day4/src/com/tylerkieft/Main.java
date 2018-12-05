package com.tylerkieft;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

  private static List<LogEntry> readFile(String filename) {
    Pattern linePattern = Pattern.compile("\\[(.*?)] (.*?)");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    List<LogEntryUnparsed> logEntriesUnparsed = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher lineMatcher = linePattern.matcher(line);
        if (lineMatcher.matches()) {
          LocalDateTime dateTime = LocalDateTime.parse(lineMatcher.group(1), dateTimeFormatter);
          String action = lineMatcher.group(2);
          logEntriesUnparsed.add(new LogEntryUnparsed(dateTime, action));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    logEntriesUnparsed.sort(Comparator.comparing(LogEntryUnparsed::getDateTime));

    String currentId = null;
    Pattern guardPattern = Pattern.compile("Guard #(\\d+)");

    List<LogEntry> logEntries = new ArrayList<>();

    for (LogEntryUnparsed logEntryUnparsed : logEntriesUnparsed) {
      String action = logEntryUnparsed.getAction();
      LogEntry.Type type = LogEntry.Type.fromString(action);

      if (type == LogEntry.Type.BEGINS_SHIFT) {
        Matcher guardMatcher = guardPattern.matcher(action);
        if (guardMatcher.lookingAt()) {
          currentId = guardMatcher.group(1);
        }
      }

      logEntries.add(new LogEntry(logEntryUnparsed.getDateTime(), currentId,  type));
    }

    return logEntries;
  }

  private static String findGuardWithMostSleepingTime(List<LogEntry> logEntries) {
    // Filter out "begins shift" entries
    List<LogEntry> filteredEntries = logEntries
        .stream()
        .filter(logEntry -> logEntry.getType() != LogEntry.Type.BEGINS_SHIFT)
        .collect(Collectors.toList());

    Map<String, Integer> sleepingTimeMap = new HashMap<>();

    for (int i = 0; i < filteredEntries.size(); i += 2) {
      LogEntry sleepEntry = filteredEntries.get(i);
      LogEntry wakeEntry = filteredEntries.get(i + 1);

      long durationMinutes = Duration.between(sleepEntry.getDateTime(), wakeEntry.getDateTime()).toMinutes();
      sleepingTimeMap.merge(sleepEntry.getId(), (int) durationMinutes, Integer::sum);
    }

    return Utils.entryForMaxValue(sleepingTimeMap).get().getKey();
  }

  private static Map<String, Map.Entry<Integer, Integer>> minuteMostAsleepMap(List<LogEntry> logEntries) {
    // Filter only entries for given id, not begin shift
    Map<String, List<LogEntry>> filteredEntriesById =
        logEntries
            .stream()
            .filter(logEntry -> logEntry.getType() != LogEntry.Type.BEGINS_SHIFT)
            .collect(Collectors.groupingBy(LogEntry::getId));

    Map<String, Map.Entry<Integer, Integer>> minuteMostAsleepMap = new HashMap<>();

    for (Map.Entry<String, List<LogEntry>> entry : filteredEntriesById.entrySet()) {
      Map<Integer, Integer> minutesMap = new HashMap<>();
      List<LogEntry> entries = entry.getValue();

      // iterate pairwise through the list
      for (int i = 0; i < entries.size(); i += 2) {
        LogEntry sleepEntry = entries.get(i);
        LogEntry wakeEntry = entries.get(i + 1);

        Duration duration = Duration.between(sleepEntry.getDateTime(), wakeEntry.getDateTime());

        for (int j = 0; j < duration.toMinutes(); j++) {
          int minutes = (sleepEntry.getDateTime().getMinute() + j) % 60;
          minutesMap.merge(minutes, 1, Integer::sum);
        }
      }

      minuteMostAsleepMap.put(entry.getKey(), Utils.entryForMaxValue(minutesMap).get());
    }

    return minuteMostAsleepMap;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<LogEntry> logEntries = readFile(filename);

    Map<String, Map.Entry<Integer, Integer>> minuteMostAsleepMap = minuteMostAsleepMap(logEntries);

    // Part 1
    String sleepiestGuard = findGuardWithMostSleepingTime(logEntries);
    int sleepiestGuardSleepiestMinute = minuteMostAsleepMap.get(sleepiestGuard).getKey();

    System.out.println("Most Asleep Guard: " + sleepiestGuard);
    System.out.println("Minute Most Asleep: " + sleepiestGuardSleepiestMinute);
    System.out.println("Part 1 Answer: " + (Integer.parseInt(sleepiestGuard) * sleepiestGuardSleepiestMinute));

    // Part 2
    Map.Entry<String, Map.Entry<Integer, Integer>> part2Answer = minuteMostAsleepMap.entrySet().stream()
        .max(Comparator.comparing(o -> o.getValue().getValue()))
        .get();

    System.out.println("Part 2 Answer: " + Integer.parseInt(part2Answer.getKey()) * part2Answer.getValue().getKey());
  }
}
