package gravelwars.core;

public class Mercenaries {

    static final int MAX_SPAWNS = 100;
    static final int MIN_SPAWNS = 0;
    private int id;
    private int spawns;
    private Enums.TFClass mercenariesClass;
    private Player player;
    private int destinationNodeId; // avoid circular reference
    private int moveProgress;

    public Mercenaries(int id, Player player, Enums.TFClass mercenariesClass) {
        this.id = id;
        this.player = player;
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
        return player.getTeam();
    }

    public int tick() {
        return moveProgress++;
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
}
