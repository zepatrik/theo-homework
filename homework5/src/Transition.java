import java.util.Comparator;
import java.util.Objects;

public class Transition implements Comparable<Transition> {
  public static final Comparator<Transition> COMPARATOR =
      Comparator.<Transition, State>comparing(t -> t.start)
          .thenComparingInt(t -> t.label)
          .thenComparing(t -> t.end);

  public final State start;
  public final State end;
  public final char label;

  public Transition(State start, State end, char label) {
    this.start = start;
    this.end = end;
    this.label = label;
  }

  public State getStart() {
    return start;
  }

  public State getEnd() {
    return end;
  }

  public char getLabel() {
    return label;
  }

  @Override
  public String toString() {
    String letter = label == Util.EPSILON ? "" : String.valueOf(label);
    return start + ";" + letter + ";" + end;
  }

  public String toStringWithoutEpsilon() {
    return start + ";" + label + ";" + end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transition that = (Transition) o;
    return Objects.equals(start, that.start) && Objects.equals(end, that.end)
        && Objects.equals(label, that.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, label);
  }

  @Override
  public int compareTo(Transition t) {
    return COMPARATOR.compare(this, t);
  }
}
