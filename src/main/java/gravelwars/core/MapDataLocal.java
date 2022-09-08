package gravelwars.core;

import java.util.HashMap;

// extended version of MapData for storing the entire server locally and keeping track of IDs
public class MapDataLocal extends MapData {

    private int nodeIndex; // used to give ids to new nodes
    private int roadIndex; // used to give ids to new roads
    private int squadIndex; // used to give ids to new squads

    public MapDataLocal() {
        super();
    }

    public MapDataLocal(HashMap<Integer, MapNode> nodes, HashMap<Integer, MapRoad> roads, int nodeIndex, int roadIndex, int squadIndex) {
        super(nodes, roads);
        this.nodeIndex = nodeIndex;
        this.roadIndex = roadIndex;
        this.squadIndex = squadIndex;
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

}
