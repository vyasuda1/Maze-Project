package DimitrovYasuda.cs146.project3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * Represents a maze.
 *
 * @author Viola Yasuda and Dimitar Dimitrov
 * @version 1.0 11/7/2021
 */
public class Maze {
    private final Cell[][] maze;
    private Cell currentCell;
    private final HashMap<Cell, LinkedList<Cell>> adjacencyList;
    private final Stack<Cell> path;
    private int visitedCells;
    private boolean lastCellFound;
    private Random rand;

    /**
     * Constructs a maze.
     * @param length the length of the maze
     * @param random a random generator
     */
    public Maze(int length, Random random) {
        maze = new Cell[length][length];
        rand = random;
        adjacencyList = new HashMap<>();
        path = new Stack<>();
        visitedCells = 0;
        lastCellFound = false;
        generateRandomMaze();
        modelMaze();
    }

    /**
     * Generates a random maze.
     */
    public void generateRandomMaze() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                maze[row][col] = new Cell(row, col);
                if (row == 0 && col == 0) {
                    maze[row][col].setHasNeighborConnection(0, true);
                }
                if (row == maze.length - 1 && col == maze[0].length - 1) {
                    maze[row][col].setHasNeighborConnection(2, true);
                }
            }
        }
        Stack<Cell> cellStack = new Stack<>();
        int totalCells = maze.length * maze[0].length;
        currentCell = maze[0][0];
        int visitedCells = 1;

        while (visitedCells < totalCells) {
            // Finds all neighbors of CurrentCell with all walls intact.
            Cell[] neighbors = new Cell[]{northNeighbor(), eastNeighbor(), southNeighbor(), westNeighbor()};
            ArrayList<Integer> validNeighborIndexes = new ArrayList<>();
            for (int i = 0; i < neighbors.length; i++) {
                if (neighbors[i] != null && neighbors[i].getColor().equals("white")) {
                    validNeighborIndexes.add(i);
                }
            }
            // If one or more valid neighbors is found, adds one of the neighbors to the maze at random.
            if (validNeighborIndexes.size() > 0) {
                int randomIndex = validNeighborIndexes.get(rand.nextInt(validNeighborIndexes.size()));
                Cell randomCell = neighbors[randomIndex];
                currentCell.setHasNeighborConnection(randomIndex, true);
                randomCell.setHasNeighborConnection((randomIndex + 2) % 4,true);
                if (validNeighborIndexes.size() == 1) {
                    currentCell.setColor("black");
                }
                else {
                    currentCell.setColor("grey");
                }
                cellStack.push(currentCell);
                currentCell = randomCell;
                visitedCells++;
            }
            else {
                currentCell.setColor("black");
                currentCell = cellStack.pop();
            }
        }
    }

    /**
     * Models the maze as a graph.
     */
    public void modelMaze() {
        for(int row = 0; row < maze.length; row++) {
            for(int col = 0; col < maze.length; col++) {
                currentCell = maze[row][col];
                LinkedList<Cell> validNeighbors = new LinkedList<>();
                if (maze[row][col].getHasNeighborConnections()[0] && northNeighbor() != null) {
                    validNeighbors.add(northNeighbor());
                }
                if (maze[row][col].getHasNeighborConnections()[1] && eastNeighbor() != null) {
                    validNeighbors.add(eastNeighbor());
                }
                if (maze[row][col].getHasNeighborConnections()[2] && southNeighbor() != null) {
                    validNeighbors.add(southNeighbor());
                }
                if (maze[row][col].getHasNeighborConnections()[3] && westNeighbor() != null) {
                    validNeighbors.add(westNeighbor());
                }
                adjacencyList.put(maze[row][col], validNeighbors);
            }
        }
    }

    /**
     * Saves maze output to a file.
     * @param file the file to save output to
     */
    public void saveToFile(String file) {
        try {
            File myFile = new File(file);
            FileWriter fWriter = new FileWriter(file);
            PrintWriter outputFile = new PrintWriter(fWriter);
            outputFile.print(getOutput());
            outputFile.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the output for the maze and its solutions.
     * @return a string with the maze and the bfs and dfs solutions
     */
    private String getOutput() {
        String solution = getASCIIMaze();
        breadthFirstSearch();
        solution += "BFS:\n" + getMazeTraversal() + "\n" + getMazePath() + "\n";
        depthFirstSearch();
        solution += "DFS:\n" + getMazeTraversal() + "\n" + getMazePath() + "\n";
        return solution;
    }

    /**
     * Searches for a path from the maze entrance to the maze exit using depth first search.
     */
    private void depthFirstSearch() {
        resetCells();
        Cell v = maze[0][0];
        v.setOrderDiscovered(visitedCells);
        visitedCells++;
        v.setColor("grey");
        for(Cell u : adjacencyList.get(v)) {
            if(lastCellFound) {
                System.out.println("working exit");
                break;
            }
            if(u.getColor().equals("white")) {
                u.setParent(v);
                DFSVisit(u);
            }
        }
        Cell pathCell = maze[maze.length - 1][maze[0].length - 1];
        while (!pathCell.equals(maze[0][0])) {
            pathCell.setOnPath(true);
            path.push(pathCell);
            pathCell = pathCell.getParent();
        }
        maze[0][0].setOnPath(true);
        path.push(maze[0][0]);
    }

    /**
     * Helper method for depthFirstSearch().
     * @param u The parent cell
     */
    private void DFSVisit(Cell u) {
        u.setColor("grey");
        u.setOrderDiscovered(visitedCells);
        Cell exitCell = maze[maze.length - 1][maze.length - 1];
        for(Cell v : adjacencyList.get(u)) {
            if(v.getColor().equals("white")) {
                v.setParent(u);
                visitedCells++;
                if(u.equals(exitCell) || v.equals(exitCell)) {
                    lastCellFound = true;
                    v.setColor("grey");
                    v.setOrderDiscovered(visitedCells);
                    visitedCells++;
                    break;
                }
                DFSVisit(v);
            }
            if (lastCellFound) {
                break;
            }
        }
        u.setColor("black");
    }

    /**
     * Searches for a path from the maze entrance to the maze exit using breadth first search.
     */
    private void breadthFirstSearch() {
        boolean lastCellFound = false;
        resetCells();
        maze[0][0].setColor("grey");
        maze[0][0].setOrderDiscovered(visitedCells);
        Queue<Cell> cellQueue = new LinkedList<>();
        cellQueue.add(maze[0][0]);
        visitedCells++;
        while (!cellQueue.isEmpty() ) {
            Cell u = cellQueue.remove();
            for (Cell v : adjacencyList.get(u)) {
                if (v.getColor().equals("white")) {
                    v.setColor("grey");
                    v.setOrderDiscovered(visitedCells);
                    v.setParent(u);
                    cellQueue.add(v);
                    visitedCells++;
                    if (v.equals(maze[maze.length - 1][maze.length - 1])) {
                        lastCellFound = true;
                        break;
                    }
                }
            }
            u.setColor("black");
            if (lastCellFound) {
                break;
            }
        }
        Cell pathCell = maze[maze.length - 1][maze[0].length - 1];
        while (!(pathCell.getCoordinates()[0] == 0 && pathCell.getCoordinates()[1] == 0)) {
            pathCell.setOnPath(true);
            path.push(pathCell);
            pathCell = pathCell.getParent();
        }
        maze[0][0].setOnPath(true);
        path.push(maze[0][0]);
    }

    /**
     * Gets the layout of the maze.
     * @return a string with the maze in ASCII
     */
    private String getASCIIMaze() {
        String mazeAsStr = "";
        mazeAsStr += "Graph Size: " + maze.length + "\nMAZE:\n";
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[0]) {
                    mazeAsStr += "+-";
                }
                else {
                    mazeAsStr += "+ ";
                }
            }
            mazeAsStr += "+\n";
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[3]) {
                    mazeAsStr += "| ";
                }
                else {
                    mazeAsStr += "  ";
                }
            }
            mazeAsStr += "|\n";
            if (row == maze.length - 1) {
                for (int col = 0; col < maze[0].length; col++) {
                    if (!maze[row][col].getHasNeighborConnections()[2]) {
                        mazeAsStr += "+-";
                    } else {
                        mazeAsStr += "+ ";
                    }
                }
                mazeAsStr += "+\n\n";
            }
        }
        return mazeAsStr;
    }

    /**
     * Gets the maze with the values of the order the cells were visited in.
     * @return a string with the maze traversal
     */
    private String getMazeTraversal() {
        String mazeTrav = "";
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[0]) {
                    mazeTrav += "+-";
                }
                else {
                    mazeTrav += "+ ";
                }
            }
            mazeTrav += "+\n";
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[3]) {
                    mazeTrav += "|";
                }
                else {
                    mazeTrav += " ";
                }
                if (maze[row][col].getOrderDiscovered() != -1) {
                    mazeTrav += maze[row][col].getOrderDiscovered() % 10;
                }
                else {
                    mazeTrav += " ";
                }
            }
            mazeTrav += "|\n";
            if (row == maze.length - 1) {
                for (int col = 0; col < maze[0].length; col++) {
                    if (!maze[row][col].getHasNeighborConnections()[2]) {
                        mazeTrav += "+-";
                    } else {
                        mazeTrav += "+ ";
                    }
                }
                mazeTrav += "+\n";
            }
        }
        return mazeTrav;
    }

    /**
     * Gets the maze with the path in #'s, the cell path, the length of the path, and the number of cells visited.
     * @return a string with the maze path
     */
    private String getMazePath() {
        String mazePath = "";
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[0]) {
                    mazePath += "+-";
                }
                else {
                    mazePath += "+ ";
                }
            }
            mazePath += "+\n";
            for (int col = 0; col < maze[0].length; col++) {
                if (!maze[row][col].getHasNeighborConnections()[3]) {
                    mazePath += "|";
                }
                else {
                    mazePath += " ";
                }
                if (maze[row][col].isOnPath()) {
                    mazePath += "#";
                }
                else {
                    mazePath += " ";
                }
            }
            mazePath += "|\n";
            if (row == maze.length - 1) {
                for (int col = 0; col < maze[0].length; col++) {
                    if (!maze[row][col].getHasNeighborConnections()[2]) {
                        mazePath += "+-";
                    } else {
                        mazePath += "+ ";
                    }
                }
                mazePath += "+\n";
            }
        }
        int pathLength = path.size();
        mazePath += "Path: ";
        while (!path.isEmpty()) {
            mazePath += path.pop() + " ";
        }
        mazePath += "\nLength of path: " + pathLength + "\n";
        mazePath += "Visited cells: " + visitedCells + "\n";
        return mazePath;
    }

    /**
     * Returns the cell to the north of the current cell.
     * @return the north neighbor cell
     */
    private Cell northNeighbor() {
        int northRow = currentCell.getCoordinates()[0] - 1;
        int northCol = currentCell.getCoordinates()[1];
        if (northRow > -1) {
            return maze[northRow][northCol];
        }
        return null;
    }

    /**
     * Returns the cell to the east of the current cell.
     * @return the east neighbor cell
     */
    private Cell eastNeighbor() {
        int eastRow = currentCell.getCoordinates()[0];
        int eastCol = currentCell.getCoordinates()[1] + 1;
        if (eastCol < maze[0].length) {
            return maze[eastRow][eastCol];
        }
        return null;
    }

    /**
     * Returns the cell to the south of the current cell.
     * @return the south neighbor cell
     */
    private Cell southNeighbor() {
        int southRow = currentCell.getCoordinates()[0] + 1;
        int southCol = currentCell.getCoordinates()[1];
        if (southRow < maze.length) {
            return maze[southRow][southCol];
        }
        return null;
    }

    /**
     * Returns the cell to the west of the current cell.
     * @return the west neighbor cell
     */
    private Cell westNeighbor() {
        int westRow = currentCell.getCoordinates()[0];
        int westCol = currentCell.getCoordinates()[1] - 1;
        if (westCol > -1) {
            return maze[westRow][westCol];
        }
        return null;
    }

    /**
     * Resets the instance variables of each cell to their default values.
     */
    private void resetCells() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                maze[row][col].setColor("white");
                maze[row][col].setParent(null);
                maze[row][col].setOrderDiscovered(-1);
                maze[row][col].setOnPath(false);
            }
        }
        visitedCells = 0;
        lastCellFound = false;
    }
}