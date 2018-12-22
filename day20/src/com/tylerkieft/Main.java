package com.tylerkieft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

  private static Map<Room, Integer> findPaths(Set<Room> rooms, Room startRoom) {
    Map<Room, Integer> visitedRooms = new HashMap<>();
    visitedRooms.put(startRoom, 0);

    Queue<Room> roomsToVisit = new ArrayDeque<>();
    roomsToVisit.add(startRoom);

    int distance = 0;

    while (visitedRooms.size() != rooms.size()) {
      Queue<Room> nextRoomsToVisit = new ArrayDeque<>();
      while (!roomsToVisit.isEmpty()) {
        Room room = roomsToVisit.poll();

        if (room.north != null && visitedRooms.putIfAbsent(room.north, distance + 1) == null) {
          nextRoomsToVisit.add(room.north);
        }
        if (room.east != null && visitedRooms.putIfAbsent(room.east, distance + 1) == null) {
          nextRoomsToVisit.add(room.east);
        }
        if (room.south != null && visitedRooms.putIfAbsent(room.south, distance + 1) == null) {
          nextRoomsToVisit.add(room.south);
        }
        if (room.west != null && visitedRooms.putIfAbsent(room.west, distance + 1) == null) {
          nextRoomsToVisit.add(room.west);
        }
      }
      roomsToVisit = nextRoomsToVisit;
      distance++;
    }

    return visitedRooms;
  }

  public static void main(String[] args) {
    String filename = args[0];

    Room startRoom = new Room();
    Room currentRoom = startRoom;

    Set<Room> rooms = new HashSet<>();
    rooms.add(startRoom);

    Stack<Room> roomStack = new Stack<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
      char c;
      while ((c = (char) reader.read()) != '$') {
        if (c == '^') continue;

        if (c == 'N') {
          if (currentRoom.north == null) {
            currentRoom.north = new Room();
            currentRoom.north.south = currentRoom;
            rooms.add(currentRoom.north);
          }
          currentRoom = currentRoom.north;
        } else if (c == 'E') {
          if (currentRoom.east == null) {
            currentRoom.east = new Room();
            currentRoom.east.west = currentRoom;
            rooms.add(currentRoom.east);
          }
          currentRoom = currentRoom.east;
        } else if (c == 'S') {
          if (currentRoom.south == null) {
            currentRoom.south = new Room();
            currentRoom.south.north = currentRoom;
            rooms.add(currentRoom.south);
          }
          currentRoom = currentRoom.south;
        } else if (c == 'W') {
          if (currentRoom.west == null) {
            currentRoom.west = new Room();
            currentRoom.west.east = currentRoom;
            rooms.add(currentRoom.west);
          }
          currentRoom = currentRoom.west;
        } else if (c == '(') {
          roomStack.push(currentRoom);
        } else if (c == ')') {
          roomStack.pop();
        } else if (c == '|') {
          currentRoom = roomStack.peek();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<Room, Integer> paths = findPaths(rooms, startRoom);

    System.out.println(paths.entrySet()
        .stream()
        .min((o1, o2) -> o2.getValue() - o1.getValue())
        .get()
        .getValue());

    System.out.println(paths.entrySet().stream().filter(entry -> entry.getValue() >= 1000).count());
  }
}
