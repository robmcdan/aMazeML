package aMazeML;

import aMazeML.GamePlay.Preferences.LeftPreference;
import aMazeML.GamePlay.Preferences.Preference;
import aMazeML.GamePlay.Preferences.RightPreference;

import java.util.ArrayList;


/**
 * implementation of standard recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 * and
 * https://rosettacode.org/wiki/Maze_generation#Java
 */
public class MazeGenerator {
    private final int x;
    private final int y;
    public final int[][] maze;
    private Preference preference;
    ArrayList<Direction> dirs = new ArrayList<>();

    public MazeGenerator(int x, int y, Preference preference) {
        this.x = x;
        this.y = y;
        maze = new int[this.x][this.y];
        this.preference = preference;
    }

    public int[][] GenerateMaze(){


        generateMaze(0, 0);
        return maze;
    }

    /**
     * converting a maze into a list of strings (for display purposes)
     * @return
     */
    public String[] toStringArray() {
       StringBuilder displayStr = new StringBuilder();
        for (int i = 0; i < y; i++) {
            // draw the north edge
            for (int j = 0; j < x; j++) {
                displayStr.append((maze[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            displayStr.append("+\n");
            // draw the west edge
            for (int j = 0; j < x; j++) {
                displayStr.append((maze[j][i] & 8) == 0 ? "|   " : "    ");
            }
            displayStr.append("|\n");
        }
        // draw the bottom line
        for (int j = 0; j < x; j++) {
            displayStr.append("+---");
        }
        displayStr.append("+\n");
        String[] lines = displayStr.toString().split("\n");
        return lines;
    }

    /**
     * recursive entrypoint for maze generation
     * @param cx the current X position
     * @param cy the current Y position
     */
    private void generateMaze(int cx, int cy) {
        dirs = preference.getStack();

        for (Direction dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            // if next x and y we are not out of range, and cell is un0visited
            if (between(nx, x) && between(ny, y)
                    && (maze[nx][ny] == 0)) {
                maze[cx][cy] |= dir.bit; // store the direction we went from this cell
                maze[nx][ny] |= dir.opposite.bit; // store the direction we came from
                generateMaze(nx, ny);
            }
        }
    }

    /**
     * between zero and upper limit
     * @param v
     * @param upper
     * @return
     */
    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }


    public static void main(String[] args) {
        int x = args.length >= 1 ? (Integer.parseInt(args[0])) : 16;
        int y = args.length == 2 ? (Integer.parseInt(args[1])) : 16;
        MazeGenerator maze = new MazeGenerator(x, y, new RightPreference());

        maze.GenerateMaze();
        for (String line : maze.toStringArray()) {
            System.out.println(line);
        }
    }

}
