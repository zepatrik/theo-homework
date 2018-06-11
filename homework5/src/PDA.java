import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PDA {
  private final Set<State> states;
  private final Set<PDATransition> transitions;
  private final Set<Character> alphabet;
  private final State startState;
  private final Set<Character> stackAlphabet;
  private final Character startSymbol;

  public PDA(Set<State> states, Set<PDATransition> transitions, Set<Character> alphabet,
      State startState, Set<Character> stackAlphabet, Character startSymbol) {
    this.states = Collections.unmodifiableSet(Objects.requireNonNull(states));
    this.transitions = Collections.unmodifiableSet(Objects.requireNonNull(transitions));
    this.alphabet = Collections.unmodifiableSet(Objects.requireNonNull(alphabet));
    this.startState = Objects.requireNonNull(startState);
    this.stackAlphabet = Collections.unmodifiableSet(Objects.requireNonNull(stackAlphabet));
    this.startSymbol = Objects.requireNonNull(startSymbol);
    checkValidPDA();
  }

  @Override
  public String toString() {
    Function<Collection<?>, String> toString = list ->
        String.join(";", list.stream().sorted().map(Object::toString).collect(Collectors.toList()));

    StringBuilder trans = new StringBuilder("Transitions:\n");
    for (PDATransition t : new TreeSet<>(transitions)) {
      trans.append(t.toString()).append("\n");
    }

    return "PDA\n"
        + "Alphabet: " + toString.apply(alphabet) + "\n"
        + "States: " + toString.apply(states) + "\n"
        + "Init: " + startState + "\n"
        + "Stackalphabet: " + toString.apply(stackAlphabet) + "\n"
        + "Startsymbol: " + startSymbol + "\n"
        + trans + "END";
  }

  public Set<State> getStates() {
    return states;
  }

  public Set<PDATransition> getTransitions() {
    return transitions;
  }

  public Set<Character> getAlphabet() {
    return alphabet;
  }

  public State getStartState() {
    return startState;
  }

  public Set<Character> getStackAlphabet() {
    return stackAlphabet;
  }

  public Character getStartSymbol() {
    return startSymbol;
  }

  /**
   * Checks whether all parameters given to the constructor are valid.
   * Throw an exception describing the problem if some parameter is invalid.
   */
  private void checkValidPDA() throws IllegalArgumentException {
    if (alphabet.contains(Util.EPSILON)) {
      throw new IllegalArgumentException(
          "The empty char is reserved and may not be part of the alphabet.");
    }
    //startState are in the state set
    if (!states.contains(startState)) {
      throw new IllegalArgumentException("PDA constructor: startState must be element of states.");
    }
    //startSymbol in stackAlphabet
    if (!stackAlphabet.contains(startSymbol)) {
      throw new IllegalArgumentException(
          "PDA constructor: startSymbol must be element of stackAlphabet.");
    }
    //All states have different names
    for (State s1 : states) {
      for (State s2 : states) {
        //If name=="", ignore, since name was not set;
        //Else, check that if the states are different, they also have different names
        if (!s1.getName().equals("") && !s1.equals(s2) && s1.getName().equals(s2.getName())) {
          throw new IllegalArgumentException(
              "The states " + s1 + " and " + s2 + " must have different names or no name at all.");
        }
      }
    }
    //Transitions only use states from the state set and letters from the alphabet
    for (PDATransition t : transitions) {
      if (!states.contains(t.getStart())) {
        throw new IllegalArgumentException(
            "PDA constructor: Transition " + t
                + " starts in a state that is not an element of states");
      }
      if (!states.contains(t.getEnd())) {
        throw new IllegalArgumentException(
            "PDA constructor: Transition " + t
                + " ends in a state that is not an element of states");
      }
      if (!alphabet.contains(t.getLabel()) && t.getLabel() != Util.EPSILON) {
        throw new IllegalArgumentException(
            "PDA constructor: Transition " + t
                + " uses a letter that is not an element of the alphabet");
      }
      if (!stackAlphabet.contains(t.getPopSymbol())) {
        throw new IllegalArgumentException(
            "PDA constructor: Transition " + t
                + "reads a stacksymbol that is not in the stackalphabet.");
      }
      for (int i = 0; i < t.getPushSymbols().length(); i++) {
        if (!stackAlphabet.contains(t.getPushSymbols().charAt(i))) {
          throw new IllegalArgumentException(
              "PDA constructor: Transition " + t + "pushes stacksymbol " + t.getPushSymbols()
                  .charAt(i)
                  + " that is not in the stackalphabet.");
        }
      }
    }
  }
}
