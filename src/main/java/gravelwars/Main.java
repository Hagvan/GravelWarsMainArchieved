package gravelwars;

import gravelwars.core.Enums;
import gravelwars.core.MapManager;
import gravelwars.core.Mercenaries;
import gravelwars.core.Player;

import javax.swing.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        MapManager.init();
        Scanner scanner = new Scanner(System.in);
        Player p = new Player(123);
        p.setMoney(11111111);
        p.setTeam(Enums.TFTeam.RED);
        p.createSquad(Enums.TFClass.DEMOMAN);
        p.moveSquad(0, 24);
        String input = scanner.nextLine();
        while (!input.equals("stop")) {
            input = scanner.nextLine();
        }
        MapManager.stop();
    }

}
