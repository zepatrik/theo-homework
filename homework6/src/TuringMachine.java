import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TuringMachine<S> {
  public static final char EMPTY_LETTER = '█';

  private final Set<S> states;
  private final Set<Character> alphabet;
  private final Map<S, Map<Character, Transition<S>>> successorFunction;
  private S initialState;
  private final Set<S> finalStates;

  public TuringMachine(Set<Character> alphabet, S initialState) {
    if (alphabet.contains(EMPTY_LETTER)) {
      throw new IllegalArgumentException("Letter " + EMPTY_LETTER + " is reserved");
    }

    this.states = new HashSet<>();
    this.alphabet = Collections.unmodifiableSet(new HashSet<>(alphabet));
    this.successorFunction = new HashMap<>();
    this.initialState = Objects.requireNonNull(initialState);
    this.finalStates = new HashSet<>();

    this.states.add(initialState);
  }

  private void checkValid(char letter) {
    if (letter != EMPTY_LETTER && !alphabet.contains(letter)) {
      throw new IllegalArgumentException("Invalid letter " + letter);
    }
  }

  public void addFinalState(S state) {
    states.add(state);
    finalStates.add(state);
  }

  public void addFinalStates(Collection<? extends S> states) {
    this.states.addAll(states);
    finalStates.addAll(states);
  }

  public void removeStates(Collection<? extends S> states) {
    if (states.contains(initialState)) {
      throw new IllegalArgumentException("Removing initial state");
    }
    successorFunction.keySet().removeAll(states);
    successorFunction.forEach(
        (state, successors) -> successors.values().removeIf(t -> states.contains(t.successor)));
    finalStates.removeAll(states);
    this.states.removeAll(states);
  }

  public void setInitialState(S state) {
    states.add(state);
    initialState = state;
  }

  public void addTransition(S sourceState, char letter, S successor, char replacement,
      Direction direction) {
    checkValid(letter);
    checkValid(replacement);

    states.addAll(Arrays.asList(sourceState, successor));
    successorFunction.computeIfAbsent(sourceState, k -> new HashMap<>())
        .put(letter, new Transition<>(successor, replacement, direction));
  }

  public void addScan(S state, char letter, Direction direction) {
    addTransition(state, letter, state, letter, direction);
  }

  public void removeTransition(S sourceState, char sourceLetter) {
    checkValid(sourceLetter);
    successorFunction.getOrDefault(sourceState, Collections.emptyMap()).remove(sourceLetter);
  }

  public Set<S> getStates() {
    return Collections.unmodifiableSet(states);
  }

  public Set<Character> getAlphabet() {
    return alphabet;
  }

  public S getInitialState() {
    return initialState;
  }

  public Set<S> getFinalStates() {
    return Collections.unmodifiableSet(finalStates);
  }

  public boolean isFinal(S state) {
    return finalStates.contains(state);
  }

  @Override
  public String toString() {
    StringBuilder transitionBuilder = new StringBuilder(successorFunction.size() * 20);
    successorFunction.entrySet().stream()
        .sorted(Comparator.comparing(entry -> entry.getKey().toString()))
        .forEach(entry -> entry.getValue().entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .forEach(transitionEntry -> {
              Transition<S> transition = transitionEntry.getValue();
              transitionBuilder.append(entry.getKey()).append(";")
                  .append(transitionEntry.getKey()).append(";")
                  .append(transition.successor).append(";")
                  .append(transition.letter).append(";")
                  .append(transition.direction).append('\n');
            }));

    return alphabet.stream().map(Object::toString).reduce("", String::concat) + "\n"
        + initialState + "\n"
        + String.join(";", finalStates.stream().map(Object::toString).collect(Collectors.toList()))
        + "\n" + transitionBuilder + "END";
  }

  public String toDot() {
    StringBuilder dotBuilder = new StringBuilder(successorFunction.size() * 20);
    dotBuilder.append("digraph turing {\n")
        .append("  node [fontname = \"Roboto\"];\n") // Nice node font
        .append("  edge [fontname = \"Courier\"];"); // Monospaced edge font

    states.forEach(state -> dotBuilder.append("  ").append("node [shape=")
        .append(finalStates.contains(state) ? "doublecircle" : "circle").append("] \"")
        .append(state).append("\";\n"));

    Map<S, Map<S, Set<String>>> fromToLabels = new HashMap<>();

    successorFunction.forEach((state, successors) -> {
      Map<S, Set<String>> successorLabels = new HashMap<>();
      successors.forEach((letter, transition) ->
          successorLabels.computeIfAbsent(transition.successor, k -> new HashSet<>())
              .add(letter + "/" + transition.letter + "," + transition.direction));
      if (!successorLabels.isEmpty()) {
        fromToLabels.put(state, successorLabels);
      }
    });

    fromToLabels.forEach((state, successors) -> successors.forEach((successor, labels) -> {
      dotBuilder.append("  \"").append(state).append("\" -> \"").append(successor)
          .append("\" [label=\"").append(String.join("\\n", labels)).append("\"];\n");
    }));

    dotBuilder.append("}");
    return dotBuilder.toString();
  }

  public Transition<S> getTransition(S state, char letter) {
    if (!states.contains(state)) {
      throw new IllegalArgumentException("Unknown state " + state);
    }
    return successorFunction.getOrDefault(state, Collections.emptyMap()).get(letter);
  }

  private static char toChar(String str) {
    if (str.length() != 1) {
      throw new IllegalArgumentException();
    }
    return str.charAt(0);
  }

  public static TuringMachine<String> parse(BufferedReader reader) throws IOException {
    String alphabetLine = reader.readLine();
    char[] alphabetChars = alphabetLine.toCharArray();
    Set<Character> alphabet = new HashSet<>();
    for (char chr : alphabetChars) {
      alphabet.add(chr);
    }

    String initial = reader.readLine();
    String[] finalStates = reader.readLine().split(";");

    TuringMachine<String> tm = new TuringMachine<>(alphabet, initial);
    tm.addFinalStates(Arrays.asList(finalStates));

    String line;
    while (!Objects.equals(line = reader.readLine(), "END")) {
      String[] split = line.split(";");
      if (split.length != 5) {
        throw new IllegalArgumentException("Invalid transition " + line);
      }

      String source = split[0];
      char sourceLetter = toChar(split[1]);
      String successor = split[2];
      char replacement = toChar(split[3]);
      Direction direction = Direction.valueOf(split[4]);

      tm.addTransition(source, sourceLetter, successor, replacement, direction);
    }

    return tm;
  }

  public enum Direction {
    L, R, N
  }

  public static final class Transition<S> {
    public final S successor;
    public final Character letter;
    public final Direction direction;

    public Transition(S successor, Character letter, Direction direction) {
      this.successor = successor;
      this.letter = letter;
      this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Transition)) {
        return false;
      }
      Transition<?> that = (Transition<?>) o;
      return Objects.equals(successor, that.successor) && Objects.equals(letter, that.letter)
          && direction == that.direction;
    }

    @Override
    public int hashCode() {
      return Objects.hash(successor, letter, direction);
    }

    @Override
    public String toString() {
      return "(" + successor + "," + letter + "," + direction + ")";
    }
  }
}
