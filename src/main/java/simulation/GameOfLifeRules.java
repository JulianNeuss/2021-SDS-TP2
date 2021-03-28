package simulation;

import java.util.Set;

public class GameOfLifeRules {
    private final Set<Integer> aroundAliveSet;
    private final Set<Integer> aroundDeadSet;

    public GameOfLifeRules(Set<Integer> aroundAliveSet, Set<Integer> aroundDeadSet) {
        this.aroundAliveSet = aroundAliveSet;
        this.aroundDeadSet = aroundDeadSet;
    }

    public Set<Integer> getAroundAliveSet() {
        return aroundAliveSet;
    }

    public Set<Integer> getAroundDeadSet() {
        return aroundDeadSet;
    }
}
