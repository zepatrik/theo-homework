import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class Simulate {
  private Simulate() {}

  public static void main(String... args) throws IOException {
    try (BufferedReader reader =
             new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
      TuringMachine<String> tm = TuringMachine.parse(reader);
      String line;
      while ((line = reader.readLine()) != null) {
        String[] split = line.split(";");
        System.out.println(simulate(tm, split[0], Integer.parseInt(split[1])));
      }
    }
  }

  public static <S> Result simulate(TuringMachine<S> tm, String word, int bound) {
    return null; // TODO
  }

  public enum Result {
    RUNNING, STUCK, ACCEPTED
  }
}
