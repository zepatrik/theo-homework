import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Rectangle {
  private static final TuringMachine.Direction LEFT = TuringMachine.Direction.L;
  private static final TuringMachine.Direction RIGHT = TuringMachine.Direction.R;
  private static final char EMPTY = TuringMachine.EMPTY_LETTER;

  private Rectangle() {}

  public static void main(String[] args) {
    TuringMachine<?> tm = rectangleMachine();
    System.out.println(tm);
  }

  public static TuringMachine<?> rectangleMachine() {
    Set<Character> alphabet = new HashSet<>(Arrays.asList('^', 'v', '>', '<')); //this is the tape-alphabet, so you may add more 
    TuringMachine<String> turingMachine = new TuringMachine<>(alphabet, "init");

    // Add transitions, final states, etc. here

    return turingMachine;
  }
}
