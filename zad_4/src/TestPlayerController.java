import java.util.HashSet;

public class TestPlayerController implements PlayerController {
    private final HashSet<Position> obstacles;
    private boolean foundExit;
    private final Tile[][] maze;

    private int remainingWaterMoves;
    private final int waterMoves;
    private boolean underWater;

    private Tile lastTile;
    private Position position;

    public TestPlayerController(Tile[][] maze, Position start, int waterMoves) {
        this.obstacles = new HashSet<>();
        this.foundExit = false;
        this.maze = maze;

        this.remainingWaterMoves = waterMoves;
        this.waterMoves = waterMoves;
        this.underWater = false;

        this.lastTile = Tile.EMPTY;
        this.position = start;

        if (maze[start.col() - 1][start.row() - 1] == Tile.WALL) {
            throw new RuntimeException("Invalid start");
        }
    }

    @Override
    public void move(Direction direction) throws OnFire, Flooded, Wall, Exit {
        var last = position;
        position = direction.step(position);

        int x = position.col(), y = position.row();
        var tile = x <= 0 || x > maze.length || y <= 0 || y > maze[0].length ? Tile.WALL : maze[x - 1][y - 1];

        System.out.println(position + ", " + direction + ", " + tile);

        if (obstacles.contains(position)) {
            throw new RuntimeException("Player hit same obstacle");
        }

        if (lastTile == Tile.FIRE && !opposite(direction).step(position).equals(last)) {
            throw new RuntimeException("Player burned down");
        }

        if (lastTile == Tile.EXIT) {
            foundExit = false;
            throw new RuntimeException("Player moved after finding exit");
        }

        if (tile == Tile.WATER) {
            underWater = true;
        } else if (tile != Tile.WALL) {
            underWater = false;
        }

        if (underWater) {
            if (remainingWaterMoves-- <= 0) {
                throw new RuntimeException("Player drown");
            }
        } else {
            remainingWaterMoves = waterMoves;
        }

        switch (lastTile = tile) {
            case EXIT -> {
                foundExit = true;
                throw new Exit();
            }

            case FIRE -> {
                obstacles.add(position);
                throw new OnFire();
            }

            case WATER -> {
                throw new Flooded();
            }

            case WALL -> {
                obstacles.add(position);
                position = last;
                throw new Wall();
            }
        }
    }

    public boolean foundExit() {
        return foundExit;
    }

    private Direction opposite(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
            case EAST -> Direction.WEST;
        };
    }
}

enum Tile {
    EMPTY, FIRE, WATER, WALL, EXIT;

    public Tile next() {
        return switch (this) {
            case WALL -> EMPTY;
            case EMPTY -> WATER;
            case WATER -> FIRE;
            case FIRE -> EXIT;
            case EXIT -> WALL;
        };
    }
}