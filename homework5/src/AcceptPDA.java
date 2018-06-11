import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public final class AcceptPDA {
  private AcceptPDA() {}

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    PDA m = PDAParser.parse(scanner);
    String word = scanner.nextLine();
    while (!word.equals("DONE")) {
      System.out.println(word + ": " + accept(m, word));
      word = scanner.nextLine();
    }
    scanner.close();
  }

  public static boolean accept(PDA m, String w) {
    return false;
  }

  public static Set<PDATransition> getAvailableTrans(PDA m, State s, char sym) {
    Set<PDATransition> result = new HashSet<>();
    for (PDATransition t : m.getTransitions()) {
      if (t.getStart().equals(s) && t.getPopSymbol().equals(sym)) {
        result.add(t);
      }
    }
    return result;
  }
}
