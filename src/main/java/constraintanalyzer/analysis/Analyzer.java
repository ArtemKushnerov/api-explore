package constraintanalyzer.analysis;

import com.google.common.base.Joiner;
import constraintanalyzer.configuration.configurators.SootConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.io.IOException;

import static constraintanalyzer.configuration.Conifg.CLASS_NAME;
import static constraintanalyzer.configuration.Conifg.METHOD_NAME;

public class Analyzer {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void run() throws IOException, XmlPullParserException {
        new SootConfigurator().configure();
        SootMethod method = getMethod(CLASS_NAME, METHOD_NAME);
        Body body = method.retrieveActiveBody();
        LOG.debug(body.toString());
        ConstraintExtractor constraintExtractor = new ConstraintExtractor(body);
        constraintExtractor.extract();
        Joiner.MapJoiner mapJoiner = Joiner.on("\n").withKeyValueSeparator(" => ");
        LOG.info("\n" +mapJoiner.join(constraintExtractor.getUnitToConstraints()));
    }

    private SootMethod getMethod(String className, String methodName) {
        SootClass sootClass = Scene.v().getSootClass(className);
        return sootClass.getMethodByName(methodName);
    }
}