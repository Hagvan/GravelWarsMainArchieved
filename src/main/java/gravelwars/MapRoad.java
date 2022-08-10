package gravelwars;

import java.util.LinkedList;

public class MapRoad {

    private int id;
    private int length;
    private MapNode node1;
    private MapNode node2;
    private LinkedList<MercSquad> node1_to_node2;
    private LinkedList<MercSquad> node2_to_node1;

    public MapRoad(int id, MapNode node1, MapNode node2) {
        this.id = id;
        this.node1 = node1;
        this.node2 = node2;
        node1.AddRoad(this);
        node2.AddRoad(this);
        length = node1.getPosition().GetDistance(node2.getPosition());
    }

    public void AddSquad(MercSquad squad, MapNode from) {
        if (from == node1) {
            node1_to_node2.add(squad);
        } else if (from == node2) {
            node2_to_node1.add(squad);
        }
    }

    public void RemoveSquad(MercSquad squad) {
        node1_to_node2.remove(squad);
        node2_to_node1.remove(squad);
    }

    public int GetId() {
        return id;
    }

    public void Tick() {

    }

}
