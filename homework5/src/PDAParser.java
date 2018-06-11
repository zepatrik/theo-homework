import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public final class PDAParser {
  private PDAParser() {}

  static PDA parse(Scanner scanner) {
    /*
     * Sample input:
     *
     * PDA
     * Alphabet: a;b;c
     * States: q;p;r
     * Init: q
     * Stackalphabet: A;B;C
     * Startsymbol: A
     * Transitions:
     * q;a;A;p;AA
     * p;;B;p;
     * END
     */

    //First line
    String first = scanner.nextLine();
    if (!first.equals("PDA")) {
      throw new IllegalArgumentException("Parsed automaton does not start with PDA.");
    }

    //Second line; Alphabet
    String second = scanner.nextLine();
    if (!second.startsWith("Alphabet: ")) {
      throw new IllegalArgumentException("Parsed automaton does not declare alphabet first.");
    }
    second = second.substring("Alphabet: ".length());

    Set<Character> alphabet = new HashSet<>();
    for (String letter : second.split(";")) {
      if (letter.length() == 1) {
        alphabet.add(letter.charAt(0));
      } else {
        throw new IllegalArgumentException(
            "Letters have to be input as a semicolon separated list without spaces. Letters may "
                + "only be chars.");
      }
    }

    //Third line; States
    String third = scanner.nextLine();
    if (!third.startsWith("States: ")) {
      throw new IllegalArgumentException("Parsed automaton does not declare states second.\"");
    }
    third = third.substring("States: ".length());

    Map<String, State> states = new HashMap<>();
    for (String stateName : third.split(";")) {
      states.put(stateName, new State(stateName));
    }

    //Fourth line; initialstate
    String fourth = scanner.nextLine();
    if (!fourth.startsWith("Init: ")) {
      throw new IllegalArgumentException("Parsed automaton does not declare initial state third.");
    }
    fourth = fourth.substring("Init: ".length());

    //fifth line; stackalphabet
    String fifth = scanner.nextLine();
    if (!fifth.startsWith("Stackalphabet: ")) {
      throw new IllegalArgumentException("Parsed automaton does not declare stackalphabet fourth.");
    }
    fifth = fifth.substring("Stackalphabet: ".length());

    Set<Character> stackAlphabet = new HashSet<>();
    for (String letter : fifth.split(";")) {
      if (letter.length() == 1) {
        stackAlphabet.add(letter.charAt(0));
      } else {
        throw new IllegalArgumentException("StackSymbols have to be input as a semicolon "
            + "separated list without spaces. StackSymbols may only be chars.");
      }
    }

    //Sixth line; startsymbol
    String sixth = scanner.nextLine();
    if (!sixth.startsWith("Startsymbol: ")) {
      throw new IllegalArgumentException("Parsed automaton does not declare startsymbol fifth.");
    }
    sixth = sixth.substring("Startsymbol: ".length());
    if (sixth.length() != 1) {
      throw new IllegalArgumentException("Startsymbol must be a char, but has length != 1");
    }
    char startSym = sixth.charAt(0);

    //Seventh line; transitions
    String seventh = scanner.nextLine();
    if (!seventh.equals("Transitions:")) {
      throw new IllegalArgumentException("Parsed automaton does not declare transitions sixth.");
    }
    Set<PDATransition> transitions = new HashSet<>();
    String transition;
    while (!(transition = scanner.nextLine()).equals("END")) {
      String[] split = transition.split(";");
      if (split.length < 4 || split.length > 5) {
        throw new IllegalArgumentException("Invalid transition " + transition);
      }
      String start = split[0];
      String letter = split[1];
      String popSymbol = split[2];
      String end = split[3];
      String pushSymbols;
      if (split.length == 5) {
        pushSymbols = split[4];
      } else {
        pushSymbols = "";
      }
      if (letter.length() > 1) {
        throw new IllegalArgumentException("Transition label may only be a char, but it is "
            + letter);
      }
      //checking label in alphabet is done by checkValidEpsNFA
      char label = letter.isEmpty() ? Util.EPSILON : letter.charAt(0);

      State l = states.get(start);
      State r = states.get(end);
      if (r == null && l == null) {
        throw new IllegalArgumentException("The states for a transition are not in the state set ("
            + start + " or " + end + ")");
      }

      char popSym;
      if (popSymbol.length() > 1) {
        throw new IllegalArgumentException(
            "Popped symbol may only be a char, but it is " + popSymbol);
      }
      if (popSymbol.isEmpty()) {
        throw new IllegalArgumentException("Popped symbol must be char, but it is empty word");
      }
      popSym = popSymbol.charAt(0);
      transitions.add(new PDATransition(l, label, popSym, r, pushSymbols));
    }

    State init = states.get(fourth);
    if (init == null) {
      throw new IllegalArgumentException("Initial state is not in state set");
    }

    return new PDA(new HashSet<>(states.values()), transitions, alphabet, init, stackAlphabet,
        startSym);
  }

}
