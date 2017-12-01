package constraintanalyzer.configuration.configurators;

import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import static constraintanalyzer.configuration.Conifg.CLASS_NAME;

public class SootConfigurator implements Configurator {

    public void configure() {
        String classPath = "C:\\SaToSS\\Sources\\constraint-analyzer\\src\\main\\resources;C:\\Program Files\\Java\\jdk1.7.0_80\\jre\\lib\\rt.jar";
        Options.v().set_soot_classpath(classPath);
        SootClass sClass = Scene.v().loadClassAndSupport(CLASS_NAME);
        sClass.setApplicationClass();

        Scene.v().loadNecessaryClasses();

    }

}
