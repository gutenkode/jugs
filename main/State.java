package main;

import java.util.*;

public class State {

    public static Map<String, State> getAllStates(List<Jug> jugs) {
        Map<String, State> allStates = new HashMap<>();
        State initialState = new State(jugs, allStates, null);
        return allStates;
    }

    public final List<Jug> jugs;
    public final Set<State> childStates;
    public final State parent;

    private State(List<Jug> j, Map<String, State> allStates, State p) {
        jugs = j;
        parent = p;

        if (allStates.containsKey(jugs.toString()))
            throw new IllegalStateException();

        allStates.put(jugs.toString(), this);
        childStates = findChildStates(allStates);
    }

    public Set<State> findChildStates(Map<String, State> allStates) {
        Set<State> c = new HashSet<>();
        for (Jug j1 : jugs)
            for (Jug j2 : jugs)
                if (j1 != j2)
                    c.add(getAdjacentState(j1, j2, allStates));
        return c;
    }

    private State getAdjacentState(Jug source, Jug dest, Map<String, State> allStates) {
        List<Jug> newJugs = source.pourInto(dest, jugs);
        if (allStates.containsKey(newJugs.toString()))
            return allStates.get(newJugs.toString());
        else
            return new State(newJugs, allStates, this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof State) {
            State s = (State)o;
            return jugs.toString().equals(s.jugs.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        return jugs.toString();
    }
}
