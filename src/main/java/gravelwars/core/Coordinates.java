package gravelwars.core;

public class Coordinates {

    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getDistance(Coordinates other) {
        return (int) Math.round(Math.sqrt(Math.pow(x, 2) - Math.pow(y, 2)));
    }
}
