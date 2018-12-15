package com.tylerkieft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  private static List<Integer> findNextTenScoresAfter(int numRecipes) {
    List<Integer> recipes = new ArrayList<>();
    recipes.add(3);
    recipes.add(7);

    int elf1Index = 0;
    int elf2Index = 1;

    while (recipes.size() < numRecipes + 10) {
      int elf1RecipeScore = recipes.get(elf1Index);
      int elf2RecipeScore = recipes.get(elf2Index);

      int recipeSum = elf1RecipeScore + elf2RecipeScore;
      char[] recipeSumChars = String.valueOf(recipeSum).toCharArray();
      for (char c : recipeSumChars) {
        recipes.add(c - '0');
      }

      elf1Index = (elf1Index + elf1RecipeScore + 1) % recipes.size();
      elf2Index = (elf2Index + elf2RecipeScore + 1) % recipes.size();
    }

    return recipes.subList(numRecipes, numRecipes + 10);
  }

  private static int findRecipesBefore(String scores) {
    List<Integer> recipes = new ArrayList<>();
    recipes.add(3);
    recipes.add(7);

    int elf1Index = 0;
    int elf2Index = 1;

    while (true) {
      int elf1RecipeScore = recipes.get(elf1Index);
      int elf2RecipeScore = recipes.get(elf2Index);

      int recipeSum = elf1RecipeScore + elf2RecipeScore;
      char[] recipeSumChars = String.valueOf(recipeSum).toCharArray();
      for (char c : recipeSumChars) {
        recipes.add(c - '0');
      }

      elf1Index = (elf1Index + elf1RecipeScore + 1) % recipes.size();
      elf2Index = (elf2Index + elf2RecipeScore + 1) % recipes.size();

      // extra buffer than we probably need to deal with overlap
      int buffer = scores.length() * 2;
      String lastRecipesMade = recipes.subList(Math.max(0, recipes.size() - buffer), recipes.size())
          .stream().map(String::valueOf).collect(Collectors.joining());
      int indexOfScores = lastRecipesMade.indexOf(scores);
      if (indexOfScores >= 0) {
        return recipes.size() - buffer + indexOfScores;
      }
    }
  }

  public static void main(String[] args) {
    System.out.println(findNextTenScoresAfter(880751).stream().map(String::valueOf).collect(Collectors.joining()));
    System.out.println(findRecipesBefore("880751"));
  }
}
