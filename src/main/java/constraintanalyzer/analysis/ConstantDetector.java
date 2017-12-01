package constraintanalyzer.analysis;

import soot.Unit;
import soot.ValueBox;
import soot.jimple.Constant;

import java.util.List;

public class ConstantDetector {

    public boolean holdsAnythingExceptConstants(Unit unit) {
        List<ValueBox> useBoxes = unit.getUseBoxes();
        for (ValueBox useBox : useBoxes) {
            if (holdsNonConstantValue(useBox)) {
                return true;
            }
        }
        return false;
    }

    public boolean holdsNonConstantValue(ValueBox box) {
        return !(box.getValue() instanceof Constant);
    }

}
