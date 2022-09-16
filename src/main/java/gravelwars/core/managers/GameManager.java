package gravelwars.core.managers;

import com.google.gson.Gson;
import gravelwars.core.Player;
import gravelwars.core.data.GameData;
import gravelwars.core.data.MapData;
import gravelwars.core.map.Mercenaries;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;

public abstract class GameManager {

    private static final Path gravelwarsGamePath = Path.of("gravel-wars-game.json");
    private static GameData data;

    public static void init() {
        load();
        for (Player player : data.getPlayers().values()) {
            player.linkMercenaries();
        }
    }

    private static void load() {
        Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(gravelwarsGamePath)) {
            data = gson.fromJson(reader, GameData.class);
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                data = new GameData(new HashMap<>());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save() {
        Gson gson = new Gson();
        try (Writer writer = Files.newBufferedWriter(gravelwarsGamePath)) {
            String bussing = gson.toJson(data);
            System.out.println(bussing.length());
            writer.write(bussing);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Player createPlayer(int steamId) {
        Player player = new Player(steamId);
        data.getPlayers().put(steamId, player);
        return player;
    }

}
