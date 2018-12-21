package com.tylerkieft;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    Computer computer = new Computer();

    // Part 1: Calculate opcode behavior
    int instructionsThatBehaveLike3OrMoreOpcodes = 0;

    Pattern beforePattern = Pattern.compile("Before: \\[(\\d), (\\d), (\\d), (\\d)]");
    Pattern afterPattern = Pattern.compile("After: {2}\\[(\\d), (\\d), (\\d), (\\d)]");

    String filename = args[0];
    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        String beforeLine = scanner.nextLine();
        Matcher beforeMatcher = beforePattern.matcher(beforeLine);
        if (!beforeMatcher.matches()) {
          // done with the sample inputs
          break;
        }

        int[] beforeRegisterValues = new int[4];
        int[] afterRegisterValues = new int[4];

        for (int i = 0; i < 4; i++) {
          beforeRegisterValues[i] = Integer.parseInt(beforeMatcher.group(i + 1));
        }

        String[] instructionLine = scanner.nextLine().split(" ");
        int opcode = Integer.parseInt(instructionLine[0]);
        int a = Integer.parseInt(instructionLine[1]);
        int b = Integer.parseInt(instructionLine[2]);
        int c = Integer.parseInt(instructionLine[3]);

        String afterLine = scanner.nextLine();
        Matcher afterMatcher = afterPattern.matcher(afterLine);
        if (afterMatcher.matches()) {
          for (int i = 0; i < 4; i++) {
            afterRegisterValues[i] = Integer.parseInt(afterMatcher.group(i + 1));
          }
        }

        if (computer.testInstruction(beforeRegisterValues, afterRegisterValues, opcode, a, b, c) >= 3) {
          instructionsThatBehaveLike3OrMoreOpcodes++;
        }

        // Consume blank line
        scanner.nextLine();
      }

      System.out.println(instructionsThatBehaveLike3OrMoreOpcodes);

      // Part 2: Run Program
      computer.deduceOpcodes();

      // Consume blank line
      scanner.nextLine();

      computer.loadRegisters(new int[] {0, 0, 0, 0});

      while (scanner.hasNextLine()) {
        String[] instructionLine = scanner.nextLine().split(" ");
        int opcode = Integer.parseInt(instructionLine[0]);
        int a = Integer.parseInt(instructionLine[1]);
        int b = Integer.parseInt(instructionLine[2]);
        int c = Integer.parseInt(instructionLine[3]);
        computer.doOpcode(opcode, a, b, c);
      }

      System.out.println(computer.getRegisters()[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
