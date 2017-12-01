public class ComplexExample {

    public void testMethod(String parameter) {
        int ifVar = 10;
        int innerloopVar = 10;
        int outerLoopVar = 10;
        System.out.println("before");
        for (int i = 0; i < outerLoopVar; i++) {
            if (ifVar > 42) {
                for (int j = 0; j < innerloopVar; j++) {
                    System.out.println("inside inner loop and if");
                }
                System.out.println("inside if");
                System.out.println("inside if2");
            } else {
                System.out.println("inside outer loop and else" );
            }
        }
        System.out.println("after");
    }
}
