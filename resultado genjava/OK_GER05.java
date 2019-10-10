import java.util.Scanner;

public class OK_GER05{
   public static void main(String []args) {
      new Program().run();
   }
}

class A 
{
    private Scanner scanner = new Scanner(System.in);
    void m( ){
      int a, b, c, d, e, f;
      a = scanner.nextInt();
      b = scanner.nextInt();
      c = scanner.nextInt();
      d = scanner.nextInt();
      e = scanner.nextInt();
      f = scanner.nextInt();
      System.out.print("" + a);
      System.out.print("" + b);
      System.out.print("" + c);
      System.out.print("" + d);
      System.out.print("" + e);
      System.out.print("" + f);
   }
}

class Program 
{
    private Scanner scanner = new Scanner(System.in);
    void run( ){
      A a;
      System.out.println("" + "");
      System.out.println("" + "Ok-ger05");
      System.out.println("" + "The output should be what you give as input.");
      System.out.println("" + "Type in six numbers");
      a = new A();
      a.m();
   }
}
