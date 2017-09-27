package main;

import java.util.*;

public class Main {

    public static void main(String[] args)
    {
        Map<String, State> states =
            State.getAllStates(Arrays.asList(
                new Jug(9, 9),
                new Jug(9, 9),
                new Jug(5, 0),
                new Jug(4, 0)
        ));

        System.out.println("Total states: "+states.size());

        State start = states.get("[9/9, 9/9, 0/5, 0/4]");
        State goal = states.get("[9/9, 5/9, 2/5, 2/4]");
        // A* search
        for (State s : aStar(start, goal))
            System.out.println(s);
        // depth first search
        //DFS(start, goal, new HashSet<>());

        // print entire graph
        /*
        for (State s : states.values()) {
            System.out.print(s+" -> ");
            for (State c : s.childStates)
                System.out.print(c+", ");
            System.out.println();
        }*/
    }

    private static boolean DFS(State current, State goal, Set<State> closed) {
        if (closed.contains(current))
            return false;
        closed.add(current);
        if (!current.equals(goal)) {
            for (State child : current.childStates)
                if (DFS(child, goal, closed)) {
                    System.out.println(current);
                    return true;
                }
                return false;
        }
        System.out.println(current);
        return true;
    }

    private static List<State> aStar(State start, State goal) {
        Set<State> closed = new HashSet<>();

        Set<State> open = new HashSet<>();
        open.add(start);

        Map<State, State> cameFrom = new HashMap<>();

        Map<State, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<State, Integer> fScore = new HashMap<>();
        fScore.put(start, getHeuristicScore(start, goal));

        while (!open.isEmpty()) {
            State parent = getLowestScoreState(open, fScore);
            if (parent.equals(goal))
                return getPath(cameFrom, goal);

            open.remove(parent);
            closed.add(parent);

            for (State child : parent.childStates)
            {
                if (closed.contains(child))
                    continue;

                if (!open.contains(child))
                    open.add(child);

                int childGScore = gScore.get(parent)+1;
                if (childGScore >= gScore.getOrDefault(child, Integer.MAX_VALUE))
                    continue;

                cameFrom.put(child, parent);
                gScore.put(child, childGScore);
                fScore.put(child, childGScore + getHeuristicScore(child, goal));
            }
        }
        return Collections.EMPTY_LIST;
    }

    private static State getLowestScoreState(Set<State> open, Map<State, Integer> fScore) {
        State minState = null;
        int minScore = Integer.MAX_VALUE;
        for (State s : open) {
            int score = fScore.get(s);
            if (score < minScore) {
                minScore = score;
                minState = s;
            }
        }
        return minState;
    }

    private static int getHeuristicScore(State current, State goal) {
        if (goal.jugs.size() != current.jugs.size())
            throw new IllegalStateException();
        int diff = 0;
        for (int i = 0; i < goal.jugs.size(); i++) {
            Jug j1 = goal.jugs.get(i);
            Jug j2 = current.jugs.get(i);
            if (j1.CAPACITY != j2.CAPACITY)
                throw new IllegalStateException();
            diff += (j1.CONTENTS-j2.CONTENTS) == 0 ? 0 : 1;
        }
        if (diff > 1)
            diff--;
        return diff;
    }

    private static List<State> getPath(Map<State, State> cameFrom, State current) {
        List<State> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.keySet().contains(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }
        return path;
    }
}
