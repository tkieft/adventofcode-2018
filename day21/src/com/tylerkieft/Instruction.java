package com.tylerkieft;

public class Instruction {

  private final Opcode mOpcode;
  private final int mValue1;
  private final int mValue2;
  private final int mValue3;

  public Instruction(Opcode opcode, int value1, int value2, int value3) {
    mOpcode = opcode;
    mValue1 = value1;
    mValue2 = value2;
    mValue3 = value3;
  }

  public Opcode getOpcode() {
    return mOpcode;
  }

  public int getValue1() {
    return mValue1;
  }

  public int getValue2() {
    return mValue2;
  }

  public int getValue3() {
    return mValue3;
  }

  @Override
  public String toString() {
    return mOpcode.toString() + " " + mValue1 + " " + mValue2 + " " + mValue3;
  }
}
