package gravelwars.core.map;

import gravelwars.core.managers.MapManager;

import java.util.LinkedList;

public class MapRoad {

    private int id;
    private final int length;
    private int node1Id; // avoid circular reference
    private transient MapNode node1;
    private int node2Id; // avoid circular reference
    private transient MapNode node2;
    private LinkedList<Mercenaries> node1ToNode2;
    private LinkedList<Mercenaries> node2ToNode1;

    public MapRoad(int id, MapNode node1, MapNode node2) {
        this.id = id;
        node1Id = node1.getId();
        this.node1 = node1;
        node2Id = node2.getId();
        this.node2 = node2;
        node1.addRoad(this);
        node2.addRoad(this);
        node1ToNode2 = new LinkedList<>();
        node2ToNode1 = new LinkedList<>();
        length = node1.getPosition().getDistance(node2.getPosition());
    }

    public void init() {
        node1 = MapManager.getNodeById(node1Id);
        node2 = MapManager.getNodeById(node2Id);
    }

    public void addSquad(Mercenaries squad, MapNode from) {
        if (from == node1) {
            node1ToNode2.add(squad);
        } else if (from == node2) {
            node2ToNode1.add(squad);
        }
    }

    public void removeSquad(Mercenaries squad) {
        node1ToNode2.remove(squad);
        node2ToNode1.remove(squad);
    }

    public MapNode getNode1() {
        return node1;
    }

    public MapNode getNode2() {
        return node2;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public void tick() {
        for (Mercenaries squad : node1ToNode2) {
            if (squad.tick() == length) {
                MapManager.moveSquad(squad, this, node2);
            }
        }
        for (Mercenaries squad : node2ToNode1) {
            if (squad.tick() == length) {
                MapManager.moveSquad(squad, this, node1);
            }
        }
    }

    public LinkedList<Mercenaries> getNode1ToNode2() {
        return node1ToNode2;
    }

    public LinkedList<Mercenaries> getNode2ToNode1() {
        return node2ToNode1;
    }
}
