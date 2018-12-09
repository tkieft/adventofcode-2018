package com.tylerkieft;

/*
 * Custom doubly-linked, circular list that has no concept of a "start" or an "end", rather just a current position
 */
public class MarbleList {

  private class Node {
    Node mNext;
    Node mPrev;
    Integer mValue;
  }

  private Node mCurrentNode;

  public MarbleList() {
  }

  public int remove() {
    Node removed = mCurrentNode;
    removed.mPrev.mNext = removed.mNext;
    removed.mNext.mPrev = removed.mPrev;
    mCurrentNode = removed.mNext;
    return removed.mValue;
  }

  public void add(int marble) {
    Node newNode = new Node();
    newNode.mValue = marble;

    if (mCurrentNode == null) {
      mCurrentNode = newNode;
      mCurrentNode.mNext = newNode;
      mCurrentNode.mPrev = newNode;
    } else {
      newNode.mNext = mCurrentNode;
      newNode.mPrev = mCurrentNode.mPrev;
      mCurrentNode.mPrev = newNode;
      newNode.mPrev.mNext = newNode;
      mCurrentNode = newNode;
    }
  }

  public void move(int delta) {
    while (delta > 0) {
      mCurrentNode = mCurrentNode.mNext;
      delta--;
    }

    while (delta < 0) {
      mCurrentNode = mCurrentNode.mPrev;
      delta++;
    }
  }
}
