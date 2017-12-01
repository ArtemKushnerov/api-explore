public class Loop {

    public void testMethod(String parameter) {
        int localVar = 10;
        System.out.println("before loop");
        for (int i = 0; i < localVar; i++) {
            System.out.println("Inside loop");
            System.out.println("Inside loop2");
        }
        System.out.println("after loop");
    }
    private static String methodOfInterest(String one) {
        return "staff";
    }


}
