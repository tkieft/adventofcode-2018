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

    int ipRegister = 0;

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

    computer.runProgram(instructions);

    System.out.println(computer.getRegister(0));

//    computer = new Computer();
//    computer.bindInstructionPointerTo(ipRegister);
//    computer.setRegisters(new int[] {1, 0, 0, 0, 0, 0});
//    computer.runProgram(instructions);
//
//    System.out.println(computer.getRegister(0));

    int sum = 0;

    for (int i = 1; i <= 10551339; i++) {
      if (10551339 % i == 0) {
        sum += i;
      }
    }

    System.out.println(sum);
  }
}
