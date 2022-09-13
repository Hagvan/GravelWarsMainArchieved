package gravelwars;

import gravelwars.core.MapManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        MapManager.init();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("stop")) {
            input = scanner.nextLine();
        }
        MapManager.stop();
    }

}
