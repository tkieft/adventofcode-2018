package com.tylerkieft;

import java.util.*;

public class Computer {

  enum Opcode {
    addr,
    addi,
    mulr,
    muli,
    banr,
    bani,
    borr,
    bori,
    setr,
    seti,
    gtir,
    gtri,
    gtrr,
    eqir,
    eqri,
    eqrr;

    int mValue;

    Set<Integer> mMatchingIntegers = new HashSet<>();
    Set<Integer> mBlacklistedIntegers = new HashSet<>();
  }

  private int[] mRegisters;
  private Map<Integer, Opcode> mOpcodeMap;

  public Computer() {
    mRegisters = new int[4];
    mOpcodeMap = new HashMap<>();
  }

  public void loadRegisters(int[] registers) {
    mRegisters = registers.clone();
  }

  public int[] getRegisters() {
    return mRegisters;
  }

  public int testInstruction(int[] registersBefore, int[] registersAfter, int opcode, int a, int b, int c) {
    int matchingOpcodes = 0;

    for (Opcode o : Opcode.values()) {
      loadRegisters(registersBefore);
      doOpcode(o, a, b, c);

      if (Arrays.equals(mRegisters, registersAfter)) {
        matchingOpcodes++;
        o.mMatchingIntegers.add(opcode);
      } else {
        o.mBlacklistedIntegers.add(opcode);
      }
    }

    return matchingOpcodes;
  }

  public void deduceOpcodes() {
    for (Opcode opcode : Opcode.values()) {
      opcode.mMatchingIntegers.removeAll(opcode.mBlacklistedIntegers);
    }

    List<Opcode> opcodes = new ArrayList<>(Arrays.asList(Opcode.values()));
    opcodes.sort(Comparator.comparingInt(o -> o.mMatchingIntegers.size()));

    List<Integer> takenValues = new ArrayList<>();

    int currentOpcode = 0;

    // process of elimination
    while (!opcodes.isEmpty()) {
      Opcode opcode = opcodes.get(currentOpcode);
      opcode.mMatchingIntegers.removeAll(takenValues);

      if (opcode.mMatchingIntegers.size() == 1) {
        opcode.mValue = (int) opcode.mMatchingIntegers.toArray()[0];
        mOpcodeMap.put(opcode.mValue, opcode);
        takenValues.add(opcode.mValue);
        opcodes.remove(opcode);
      }

      if (opcodes.size() != 0) {
        currentOpcode = (currentOpcode + 1) % opcodes.size();
      }
    }
  }

  public void doOpcode(int opcode, int a, int b, int c) {
    doOpcode(mOpcodeMap.get(opcode), a, b, c);
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
}
