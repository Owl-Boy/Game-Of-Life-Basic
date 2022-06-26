package backend;
import java.awt.Point;
import java.util.*;

public final class Cell implements Cloneable {
    private boolean new_state;  // State of the cell in the next generation
    private boolean state;
    private Point position;
    
    // Standard constructor for a cell object, sets the cell to dead, only defines it's position
    public Cell(Point pos) {
        this.position = pos;
    }

    // Standard constructor for a cell object
    public Cell(boolean state, Point pos) {
        this.state = state;
        this.position = pos;
    }

    // This function returns all 8 adjacent cells to a particular cell
    // adjacent cells might wrap around the board
    public ArrayList<Cell> getAdjacentCells(Cell[][] parent) {
        ArrayList<Cell> cells = new ArrayList<>(0);
        int x = this.position.x;
        int y = this.position.y;
        int xm = parent.length;       // Length of the x axis of the grid
        int ym = parent[xm-1].length; // Length of the y axis of the grid
        Point top = new Point(x-1,y);
        Point right = new Point(x,y+1);
        Point bottom = new Point(x+1,y);
        Point left = new Point(x,y-1);
        Point top_right = new Point(x-1,y+1);
        Point top_left = new Point(x-1,y-1);
        Point bottom_right = new Point(x+1,y+1);
        Point bottom_left = new Point(x+1,y-1);
        Point[] array =  {top,right,bottom,left,top_right,top_left,bottom_right,bottom_left};

        for(Point p:array) {
            p.x %= xm;
            p.y %= ym;
            Cell adj = parent[p.x][p.y];
            cells.add(adj);
        }

        return cells;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public Point getPosition() {
        return this.position;
    }

    // This method gets a String representation for all the information currently
    // stored in the cell.
    public String getCellData()  {
        String state = (this.state)?"ALIVE":"DEAD";
        String pos = this.position.x+", "+this.position.y;
        return "The cell at "+pos+" is currently "+state;
    }

    // this function checks if two cells have the same state and position info,
    // generally used to compare the same cells from consecutive generations
    public boolean equals(Cell c) {
        return this.getCellData().equals(c.getCellData());
    }

    // This function returns the number of cells alive in a particular list.
    public static int countAlive(ArrayList<Cell> cells) {
        int alive = 0;
        for(Cell c:cells) {
            if(c.getState()) {
                ++alive;
            }
        }
        return alive;
    }

    // This function returns the number of cells dead in a particular list.
    public static int countDead(ArrayList<Cell> cells) {
        int alive = Cell.countAlive(cells);
        return cells.size()-alive;
    }

    // Parameter definitions:
    // parent = parent grid for invoking cell
    // LB = minimum number of cells required to exist so that a cell may continue existing without dying
    // UB = maximum number of cells after which a cell will die of overcrowding
    // ress = exact number of cells required to revive a cell
    public boolean stateUpdate(Cell[][] parent,final int LB,final int UB,final int ress ) {
        ArrayList<Cell> cells =  this.getAdjacentCells(parent);
        int alive = Cell.countAlive(cells); // number of living adjacent cells

        // Dies of loneliness
        // #Relatable
        if(alive<LB) {
            this.new_state = false;
        }

        // Resurrects due to optimal conditions
        else if(alive == ress) {
            this.new_state = true;
        }

        // Dies of overcrowding
        // #INDIA
        else if(alive>UB) {
            this.new_state = false;
        }

        // Maintains same state as before
        else {
            this.new_state = this.state;
        }

        return this.new_state;
    }

    // This method "refreshes" the cell by updating the "state" variable's value
    // with the value of the "new_state" variable
    public void refresh() {
        this.state = this.new_state;
    }

    // This method copies the cell information without copying the cell's reference
    public Cell clone() {
        Point p = new Point(this.position.x,this.position.y);
        Cell c = new Cell(this.state,p);
        return c;
    }
}
