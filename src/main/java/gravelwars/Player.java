package gravelwars;

public class Player {
    private final int MAX_MERC_SQUADS = 3;
    private final int MIN_MONEY = 0;
    private int steam_id; // user's steam id
    private long session_id; // user's session id
    private MercSquad[] mercSquads;
    private int squad_count;
    private int money;
    private Enums.TFTeam team;

    public Player(int steam_id) {
        this.steam_id = steam_id;
        team = Enums.TFTeam.UNASSIGNED;
        money = MIN_MONEY;
        mercSquads = new MercSquad[MAX_MERC_SQUADS];
        squad_count = 0;
    }

    public boolean CanAfford(int cost) {
        if (cost > money) {
            return false;
        }
        return true;
    }

    public void SpendMoney(int amount) {
        money -= amount;
    }

    public Enums.TFTeam GetTeam() {
        return team;
    }

    public boolean CreateSquad(Enums.TFClass merc_class) {
        if (squad_count == MAX_MERC_SQUADS || CanAfford(100)) { // arbitrary squad creating cost
            return false;
        }
        mercSquads[squad_count] = MapManager.CreateSquad(this, merc_class);
        squad_count++;
        return true;
    }

    public boolean ReinforceSquad(int squad_index, int count) {
        MapManager.ReinforceSquad(mercSquads[squad_index], count);
        return false;
    }

    public void MoveSquad(int squad_index, int destination_id) {

        // will find the destination and assign it inside squad
    }

}
