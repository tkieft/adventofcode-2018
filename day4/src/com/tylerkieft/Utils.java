package com.tylerkieft;

import java.util.Map;
import java.util.Optional;

public class Utils {

  public static <K> Optional<Map.Entry<K, Integer>> entryForMaxValue(Map<K, Integer> map) {
    return map.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue());
  }
}
