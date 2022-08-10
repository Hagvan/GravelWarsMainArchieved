package gravelwars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapNode {

    private final int MAX_INFLUECE = 100;
    private final int MIN_INFLUECE = -100;
    private final int INFLUECE_PER_POINT = 5;
    private int id;
    private Coordinates position;
    private String map_name;
    private HashMap<Integer, MercSquad> red_squads;
    private HashMap<Integer, MercSquad> blu_squads;
    private HashMap<Integer, MapRoad> roads;
    private int influence; // -100 = fully controlled by BLU, 100 - fully controlled by RED

    public MapNode(int id, int pos_x, int pos_y, String map_name) {
        this.id = id;
        this.position = new Coordinates(pos_x, pos_y);
        this.map_name = map_name;
        red_squads = new HashMap<>();
        blu_squads = new HashMap<>();
        influence = 0;
        roads = new HashMap<>();
    }

    public void AddRoad(MapRoad road) {
        if (!roads.containsKey(road.GetId())) {
            roads.put(road.GetId(), road);
        }
    }

    public void AddInfluence(Enums.TFTeam winner, int count) { // TODO: maybe add cause later - BLU captured 3 points for example
        if (winner.equals(Enums.TFTeam.RED)) {
            influence = Math.min(influence + INFLUECE_PER_POINT * count, MAX_INFLUECE);
        } else if (winner.equals(Enums.TFTeam.BLUE)) {
            influence = Math.max(influence - INFLUECE_PER_POINT * count, MIN_INFLUECE);
        }
    }

    public void AddSquad(MercSquad squad) {
        if (squad.GetTeam().equals(Enums.TFTeam.RED)) {
            red_squads.put(squad.GetId(), squad);
        } else if (squad.GetTeam().equals(Enums.TFTeam.BLUE)) {
            blu_squads.put(squad.GetId(), squad);
        }
    }

    public void RemoveSquad(MercSquad squad) {
        if (squad.GetTeam().equals(Enums.TFTeam.RED)) {
            red_squads.remove(squad.GetId());
        } else if (squad.GetTeam().equals(Enums.TFTeam.BLUE)) {
            red_squads.remove(squad.GetId());
        }
    }

    public Coordinates getPosition() {
        return position;
    }

}
