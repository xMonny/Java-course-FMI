package bg.sofia.uni.fmi.mjt.dungeon;

public enum Direction {
    LEFT("LEFT"),
    UP("UP"),
    DOWN("DOWN"),
    RIGHT("RIGHT");

    final String directionString;

    Direction(String directionString) {
        this.directionString = directionString;
    }
}
