package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    String filename = args[0];

    Computer computer = new Computer();

    List<Instruction> instructions = new ArrayList<>();

    int ipRegister;

    try (Scanner scanner = new Scanner(new File(filename))) {
      String line = scanner.nextLine();
      ipRegister = Integer.parseInt(line.split(" ")[1]);
      computer.bindInstructionPointerTo(ipRegister);

      Pattern pattern = Pattern.compile("(\\w+) (\\d+) (\\d+) (\\d+)");

      while (scanner.hasNextLine()) {
        Matcher matcher = pattern.matcher(scanner.nextLine());
        if (matcher.matches()) {
          instructions.add(
              new Instruction(
                  Opcode.valueOf(matcher.group(1)),
                  Integer.parseInt(matcher.group(2)),
                  Integer.parseInt(matcher.group(3)),
                  Integer.parseInt(matcher.group(4))));
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // Part 1 - Run the program until hit IP = 29, get value of r4
//    computer.runProgram(instructions);

//    System.out.println(computer.getRegister(0));

    List<Integer> r4List = new ArrayList<>();

    // Part 2 - This is the program, converted into Java.
    // Loop until the value repeats, then take the previous value

    int r1 = 0;
    int r2 = 0;
    int r3 = 0;
    int r4 = 0;

    while (true) {
      r3 = r4 | 65536;
      r4 = 14464005;

      while (true) {
        r2 = r3 & 255;
        r4 = r4 + r2;
        r4 = r4 & 16777215;
        r4 = r4 * 65899;
        r4 = r4 & 16777215;

        if (r3 < 256) break;

        r2 = 0;

        while (true) {
          r1 = r2 + 1;
          r1 = r1 * 256;

          if (r1 > r3) {
            r3 = r2;
            break;
          } else {
            r2 = r2 + 1;
          }
        }
      }
      if (r4List.contains(r4)) {
        System.out.println(r4List.get(r4List.size() - 1));
        break;
      }
      r4List.add(r4);
    }
  }
}
