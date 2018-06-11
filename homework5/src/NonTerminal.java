import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class NonTerminal extends Atom {
  public static List<NonTerminal> of(String... names) {
    return Arrays.stream(names).map(NonTerminal::new).collect(Collectors.toList());
  }

  public final String name;

  public NonTerminal(String name) {
    if (name == null || name.isEmpty() || name.contains(",") || name.contains("|")
        || name.contains(" ")) {
      throw new IllegalArgumentException("Illegal name " + Objects.toString(name, "null"));
    }
    this.name = name;
  }

  @Override
  public boolean isTerminal() {
    return false;
  }

  @Override
  public char letter() {
    throw new IllegalStateException();
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NonTerminal)) {
      return false;
    }
    NonTerminal that = (NonTerminal) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode() * 31;
  }

  public static NonTerminal unique(String name, Set<NonTerminal> existing) {
    NonTerminal result = new NonTerminal(name);
    if (!existing.contains(result)) {
      return result;
    }
    int suffix = 0;
    while (existing.contains((result = new NonTerminal(name + "_" + suffix)))) {
      suffix++;
    }
    return result;
  }
}
