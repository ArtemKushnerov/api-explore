public class IFCond {

    private static String methodOfInterest(String one) {
        return "staff";
    }

    public void testMethod(String parameter) {

        int localVar = 10;
        System.out.println("before condition");
        if (localVar > 11) {
            System.out.println("inside if");
            System.out.println("inside if2");
        }
        System.out.println("after condition");
    }
}
