package apiexplore;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.internal.JIfStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

import java.util.*;


/**
 * This is merely a proof of concept, as it only collects constraints imposed by local variables (statements and branches)
 * This has to be extended for handling objects, fields, and return values.
 *
 */

public class GetPathConstraints {
    protected PathConstraintsAnalysis analysis = null;

    /**
     * Constructor
     * @param graph - UnitGraph object (CFG of a method)
     */
    public GetPathConstraints(DirectedGraph<Unit> graph) {
        this.analysis = new PathConstraintsAnalysis(graph);
    }

    /**
     *  Gets a list of local variables that must be initialized in order to reach a statement
     * @param unit - Target statement
     * @return List of local variables
     */
    public List<FlowSet> getConstraints(Unit unit) {
        FlowSet flowSet = analysis.getUnitFlows().get(unit);
        return flowSet.toList();
    }

}
