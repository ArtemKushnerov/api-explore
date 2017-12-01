public class Test {
    private String instanceVar;
    private static String classVar;

    public void testMethod(String parameter) {
        String localVar = "test";
        String localVar2 =localVar;
        String localVar3 = instanceVar;
        String localVar4 = classVar;
        String localVar5 = parameter;
        int methodParameter = 8;
        int localVar7 = methodParameter;
        String localVar6 = method(localVar7);
        Main localVar8 = new Main();
        String cond = "cond";
        if(cond != null) {
            Test.methodOfInterest(localVar, localVar2, localVar3,localVar4, localVar6);
        }

        System.out.println("Outside everywhere");
    }

    private static String methodOfInterest(String one, String two, String three, String four, String five) {
        return "staff";
    }

    private String method(int param) {
        return "hello";
    }
    private static String saySomething(int str) {
        System.out.println(str);
        return "hello";
    }

}
