package gravelwars.core;

public class MoveSquadOrder {

    private final MapNode node;
    private final MapRoad road;
    private final Mercenaries squad;
    private final boolean nodeToRoad;

    public MoveSquadOrder(Mercenaries squad, MapNode node, MapRoad road) {
        this.squad = squad;
        this.node = node;
        this.road = road;
        nodeToRoad = true;
    }

    public MoveSquadOrder(Mercenaries squad, MapRoad road, MapNode node) {
        this.squad = squad;
        this.node = node;
        this.road = road;
        nodeToRoad = false;
    }

    public MapNode getNode() {
        return node;
    }

    public MapRoad getRoad() {
        return road;
    }

    public Mercenaries getSquad() {
        return squad;
    }

    public boolean isNodeToRoad() {
        return nodeToRoad;
    }

}
