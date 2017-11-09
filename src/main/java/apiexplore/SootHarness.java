package apiexplore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PackManager;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.infoflow.android.SetupApplication;
import soot.options.Options;

import java.util.Collections;

public class SootHarness {

    private static Logger log = LoggerFactory.getLogger(SootHarness.class);

    public SootHarness(String callbacksFilePath, String sourcesAndSinksFilePath, String androidJarsPath, String apkFilePath) {
        try {
            SetupApplication app = new SetupApplication(androidJarsPath, apkFilePath);
            app.setCallbackFile(callbacksFilePath);
            app.calculateSourcesSinksEntrypoints(sourcesAndSinksFilePath);
            soot.G.reset();

            Options.v().set_src_prec(Options.src_prec_apk);
            Options.v().set_process_dir(Collections.singletonList(apkFilePath));
            Options.v().set_android_jars(androidJarsPath);
            Options.v().set_whole_program(true);
            Options.v().set_allow_phantom_refs(true);
            Options.v().set_output_format(Options.output_format_jimple);
            Options.v().setPhaseOption("cg.spark", "on");

            Scene.v().loadNecessaryClasses();
            SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
            Options.v().set_main_class(entryPoint.getSignature());
            Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
            PackManager.v().runPacks();
        }
        catch (Exception e) {
            log.error("Failed to build the call graph!");
            log.error(String.format("ERROR: %s\n%s\n%s\n", e.getMessage(), e.getCause(), e.getStackTrace().toString()));
            System.exit(1);
        }
    }

}
