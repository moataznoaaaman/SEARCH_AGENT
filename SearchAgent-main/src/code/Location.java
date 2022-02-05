package code;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class representing a discrete point on our grid. 0-indexed,
 * starting from the bottom left of the grid (0, 0). position cannot be negative.
 */

public class Location {

    /**
     * _x: horizontal position
     * _y: vertical position
     */

    private int _x;
    private int _y;

    public Location(int x, int y){
        _x = x;
        _y = y;
    }

    // ==========================Getters-and-Setters==========================
    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public void setX(int x) {
        _x = x;
    }

    public void setY(int y) {
        _y = y;
    }

    // ============================Additional-Methods===========================

    /**
     * return whether or not 2 locations are adjacent. Vertically or horizontally.
     * @param l location to compare with
     * @return true if vertical or horizontal adjacency is detected. Identical locations and
     * diagonal adjacency return false
     */

    public boolean adjacent(Location l){
        int adjx = Math.abs(_x - l.getX()); //if 1 then adjacent columns, 0 same column, otherwise not adjacent
        int adjy = Math.abs(_y - l.getY()); //if 1 then adjacent rows, 0 same rows, otherwise not adjacent

        // true if adjacent columns and same row (left, right), or adjacent rows and same column (top, bottom).
        return (adjx == 1 && adjy == 0) || (adjy == 1 && adjx == 0);
    }

    // ==========================Equality-and-Hashing==========================
    @Override
    public boolean equals(Object obj) {
        Location l = (Location) obj;
        return (_x == l.getX()) && (_y == l.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y);
    }
}
