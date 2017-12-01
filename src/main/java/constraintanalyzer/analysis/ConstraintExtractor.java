package constraintanalyzer.analysis;

import constraintanalyzer.analysis.conditions.ConditionExtractor;
import constraintanalyzer.analysis.conditions.ConditionHandler;
import soot.Body;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConstraintExtractor {
    private Map<Unit, Set<Value>> unitToConstraints = new LinkedHashMap<>();
    private ConditionHandler conditionHandler;
    private ConditionExtractor conditionExtractor;
    private Body body;
    private ValueExtractor valueExtractor = new ValueExtractor();
    private ConstantDetector constantDetector = new ConstantDetector();

    public ConstraintExtractor(Body body) {
        this.body = body;
        DirectedGraph<Unit> graph = new ExceptionalUnitGraph(body);
        conditionExtractor = new ConditionExtractor(graph);
        conditionHandler = new ConditionHandler(graph, unitToConstraints, body);
        initConstraintsWithUsedVariables();
        addConditionsAsConstraints();
    }

    public void extract() {
        Set<Unit> units = unitToConstraints.keySet();
        for (Unit unit : units) {
            if (constantDetector.holdsAnythingExceptConstants(unit)) {
                List<Value> definedValues = valueExtractor.getDefinedValues(unit);
                Set<Value> usedValues = unitToConstraints.get(unit);
                substituteDefinedValuesByUsedValuesForAllUnits(definedValues, usedValues);
            }
        }
    }

    private void initConstraintsWithUsedVariables() {
        for (Unit unit : body.getUnits()) {
            unitToConstraints.put(unit, valueExtractor.getUsedValues(unit));
        }
    }

    private void addConditionsAsConstraints() {
        List<IfStmt> conditions = conditionExtractor.getConditions();
        for (IfStmt condition : conditions) {
            Set<Value> conditionConstraints = unitToConstraints.get(condition);
            conditionHandler.makeIfElseBlocksDependOnConditionConstraints(condition, conditionConstraints);
        }
    }

    private void substituteDefinedValuesByUsedValuesForAllUnits(List<Value> defValues, Set<Value> usedVariables) {
        for (Set<Value> constraints : unitToConstraints.values()) {
            for (Value defValue : defValues) {
                if (constraints.contains(defValue)) {
                    constraints.remove(defValue);
                    constraints.addAll(usedVariables);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "unitToConstraints = " + unitToConstraints;
    }

    public Map<Unit, Set<Value>> getUnitToConstraints() {
        return unitToConstraints;
    }
}
