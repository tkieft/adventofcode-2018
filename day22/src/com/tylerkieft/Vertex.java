package com.tylerkieft;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

  enum Type {
    ROCKY(Equipment.TORCH, Equipment.CLIMBING_GEAR),
    WET(Equipment.NEITHER, Equipment.CLIMBING_GEAR),
    NARROW(Equipment.NEITHER, Equipment.TORCH);

    final List<Equipment> mAllowedEquipment;

    Type(Equipment e1, Equipment e2) {
      mAllowedEquipment = new ArrayList<>();
      mAllowedEquipment.add(e1);
      mAllowedEquipment.add(e2);
    }
  }

  enum Equipment {
    NEITHER,
    TORCH,
    CLIMBING_GEAR
  }

  public final Type type;
  public final Equipment equipment;
  public final List<Edge> edges;

  public static List<Vertex> create(int riskLevel) {
    Type type = Type.values()[riskLevel];

    List<Vertex> vertices = new ArrayList<>();

    for (Equipment equipment : type.mAllowedEquipment) {
      vertices.add(new Vertex(type, equipment));
    }

    vertices.get(0).edges.add(new Edge(vertices.get(1), 7));
    vertices.get(1).edges.add(new Edge(vertices.get(0), 7));

    return vertices;
  }

  public Vertex(Type type, Equipment equipment) {
    this.type = type;
    this.equipment = equipment;
    edges = new ArrayList<>();
  }
}
