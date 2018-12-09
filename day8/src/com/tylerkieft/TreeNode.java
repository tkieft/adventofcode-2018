package com.tylerkieft;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

  private List<TreeNode> mChildren;
  private List<Integer> mMetadata;

  public TreeNode() {
    mChildren = new ArrayList<>();
    mMetadata = new ArrayList<>();
  }

  public void addChild(TreeNode child) {
    mChildren.add(child);
  }

  public void addMetadata(int metadata) {
    mMetadata.add(metadata);
  }

  public List<TreeNode> getChildren() {
    return mChildren;
  }

  public List<Integer> getMetadata() {
    return mMetadata;
  }
}
