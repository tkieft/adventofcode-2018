package com.tylerkieft;

public class Claim {

  private String mId;
  private int mX;
  private int mY;
  private int mWidth;
  private int mHeight;

  public Claim(String id, int x, int y, int width, int height) {
    mId = id;
    mX = x;
    mY = y;
    mWidth = width;
    mHeight = height;
  }

  public String getId() {
    return mId;
  }

  public int getX() {
    return mX;
  }

  public int getY() {
    return mY;
  }

  public int getWidth() {
    return mWidth;
  }

  public int getHeight() {
    return mHeight;
  }

  @Override
  public String toString() {
    return "#" + mId + " @ " + mX + "," + mY + ": " + mWidth + "x" + mHeight;
  }
}
