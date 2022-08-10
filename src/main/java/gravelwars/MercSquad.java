package gravelwars;

public class MercSquad {

    static final int MAX_SPAWNS = 100;
    static final int MIN_SPAWNS = 0;
    private int id;
    private int spawns;
    private Enums.TFClass merc_class;
    private Player player;

    private MapNode destination;

    public MercSquad(int id, Player player, Enums.TFClass merc_class) {
        this.id = id;
        this.player = player;
        this.merc_class = merc_class;
        spawns = MIN_SPAWNS;
    }

    public int GetId() {
        return id;
    }

    public void AddSpawns(int spawns_to_add) {
        spawns += spawns_to_add;
    }

    public Enums.TFClass getMerc_class() {
        return merc_class;
    }

    public int getSpawns() {
        return spawns;
    }

    public Player GetPlayer() {
        return player;
    }

    public Enums.TFTeam GetTeam() {
        return player.GetTeam();
    }
}
