package bg.sofia.uni.fmi.mjt.dungeon;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Enemy;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Position;
import bg.sofia.uni.fmi.mjt.dungeon.message.Messages;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Treasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameEngine {

    private final char[][] map;
    private final Hero hero;
    private final Position heroPosition;
    private final List<Enemy> enemies;
    private final List<Treasure> treasures;

    public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures) {
        this.map = copy(map);
        this.hero = copy(hero);
        this.heroPosition = generateStartPosition();
        this.enemies = new ArrayList<>(Arrays.asList(enemies));
        this.treasures = new ArrayList<>(Arrays.asList(treasures));
    }

    private Hero copy(Hero copyHero) {
        return new Hero(copyHero);
    }

    private char[][] copy(char[][] map) {
        int mapRows = map.length;
        int mapColumns = map[0].length;
        char[][] copiedMap = new char[mapRows][mapColumns];
        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapColumns; col++) {
                copiedMap[row][col] = map[row][col];
            }
        }
        return copiedMap;
    }

    private Position generateStartPosition() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'S') {
                    return new Position(row, col);
                }
            }
        }
        return new Position(-1, -1);
    }

    private int rowBound() {
        return map.length;
    }

    private int columnBound() {
        return map[0].length;
    }

    public char[][] getMap() {
        return this.map;
    }

    public Hero getHero() {
        return this.hero;
    }

    public Position getHeroPosition() {
        return this.heroPosition;
    }

    private Position getPositionToMove(Direction direction) {
        int heroRow = heroPosition.getRow();
        int heroColumn = heroPosition.getColumn();

        if (direction == Direction.UP) {
            return new Position(heroRow - 1, heroColumn);
        } else if (direction == Direction.DOWN) {
            return new Position(heroRow + 1, heroColumn);
        } else if (direction == Direction.LEFT) {
            return new Position(heroRow, heroColumn - 1);
        } else if (direction == Direction.RIGHT) {
            return new Position(heroRow, heroColumn + 1);
        }

        return null;
    }

    private boolean isMoveOutOfBounds(Position position) {
        int positionRow = position.getRow();
        int positionColumn = position.getColumn();
        return positionRow < 0 || positionRow >= rowBound()
                || positionColumn < 0 || positionColumn >= columnBound();
    }

    private char getPositionSymbol(Position position) {
        int positionRow = position.getRow();
        int positionColumn = position.getColumn();
        return map[positionRow][positionColumn];
    }

    private boolean isObstaclePosition(Position position) {
        return getPositionSymbol(position) == '#';
    }

    private boolean isExitPosition(Position position) {
        return getPositionSymbol(position) == 'G';
    }

    private boolean isTreasurePosition(Position position) {
        return getPositionSymbol(position) == 'T';
    }

    private boolean isEnemyOnPosition(Position position) {
        return getPositionSymbol(position) == 'E';
    }

    private boolean isFreePosition(Position position) {
        return getPositionSymbol(position) == '.';
    }

    private void createBattle() {
        Enemy enemy = enemies.get(0);
        enemies.remove(0);

        while (hero.isAlive()) {
            int heroDamage = hero.attack();
            enemy.takeDamage(heroDamage);
            if (!enemy.isAlive()) {
                break;
            }
            int enemyDamage = enemy.attack();
            hero.takeDamage(enemyDamage);
        }
    }

    private void makeStepTo(Position position) {
        int heroRow = heroPosition.getRow();
        int heroColumn = heroPosition.getColumn();

        int newPositionRow = position.getRow();
        int newPositionColumn = position.getColumn();

        map[heroRow][heroColumn] = '.';
        map[newPositionRow][newPositionColumn] = 'H';
        heroPosition.setPosition(newPositionRow, newPositionColumn);
    }

    private String move(Direction direction) {
        Position newPosition = getPositionToMove(direction);
        if (newPosition == null) {
            return Messages.UNKNOWN_COMMAND_MESSAGE;
        } else if (isMoveOutOfBounds(newPosition) || isObstaclePosition(newPosition)) {
            return Messages.WRONG_MOVE_MESSAGE;
        }

        if (isFreePosition(newPosition)) {
            makeStepTo(newPosition);
            return Messages.SUCCESSFUL_MOVE_MESSAGE;
        }

        if (isTreasurePosition(newPosition)) {
            makeStepTo(newPosition);

            Treasure treasure = treasures.get(0);
            treasures.remove(0);
            return treasure.collect(hero);
        }

        if (isEnemyOnPosition(newPosition)) {
            createBattle();

            if (hero.isAlive()) {
                makeStepTo(newPosition);
                return Messages.ENEMY_DIED_MESSAGE;
            }
            return Messages.GAME_OVER_MESSAGE;
        }

        if (isExitPosition(newPosition)) {
            return Messages.WIN_MESSAGE;
        }

        return null;
    }

    public String makeMove(Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Cannot move to null direction");
        }

        switch (direction) {
            case UP -> {
                return move(Direction.UP);
            }
            case DOWN -> {
                return move(Direction.DOWN);
            }
            case LEFT -> {
                return move(Direction.LEFT);
            }
            case RIGHT -> {
                return move(Direction.RIGHT);
            }
            default -> {
                return Messages.UNKNOWN_COMMAND_MESSAGE;
            }
        }
    }
}
