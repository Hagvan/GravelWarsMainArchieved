package gravelwars.core.data;

import gravelwars.core.Player;

import java.nio.file.Path;
import java.util.HashMap;

public class GameData {
    private HashMap<Integer, Player> players;

    public GameData() {

    }

    public GameData(HashMap<Integer, Player> players) {
        this.players = players;
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<Integer, Player> players) {
        this.players = players;
    }
}
