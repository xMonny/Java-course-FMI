package bg.sofia.uni.fmi.mjt.dungeon;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Enemy;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import bg.sofia.uni.fmi.mjt.dungeon.actor.Position;
import bg.sofia.uni.fmi.mjt.dungeon.message.Messages;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Treasure;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameEngineTest {

    private static final char[][] simpleMapWithObstacles = new char[][] {
            {'.', '#', '.'},
            {'#', 'S', '#'},
            {'.', '#', '.'}
    };

    private static final Enemy[] emptyEnemies = new Enemy[] {};
    private static final Treasure[] emptyTreasures = new Treasure[] {};

    private static final Hero simpleHero = new Hero("Simple", 100, 100);

    private GameEngine gameWithObstacles;

    private static final char[][] simpleMapWithAllFree = new char[][] {
            {'.', '.', '.'},
            {'.', 'S', '.'},
            {'.', '.', '.'}
    };

    private GameEngine gameWithAllFree;

    @Before
    public void prepareForTests() {
        gameWithObstacles = new GameEngine(simpleMapWithObstacles, simpleHero, emptyEnemies, emptyTreasures);
        gameWithAllFree = new GameEngine(simpleMapWithAllFree, simpleHero, emptyEnemies, emptyTreasures);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeMoveWhenDirectionIsNull() {
        gameWithObstacles.makeMove(null);
    }

    @Test
    public void testMakeMoveUpWhenUpIsObstacle() {
        String expectedMoveMessage = Messages.WRONG_MOVE_MESSAGE;
        String actualMoveMessage = gameWithObstacles.makeMove(Direction.UP);

        Position expectedHeroPosition = new Position(1, 1);
        Position actualHeroPosition = gameWithObstacles.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveDownWhenDownIsObstacle() {
        String expectedMoveMessage = Messages.WRONG_MOVE_MESSAGE;
        String actualMoveMessage = gameWithObstacles.makeMove(Direction.DOWN);

        Position expectedHeroPosition = new Position(1, 1);
        Position actualHeroPosition = gameWithObstacles.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveLeftWhenLeftIsObstacle() {
        String expectedMoveMessage = Messages.WRONG_MOVE_MESSAGE;
        String actualMoveMessage = gameWithObstacles.makeMove(Direction.LEFT);

        Position expectedHeroPosition = new Position(1, 1);
        Position actualHeroPosition = gameWithObstacles.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveRightWhenRightIsObstacle() {
        String expectedMoveMessage = Messages.WRONG_MOVE_MESSAGE;
        String actualMoveMessage = gameWithObstacles.makeMove(Direction.RIGHT);

        Position expectedHeroPosition = new Position(1, 1);
        Position actualHeroPosition = gameWithObstacles.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveUpWhenUpIsFree() {
        String expectedMoveMessage = Messages.SUCCESSFUL_MOVE_MESSAGE;
        String actualMoveMessage = gameWithAllFree.makeMove(Direction.UP);

        Position expectedHeroPosition = new Position(0, 1);
        Position actualHeroPosition = gameWithAllFree.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveDownWhenDownIsFree() {
        String expectedMoveMessage = Messages.SUCCESSFUL_MOVE_MESSAGE;
        String actualMoveMessage = gameWithAllFree.makeMove(Direction.DOWN);

        Position expectedHeroPosition = new Position(2, 1);
        Position actualHeroPosition = gameWithAllFree.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveLeftWhenLeftIsFree() {
        String expectedMoveMessage = Messages.SUCCESSFUL_MOVE_MESSAGE;
        String actualMoveMessage = gameWithAllFree.makeMove(Direction.LEFT);

        Position expectedHeroPosition = new Position(1, 0);
        Position actualHeroPosition = gameWithAllFree.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveRightWhenRightIsFree() {
        String expectedMoveMessage = Messages.SUCCESSFUL_MOVE_MESSAGE;
        String actualMoveMessage = gameWithAllFree.makeMove(Direction.RIGHT);

        Position expectedHeroPosition = new Position(1, 2);
        Position actualHeroPosition = gameWithAllFree.getHeroPosition();

        assertEquals(expectedMoveMessage, actualMoveMessage);
        assertEquals(expectedHeroPosition, actualHeroPosition);
    }

    @Test
    public void testMakeMoveWhenMoveIsOutOfBounds() {
        String expectedFirstMoveMessage = Messages.SUCCESSFUL_MOVE_MESSAGE;
        String actualFirstMoveMessage = gameWithAllFree.makeMove(Direction.RIGHT);

        String expectedSecondMoveMessage = Messages.WRONG_MOVE_MESSAGE;
        String actualSecondMoveMessage = gameWithAllFree.makeMove(Direction.RIGHT);

        Position expectedHeroPositionAfterMoving = new Position(1, 2);
        Position actualHeroPositionAfterMoving = gameWithAllFree.getHeroPosition();

        assertEquals("First moving to right should be successful"
                , expectedFirstMoveMessage, actualFirstMoveMessage);
        assertEquals("Second moving to right should be unsuccessful"
                , expectedSecondMoveMessage, actualSecondMoveMessage);
        assertEquals("Final hero position should be (1, 2)"
                , expectedHeroPositionAfterMoving, actualHeroPositionAfterMoving);
    }

    @Test
    public void testMakeMoveOnTreasureHealthPotion() {
        char[][] treasureMap = new char[][] {{'S', 'T'}};
        Treasure healthPotion = new HealthPotion(10);
        Treasure[] treasures = new Treasure[] {healthPotion};
        GameEngine game = new GameEngine(treasureMap, simpleHero, emptyEnemies, treasures);

        String expectedMoveMessage = "Health potion found! 10 health points added to your hero!";
        String actualMoveMessage = game.makeMove(Direction.RIGHT);

        Position expectedFinalHeroPosition = new Position(0, 1);
        Position actualFinalHeroPosition = game.getHeroPosition();

        assertEquals("Health potion message should be returned",
                expectedMoveMessage, actualMoveMessage);
        assertEquals("Final hero position should be (0, 1)",
                expectedFinalHeroPosition, actualFinalHeroPosition);
    }

    @Test
    public void testMakeMoveOnStrongerEnemy() {
        char[][] treasureMap = new char[][] {{'E', 'S'}};
        Weapon ak47 = new Weapon("AK-47", 50);
        Enemy enemy = new Enemy("Enemy", 100, 100, ak47, null);
        Enemy[] enemies = new Enemy[] {enemy};
        GameEngine game = new GameEngine(treasureMap, simpleHero, enemies, emptyTreasures);

        String expectedMoveMessage = Messages.GAME_OVER_MESSAGE;
        String actualMoveMessage = game.makeMove(Direction.LEFT);

        Position expectedFinalHeroPosition = new Position(0, 1);
        Position actualFinalHeroPosition = game.getHeroPosition();

        assertEquals("Message for game over should be returned",
                expectedMoveMessage, actualMoveMessage);
        assertEquals("Final hero position should be (0, 1)",
                expectedFinalHeroPosition, actualFinalHeroPosition);
    }

    @Test
    public void testMakeMoveOnWeakerEnemy() {
        char[][] treasureMap = new char[][] {{'S'}, {'E'}};
        Enemy enemy = new Enemy("Enemy", 100, 100, null, null);
        Enemy[] enemies = new Enemy[] {enemy};

        Hero hero = new Hero("Hero", 100, 100);
        Spell fire = new Spell("FIRE", 50, 1);
        hero.learn(fire);
        GameEngine game = new GameEngine(treasureMap, hero, enemies, emptyTreasures);

        String expectedMoveMessage = Messages.ENEMY_DIED_MESSAGE;
        String actualMoveMessage = game.makeMove(Direction.DOWN);

        Position expectedFinalHeroPosition = new Position(1, 0);
        Position actualFinalHeroPosition = game.getHeroPosition();

        assertEquals("Message for died enemy should be returned",
                expectedMoveMessage, actualMoveMessage);
        assertEquals("Final hero position should be (1, 0)",
                expectedFinalHeroPosition, actualFinalHeroPosition);
    }

    @Test
    public void testMakeMoveOnFinal() {
        char[][] treasureMap = new char[][] {{'G'}, {'S'}};
        GameEngine game = new GameEngine(treasureMap, simpleHero, emptyEnemies, emptyTreasures);

        String expectedMoveMessage = Messages.WIN_MESSAGE;
        String actualMoveMessage = game.makeMove(Direction.UP);

        Position expectedFinalHeroPosition = new Position(1, 0);
        Position actualFinalHeroPosition = game.getHeroPosition();

        assertEquals("Message for win game should be returned",
                expectedMoveMessage, actualMoveMessage);
        assertEquals("Final hero position should be (1, 0)",
                expectedFinalHeroPosition, actualFinalHeroPosition);
    }
}
