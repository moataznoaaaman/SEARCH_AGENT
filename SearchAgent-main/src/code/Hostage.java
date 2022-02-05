package code;

import java.io.Serializable;
import java.util.Objects;

/**
 * Helper class which contains all the attributes needed for a hostage.
 */

public class Hostage {
    /**
     * We need to track hostage location on the grid, hostage health, and
     * if they're currently being carried by neo.
     * Location and damage together determine if a hostage is at the telephone
     * booth dead or alive (to avoid updating damage and to count them as saved hostages).
     * carried and damage together determine if a hostage should turn or die. If damage is
     * 100 and location is not the booth, that's an agent and must be killed.
     */

    private int _id; // id for hashing and identifying hostage instances
    private Location _location;
    private int _damage;
    private boolean _carried;

    public Hostage(Location location, int damage, boolean carried) {
        _id = 0;
        _location = location;
        _damage = damage;
        _carried = carried;
    }

    // ==========================Getters-and-Setters==========================


    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Location getLocation() {
        return _location;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    public int getDamage() {
        return _damage;
    }

    public void setDamage(int damage) {
        if (damage <= 0) {
            _damage = 0;
        }
        _damage = damage;
    }

    public boolean isCarried() {
        return _carried;
    }

    public void setCarried(boolean carried) {
        _carried = carried;
    }

    public boolean isAlive() {
        return _damage < 100;
    }
    // ================================Hashing=================================


    /**
     * returns whether two hostages are equal or not. Two hostages are equal if
     * both have the same location, carried status, being alive or dead, and id.
     *
     * @param obj a hostage
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        Hostage h = (Hostage) obj;

        if (!this.getLocation().equals(h.getLocation())) return false;
        if (this.getDamage() >= 100 &&  h.getDamage() < 100) return false;
        if (this.getDamage() < 100 &&  h.getDamage() >= 100) return false;
        if (this.isCarried() != h.isCarried()) return false;
        return this.getId() == h.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
