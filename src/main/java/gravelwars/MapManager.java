package gravelwars;

import java.util.HashMap;

public class MapManager {

    public static HashMap<Integer, MapNode> nodes;
    public static HashMap<Integer, MapRoad> roads;
    private static int node_index; // used to give ids to new nodes
    private static int road_index; // used to give ids to new roads
    private static int squad_index; // used to give ids to new squads

    public static void InitMap() {
        // load from JSON or some other format to create the map
        // first should load all nodes and then load all the roads creating connections
    }

    public static MercSquad CreateSquad(Player player, Enums.TFClass merc_class) {
        return new MercSquad(squad_index++, player, merc_class);
    }

    public static boolean ReinforceSquad(MercSquad squad, int count) { // returns true if successful
        int spawns = squad.getSpawns();
        Enums.TFClass merc_class = squad.getMerc_class();
        if (spawns + count > MercSquad.MAX_SPAWNS) { // prevent overflow
            return false;
        }
        int cost_per_spawn = 0;
        switch (merc_class) { // should declare constants later
            case SCOUT -> cost_per_spawn = 2;
            case SOLDIER, PYRO -> cost_per_spawn = 4;
            case DEMOMAN, HEAVY -> cost_per_spawn = 6;
            case ENGINEER, MEDIC -> cost_per_spawn = 8;
            case SNIPER -> cost_per_spawn = 20;
            case SPY -> cost_per_spawn = 10;
        }
        int total_cost = cost_per_spawn * count;
        Player owner = squad.GetPlayer();
        if (owner.CanAfford(total_cost)) {
            owner.SpendMoney(total_cost);
            squad.AddSpawns(count);
            return true;
        }
        return false;
    }

    public static void MoveSquad(MercSquad squad, MapNode node, MapRoad road) { // node -> road
        node.RemoveSquad(squad);
        road.AddSquad(squad, node);
    }

    public static void MoveSquad(MercSquad squad, MapRoad road, MapNode node) { // road -> node
        road.RemoveSquad(squad);
        node.AddSquad(squad);
    }

}
