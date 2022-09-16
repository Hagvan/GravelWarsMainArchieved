package gravelwars.core.map;

import gravelwars.core.Enums;
import gravelwars.core.Player;

public class Mercenaries {

    static final int MAX_SPAWNS = 100;
    static final int MIN_SPAWNS = 0;
    private int id;
    private int spawns;
    private Enums.TFClass mercenariesClass;
    private int playerId;
    private Enums.TFTeam team;
    private transient Player player;
    private transient MapNode currentNode;
    private transient MapRoad currentRoad;
    private int destinationNodeId; // avoid circular reference
    private int moveProgress;

    public Mercenaries(int id, Player player, Enums.TFClass mercenariesClass) {
        this.id = id;
        this.player = player;
        playerId = player.getSteamId();
        team = player.getTeam();
        this.mercenariesClass = mercenariesClass;
        spawns = MIN_SPAWNS;
    }

    public int getId() {
        return id;
    }

    public void addSpawns(int spawnsToAdd) {
        spawns += spawnsToAdd;
    }

    public Enums.TFClass getMercenariesClass() {
        return mercenariesClass;
    }

    public int getSpawns() {
        return spawns;
    }

    public Player getPlayer() {
        return player;
    }

    public Enums.TFTeam getTeam() {
        return team;
    }

    public int tick() {
        return moveProgress++;
    }

    public boolean canReinforce(int count) {
        return spawns + count <= MAX_SPAWNS;
    }

    public void resetMoveProgress() {
        moveProgress = 0;
    }

    public int getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(int nodeId) {
        destinationNodeId = nodeId;
    }

    public void setCurrentNode(MapNode node) {
        currentNode = node;
        currentRoad = null;
    }

    public void setCurrentRoad(MapRoad road) {
        currentRoad = road;
        currentNode = null;
    }
}
