package gravelwars.core;

import java.util.ArrayList;
import java.util.HashMap;

// extended version of MapData for storing the entire server locally and keeping track of IDs
public class MapDataLocal extends MapData {

    private int nodeIndex; // used to give ids to new nodes
    private int roadIndex; // used to give ids to new roads
    private int squadIndex; // used to give ids to new squads
    private ArrayList<Integer> redCapitalNodes;
    private ArrayList<Integer> blueCapitalNodes;
    private ArrayList<Integer> unassignedCapitalNodes;

    public MapDataLocal() {
        super();
    }

    public MapDataLocal(HashMap<Integer, MapNode> nodes, HashMap<Integer, MapRoad> roads, int nodeIndex, int roadIndex, int squadIndex) {
        super(nodes, roads);
        this.nodeIndex = nodeIndex;
        this.roadIndex = roadIndex;
        this.squadIndex = squadIndex;
        redCapitalNodes = new ArrayList<>();
        blueCapitalNodes = new ArrayList<>();
        unassignedCapitalNodes = new ArrayList<>();
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public int getRoadIndex() {
        return roadIndex;
    }

    public void setRoadIndex(int roadIndex) {
        this.roadIndex = roadIndex;
    }

    public int getSquadIndex() {
        return squadIndex;
    }

    public void setSquadIndex(int squadIndex) {
        this.squadIndex = squadIndex;
    }

    public void addCapital(MapNode mapNode, Enums.TFTeam team) {
        switch (team) {
            case UNASSIGNED -> unassignedCapitalNodes.add(mapNode.getId());
            case BLUE -> blueCapitalNodes.add(mapNode.getId());
            case RED -> redCapitalNodes.add(mapNode.getId());
        }
        mapNode.setCapital(team);
    }

    public ArrayList<Integer> getRedCapitalNodes() {
        return redCapitalNodes;
    }

    public ArrayList<Integer> getBlueCapitalNodes() {
        return blueCapitalNodes;
    }

    public ArrayList<Integer> getUnassignedCapitalNodes() {
        return unassignedCapitalNodes;
    }
}
