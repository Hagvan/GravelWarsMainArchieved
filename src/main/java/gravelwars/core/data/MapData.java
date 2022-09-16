package gravelwars.core.data;

import gravelwars.core.Enums;
import gravelwars.core.map.MapNode;
import gravelwars.core.map.MapRoad;

import java.util.ArrayList;
import java.util.HashMap;

// class used for storing the map structure and mercenaries
public class MapData {

    private HashMap<Integer, MapNode> nodes;
    private HashMap<Integer, MapRoad> roads;
    private int nodeIndex; // used to give ids to new nodes
    private int roadIndex; // used to give ids to new roads
    private int squadIndex; // used to give ids to new squads
    private ArrayList<Integer> redCapitalNodes;
    private ArrayList<Integer> blueCapitalNodes;
    private ArrayList<Integer> unassignedCapitalNodes;

    public MapData() {

    }

    public MapData(HashMap<Integer, MapNode> nodes, HashMap<Integer, MapRoad> roads, int nodeIndex, int roadIndex, int squadIndex) {
        this.nodes = nodes;
        this.roads = roads;
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

    public HashMap<Integer, MapNode> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<Integer, MapNode> nodes) {
        this.nodes = nodes;
    }

    public HashMap<Integer, MapRoad> getRoads() {
        return roads;
    }

    public void setRoads(HashMap<Integer, MapRoad> roads) {
        this.roads = roads;
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
