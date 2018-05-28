import java.util.Scanner;

public class Minimize {


    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();//remove the number of lines in the beginning
    EpsilonNFA e = Parser.parse(scanner);
    DFA d = null;
    if (e instanceof DFA) {
      d = (DFA) e;
    } else {
      System.out.println("No DFA provided, aborting");
      System.exit(3);
    }

    System.out.println("Case #1:\n" + minimize(d));
    scanner.close();
  }

    private static DFA minimize(DFA d) {
        //TODO
    }

}
