package com.tylerkieft;

import java.util.List;

public class Computer {

  private final int[] mRegisters;
  private int mInstructionPointerRegister;
  private int mInstructionPointer;

  public Computer() {
    mRegisters = new int[6];
    mInstructionPointerRegister = 0;
    mInstructionPointer = 0;
  }

  public int getRegister(int register) {
    return mRegisters[register];
  }

  public void setRegisters(int[] registers) {
    for (int i = 0; i < registers.length; i++) {
      mRegisters[i] = registers[i];
    }
  }

  public void bindInstructionPointerTo(int register) {
    mInstructionPointerRegister = register;
  }

  public void runProgram(List<Instruction> instructions) {
    while (mInstructionPointer < instructions.size()) {
      System.out.print("ip=" + mInstructionPointer + " ");

      mRegisters[mInstructionPointerRegister] = mInstructionPointer;

      printRegisters();
      System.out.print(instructions.get(mInstructionPointer) + " ");
      doInstruction(instructions.get(mInstructionPointer));
      printRegisters();

      mInstructionPointer = mRegisters[mInstructionPointerRegister];
      mInstructionPointer++;
      System.out.println();
    }
  }

  private void doInstruction(Instruction instruction) {
    doOpcode(instruction.getOpcode(), instruction.getValue1(), instruction.getValue2(), instruction.getValue3());
  }

  private void doOpcode(Opcode opcode, int a, int b, int c) {
    switch (opcode) {
      case addr:
        mRegisters[c] = mRegisters[a] + mRegisters[b];
        break;
      case addi:
        mRegisters[c] = mRegisters[a] + b;
        break;
      case mulr:
        mRegisters[c] = mRegisters[a] * mRegisters[b];
        break;
      case muli:
        mRegisters[c] = mRegisters[a] * b;
        break;
      case banr:
        mRegisters[c] = mRegisters[a] & mRegisters[b];
        break;
      case bani:
        mRegisters[c] = mRegisters[a] & b;
        break;
      case borr:
        mRegisters[c] = mRegisters[a] | mRegisters[b];
        break;
      case bori:
        mRegisters[c] = mRegisters[a] | b;
        break;
      case setr:
        mRegisters[c] = mRegisters[a];
        break;
      case seti:
        mRegisters[c] = a;
        break;
      case gtir:
        mRegisters[c] = a > mRegisters[b] ? 1 : 0;
        break;
      case gtri:
        mRegisters[c] = mRegisters[a] > b ? 1 : 0;
        break;
      case gtrr:
        mRegisters[c] = mRegisters[a] > mRegisters[b] ? 1 : 0;
        break;
      case eqir:
        mRegisters[c] = a == mRegisters[b] ? 1 : 0;
        break;
      case eqri:
        mRegisters[c] = mRegisters[a] == b ? 1 : 0;
        break;
      case eqrr:
        mRegisters[c] = mRegisters[a] == mRegisters[b] ? 1 : 0;
        break;
    }
  }

  private void printRegisters() {
    System.out.print("[");
    for (int i = 0; i < mRegisters.length; i++) {
      System.out.print(mRegisters[i] + (i < mRegisters.length - 1 ? "," : ""));
    }
    System.out.print("] ");
  }
}
