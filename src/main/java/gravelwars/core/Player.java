package gravelwars.core;

import gravelwars.core.managers.MapManager;
import gravelwars.core.map.Mercenaries;

public class Player {
    private static final int MAX_MERCENARY_SQUADS = 3;
    private static final int MIN_MONEY = 0;
    private int steamId; // user's steam id
    private transient long sessionId; // user's session id
    private int[] mercenariesIds;
    private transient Mercenaries[] mercenaries; // avoid circular reference, mercs refer to owner in messages but not vise versa
    private int squadCount;
    private int money;
    private Enums.TFTeam team;

    public Player() {

    }

    public Player(int steamId) {
        this.steamId = steamId;
        team = Enums.TFTeam.UNASSIGNED;
        money = MIN_MONEY;
        mercenaries = new Mercenaries[MAX_MERCENARY_SQUADS];
        mercenariesIds = new int[MAX_MERCENARY_SQUADS];
        for (int i = 0; i < MAX_MERCENARY_SQUADS; i++) {
            mercenariesIds[i] = -1;
        }
        squadCount = 0;
        //vision = new MapData();
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
        for (int i = 0; i < MAX_MERCENARY_SQUADS; i++) {
            if (mercenaries[i] == null) {
                mercenaries[i] = MapManager.createSquad(this, mercenaryClass);
                mercenariesIds[i] = mercenaries[squadCount].getId();
                squadCount++;
                return true;
            }
        }
        throw new RuntimeException();
    }

    public void linkMercenaries() {
        for (int i = 0; i < MAX_MERCENARY_SQUADS; i++) {
            if (mercenariesIds[i] >= 0) {
                mercenaries[i] = MapManager.mapMercenaries.get(mercenariesIds[i]);
            }
        }
    }

    public void disbandSquad(int squadIndex) {
        if (squadIndex < 0 || squadIndex > MAX_MERCENARY_SQUADS - 1) {
            throw new RuntimeException();
        }
        mercenaries[squadIndex] = null;
        mercenariesIds[squadIndex] = -1;
    }

    public boolean reinforceSquad(int squadIndex, int count) {
        MapManager.reinforceSquad(mercenaries[squadIndex], count);
        return false;
    }

    public void moveSquad(int squadIndex, int destinationId) {
        mercenaries[squadIndex].setDestinationNodeId(destinationId);
    }

    public void setTeam(Enums.TFTeam team) {
        this.team = team;
    }

    public int getSteamId() {
        return steamId;
    }

    public long getSessionId() {
        return sessionId;
    }
}
