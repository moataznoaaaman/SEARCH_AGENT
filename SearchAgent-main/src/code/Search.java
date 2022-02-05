package code;


import java.io.IOException;
import java.util.*;

public class Search {


    public static Object[] searchProcedure(SearchProblem problem, String strategy) throws IOException, ClassNotFoundException {

        if (problem instanceof Matrix) {
            Matrix mProblem = (Matrix) problem;

            //root node
            Node<MatrixState, MatrixOperator> root = new Node<>();
            root.setState(mProblem.initialState);

            switch (strategy) {
                case "BF":
                    return BFS(mProblem, root);
                case "DF":
                    return DFS(mProblem, root);
                case "ID":
                    return IDS(mProblem, root, 1000);
                case "UC":
                    return UCS(mProblem, root);
                case "GR1":
                    return GR(mProblem, root, 1);
                case "GR2":
                    return GR(mProblem, root, 2);
                case "AS1":
                    return AS(mProblem, root, 1);
                case "AS2":
                    return AS(mProblem, root, 2);
                default:
                    break;
            }

        }

        return null;
    }

    public static Object[] BFS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
        HashSet<MatrixState> visitedStates = new HashSet<MatrixState>();
        Queue<Node<MatrixState, MatrixOperator>> Q = new LinkedList<>();
        Q.add(root);
        visitedStates.add(root.getState());
        int expandedNodes = 0;

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            
            // Expand node
            expandedNodes++;
            
            if (problem.isGoal(head.getState())) return new Object[]{head, expandedNodes};

            ArrayList<MatrixOperator> possibleActions = problem.actions(head);
            for (MatrixOperator a : possibleActions) {
                MatrixState possibleState = problem.result(head.getState(), a);
                if (!visitedStates.contains(possibleState)) {
                    int[] cost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                    cost[0]+= problem.stepCost(head.getState(), a, possibleState)[0];
                    cost[1]+= problem.stepCost(head.getState(), a, possibleState)[1];
                    Node<MatrixState, MatrixOperator> newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth() + 1);
                    Q.add(newNode);
                    visitedStates.add(possibleState);
                }
            }
        }

        // null == failure
        return new Object[]{null,expandedNodes};
    }

    public static Object[] DFS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
        HashSet<MatrixState> visitedStates = new HashSet<MatrixState>();
        Stack<Node<MatrixState, MatrixOperator>> S = new Stack<>();
        S.add(root);
        visitedStates.add(root.getState());
        int expandedNodes = 0;

        while (!S.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = S.pop();
            
            // Expand node
            expandedNodes++;
            
            if (problem.isGoal(head.getState())) return new Object[]{head, expandedNodes};

            ArrayList<MatrixOperator> possibleActions = problem.actions(head);
            for (MatrixOperator a : possibleActions) {
                MatrixState possibleState = problem.result(head.getState(), a);
                if (!visitedStates.contains(possibleState)) {
                    int[] cost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                    cost[0]+= problem.stepCost(head.getState(), a, possibleState)[0];
                    cost[1]+= problem.stepCost(head.getState(), a, possibleState)[1];
                    Node<MatrixState, MatrixOperator> newNode = new Node<MatrixState, MatrixOperator>(possibleState, head, a, cost, 0, head.getDepth() + 1);
                    S.push(newNode);
                    visitedStates.add(possibleState);
                }
            }
        }

        // null == failure
        return new Object[]{null,expandedNodes};
    }

    public static Object[] IDS(Matrix problem, Node<MatrixState, MatrixOperator> root, int maxDepth)
            throws IOException, ClassNotFoundException {
        Object[] DLSResult;
        int expandedNodes = 0;

        // Perform limited depth search until we reach the specified maximum depth
        for (int i = 0; i < maxDepth + 1; i++) {
            DLSResult = DLS(problem, root, i); // goal node and number of expanded nodes
            expandedNodes += (int) DLSResult[1]; // add the number of expanded nodes to the total
            DLSResult[1] = expandedNodes;

            if (DLSResult[0] != null) return DLSResult; // if goal is found
        }

        // null goal node == failure
        return new Object[]{null, expandedNodes};
    }

    public static Object[] UCS(Matrix problem, Node<MatrixState, MatrixOperator> root) throws IOException, ClassNotFoundException {
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>();
        HashSet<MatrixState> visitedStates = new HashSet<>();
        int expandedNodes = 0;

        Q.add(root);
        visitedStates.add(root.getState());

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();

            // Expand node
            expandedNodes++;

            if (problem.isGoal(head.getState())) return new Object[]{head, expandedNodes};


            // All possible actions from current state
            ArrayList<MatrixOperator> actions = problem.actions(head);

            //current action
            for (MatrixOperator action : actions) {
                MatrixState result = problem.result(head.getState(), action); //state resulting from action
                int[] stepCost = problem.stepCost(head.getState(), action, result);
                int[] pathCost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                pathCost[0] += stepCost[0];
                pathCost[1] += stepCost[1];

                // if state is not repeated
                if (!visitedStates.contains(result)) {
                    Node<MatrixState, MatrixOperator> child = new Node<>(result, head, action, pathCost,
                            0, head.getDepth() + 1);
                    Q.add(child); //added to queue
                    visitedStates.add(result); // state marked as visited to avoid adding it to queue again
                }
            }
        }

        // null == failure
        return new Object[]{null, expandedNodes};
    }

    public static Object[]
    GR(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) throws ClassNotFoundException, IOException {

        HashSet<MatrixState> visitedStates = new HashSet<MatrixState>();
        int expandedNodes = 0;
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>();
        root.setgreedy(true);
        Q.add(root);
        visitedStates.add(root.getState());

        while (!Q.isEmpty()) {
        	expandedNodes++;
            Node<MatrixState, MatrixOperator> head = Q.poll();
            if (problem.isGoal(head.getState())) return new Object[]{head,expandedNodes};
            ArrayList <MatrixOperator> possible_actions = problem.actions(head);
            for (MatrixOperator x : possible_actions)
            {
            	MatrixState new_state=problem.result(head.getState(), x);
            	if(!visitedStates.contains(new_state))
            	{
                	int[]cost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                    cost[0]+= problem.stepCost(head.getState(), x, new_state)[0];
                    cost[1]+= problem.stepCost(head.getState(), x, new_state)[1];

                	float hur_value=0;
                	
                	if(heuristicNum == 1)
                	{
                		hur_value = problem.GreedyHeuristic1(new_state);
                	}
                	else
                	{
                		hur_value = problem.GreedyHeuristic2(new_state);
                	}
                	
                	Node<MatrixState,MatrixOperator> result_node = new Node<MatrixState,MatrixOperator>(new_state,head,x,cost,hur_value,head.getDepth()+1);
                	result_node.setgreedy(true);
            		Q.add(result_node);
            		visitedStates.add(new_state);
            	}

            }
        }

        return new Object[]{null,expandedNodes};
    }

    public static Object[] AS(Matrix problem, Node<MatrixState, MatrixOperator> root, int heuristicNum) throws IOException, ClassNotFoundException {
        HashSet<MatrixState> visitedStates = new HashSet<MatrixState>();
        PriorityQueue<Node<MatrixState, MatrixOperator>> Q = new PriorityQueue<>();
        Q.add(root);
        visitedStates.add(root.getState());
        int expandedNodes = 0;

        while (!Q.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = Q.poll();
            
         // Expand node
            expandedNodes++;
            
            if (problem.isGoal(head.getState())) return new Object[]{head, expandedNodes};

            ArrayList<MatrixOperator> possibleActions = problem.actions(head);
            for (MatrixOperator a : possibleActions) {
                MatrixState possibleState = problem.result(head.getState(), a);
                if (!visitedStates.contains(possibleState)) {
                    int[] cost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                    cost[0]+= problem.stepCost(head.getState(), a, possibleState)[0];
                    cost[1]+= problem.stepCost(head.getState(), a, possibleState)[1];
                    float heuristic = 0;
                    if (heuristicNum == 1) {
                        heuristic = problem.ASHeuristic1(possibleState);
                    } else if (heuristicNum == 2) {
                        heuristic = problem.ASHeuristic2(possibleState);
                    }
                    Node<MatrixState, MatrixOperator> newNode = new Node<>(possibleState, head, a, cost, heuristic, head.getDepth() + 1);
                    Q.add(newNode);
                    visitedStates.add(possibleState);
                }
            }
        }

        // null == failure
        return new Object[]{null,expandedNodes};
    }

    public static Object[] DLS(Matrix problem, Node<MatrixState, MatrixOperator> root, int d)
            throws IOException, ClassNotFoundException {
        HashSet<MatrixState> visitedStates = new HashSet<>();
        Stack<Node<MatrixState, MatrixOperator>> S = new Stack<>();
        int expandedNodes = 0;

        S.push(root);
        visitedStates.add(root.getState());

        while (!S.isEmpty()) {
            Node<MatrixState, MatrixOperator> head = S.pop();

            // Expand node
            expandedNodes++;

            if (problem.isGoal(head.getState())) return new Object[]{head, expandedNodes};
            if (head.getDepth() == d) continue; // maximum depth reached


            // All possible actions from current state
            ArrayList<MatrixOperator> actions = problem.actions(head);

            //current action
            for (MatrixOperator action : actions) {
                MatrixState result = problem.result(head.getState(), action); //state resulting from action
                int[] stepCost = problem.stepCost(head.getState(), action, result);
                int[] pathCost = new int[]{head.getPathCost()[0], head.getPathCost()[1]};
                pathCost[0] += stepCost[0];
                pathCost[1] += stepCost[1];

                // if state is not repeated and is not maximum depth
                if (!visitedStates.contains(result)) {
                    Node<MatrixState, MatrixOperator> child = new Node<>(result, head, action, pathCost,
                            0, head.getDepth() + 1);
                    S.add(child); //added to stack
                    visitedStates.add(result); // state marked as visited to avoid adding it to queue again
                }
            }
        }

        return new Object[]{null, expandedNodes};
    }
}
