import java.util.Scanner;

public final class CYK {
  private CYK() {}

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Grammar g = Grammar.parse(scanner);
    String word;
    while (!(word = scanner.nextLine()).equals("DONE")) {
      System.out.println(word + "\n" + cyk(g, word) + "\n");
    }
    scanner.close();
  }

  public static CYKTable cyk(Grammar cnf, String word) {
    return null;
  }
}
