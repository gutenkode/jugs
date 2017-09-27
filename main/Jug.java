package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jug implements Comparable<Jug> {

    public final int CAPACITY, CONTENTS;

    public Jug(int capacity, int contents) {
        if (contents > capacity || contents < 0 || capacity < 0)
            throw new IllegalArgumentException();
        CAPACITY = capacity;
        CONTENTS = contents;
    }

    /**
     * Pour this Jug into another Jug.
     * @param j
     * @return The new state of the jugs.
     */
    public List<Jug> pourInto(Jug dest, List<Jug> jugs) {
        int availableCapacity = dest.CAPACITY - dest.CONTENTS;
        Jug[] pouredJugs;
        if (CONTENTS <= availableCapacity) {
            pouredJugs = new Jug[] {
                    new Jug(CAPACITY, 0),
                    new Jug(dest.CAPACITY, dest.CONTENTS+CONTENTS)
            };
        } else {
            pouredJugs = new Jug[] {
                    new Jug(CAPACITY, CONTENTS-availableCapacity),
                    new Jug(dest.CAPACITY, dest.CAPACITY)
            };
        }

        List<Jug> newJugs = new ArrayList<>();
        jugs.forEach(j -> {
            if (j.equals(this))
                newJugs.add(pouredJugs[0]);
            else if (j.equals(dest))
                newJugs.add(pouredJugs[1]);
            else
                newJugs.add(j);
        });
        Collections.sort(newJugs);
        return Collections.unmodifiableList(newJugs);
    }

    @Override
    public int compareTo(Jug j) {
        return (CAPACITY == j.CAPACITY) ? (j.CONTENTS - CONTENTS) : (j.CAPACITY - CAPACITY);
    }

    @Override
    public String toString() {
        return CONTENTS+"/"+CAPACITY;
    }
}
