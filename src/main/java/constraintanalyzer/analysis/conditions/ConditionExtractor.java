package constraintanalyzer.analysis.conditions;

import soot.Unit;
import soot.jimple.IfStmt;
import soot.toolkits.graph.DirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class ConditionExtractor {
    private DirectedGraph<Unit> graph;

    public ConditionExtractor(DirectedGraph<Unit> graph) {
        this.graph = graph;
    }

    public List<IfStmt> getConditions() {
        List<IfStmt> conditionals = new ArrayList<>();
        for (Unit unit : graph) {
            if (isConditional(unit)) {
                conditionals.add((IfStmt) unit);
            }
        }
        return conditionals;
    }


    private boolean isConditional(Unit unit) {
        return unit.branches() && unit instanceof IfStmt;
    }

}
