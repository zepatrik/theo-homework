import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EpsilonNFA<S> {
  private final Set<Character> alphabet;
  private final Map<S, Set<S>> epsilonSuccessors = new HashMap<>();
  private final Map<S, Map<Character, Set<S>>> successors = new HashMap<>();
  private final Set<S> finalStates = new HashSet<>();
  private final S initialState;

  public EpsilonNFA(Set<Character> alphabet, S initialState) {
    this.alphabet = Collections.unmodifiableSet(new HashSet<>(alphabet));
    this.initialState = initialState;
  }

  private void checkValid(char letter) {
    if (!alphabet.contains(letter)) {
      throw new IllegalArgumentException("Invalid letter " + letter + ", alphabet is " + alphabet);
    }
  }

  public void addState(S state) {
    successors.computeIfAbsent(state, k -> new HashMap<>());
  }

  public Set<S> getStates() {
    Set<S> states = new HashSet<>(successors.keySet());
    states.addAll(epsilonSuccessors.keySet());
    successors.values().forEach(map -> map.values().forEach(states::addAll));
    epsilonSuccessors.values().forEach(states::addAll);
    return states;
  }

  public void addTransition(S state, char letter, S successor) {
    checkValid(letter);
    successors.computeIfAbsent(Objects.requireNonNull(state), k -> new HashMap<>())
        .computeIfAbsent(letter, k -> new HashSet<>())
        .add(Objects.requireNonNull(successor));
  }

  public void removeTransition(S state, char letter, S successor) {
    successors.getOrDefault(state, Collections.emptyMap())
        .getOrDefault(letter, Collections.emptySet()).remove(successor);
  }

  public void removeTransitions(S state, char letter) {
    checkValid(letter);
    successors.getOrDefault(state, Collections.emptyMap()).remove(letter);
  }

  public Set<S> getSuccessors(S state, char letter) {
    return Collections.unmodifiableSet(successors.getOrDefault(state, Collections.emptyMap())
        .getOrDefault(letter, Collections.emptySet()));
  }

  public void addEpsilonTransition(S state, S successor) {
    epsilonSuccessors.computeIfAbsent(state, k -> new HashSet<>()).add(successor);
  }

  public void removeEpsilonTransition(S state, S successor) {
    epsilonSuccessors.getOrDefault(state, Collections.emptySet()).remove(successor);
  }

  public Set<S> getEpsilonSuccessors(S state) {
    return Collections.unmodifiableSet(epsilonSuccessors.getOrDefault(state,
        Collections.emptySet()));
  }


  public void addFinalState(S state) {
    finalStates.add(state);
  }

  public void removeFinalState(S state) {
    finalStates.remove(state);
  }

  @Override
  public String toString() {
    Set<S> states = getStates();

    List<String> transitionStrings = new ArrayList<>(successors.size() * alphabet.size());

    successors.forEach((state, map) -> map.forEach((letter, successors) ->
        successors.forEach(successor -> transitionStrings.add(state + ";" + letter + ";"
            + successor))));
    epsilonSuccessors.forEach((state, successors) -> successors.forEach(successor ->
        transitionStrings.add(state + ";;" + successor)));
    Collections.sort(transitionStrings);
    String transitions = String.join("\n", transitionStrings);

    return "EpsilonNFA\n"
        + "Alphabet: " + String.join(";", alphabet.stream().sorted()
        .map(Object::toString).collect(Collectors.toList())) + "\n"
        + "States: " + String.join(";", states.stream().map(Object::toString).sorted()
        .collect(Collectors.toList())) + "\n"
        + "Init: " + initialState + "\n"
        + "Final: " + String.join(";", finalStates.stream().map(Object::toString).sorted()
        .collect(Collectors.toList())) + "\n"
        + "Transitions:\n"
        + transitions + "\n"
        + "END";
  }
}
