public class Main {

    private static String str = "Hello world";


    public static void main(String[] args) {
        new Main().someMethod(str);
    }

    public void someMethod(String str) {
       String str2 = this.str;
       int a = 42;
       int b = 23;
       int c = 0;

       String str3 = str2;

       a = b + c;
       if (a != 42) {
           int z = c;
       }
       else {
           int z = 2134;
       }

       if (str != null) {
           saySomething(str3);
       }
    }

    private static void saySomething(String str) {
        System.out.println(str);
    }
}
