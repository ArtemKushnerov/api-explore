package constraintanalyzer.analysis;

import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValueExtractor {
    private ConstantDetector constantDetector = new ConstantDetector();

    public Set<Value> getUsedValues(Unit unit) {
        Set<Value> values = new HashSet<>();
        for (ValueBox useBox : unit.getUseBoxes()) {
            if (constantDetector.holdsNonConstantValue(useBox) && isNotMethod(useBox)) {
                values.add(useBox.getValue());
            }
        }
        return values;
    }

    public List<Value> getDefinedValues(Unit unit) {
        List<ValueBox> defBoxes = unit.getDefBoxes();
        ArrayList<Value> values = new ArrayList<>();
        for (ValueBox defBox : defBoxes) {
            values.add(defBox.getValue());
        }
        return values;
    }

    private boolean isNotMethod(ValueBox useBox) {
        return !(useBox.getValue() instanceof InvokeExpr);
    }


}
