package gravelwars.core;

public class Player {
    private static final int MAX_MERCENARY_SQUADS = 3;
    private static final int MIN_MONEY = 0;
    private int steamId; // user's steam id
    private transient long sessionId; // user's session id
    private transient Mercenaries[] mercenaries; // avoid circular reference, mercs refer to owner in messages but not vise versa
    private int squadCount;
    private int money;
    private Enums.TFTeam team;
    private transient MapData vision;

    public Player(int steamId) {
        this.steamId = steamId;
        team = Enums.TFTeam.UNASSIGNED;
        money = MIN_MONEY;
        mercenaries = new Mercenaries[MAX_MERCENARY_SQUADS];
        squadCount = 0;
        vision = new MapData();
    }

    public boolean canAfford(int cost) {
        if (cost > money) {
            return false;
        }
        return true;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void spendMoney(int amount) {
        money -= amount;
    }

    public Enums.TFTeam getTeam() {
        return team;
    }

    public boolean createSquad(Enums.TFClass mercenaryClass) {
        if (squadCount == MAX_MERCENARY_SQUADS || !canAfford(100)) { // arbitrary squad creating cost
            return false;
        }
        mercenaries[squadCount] = MapManager.createSquad(this, mercenaryClass);
        squadCount++;
        return true;
    }

    public boolean reinforceSquad(int squadIndex, int count) {
        MapManager.reinforceSquad(mercenaries[squadIndex], count);
        return false;
    }

    public void moveSquad(int squadIndex, int destinationId) {

        // will find the destination and assign it inside squad
    }

    public void setTeam(Enums.TFTeam team) {
        this.team = team;
    }
}
