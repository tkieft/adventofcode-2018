package com.tylerkieft;

import java.util.stream.IntStream;

/**
 * This algorithm probably isn't optimal - there's probably some duplicate work done. But it's fast enough.
 */
public class WaterFiller {

  private int mX;
  private int mY;

  private Reservoir mReservoir;

  public WaterFiller(Reservoir reservoir) {
    mReservoir = reservoir;
  }

  private boolean isWallOrWater(char c) {
    return c == '#' || c == '~';
  }

  private boolean isEmptyOrWater(char c) {
    return c == '|' || c == '.' || c == '~';
  }

  private void goDown() {
    while (mY < mReservoir.rows() - 1 && !isWallOrWater(mReservoir.get(mY + 1, mX))) {
      mReservoir.set(mY + 1, mX, '|');
      mY++;
    }
  }

  private boolean spreadHorizontal() {
    // find left bound
    int xl = mX - 1;
    while (isEmptyOrWater(mReservoir.get(mY, xl)) && isWallOrWater(mReservoir.get(mY + 1, xl))) {
      xl--;
    }

    // find right bound
    int xr = mX + 1;
    while (isEmptyOrWater(mReservoir.get(mY, xr)) && isWallOrWater(mReservoir.get(mY + 1, xr))) {
      xr++;
    }

    if (mReservoir.get(mY, xl) == '#' && mReservoir.get(mY, xr) == '#') {
      // We're in a basin
      for (int i = xl + 1; i <= xr - 1; i++) {
        mReservoir.set(mY, i, '~');
      }
      return true;
    } else {
      // We're going to run off the side

      // Set all blocks to be touching water
      for (int i = xl + 1; i <= xr - 1; i++) {
        mReservoir.set(mY, i, '|');
      }

      // And continue off one side or both
      if (mReservoir.get(mY, xl) == '#') {
        mReservoir.set(mY, xr, '|');
        mX = xr;
      } else if (mReservoir.get(mY, xr) == '#') {
        mReservoir.set(mY, xl, '|');
        mX = xl;
      } else {
        // spawn a new water filler to fill down the left side
        mReservoir.set(mY, xl, '|');
        new WaterFiller(mReservoir).fill(mY, xl);

        // and continue filling down the right side
        mReservoir.set(mY, xr, '|');
        mX = xr;
      }

      return false;
    }
  }

  private void goUp() {
    mY--;
  }

  public void fill(int y, int x) {
    mX = x;
    mY = y;

    while (true) {
      goDown();

      if (mY == mReservoir.rows() - 1) {
        // we've flowed to the bottom row, simulation is done
        return;
      }

      while (true) {
        boolean filled = spreadHorizontal();

        if (!filled) break;

        goUp();
      }
    }
  }

  public void fill() {
    // Start at the water source
    int x = IntStream
        .range(0, mReservoir.columns())
        .filter(i -> '+' == mReservoir.get(0, i))
        .findFirst().getAsInt();

    fill(0, x);
  }
}
