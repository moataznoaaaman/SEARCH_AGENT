package code;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * A class representing a state of the world specific to the matrix problem. Contains enough information to
 * know the effects of our actions, and determine goal configurations.
 */

public class MatrixState {
    /**
     * The dimensions of our grid. Needed for a complete transition model. REMOVE
     * IF PROVEN UNNECESSARY.
     */
    private Location _gridDims;

    /**
     * A state should track the locations of all objects in the world, as
     * long as they're relevant to the agent. In order, we track:
     * neoLoc: The location of Neo (player/agent).
     * teleBoothLoc: Location of the telephone booth.
     * hostageLocs: An array containing the locations of all hostages, turned or not.
     * killed turned hostages are deleted from the array.
     * agentLocs: Locations of all ALIVE agents. Dead agents are deleted from the array.
     * padLocs: Locations of all pads;
     * pillLocs: Locations of all UNTAKEN pills. Taken pills are deleted from the array
     * <p>
     * Additionally, we track the health of all hostages and neo, if a hostage is being carried, and the number of
     * hostages neo can currently carry.
     * <p>
     * We delete killed turned hostages from the hostages array, since we don't need them to know
     * the effects of our actions further down the tree.
     */

    private Neo _neo;
    private ArrayList<Hostage> _hostages;
    private ArrayList<Location> _agentLocs;
    private HashMap<Location, Location> _padLocs;
    private ArrayList<Location> _pillLocs;
    private Location _teleBoothLoc;

    /**
     * onstructor. Be careful when passing objects to make sure they are passed
     * by value and not reference
     */

    public MatrixState(Location gridDims, Neo neo, ArrayList<Hostage> hostages, ArrayList<Location> agentLocs,
                       HashMap<Location, Location> padLocs, ArrayList<Location> pillLocs, Location teleBoothLoc) {
        _gridDims = gridDims;
        _neo = neo;
        _hostages = hostages;
        _agentLocs = agentLocs;
        _padLocs = padLocs;
        _pillLocs = pillLocs;
        _teleBoothLoc = teleBoothLoc;
    }

    /**
     * Default constructor. everything is either 0 or empty.
     */

    public MatrixState() {
        _gridDims = new Location(0, 0);
        _neo = new Neo(new Location(0, 0), 0, 0);
        _hostages = new ArrayList<>();
        _agentLocs = new ArrayList<>();
        _padLocs = new HashMap<>();
        _pillLocs = new ArrayList<>();
        _teleBoothLoc = new Location(0, 0);
    }

    // ==========================Getters-and-Setters==========================

    public Location getGridDims() {
        return _gridDims;
    }

    public void setGridDims(Location gridDims) {
        _gridDims = gridDims;
    }

    public Neo getNeo() {
        return _neo;
    }

    public void setNeo(Neo neo) {
        _neo = neo;
    }

    public ArrayList<Hostage> getHostages() {
        return _hostages;
    }

    public void setHostages(ArrayList<Hostage> hostages) {
        _hostages = hostages;
    }

    public ArrayList<Location> getAgentLocs() {
        return _agentLocs;
    }

    public void setAgentLocs(ArrayList<Location> agentLocs) {
        _agentLocs = agentLocs;
    }

    public HashMap<Location, Location> getPadLocs() {
        return _padLocs;
    }

    public void setPadLocs(HashMap<Location, Location> padLocs) {
        _padLocs = padLocs;
    }

    public ArrayList<Location> getPillLocs() {
        return _pillLocs;
    }

    public void setPillLocs(ArrayList<Location> pillLocs) {
        _pillLocs = pillLocs;
    }

    public Location getTeleBoothLoc() {
        return _teleBoothLoc;
    }

    public void setTeleBoothLoc(Location teleBoothLoc) {
        _teleBoothLoc = teleBoothLoc;
    }

    // ============================Additional-Methods===========================

    /**
     * This creates a deep clone of the current state. Use it when making new nodes.
     * 1- copy the parent state
     * 2- modify the copied state
     * 3- assign the state to the child node's state
     *
     * @return returns a deep clone of the state
     * @throws IOException
     * @throws ClassNotFoundException
     */

//    public MatrixState copy() throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(outputStream);
//        out.writeObject(this);
//
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(inputStream);
//        MatrixState copied = (MatrixState) in.readObject();
//        return copied;
//    }

    public MatrixState copy() {
        Location grid = new Location(this.getGridDims().getX(), this.getGridDims().getY());
        Location neoLoc = new Location(this.getNeo().getLocation().getX(), this.getNeo().getLocation().getY());
        Neo neo = new Neo(neoLoc, this.getNeo().getDamage(),
                this.getNeo().getCurrentCapacity(), this.getNeo().getOriginalCapacity());

        ArrayList<Hostage> hostages = new ArrayList<>();
        for (Hostage h : this.getHostages()) {
            Location newHLoc = new Location(h.getLocation().getX(), h.getLocation().getY());
            Hostage newH = new Hostage(newHLoc, h.getDamage(), h.isCarried());
            hostages.add(newH);
        }

        ArrayList<Location> agents = new ArrayList<>();
        for (Location a : this.getAgentLocs()) {
            Location newALoc = new Location(a.getX(), a.getY());
            agents.add(newALoc);
        }

        ArrayList<Location> pills = new ArrayList<>();
        for (Location p : this.getPillLocs()) {
            Location newPLoc = new Location(p.getX(), p.getY());
            pills.add(newPLoc);
        }

        return new MatrixState(grid, neo, hostages, agents, this.getPadLocs(),
                pills, this.getTeleBoothLoc());
    }

    //====================

    public void removeHostage(Hostage h) {
        _hostages.remove(h); // removes by reference
    }

    public void removeHostage(int indx) {
        _hostages.remove(indx);
    }

    public void addHostage(Hostage h) {
        _hostages.add(h);
    }

    //====================

    public void removeAgent(Location a) {
        _agentLocs.remove(a); // removes by reference
    }

    public void removeAgent(int indx) {
        _agentLocs.remove(indx);
    }

    public void addAgent(Location a) {
        _agentLocs.add(a);
    }

    //====================


    public void removePill(Location p) {
        _pillLocs.remove(p); // removes by reference
    }

    public void removePill(int indx) {
        _pillLocs.remove(indx);
    }

    public void addPill(Location p) {
        _pillLocs.add(p);
    }

    //====================

    public void removePad(Location p) {
        _padLocs.remove(p); // removes by hash
    }

    public void addPad(Location src, Location dest) {
        _padLocs.put(src, dest);
    }

    // ================================Equality-and-Hashing=================================


    @Override
    public boolean equals(Object obj) {
        MatrixState m = (MatrixState) obj;

        if (!this.getGridDims().equals(m.getGridDims())) return false;

        if (!this.getNeo().equals(m.getNeo())) return false;

        if (!this.getHostages().equals(m.getHostages())) return false;

        if (!this.getAgentLocs().equals(m.getAgentLocs())) return false;

        if (!this.getPadLocs().equals(m.getPadLocs())) return false;

        if (!this.getPillLocs().equals(m.getPillLocs())) return false;

        return this.getTeleBoothLoc().equals(m.getTeleBoothLoc());
    }

    /**
     * Array would hash to the same code only if their order is the same.
     * Don't mess up the order!
     */
    @Override
    public int hashCode() {
        return Objects.hash(_gridDims, _neo, _hostages, _agentLocs, _padLocs, _pillLocs, _teleBoothLoc);
    }

    /**
     *
     * @return a visualization of the current state as a grid
     */
    @Override
    public String toString() {
        StringBuilder grid = new StringBuilder();
        String[][] info = new String[this.getGridDims().getX()][this.getGridDims().getY()];
        for(String[] row : info) Arrays.fill(row, "");

        Location neoLoc = this.getNeo().getLocation();

        info[neoLoc.getX()][neoLoc.getY()] += "N(" + this.getNeo().getDamage() +
                ","+ this.getNeo().getCurrentCapacity() + ");";

        int maxlen = info[neoLoc.getX()][neoLoc.getY()].length();

        for(Hostage h : this.getHostages()){
            info[h.getLocation().getX()][h.getLocation().getY()] +=
                    "H(" + h.getDamage() + (h.isCarried()? ", CARRIED);":");");

            maxlen = Math.max(maxlen, info[h.getLocation().getX()][h.getLocation().getY()].length());
        }

        for(Location a : this.getAgentLocs()){
            info[a.getX()][a.getY()] += "A;";
            maxlen = Math.max(maxlen, info[a.getX()][a.getY()].length());
        }

        for(Location p : this.getPillLocs()){
            info[p.getX()][p.getY()] += "P;";
            maxlen = Math.max(maxlen, info[p.getX()][p.getY()].length());
        }

        for(Location p : this.getPadLocs().keySet()){
            Location dest = this.getPadLocs().get(p);
            info[p.getX()][p.getY()] += "P(" + dest.getX() + "," + dest.getY() + ");";
            maxlen = Math.max(maxlen, info[p.getX()][p.getY()].length());
        }

        info[this.getTeleBoothLoc().getX()][this.getTeleBoothLoc().getY()] += "TB;";
        maxlen = Math.max(maxlen, info[this.getTeleBoothLoc().getX()][this.getTeleBoothLoc().getY()].length());

        // Column numbers
        grid.append("    |");
        for (int i = 0; i < info[0].length; i++){
            for (int k = 0; k < maxlen/2 + (maxlen % 2) ; k++) {
                grid.append(" ");
            }

            grid.append(i);

            for (int k = 0; k < maxlen/2 - 1; k++) {
                grid.append(" ");
            }
            grid.append("|");
        }
        grid.append("\n");

        // Row numbers + rows
        for (int i = 0; i < info.length; i++){

            // Append separator
            grid.append("----+");
            for (int j = 0; j < info[0].length; j++){
                for (int k = 0; k < maxlen; k++) {
                    grid.append("-");
                }
                grid.append("+");
            }
            grid.append("\n");

            // Append row number
            grid.append("  " + (i) + " |");


            for (int j = 0; j < info[0].length; j++){
                    int spaces = (maxlen - info[i][j].length()) / 2;

                    for (int k = 0; k < spaces; k++){
                        grid.append(" ");
                    }

                    grid.append(info[i][j]);

                    for (int k = 0; k < (maxlen - info[i][j].length()) - spaces; k++){
                        grid.append(" ");
                    }

                grid.append("|");
            }
            grid.append("\n");
        }
        
        return grid.toString();
    }
}
