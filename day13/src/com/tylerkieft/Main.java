package com.tylerkieft;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  private static List<List<Character>> readFile(String filename) {
    List<List<Character>> map = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filename))) {
      while (scanner.hasNextLine()) {
        map.add(scanner.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return map;
  }

  private static List<Car> findCars(List<List<Character>> map) {
    List<Car> cars = new ArrayList<>();

    for (int y = 0; y < map.size(); y++) {
      List<Character> row = map.get(y);
      for (int x = 0; x < row.size(); x++) {
        if (Car.isCarCharacter(row.get(x))) {
          Car newCar = Car.from(row.get(x), new Point(x, y));
          cars.add(newCar);
          // We assume a car never starts on a curve
          row.set(x, newCar.getDirection() == Direction.NORTH || newCar.getDirection() == Direction.SOUTH ? '|' : '-');
        }
      }
    }

    return cars;
  }

  public static void main(String[] args) {
    String filename = args[0];
    List<List<Character>> map = readFile(filename);

    List<Car> cars = findCars(map);

    while (true) {
      for (int i = 0; i < cars.size(); i++) {
        Car car = cars.get(i);
        car.advance(map);

        Map<Point, List<Car>> carsMap = cars.stream().collect(Collectors.groupingBy(Car::getLocation));

        for (Map.Entry<Point, List<Car>> entry : carsMap.entrySet()) {
          if (entry.getValue().size() > 1) {
            System.out.println("Crash! at (" + entry.getKey().x + "," + entry.getKey().y + ")");
            cars.removeAll(entry.getValue());
          }
        }
      }

      if (cars.size() == 1) {
        System.out.println("Last car at (" + cars.get(0).getLocation().x + "," + cars.get(0).getLocation().y + ")");
        break;
      }

      cars.sort((c1, c2) -> {
        Point l1 = c1.getLocation();
        Point l2 = c2.getLocation();

        if (l1.y == l2.y) {
          return l1.x - l2.x;
        } else {
          return l1.y - l2.y;
        }
      });
    }
  }
}
