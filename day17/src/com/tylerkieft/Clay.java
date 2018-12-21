package com.tylerkieft;

public class Clay {

  enum Type {
    VERTICAL,
    HORIZONTAL
  }

  public final Type mType;
  public final int mDimen1;
  public final int mDimen2;
  public final int mDimen3;

  public Clay(Type type, int dimen1, int dimen2, int dimen3) {
    mType = type;
    mDimen1 = dimen1;
    mDimen2 = dimen2;
    mDimen3 = dimen3;
  }
}
