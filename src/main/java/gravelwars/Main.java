package gravelwars;

import gravelwars.core.Enums;
import gravelwars.core.Player;
import gravelwars.core.data.GameData;
import gravelwars.core.managers.GameManager;
import gravelwars.core.managers.MapManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        MapManager.init();
        GameManager.init();
        MapManager.start();
        Player p = GameManager.createPlayer(123);
        p.setMoney(10000);
        p.setTeam(Enums.TFTeam.BLUE);
        p.createSquad(Enums.TFClass.DEMOMAN);
        p.reinforceSquad(0, 100);
        p.moveSquad(0, 20);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("stop")) {
            input = scanner.nextLine();
        }
        MapManager.stop();
        MapManager.save();
        GameManager.save();
    }

}
