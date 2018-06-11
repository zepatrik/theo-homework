import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Grammar {
  private static final Pattern TRANSITION_PATTERN = Pattern.compile(" -> *");
  private static final Pattern PRODUCTION_PATTERN = Pattern.compile(" +");

  public final Set<Terminal> alphabet;
  public final Set<NonTerminal> nonTerminals;
  public final Set<Production> productions;
  public final NonTerminal startSymbol;

  public Grammar(Set<Terminal> alphabet, Set<NonTerminal> nonTerminals, Set<Production> productions,
      NonTerminal startSymbol) {
    this.alphabet = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(alphabet)));
    this.nonTerminals =
        Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(nonTerminals)));
    this.productions =
        Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(productions)));
    this.startSymbol = startSymbol;
    checkValidGrammar();
  }

  static Grammar parse(Scanner scanner) {
    // First line
    String first = scanner.nextLine().trim();
    if (!first.equals("Grammar")) {
      throw new IllegalArgumentException("Parsed grammar does not start with 'Grammar'.");
    }

    // Second line: Non-Terminals
    String nonTerminalsString = scanner.nextLine();
    if (!nonTerminalsString.startsWith("Nonterminals:")) {
      throw new IllegalArgumentException("Parsed grammar does not declare Nonterminals first.");
    }
    nonTerminalsString = nonTerminalsString.substring("Nonterminals:".length()).trim();

    Set<NonTerminal> nonTerminals = new HashSet<>(NonTerminal.of(nonTerminalsString.split(",")));

    //Third line: Alphabet
    String alphabetString = scanner.nextLine();
    if (!alphabetString.startsWith("Alphabet:")) {
      throw new IllegalArgumentException("Parsed grammar does not declare Alphabet second.");
    }
    alphabetString = alphabetString.substring("Alphabet:".length()).trim();

    Set<Character> alphabetLetters = new HashSet<>();
    for (String terminal : alphabetString.split(",")) {
      if (terminal.length() == 1) {
        alphabetLetters.add(terminal.charAt(0));
      } else {
        throw new IllegalArgumentException("Alphabet has to be input as a comma separated list "
            + "without spaces. Terminals may only be chars.");
      }
    }

    //Fourth line; Startsymbol
    String initialName = scanner.nextLine();
    if (!initialName.startsWith("Startsymbol:")) {
      throw new IllegalArgumentException("Parsed grammar does not declare start symbol third.");
    }
    initialName = initialName.substring("Startsymbol:".length()).trim();
    NonTerminal startSymbol = new NonTerminal(initialName);

    // Productions
    if (!scanner.nextLine().equals("Productions:")) {
      throw new IllegalArgumentException("Parsed grammar does not declare productions");
    }

    Set<Production> productions = new HashSet<>();
    String production;
    while (!(production = scanner.nextLine()).equals("END")) {
      if (!production.contains(" ->")) {
        throw new IllegalArgumentException("Production " + production + " does not contain ' ->'");
      }
      String[] split = TRANSITION_PATTERN.split(production);
      if (split.length > 2) {
        throw new IllegalArgumentException("Invalid production " + production);
      }

      List<NonTerminal> left = NonTerminal.of(split[0].split(" "));

      if (split.length == 1) { // "A -> "; empty production.
        productions.add(new Production(left, Collections.emptyList()));
      } else {
        String[] right = split[1].split("\\|");
        for (String r : right) {
          r = r.trim();
          String[] rightSideProductions = PRODUCTION_PATTERN.split(r);
          List<Atom> atoms = new ArrayList<>(rightSideProductions.length);
          for (String atom : rightSideProductions) {
            atom = atom.trim();
            if (atom.isEmpty()) {
              atoms.add(new Terminal());
            } else if (atom.length() == 1 && alphabetLetters.contains(atom.charAt(0))) {
              atoms.add(new Terminal(atom.charAt(0)));
            } else {
              atoms.add(new NonTerminal(atom));
            }
          }
          productions.add(new Production(left, atoms));
        }
        if (split[1].startsWith("|") || split[1].endsWith("|")) {
          productions.add(new Production(left, new Terminal()));
        }
      }
    }

    Set<Terminal> alphabet = alphabetLetters.stream()
        .map(Terminal::new)
        .collect(Collectors.toSet());
    return new Grammar(alphabet, nonTerminals, productions, startSymbol);
  }

  @Override
  public String toString() {
    String productionsString = productions.isEmpty()
        ? "" : String.join("\n", Util.toString(new TreeSet<>(productions))) + "\n";
    return "Grammar\n"
        + "Nonterminals: " + String.join(",", Util.toString(new TreeSet<>(nonTerminals))) + "\n"
        + "Alphabet: " + String.join(",", Util.toString(new TreeSet<>(alphabet))) + "\n"
        + "Startsymbol: " + startSymbol + "\n"
        + "Productions:\n"
        + productionsString
        + "END";
  }

  private void checkValidGrammar() {
    for (Terminal t : alphabet) {
      for (NonTerminal nonTerminal : nonTerminals) {
        if (String.valueOf(t.letter()).equals(nonTerminal.name)) {
          throw new IllegalArgumentException(
              "Terminals and non-terminals must be disjoint! " + t + " is part of both.");
        }
      }
    }
    if (!nonTerminals.contains(startSymbol)) {
      throw new IllegalArgumentException(
          "Starting symbol " + startSymbol + " must be part of the non-terminals");
    }

    Set<Atom> atoms = new HashSet<>(nonTerminals);
    atoms.addAll(alphabet);
    atoms.add(new Terminal()); // eps is always valid

    for (Production p : productions) {
      for (Atom atom : p.right) {
        if (!atoms.contains(atom)) {
          throw new IllegalArgumentException(atom + " is not in the grammar");
        }
      }
    }
  }
}
