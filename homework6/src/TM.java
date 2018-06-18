import java.util.Arrays;
import java.util.HashSet;

public class TM {
  public static void main(String... args) {
    TuringMachine<String> tm = new TuringMachine<>(
        new HashSet<>(Arrays.asList('0', '1')), "init");
    tm.addTransition("init", '0', "flip", '1', TuringMachine.Direction.R);
    tm.addScan("init", '1', TuringMachine.Direction.R);
    tm.addTransition("flip", '1', "init", '0', TuringMachine.Direction.L);
    tm.addScan("flip", '0', TuringMachine.Direction.L);
    tm.addTransition("init", TuringMachine.EMPTY_LETTER, "fin", '1', TuringMachine.Direction.R);
    tm.addFinalState("fin");
    System.out.println(tm);
  }
}
