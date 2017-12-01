package constraintanalyzer.configuration.configurators;

import constraintanalyzer.exceptions.SootConfigurationException;
import org.xmlpull.v1.XmlPullParserException;
import soot.PackManager;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.infoflow.android.SetupApplication;
import soot.options.Options;

import java.io.IOException;
import java.util.Collections;

import static constraintanalyzer.configuration.Conifg.*;

public class FlowDroidConfigurator implements Configurator {

    @Override
    public void configure() {
        try {
            SetupApplication app = new SetupApplication(ANDROID_FRAMEWORKS_PATH, APK_PATH);
            app.setCallbackFile(ANDROID_CALLBACKS_FILE_PATH);
            app.calculateSourcesSinksEntrypoints(ANDROID_SOURCES_AND_SINKS_FILE_PATH);
            soot.G.reset();

            Options.v().set_src_prec(Options.src_prec_apk);
            Options.v().set_process_dir(Collections.singletonList(APK_PATH));
            Options.v().set_android_jars(ANDROID_FRAMEWORKS_PATH);
            Options.v().set_whole_program(true);
            Options.v().set_allow_phantom_refs(true);
            Options.v().set_output_format(Options.output_format_jimple);
            Options.v().setPhaseOption("cg.spark", "on");

            Scene.v().loadNecessaryClasses();
            SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
            Options.v().set_main_class(entryPoint.getSignature());
            Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
            PackManager.v().runPacks();
        } catch (IOException | XmlPullParserException e) {
            throw new SootConfigurationException(e);
        }
    }

}
