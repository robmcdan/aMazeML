package aMazeML;
import aMazeML.GamePlay.Preferences.LeftPreference;
import aMazeML.GamePlay.Preferences.Preference;
import aMazeML.GamePlay.Preferences.RightPreference;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * implementation of standard recursive backtracking algorithm
 * https://rosettacode.org/wiki/Maze_solving#Java
 */
public class MazeSolver
{
    ArrayList<Direction> dirs = new ArrayList<>();

    public MazeSolver(Preference preference){
        dirs = preference.getStack();
        /*dirs.add(Direction.N);
        dirs.add(Direction.E);
        dirs.add(Direction.S);
        dirs.add(Direction.W);*/
    }

    /**
     * Given the maze, the x and y coordinates (which must be odd),
     * and the direction we came from, return true if the maze is
     * solvable, and draw the solution if so.
     */
    private boolean solveMazeRecursively (char[][] maze,
                                                 int x, int y, Direction d) throws InterruptedException {
        boolean ok = false;
        for(Direction dir : dirs) {
            if (dir != d &&  !ok) {
                switch (dir) {
                    // 0 = up, 1 = right, 2 = down, 3 = left
                    case N:
                        if (maze[y - 1][x] == ' ')
                            ok = solveMazeRecursively(maze, x, y - 2, Direction.S);
                        break;
                    case E:
                        if (maze[y][x + 1] == ' ')
                            ok = solveMazeRecursively(maze, x + 2, y, Direction.W);
                        break;
                    case S:
                        if (maze[y + 1][x] == ' ')
                            ok = solveMazeRecursively(maze, x, y + 2, Direction.N);
                        break;
                    case W:
                        if (maze[y][x - 1] == ' ')
                            ok = solveMazeRecursively(maze, x - 2, y, Direction.E);
                        break;
                }
            }
        }
        // check for end condition
        if (x == 1  &&  y == 1)
            ok = true;
        // once we have found a solution, draw it as we unwind the recursion
        if (ok)
        {
            maze[y][x] = '*';
            if(d != null)
                switch (d)
                {
                    case N:
                        maze[y-1][x] = '*';
                        break;
                    case E:
                        maze[y][x+1] = '*';
                        break;
                    case S:
                        maze[y+1][x] = '*';
                        break;
                    case W:
                        maze[y][x-1] = '*';
                        break;
                }
        }
        return ok;
    }

    /**
     * Solve the maze and draw the solution.  For simplicity,
     * assumes the starting point is the lower right, and the
     * ending point is the upper left.
     */
    public Pair<Float, String[]> SolveMaze(MazeGenerator maze) throws InterruptedException {
        char[][] m = decimateHorizontally (maze.toStringArray());
        long startTime = System.nanoTime();
        boolean solved = solveMazeRecursively (m, m[0].length - 2, m.length - 2, null);
        long endTime = System.nanoTime();

        return new Pair<>((endTime - startTime)/1000000f, expandHorizontally(m));
    }

    /**
     * Makes the maze half as wide (i. e. "+---+" becomes "+-+"), so that
     * each cell in the maze is the same size horizontally as vertically.
     * (Versus the expanded version, which looks better visually.)
     * Also, converts each line of the maze from a String to a
     * char[], because we'll want mutability when drawing the solution later.
     */
    private static char[][] decimateHorizontally (String[] lines)
    {
        final int width = (lines[0].length() + 1) / 2;
        char[][] c = new char[lines.length][width];
        for (int i = 0  ;  i < lines.length  ;  i++)
            for (int j = 0  ;  j < width  ;  j++)
                c[i][j] = lines[i].charAt (j * 2);
        return c;
    }

    /**
     * Reads a file into an array of strings, one per line.
     */
    private static String[] readLines (InputStream f) throws IOException
    {
        BufferedReader r =
                new BufferedReader (new InputStreamReader (f, "US-ASCII"));
        ArrayList<String> lines = new ArrayList<String>();
        String line;
        while ((line = r.readLine()) != null)
            lines.add (line);
        return lines.toArray(new String[0]);
    }

    /**
     * Opposite of decimateHorizontally().  Adds extra characters to make
     * the maze "look right", and converts each line from char[] to
     * String at the same time.
     */
    private static String[] expandHorizontally (char[][] maze)
    {
        char[] tmp = new char[3];
        String[] lines = new String[maze.length];
        for (int i = 0  ;  i < maze.length  ;  i++)
        {
            StringBuilder sb = new StringBuilder(maze[i].length * 2);
            for (int j = 0  ;  j < maze[i].length  ;  j++)
                if (j % 2 == 0)
                    sb.append (maze[i][j]);
                else
                {
                    tmp[0] = tmp[1] = tmp[2] = maze[i][j];
                    if (tmp[1] == '*')
                        tmp[0] = tmp[2] = ' ';
                    sb.append (tmp);
                }
            lines[i] = sb.toString();
        }
        return lines;
    }

    /**
     * Accepts a maze as generated by:
     * http://rosettacode.org/wiki/Maze_generation#Java
     * in a file whose name is specified as a command-line argument,
     * or on standard input if no argument is specified.
     */
    public static void main (String[] args) throws IOException, InterruptedException {
        //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("maze.txt")), true));


        MazeGenerator maze = new MazeGenerator(15, 15, new RightPreference());
        MazeSolver solver = new MazeSolver(new LeftPreference());
        maze.GenerateMaze();
        Pair<Float, String[]> ret = solver.SolveMaze(maze);

        for (String solvedLine : ret.getValue()) {
            System.out.println(solvedLine);
        }

        System.out.println("\n\n\nsolve time: " + ret.getKey());
    }
}
