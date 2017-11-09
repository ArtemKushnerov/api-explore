package apiexplore;

import soot.*;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.Iterator;

public class SootUtils {

    private SootUtils() {

    }

    /**
     * Retrieves the Soot CallGraph
     *
     * @return CallGraph
     */
    public CallGraph getCallGraph() {
        return Scene.v().getCallGraph();
    }

    /**
     * Gets a SootMethod by its class name and method name
     *
     * @param className
     * @param methodName
     * @return SootMethod
     */
    public static SootMethod getMethod(String className, String methodName) {
        SootClass sootClass = Scene.v().getSootClass(className);
        return sootClass.getMethodByName(methodName);
    }

    /**
     * Determines whether a method from a class is statically reachable
     * (i.e. is a node of the MCG)
     *
     * @param className - full package name + class name
     * @param methodName - method name (without parameters)
     * @return true/false
     */
    public static boolean isMethodStaticallyReachable(String className, String methodName) {
        SootClass sootClass = Scene.v().loadClassAndSupport(className);
        SootMethod sootMethod = null;
        try {
            sootMethod = sootClass.getMethodByName(methodName);
        }
        catch (RuntimeException e) {
            if (e.getMessage().contains("couldn't find method")) {
                return false;
            }
            else throw new RuntimeException(e);
        }
        return Scene.v().getReachableMethods().contains(sootMethod);
    }

    /**
     * Gets DirectedGraph cfg for specific Soot method
     *
     * @param sootMethod
     * @return DirectedGraph
     */
    public static DirectedGraph<Unit> getMethodCFG(final SootMethod sootMethod) {
        Body body = sootMethod.retrieveActiveBody();
        return new ExceptionalUnitGraph(body);
    }

    /**
     * Very lame method to get a target unit from the IR
     * (Jimple-specific)
     *
     * @param method
     * @param str
     * @return Unit
     */
    public static Unit getSingleUnit(final SootMethod method, String str) {
        Body body = method.retrieveActiveBody();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof Stmt)  {
                Stmt statement = (Stmt) u;
                if (statement.toString().contains(str)) {
                    return u;
                }
            }
        }
        return null;
    }
}
