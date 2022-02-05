package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Helpers {

    /**
     * @param s string of locations delimited by ,
     * @return an arraylist of locations
     */

    private static ArrayList<Location> stringToLocArray(String s) {
        String[] arrStr = s.split(",");
        ArrayList<Location> locs = new ArrayList<>();

        for (int i = 0; i < arrStr.length - 1; i += 2) {
            Location loc = new Location(Integer.parseInt(arrStr[i]), Integer.parseInt(arrStr[i + 1]));
            locs.add(loc);
        }

        return locs;
    }

    /**
     * Takes grid string and deduces the initial state
     *
     * @param grid initial state represented as a string
     */

    public static MatrixState parseGrid(String grid) {
        String[] segments = grid.split(";");

        // map dimensions
        String[] dimsStr = segments[0].split(",");
        Location dims = new Location(Integer.parseInt(dimsStr[0]), Integer.parseInt(dimsStr[1]));

        //maximum capacity
        int c = Integer.parseInt(segments[1]);

        //neo location
        String[] neolocStr = segments[2].split(",");
        Location neoLoc = new Location(Integer.parseInt(neolocStr[0]), Integer.parseInt(neolocStr[1]));

        //telephone booth location
        String[] tbStr = segments[3].split(",");
        Location tbLoc = new Location(Integer.parseInt(tbStr[0]), Integer.parseInt(tbStr[1]));

        //agent locations
        ArrayList<Location> agents = stringToLocArray(segments[4]);

        //pill locations
        ArrayList<Location> pills = stringToLocArray(segments[5]);

        //pad locations
        ArrayList<Location> padArr = stringToLocArray(segments[6]);
        HashMap<Location, Location> pads = new HashMap<>();

        for (int i = 0; i < padArr.size() - 1; i += 2) {
            pads.put(padArr.get(i), padArr.get(i + 1));
        }

        //hostages
        String[] hostageStr = segments[7].split(",");
        ArrayList<Hostage> hostages = new ArrayList<>();

        int hostageId = 0;
        for (int i = 0; i < hostageStr.length - 2; i += 3) {
            Location hostageLoc = new Location(Integer.parseInt(hostageStr[i]), Integer.parseInt(hostageStr[i + 1]));
            Hostage hostage = new Hostage(hostageLoc, Integer.parseInt(hostageStr[i + 2]), false);
            hostage.setId(hostageId++);

            hostages.add(hostage);
        }

        Neo neo = new Neo(neoLoc, 0, c);

        return new MatrixState(dims, neo, hostages, agents, pads, pills, tbLoc);
    }

    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static boolean hostage98(Location neo, ArrayList<Hostage> hostages, MatrixOperator operator) {
        Location temp;
        switch (operator) {
            case UP:
                temp = new Location(neo.getX() - 1, neo.getY());
                break;
            case DOWN:
                temp = new Location(neo.getX() + 1, neo.getY());
                break;
            case LEFT:
                temp = new Location(neo.getX(), neo.getY() - 1);
                break;
            case RIGHT:
                temp = new Location(neo.getX(), neo.getY() + 1);
                break;
            default:
                temp = null;
        }
        for (Hostage h : hostages) {
            if (h.getLocation().equals(temp) && h.getDamage() >= 98) {
                return true;
            }
        }
        return false;
    }

    /**
     * Constructs the output string of the solve method.
     *
     * @param goal          goal node found by the search algorithm
     * @param expandedNodes number of expanded nodes during the search
     * @return A String of the following format: plan;deaths;kills;nodes where
     * - plan is a string representing the operators Neo needs to follow separated by commas.
     * The possible operator names are: up, down, left, right, carry, drop, takePill, kill, and fly.
     * – deaths is a number representing the number of dead hostages in the found goal state
     * (whether they turned into agents or not).
     * – kills is a number representing the number of killed agents in the found goal state
     * (including the number of agents that were hostages before).
     * – nodes is the number of nodes chosen for expansion during the search.
     */
    public static String solutionStr(Node<MatrixState, MatrixOperator> goal, int expandedNodes) {
        if (goal == null) return "No Solution";
        StringBuilder ret = new StringBuilder();

        Node<MatrixState, MatrixOperator> head = goal;

        // Add actions in reverse
        while (head.getParent() != null) {
            switch (head.getAction()) {
                case UP:
                    ret.append("pu,");
                    break;
                case DOWN:
                    ret.append("nwod,");
                    break;
                case LEFT:
                    ret.append("tfel,");
                    break;
                case RIGHT:
                    ret.append("thgir,");
                    break;
                case CARRY:
                    ret.append("yrrac,");
                    break;
                case DROP:
                    ret.append("pord,");
                    break;
                case TAKE_PILL:
                    ret.append("lliPekat,");
                    break;
                case KILL:
                    ret.append("llik,");
                    break;
                case FLY:
                    ret.append("ylf,");
                    break;
                default:
                    break;
            }
            head = head.getParent();
        }

        // Remove last comma
        ret.setLength(Math.max(ret.length() - 1, 0));

        // Reverse string and add semicolon
        ret.reverse();
        ret.append(";");

        // Deaths
        ret.append(goal.getPathCost()[0]);
        ret.append(";");

        // Kills
        ret.append(goal.getPathCost()[1]);
        ret.append(";");

        // Expanded Nodes
        ret.append(expandedNodes);

        return ret.toString();
    }

    /**
     * Constructs a visual presentation of the grid as it undergoes the
     * different steps of the discovered solution (if one was discovered).
     *
     * @param head the current node/step of the goal path
     * @return A stringBuilder that contains the visualized steps
     */
    public static StringBuilder visualize(Node<MatrixState, MatrixOperator> head) {
        if (head == null) return new StringBuilder();

        StringBuilder ret = new StringBuilder(visualize(head.getParent()));

        ret.append("========================NODE-AT-DEPTH-" + head.getDepth() + "========================\n");
        ret.append("ACTION: ").append(head.getAction() == null ? "NOP" : head.getAction().name())
                .append(", ").append("PATH COST: [").append(head.getPathCost()[0])
                .append(", ").append(head.getPathCost()[1]).append("], ")
                .append("HEURISTIC: ").append(head.getHeuristic()).append("\n\n")
                .append(head.getState()).append("\n");

        return ret;
    }
}
