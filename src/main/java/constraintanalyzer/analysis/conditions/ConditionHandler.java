package constraintanalyzer.analysis.conditions;

import soot.Body;
import soot.NormalUnitPrinter;
import soot.Unit;
import soot.Value;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConditionHandler {
    private Body body;
    private DirectedGraph<Unit> graph;
    private Map<Unit, Set<Value>> unitToConstraints;
    private Map<Unit, String> unitToLabels;

    public ConditionHandler(DirectedGraph<Unit> graph, Map<Unit, Set<Value>> unitToConstraints, Body body) {
        this.graph = graph;
        this.unitToConstraints = unitToConstraints;
        this.unitToLabels = new NormalUnitPrinter(body).labels();
        this.body = body;
    }

    public void makeIfElseBlocksDependOnConditionConstraints(IfStmt conditional, Set<Value> conditionConstraints) {
        String successLabel = getSuccessLabel(conditional);
        String failLabel = getFailLabel(conditional);
        List<Unit> ifUnits = getBlock(successLabel, failLabel);
        makeUnitsDependOnCondition(ifUnits, conditionConstraints);
        Unit lastIfUnit = ifUnits.get(ifUnits.size() - 1);
        if (elseExists(lastIfUnit, failLabel)) {
            Unit afterElseUnit = ((GotoStmt) lastIfUnit).getTarget();
            String afterElseLabel = unitToLabels.get(afterElseUnit);
            List<Unit> elseUnits = getBlock(failLabel, afterElseLabel);
            makeUnitsDependOnCondition(elseUnits, conditionConstraints);
        }
    }

    private String getSuccessLabel(IfStmt conditional) {
        List<Unit> ifDirectSuccessors = graph.getSuccsOf(conditional);
        return unitToLabels.get(ifDirectSuccessors.get(1));
    }

    private String getFailLabel(IfStmt conditional) {
        List<Unit> ifDirectSuccessors = graph.getSuccsOf(conditional);
        Unit goTo = ifDirectSuccessors.get(0);
        return unitToLabels.get(graph.getSuccsOf(goTo).get(0));
    }

    private List<Unit> getBlock(String startLabel, String endLabel) {
        Chain<Unit> units = body.getUnits();
        List<Unit> block = new ArrayList<>();
        boolean isInsideBlock = false;
        for (Unit unit : units) {
            String label = unitToLabels.get(unit);
            if (label != null) {
                if (label.equals(startLabel)) {
                    isInsideBlock = true;
                } else if (label.equals(endLabel)) {
                    isInsideBlock = false;
                }
            }
            if (isInsideBlock) {
                block.add(unit);
            }
        }
        return block;
    }

    private boolean elseExists(Unit lastIfUnit, String failLabel) {
        return lastIfUnit instanceof GotoStmt && goesForward((GotoStmt) lastIfUnit, failLabel);
    }

    private boolean goesForward(GotoStmt goTo, String failLabel) {
        Unit unit = goTo.getTarget();
        String label = unitToLabels.get(unit);
        return labelNumber(label) > labelNumber(failLabel);
    }

    private int labelNumber(String label) {
        return Character.getNumericValue(label.charAt(label.length() - 1));
    }

    private void makeUnitsDependOnCondition(List<Unit> units, Set<Value> conditionConstraints) {
        for (Unit unit : units) {
            Set<Value> unitConstraints = unitToConstraints.get(unit);
            if (unitConstraints != null) {
                unitConstraints.addAll(conditionConstraints);
            }
        }
    }

}
