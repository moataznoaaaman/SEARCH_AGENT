package code;

/**
 * The Node class represents a node in a search tree. its attributes
 * are taken straight from the reference book, Except for depth and heuristic. Depth
 * is used in the lecture slides, and should be removed unless we find a
 * good use for it.
 */

public class Node<State, Operator> implements Comparable<Node<State, Operator>> {

    /**
     * state: The state in the state space to which the node corresponds
     * parent: The node in the search tree that generated this node
     * action: The action that was applied to the parent to generate the node
     * pathCost: The cost of the path from the initial state to the node
     * heuristic: the value of the heuristic for the current node
     * depth: the depth level of the node from the root, 0-indexed
     */

    private State _state;
    private Node _parent;
    private Operator _action;
    private int[] _pathCost;
    private float _heuristic;
    private int _depth;
    private boolean _isgreedy;

    public Node(State state, Node parent, Operator action, int[] pathCost, float heuristic, int depth) {
        _state = state;
        _parent = parent;
        _action = action;
        _pathCost = pathCost;
        _heuristic = heuristic;
        _depth = depth;
        _isgreedy = false;
    }

    /**
     * default constructor. use it for the root node or nodes that have their attributes
     * set later.
     */

    public Node() {
        _state = null;
        _parent = null;
        _action = null;
        _pathCost = new int[]{0, 0};
        _heuristic = 0;
        _depth = 0;
    }

    // ==========================Getters-and-Setters==========================
    public void setgreedy(boolean isgreedy)
    {
    	_isgreedy = isgreedy;
    }
    public State getState() {
        return _state;
    }
    
    public void setState(State state) {
        _state = state;
    }

    public Node getParent() {
        return _parent;
    }

    public void setParent(Node parent) {
        _parent = parent;
    }

    public Operator getAction() {
        return _action;
    }

    public void setAction(Operator action) {
        _action = action;
    }

    public int[] getPathCost() {
        return _pathCost;
    }

    public void setPathCost(int[] pathCost) {
        _pathCost = pathCost;
    }

    public float getHeuristic() {
        return _heuristic;
    }

    public void setHeuristic(int heuristic) {
        _heuristic = heuristic;
    }

    public int getDepth() {
        return _depth;
    }

    public void setDepth(int depth) {
        _depth = depth;
    }

    // ==========================Comparison==========================

    @Override
    public int compareTo(Node<State, Operator> node) {
    	if(!_isgreedy)
    	{
    		float myTotal = this.getPathCost()[0] + this.getHeuristic();
            float otherTotal = node.getPathCost()[0] + node.getHeuristic();

            if (myTotal > otherTotal) {
                // if current object is greater,then return 1
                return 1;
            } else if (myTotal < otherTotal) {
                // if current object is less,then return -1
                return -1;
            } else {
                myTotal += this.getPathCost()[1];
                otherTotal += node.getPathCost()[1];

                if (myTotal > otherTotal) {
                    return 1;
                } else if (myTotal < otherTotal) {
                    // if current object is less,then return -1
                    return -1;
                }
                return 0;
            	}
            
        }
    	else
        {
    		float myTotal = this.getHeuristic();
            float otherTotal = node.getHeuristic();

            if (myTotal > otherTotal) {
                // if current object is greater,then return 1
                return 1;
            } else if (myTotal < otherTotal) {
                // if current object is less,then return -1
                return -1;
            }
            else
            {
            	return 0;
            }
        }
    }
}
