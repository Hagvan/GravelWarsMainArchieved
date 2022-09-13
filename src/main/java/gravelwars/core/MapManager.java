package gravelwars.core;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public abstract class MapManager {

    private static final Path gravelwarsPath = Path.of("gravel-wars.json");
    private static MapDataLocal data;
    private static final Timer worldTimer = new Timer();

    public static void init() {
        // load from JSON or some other format to create the map
        // first should load all nodes and then load all the roads creating connections
        // then generate path routes
        load();
        for (MapNode node : data.getNodes().values()) {
            node.init();
        }
        for (MapRoad road : data.getRoads().values()) {
            road.init();
        }
        worldTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        tick();
                    }
                }, 1000, 1000);
    }

    public static void stop() {
        worldTimer.cancel();
        save();
    }

    private static void load() {
        Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(gravelwarsPath)) {
            data = gson.fromJson(reader, MapDataLocal.class);
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                generateWorld();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private static void save() {
        Gson gson = new Gson();
        try (Writer writer = Files.newBufferedWriter(gravelwarsPath)) {
            String bussing = gson.toJson(data);
            System.out.println(bussing.length());
            writer.write(bussing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateWorld() {
        data = new MapDataLocal(new HashMap<>(), new HashMap<>(), 0, 0, 0);
        Random rng = new Random();
        int nodeCount = Math.floorMod(rng.nextInt(), 31) + 10;
        MapNode[] generatedNodes = new MapNode[nodeCount];
        // first generate all the nodes
        int posX = 0;
        int posY = 0;
        for (int i = 0; i < nodeCount; i++) {
            posX += Math.floorMod(rng.nextInt(), 101);
            posY += Math.floorMod(rng.nextInt(), 31);
            String map = posX + " : " + posY;
            generatedNodes[i] = createNode(posX, posY, map);
        }
        // generate the roads themselves
        for (int i = 0; i < nodeCount - 1; i++) {
            int roads = Math.floorMod(rng.nextInt(), Math.min(nodeCount - i - 1, 4)) + 1;
            for (int j = 0; j < roads; j++) {
                createRoad(generatedNodes[i], generatedNodes[i + j + 1]);
            }
        }
    }

    public static MapNode getNodeById(int nodeId) {
        return data.getNodes().get(nodeId);
    }

    public static MapRoad getRoadById(int roadId) {
        return data.getRoads().get(roadId);
    }

    private static MapNode createNode(int posX, int posY, String map) {
        int nodeIndex = data.getNodeIndex();
        MapNode node = new MapNode(nodeIndex, posX, posY, map);
        data.getNodes().put(nodeIndex, node);
        data.setNodeIndex(nodeIndex + 1);
        return node;
    }

    private static MapRoad createRoad(MapNode node1, MapNode node2) {
        int roadIndex = data.getRoadIndex();
        MapRoad road = new MapRoad(roadIndex, node1, node2);
        data.getRoads().put(roadIndex, road);
        data.setRoadIndex(roadIndex + 1);
        return road;
    }

    public static Mercenaries createSquad(Player player, Enums.TFClass mercenariesClass) {
        int squadIndex = data.getSquadIndex();
        Mercenaries mercenaries = new Mercenaries(squadIndex, player, mercenariesClass);
        data.setSquadIndex(squadIndex + 1);
        return mercenaries;
    }

    public static boolean reinforceSquad(Mercenaries squad, int count) { // returns true if successful
        int spawns = squad.getSpawns();
        Enums.TFClass mercenariesClass = squad.getMercenariesClass();
        if (spawns + count > Mercenaries.MAX_SPAWNS) { // prevent overflow
            return false;
        }
        int costPerSpawn = 0;
        switch (mercenariesClass) { // should declare constants later
            case SCOUT -> costPerSpawn = 2;
            case SOLDIER, PYRO -> costPerSpawn = 4;
            case DEMOMAN, HEAVY -> costPerSpawn = 6;
            case ENGINEER, MEDIC -> costPerSpawn = 8;
            case SNIPER -> costPerSpawn = 20;
            case SPY -> costPerSpawn = 10;
        }
        int totalCost = costPerSpawn * count;
        Player owner = squad.getPlayer();
        if (owner.canAfford(totalCost)) {
            owner.spendMoney(totalCost);
            squad.addSpawns(count);
            return true;
        }
        return false;
    }

    public static void moveSquad(Mercenaries squad, MapNode node, MapRoad road) { // node -> road
        node.removeSquad(squad);
        road.addSquad(squad, node);
    }

    public static void moveSquad(Mercenaries squad, MapRoad road, MapNode node) { // road -> node
        road.removeSquad(squad);
        node.addSquad(squad);
    }

    private static void tick() {

    }

}
