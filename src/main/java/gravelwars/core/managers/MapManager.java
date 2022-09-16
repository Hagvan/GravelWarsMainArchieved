package gravelwars.core.managers;

import com.google.gson.Gson;
import gravelwars.core.*;
import gravelwars.core.data.MapData;
import gravelwars.core.map.MapNode;
import gravelwars.core.map.MapRoad;
import gravelwars.core.map.Mercenaries;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public abstract class MapManager {

    private static final Path gravelwarsMapPath = Path.of("gravel-wars-map.json");
    private static MapData data;
    private static final Timer worldTimer = new Timer();
    private final static LinkedList<MoveSquadOrder> moveSquadOrders = new LinkedList<>();
    public final static HashMap<Integer, Mercenaries> mapMercenaries = new HashMap<>();

    public static void init() {
        // load from JSON or some other format to create the map
        // first should load all nodes and then load all the roads creating connections
        // then generate path routes
        load();
        for (MapNode node : data.getNodes().values()) {
            node.init();
            for (Mercenaries nodeMercenaries : node.getRedSquads().values()) {
                mapMercenaries.put(nodeMercenaries.getId(), nodeMercenaries);
            }
            for (Mercenaries nodeMercenaries : node.getBlueSquads().values()) {
                mapMercenaries.put(nodeMercenaries.getId(), nodeMercenaries);
            }
        }
        for (MapRoad road : data.getRoads().values()) {
            road.init();
            for (Mercenaries roadMercenaries : road.getNode1ToNode2()) {
                mapMercenaries.put(roadMercenaries.getId(), roadMercenaries);
            }
            for (Mercenaries roadMercenaries : road.getNode2ToNode1()) {
                mapMercenaries.put(roadMercenaries.getId(), roadMercenaries);
            }
        }
        // generate the paths after the map is fully loaded
        generatePaths();
    }

    private static void generatePaths() {
        HashMap<Integer, DijkstraTableRow> sampleRows = new HashMap<>();
        for (Map.Entry<Integer, MapNode> entry : data.getNodes().entrySet()) {
            sampleRows.put(entry.getKey(), new DijkstraTableRow(entry.getValue()));
        }
        for (Map.Entry<Integer, MapNode> entry : data.getNodes().entrySet()) {
            HashMap<Integer, MapRoad> resultForNode = new HashMap<>();
            HashMap<Integer, DijkstraTableRow> rows = new HashMap<>(sampleRows);
            ArrayList<DijkstraTableRow> visited = new ArrayList<>();
            ArrayList<DijkstraTableRow> unvisited = new ArrayList<>(sampleRows.values());
            MapNode startNode = entry.getValue();
            DijkstraTableRow startRow = rows.get(startNode.getId());
            for (DijkstraTableRow row : rows.values()) {
                row.distance = Integer.MAX_VALUE;
            }
            startRow.distance = 0;
            while (unvisited.size() > 0) {
                DijkstraTableRow currentRow = unvisited.stream().min(Comparator.comparingInt(DijkstraTableRow::getDistance)).get();
                MapNode currentNode = currentRow.node;
                for (MapRoad road : currentNode.getRoads().values()) {
                    MapNode neighborNode;
                    if (road.getNode1() == currentNode) {
                        neighborNode = road.getNode2();
                    } else {
                        neighborNode = road.getNode1();
                    }
                    DijkstraTableRow neighborRow = rows.get(neighborNode.getId());
                    if (visited.contains(neighborRow)) { // avoid going to a visited node while checking neighbors
                        continue;
                    }
                    int distanceToNeighbor = currentRow.distance + road.getLength();
                    DijkstraTableRow neighborNodeRow = rows.get(neighborNode.getId());
                    if (neighborNodeRow.distance > distanceToNeighbor) {
                        if (currentNode != startNode) {
                            distanceToNeighbor += 10; // adding the delay of passing through a node
                        }
                        neighborNodeRow.distance = distanceToNeighbor;
                        neighborNodeRow.previousNode = currentNode;
                        neighborNodeRow.previousRoad = road;
                    }
                }
                visited.add(rows.get(currentNode.getId()));
                unvisited.remove(rows.get(currentNode.getId()));
                if (currentNode == startNode) {
                    continue; // prevent starting node from being added to the result hashmap
                }
                DijkstraTableRow backtrackRow = currentRow; // backtrack to one of the roads originating from the startNode
                while (backtrackRow.previousNode != startNode) {
                    backtrackRow = rows.get(backtrackRow.previousNode.getId());
                }
                resultForNode.put(currentNode.getId(), backtrackRow.previousRoad); // put the current road
            }
            entry.getValue().setPathMap(resultForNode);
        }
    }

    private static class DijkstraTableRow {
        public MapNode node;
        public int distance;
        public MapNode previousNode;
        public MapRoad previousRoad;

        public DijkstraTableRow(MapNode node) {
            this.node = node;
            distance = Integer.MAX_VALUE;
            previousNode = null;
            previousRoad = null;
        }

        public int getDistance() {
            return distance;
        }
    }

    public static void start() {
        worldTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        tick();
                    }
                }, 100, 100);
    }

    public static void stop() {
        worldTimer.cancel();
    }

    private static void load() {
        Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(gravelwarsMapPath)) {
            data = gson.fromJson(reader, MapData.class);
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                generateWorld();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save() {
        Gson gson = new Gson();
        try (Writer writer = Files.newBufferedWriter(gravelwarsMapPath)) {
            String bussing = gson.toJson(data);
            System.out.println(bussing.length());
            writer.write(bussing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateWorld() {
        data = new MapData(new LinkedHashMap<>(), new LinkedHashMap<>(), 0, 0, 0);
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
        int redCapital = Math.floorMod(rng.nextInt(), nodeCount);
        int blueCapital = Math.max(0, nodeCount - redCapital);
        data.addCapital(getNodeById(redCapital), Enums.TFTeam.RED);
        data.addCapital(getNodeById(blueCapital), Enums.TFTeam.BLUE);
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
        switch (player.getTeam()) {
            case RED -> getNodeById(data.getRedCapitalNodes().get(0)).addSquad(mercenaries);
            case BLUE -> getNodeById(data.getBlueCapitalNodes().get(0)).addSquad(mercenaries);
        }
        mapMercenaries.put(mercenaries.getId(), mercenaries);
        return mercenaries;
    }

    public static boolean reinforceSquad(Mercenaries squad, int count) { // returns true if successful
        if (squad.canReinforce(count)) { // prevent overflow
            return false;
        }
        int costPerSpawn = 0;
        Enums.TFClass mercenariesClass = squad.getMercenariesClass();
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
        moveSquadOrders.add(new MoveSquadOrder(squad, node, road));
    }

    public static void moveSquad(Mercenaries squad, MapRoad road, MapNode node) { // road -> node
        moveSquadOrders.add(new MoveSquadOrder(squad, road, node));
    }

    private static void commitMoves() {
        for (MoveSquadOrder order : moveSquadOrders) {
            if (order.isNodeToRoad()) {
                order.getNode().removeSquad(order.getSquad()); // remove the mercenaries from origin node
                order.getRoad().addSquad(order.getSquad(), order.getNode()); // add the mercenaries to recipient road
                order.getSquad().resetMoveProgress(); // reset squad move progress
                order.getSquad().setCurrentRoad(order.getRoad()); // update reference to recipient road
            } else {
                order.getRoad().removeSquad(order.getSquad()); // remove the mercenaries from origin road
                order.getNode().addSquad(order.getSquad()); // add the mercenaries to recipient node
                order.getSquad().resetMoveProgress(); // reset squad move progress
                order.getSquad().setCurrentNode(order.getNode()); // update reference to recipient road
            }
        }
        moveSquadOrders.clear();
    }

    private static void tick() {
        for (MapRoad road : data.getRoads().values()) {
            road.tick();
        }
        for (MapNode node : data.getNodes().values()) {
            node.tick();
        }
        commitMoves();
    }

}
