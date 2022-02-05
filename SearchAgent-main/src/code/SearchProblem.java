package code;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Generic search problem.
 * According to the reference, a problem can be defined formally by five components:
 * 1- The initial state that the agent starts in.
 * 2- A description of the possible actions available to the agent. Given a particular state s,
 * ACTIONS(s) returns the set of actions that can be executed in s.
 * 3- A description of what each action does; the formal name for this is the transition model,
 * specified by a function RESULT(s,a).
 * 4- The goal test, which determines whether a given state is a goal state.
 * 5- A path cost function that assigns a numeric cost to each path. The step
 * cost of taking action a in state s to reach state s' is denoted by c(s,a,s').
 */

public abstract class SearchProblem<State, Operator, costType> {

    /**
     * The initial state, depending on the problem
     */
    State initialState; // component 1

    public SearchProblem() {
    }

    /**
     * Gives the possible actions in the current state.
     *
     * @param n current node state.
     * @return a list of possible actions in this state.
     */
    abstract ArrayList<Operator> actions(Node<State, Operator> n); // component 2

    /**
     * Returns the state resulting from applying action a on state s.
     *
     * @param s state before applying action a.
     * @param a the action to be applied on state s.
     * @return the new state resulting from applying action a on state s.
     */
    abstract State result(State s, Operator a) throws IOException, ClassNotFoundException; // component 3

    /**
     * The goal test. Determines if state s is a goal state or not.
     *
     * @param s state to be tested.
     * @return true if goal, false otherwise.
     */

    abstract boolean isGoal(State s); // component 4


    /**
     * Calculates the step cost of applying operator a on state s1 to reach
     * state s2. Data type can be changed according to problem.
     *
     * @param s1 state before applying action a.
     * @param a  the action to be applied on state s1.
     * @param s2 the destination state after applying action a on state s1.
     * @return the step cost.
     */

    abstract costType stepCost(State s1, Operator a, State s2); // component 5
}
