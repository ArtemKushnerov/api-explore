package constraintanalyzer.analysis;

import com.google.common.base.Joiner;
import constraintanalyzer.configuration.configurators.Configurator;
import constraintanalyzer.configuration.configurators.SootConfigurator;
import org.xmlpull.v1.XmlPullParserException;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.io.IOException;

import static constraintanalyzer.configuration.Conifg.CLASS_NAME;
import static constraintanalyzer.configuration.Conifg.METHOD_NAME;

public class Analyzer {
    private Configurator configurator = new SootConfigurator();

    public void run() throws IOException, XmlPullParserException {
        configurator.configure();
        SootMethod method = getMethod(CLASS_NAME, METHOD_NAME);
        Body body = method.retrieveActiveBody();
        ConstraintExtractor constraintExtractor = new ConstraintExtractor(body);
        constraintExtractor.extract();
        Joiner.MapJoiner mapJoiner = Joiner.on("\n").withKeyValueSeparator(" => ");
        System.out.println(mapJoiner.join(constraintExtractor.getUnitToConstraints()));
    }

    private SootMethod getMethod(String className, String methodName) {
        SootClass sootClass = Scene.v().getSootClass(className);
        return sootClass.getMethodByName(methodName);
    }
}