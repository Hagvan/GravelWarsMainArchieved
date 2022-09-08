package gravelwars.core;

import java.util.HashMap;

// acts as a serializable container for MapManager.java, basic version only for sharing data with
// web clients
public class MapData {

    private HashMap<Integer, MapNode> nodes;
    private HashMap<Integer, MapRoad> roads;

    public MapData() {

    }

    public MapData(HashMap<Integer, MapNode> nodes, HashMap<Integer, MapRoad> roads) {
        this.nodes = nodes;
        this.roads = roads;
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

}
