import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PrimeTM {
  private static final TuringMachine.Direction LEFT = TuringMachine.Direction.L;
  private static final TuringMachine.Direction RIGHT = TuringMachine.Direction.R;
  private static final char EMPTY = TuringMachine.EMPTY_LETTER;

  private PrimeTM() {}

  public static void main(String... args) {
    TuringMachine<?> tm = primeMachine();
    System.out.println(tm);
  }

  public static TuringMachine<?> primeMachine() {
    Set<Character> alphabet = new HashSet<>(Arrays.asList('a')); // TODO Adapt?
    TuringMachine<String> turingMachine = new TuringMachine<>(alphabet, "init");

    // Add transitions, final states, etc. here

    return turingMachine;
  }
}
