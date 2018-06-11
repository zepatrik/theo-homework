import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Util {
  public static final char EPSILON = '\u03B5';

  private Util() {}

  public static List<String> toString(Collection<?> list) {
    return list.stream().map(Object::toString).collect(Collectors.toList());
  }

  public static String cnfLiteralName(Atom terminal) {
    return "Y_" + terminal.letter();
  }

  public static String cnfChainName(List<Atom> remaining) {
    return "X_[" + String.join(";", Util.toString(remaining)) + "]";
  }
}
