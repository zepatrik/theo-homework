import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PDAPrefix {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();//throw away number of lines
    PDA m = PDAParser.parse(scanner);
    System.out.println("Case #1:\n" + pdaPrefix(m));
    scanner.close();
  }

  public static PDA pdaPrefix(PDA m) {
    return null;
  }
}
