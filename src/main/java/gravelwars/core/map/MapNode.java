package gravelwars.core.map;

import gravelwars.core.Enums;
import gravelwars.core.managers.MapManager;

import java.util.*;

public class MapNode {

    private static final int MAX_INFLUENCE = 100;
    private static final int MIN_INFLUENCE = -100;
    private static final int INFLUENCE_PER_POINT = 5;
    private int id;
    private Coordinates position;
    private String mapName;
    private HashMap<Integer, Mercenaries> redSquads;
    private HashMap<Integer, Mercenaries> blueSquads;
    private ArrayList<Integer> roadsById;
    private transient HashMap<Integer, MapRoad> roads; // avoid circular reference
    private int influence; // -100 = fully controlled by BLU, 100 - fully controlled by RED
    private transient HashMap<Integer, MapRoad> pathMap;

    public MapNode(int id, int posX, int posY, String mapName) {
        this.id = id;
        this.position = new Coordinates(posX, posY);
        this.mapName = mapName;
        redSquads = new HashMap<>();
        blueSquads = new HashMap<>();
        influence = 0;
        roadsById = new ArrayList<>();
        roads = new HashMap<>();
    }

    public void init() {
        roads = new HashMap<>();
        for (Integer roadId : roadsById) {
            roads.put(roadId, MapManager.getRoadById(roadId));
        }
    }

    public void setPathMap(HashMap<Integer, MapRoad> pathMap) {
        this.pathMap = pathMap;
    }

    public void addRoad(MapRoad road) {
        if (!roads.containsKey(road.getId())) {
            roadsById.add(road.getId());
            roads.put(road.getId(), road);
        }
    }

    public void addInfluence(Enums.TFTeam winner, int count) { // TODO: maybe add cause later - BLU captured 3 points for example
        if (winner.equals(Enums.TFTeam.RED)) {
            influence = Math.min(influence + INFLUENCE_PER_POINT * count, MAX_INFLUENCE);
        } else if (winner.equals(Enums.TFTeam.BLUE)) {
            influence = Math.max(influence - INFLUENCE_PER_POINT * count, MIN_INFLUENCE);
        }
    }

    public void addSquad(Mercenaries squad) {
        if (squad.getTeam().equals(Enums.TFTeam.RED)) {
            redSquads.put(squad.getId(), squad);
        } else if (squad.getTeam().equals(Enums.TFTeam.BLUE)) {
            blueSquads.put(squad.getId(), squad);
        }
    }

    public void removeSquad(Mercenaries squad) {
        if (squad.getTeam().equals(Enums.TFTeam.RED)) {
            redSquads.remove(squad.getId());
        } else if (squad.getTeam().equals(Enums.TFTeam.BLUE)) {
            redSquads.remove(squad.getId());
        }
    }

    public Coordinates getPosition() {
        return position;
    }

    public String getMapName() {
        return mapName;
    }

    public HashMap<Integer, MapRoad> getRoads() {
        return roads;
    }

    public int getInfluence() {
        return influence;
    }

    public void tick() {
        for (Mercenaries squad : redSquads.values()) {
            if (squad.getDestinationNodeId() == id) {
                continue;
            }
            if (squad.tick() == 10) {
                MapManager.moveSquad(squad, this, pathMap.get(squad.getDestinationNodeId()));
            }
        }
        for (Mercenaries squad : blueSquads.values()) {
            if (squad.getDestinationNodeId() == id) {
                continue;
            }
            if (squad.tick() == 10) {
                MapManager.moveSquad(squad, this, pathMap.get(squad.getDestinationNodeId()));
            }
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getRoadsById() {
        return roadsById;
    }

    public void setRoadsById(ArrayList<Integer> roadsById) {
        this.roadsById = roadsById;
    }

    public void setCapital(Enums.TFTeam team) {
        switch (team) {
            case UNASSIGNED -> influence = 0;
            case RED -> influence = MAX_INFLUENCE;
            case BLUE -> influence = MIN_INFLUENCE;
        }
    }

    public HashMap<Integer, Mercenaries> getRedSquads() {
        return redSquads;
    }

    public HashMap<Integer, Mercenaries> getBlueSquads() {
        return blueSquads;
    }
}
