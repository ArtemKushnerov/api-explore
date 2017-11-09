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
 * class that collects intra-procedural path constraints
 */
class PathConstraintsAnalysis extends BackwardFlowAnalysis<Unit, FlowSet> {

    private FlowSet emptySet = new ArraySparseSet();
    private Map<Unit, FlowSet> unitFlows = new LinkedHashMap<>();

    /**
     * Constructor:
     * @param graph - UnitGraph object (CFG of a method)
     */
    public PathConstraintsAnalysis(final DirectedGraph<Unit> graph) {
        super(graph);

        List<Unit> conditionals = new ArrayList();
        // first, collect all used variables for all statements, and
        // collect conditional statements
        for (Unit unit : graph)  {
            unitFlows.put(unit, emptySet.clone());
            addUsedVariables(unitFlows.get(unit), unit);
            if (unit.branches()) {
                if (unit instanceof JIfStmt) {
                    conditionals.add(unit);
                }
            }
        }
        // second, add used variables from the conditional statemnts
        // as dependencies to its successors ("if" and "else" branches)
        for (Unit conditional : conditionals) {
            for (Unit succ : graph.getSuccsOf(conditional))  {
                addUsedVariables(unitFlows.get(succ), conditional);
            }
        }

        // find the fix point
        doAnalysis();
    }

    /**
     * Find all value box objects in a list whose values are not a constant
     * @param useBoxes - List of ValueBox objects
     * @return List of ValueBox objects
     */
    private List<ValueBox> getNonConstantUseBoxes(List<ValueBox> useBoxes) {
        List<ValueBox> boxes = new ArrayList<>();
        for (ValueBox useBox : useBoxes) {
            if (useBox.getValue() instanceof Local &&
                    !(useBox.getValue() instanceof Constant)){
                boxes.add(useBox);
            }
        }
        return boxes;
    }

    /**
     * Add a set of local variables used in a Unit to a specified FlowSet
     * @param flow - FlowSet object
     * @param node - Unit object
     */
    private void addUsedVariables(FlowSet flow, Unit node) {
        List<ValueBox> boxes = getNonConstantUseBoxes(node.getUseBoxes());
        for (ValueBox box : boxes) {
            if (box.getValue() instanceof Local) {
                flow.add(box.getValue());
            }
        }
    }

    /**
     *  Remove variables defined in a Unit from a specified FlowSet
     * @param flow - FlowSet object
     * @param node - Unit object
     * @return List of removed Value objects
     */
    private List<Value> removeDefinedVariables(FlowSet flow, Unit node) {
        List<Value> removedValues = new ArrayList<>();
        for (ValueBox defBox : node.getDefBoxes()) {
            if (defBox.getValue() instanceof Local) {
                Iterator flowIt = flow.iterator();
                while (flowIt.hasNext()) {
                    Value flowValue = (Value) flowIt.next();
                    if (flowValue.equivTo(defBox.getValue()) &&
                            !getNonConstantUseBoxes(node.getUseBoxes()).isEmpty()) {
                        flow.remove(flowValue);
                        removedValues.add(flowValue);
                    }
                }
            }
        }
        return removedValues;
    }

    /**
     * Custom flowThrough implementation: here the flow equation is implemented
     */
    @Override
    protected void flowThrough(FlowSet in, Unit node, FlowSet out) {
        addUsedVariables(in, node);
        out.union(in);
        List<Value> removedValues = removeDefinedVariables(out, node);
        if (!removedValues.isEmpty()) {
            for (Map.Entry<Unit, FlowSet> entry : unitFlows.entrySet()) {
                for (Value valueToRemove : removedValues) {
                    if (entry.getValue().contains(valueToRemove)) {
                        entry.getValue().remove(valueToRemove);
                        addUsedVariables(entry.getValue(), node);
                    }
                }
            }
        }
    }

    /**
     * Joins two "IN" flows of the CFG into one "OUT"
     */
    @Override
    protected void merge(FlowSet in1, FlowSet in2, FlowSet out) {
        in1.union(in2, out);
    }

    /**
     * Copies the "IN" flow set into the "OUT" flow set
     * @param in - "IN" FlowSet
     * @param out - "OUT" FlowSet
     */
    @Override
    protected void copy(FlowSet in, FlowSet out) {
        in.copy(out);
    }

    /**
     * Initializes a new flow (empty)
     * @return FlowSet object
     */
    @Override
    protected FlowSet newInitialFlow() {
        return emptySet.clone();
    }

    /**
     * Initializes the lattice at the entry statement (empty)
     * @return FlowSet object
     */
    @Override
    protected FlowSet entryInitialFlow() {
        return emptySet.clone();
    }

    public Map<Unit, FlowSet> getUnitFlows() {
        return unitFlows;
    }
}
