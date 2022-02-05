package code;

import java.io.Serializable;
import java.util.Objects;

/**
 * Helper class which contains all the attributes needed for Neo (player/agent).
 */

public class Neo implements Serializable {

    /**
     * We need to track Neo's location on the grid, Neo's health, and how many hostages neo can currently carry
     *
     * When Neo's damage is at 100, he dies and the game is over. When currentCapacity is at 0, he cannot
     * carry any more hostages.
     */

    private Location _location;
    private int _damage;
    private int _currentCapacity;
    private int _originalCapacity;

    public Neo(Location location, int damage, int currentCapacity){
        _location = location;
        _damage = damage;
        _currentCapacity = currentCapacity;
        _originalCapacity = currentCapacity;
    }

    public Neo(Location location, int damage, int currentCapacity, int originalCapacity){
        _location = location;
        _damage = damage;
        _currentCapacity = currentCapacity;
        _originalCapacity = originalCapacity;
    }

    // ==========================Getters-and-Setters==========================

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
    	if(damage<=0)
    	{
    		_damage=0;
    	}
    	_damage=damage;
    }

    public int getCurrentCapacity() {
        return _currentCapacity;
    }
    
    public int getOriginalCapacity() {
        return _originalCapacity;
    }

    // ============================Additional-Methods===========================

    /**
     * increases current carry capacity by 1
     */

    public void incCurrentCapacity() {
        _currentCapacity ++;
    }

    /**
     * decreases current carry capacity by 1
     */

    public void decCurrentCapacity() {
        _currentCapacity --;
    }

    /**
     * @return true if Neo can carry more hostages, false otherwise.
     */
    public boolean canCarry() {
        return _currentCapacity > 0;
    }

    // ================================Hashing=================================


    @Override
    public boolean equals(Object obj) {
        Neo n = (Neo) obj;

        if (!this.getLocation().equals(n.getLocation())) return false;

        if (this.getDamage() != n.getDamage()) return false;

        if (this.getCurrentCapacity() != n.getCurrentCapacity()) return false;

        return this.getOriginalCapacity() == n.getOriginalCapacity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(_location, _damage, _currentCapacity);
    }
}
