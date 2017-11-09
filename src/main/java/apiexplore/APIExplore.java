package apiexplore;

import soot.*;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;

import java.util.Iterator;
import java.util.List;

public class APIExplore {

    public static void main(String[] args) {
        String path = "C:\\SaToSS\\Sources\\api-explore\\src\\main\\resources;C:\\Program Files\\Java\\jdk1.7.0_80\\jre\\lib\\rt.jar";
        Options.v().set_soot_classpath(path);
        SootClass sClass = Scene.v().loadClassAndSupport("Main");
        sClass.setApplicationClass();

        Scene.v().loadNecessaryClasses();

        SootMethod method = sClass.getMethodByName("someMethod");
        System.out.println(method.retrieveActiveBody().toString());
        Unit targetStatement = SootUtils.getSingleUnit(method, "staticinvoke <Main: void saySomething(java.lang.String)>(");
        DirectedGraph<Unit> unitGraph = SootUtils.getMethodCFG(method);

        GetPathConstraints pc = new GetPathConstraints(unitGraph);
        List constraints = pc.getConstraints(targetStatement);

        Iterator it = constraints.iterator();
        System.out.println("Statement: " + targetStatement);
        while (it.hasNext()) {
            Local flow = (Local) it.next();
            System.out.println("\tvar: " + flow.toString() + " -> " + flow.getType());
        }
    }
}
