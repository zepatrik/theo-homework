import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ToEpsilon {
  private ToEpsilon() {}

  public static void main(String... args) throws IOException {
    try (BufferedReader reader =
             new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
      System.out.println(toEpsilonNFA(TuringMachine.parse(reader)));
    }
  }

  public static <S> EpsilonNFA<ProductState<S>> toEpsilonNFA(TuringMachine<S> tm) {
    // TODO
  }
}
