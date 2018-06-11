import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class CNF {
  private CNF() {}

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Grammar g = Grammar.parse(scanner);
    scanner.close();

    Grammar cnf = cnf(g);
    System.out.println(cnf);
  }

  public static Grammar cnf(Grammar g) {
    return null;
  }
}
