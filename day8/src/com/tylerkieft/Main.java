package com.tylerkieft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

  private static TreeNode buildTree(Scanner scanner) {
    TreeNode node = new TreeNode();

    int childCount = scanner.nextInt();
    int metadataCount = scanner.nextInt();

    for (int i = 0; i < childCount; i++) {
      node.addChild(buildTree(scanner));
    }

    for (int i = 0; i < metadataCount; i++) {
      node.addMetadata(scanner.nextInt());
    }

    return node;
  }

  private static TreeNode readFile(String filename) {
    try (Scanner scanner = new Scanner(new File(filename))) {
      return buildTree(scanner);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static int metadataSum(TreeNode root) {
    return root.getChildren().stream().map(Main::metadataSum).reduce(Integer::sum).orElseGet(() -> 0) +
        root.getMetadata().stream().reduce(Integer::sum).orElseGet(() -> 0);
  }

  private static int value(TreeNode root) {
    if (root.getChildren().isEmpty()) {
      return root.getMetadata().stream().reduce(Integer::sum).orElseGet(() -> 0);
    } else {
      return root.getMetadata().stream().map(metadata -> {
        if (metadata > 0 && metadata <= root.getChildren().size()) {
          return value(root.getChildren().get(metadata - 1));
        } else {
          return 0;
        }
      }).reduce(Integer::sum).orElseGet(() -> 0);
    }
  }

  public static void main(String[] args) {
    String filename = args[0];
    TreeNode root = readFile(filename);

    System.out.println(metadataSum(root));
    System.out.println(value(root));
  }
}
