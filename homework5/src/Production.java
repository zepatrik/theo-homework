import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Production implements Comparable<Production> {
  public final List<NonTerminal> left;
  public final List<Atom> right;

  Production(NonTerminal left, Atom right) {
    this(Collections.singletonList(left), Collections.singletonList(right));
  }

  Production(NonTerminal left, Atom... right) {
    this(Collections.singletonList(left), Arrays.asList(right));
  }

  Production(List<NonTerminal> left, Atom right) {
    this(left, Collections.singletonList(right));
  }

  Production(NonTerminal left, List<Atom> right) {
    this(Collections.singletonList(left), right);
  }

  Production(List<NonTerminal> left, List<? extends Atom> right) {
    if (left.isEmpty()) {
      throw new IllegalArgumentException("Empty production");
    }
    this.left = left.size() == 1
        ? Collections.singletonList(left.get(0))
        : Collections.unmodifiableList(new ArrayList<>(left));
    if (right.isEmpty()) {
      this.right = Collections.singletonList(new Terminal());
    } else if (right.size() == 1) {
      this.right = Collections.singletonList(right.get(0));
    } else {
      this.right = Collections.unmodifiableList(new ArrayList<>(right));
    }
  }

  @Override
  public String toString() {
    //do not print the greek letter epsilon for empty right side
    return String.join(" ", Util.toString(left)) + " -> "
        + (right.isEmpty() ? "" : String.join(" ", Util.toString(right)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Production)) {
      return false;
    }
    Production that = (Production) o;
    return Objects.equals(left, that.left) && Objects.equals(right, that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  public NonTerminal onlyLeft() {
    if (left.size() != 1) {
      throw new IllegalStateException("Expected only one left production, got " + left);
    }
    return left.get(0);
  }

  public Atom onlyRight() {
    if (right.size() != 1) {
      throw new IllegalStateException("Expected only one right production, got " + right);
    }
    return right.get(0);
  }

  public boolean isEpsilonProducing() {
    return right.size() == 1 && right.get(0) instanceof Terminal &&
        ((Terminal) right.get(0)).isEpsilon();
  }

  @Override
  public int compareTo(Production o) {
    int compare = compareLexicographic(left.iterator(), o.left.iterator());
    return compare != 0 ? compare : compareLexicographic(right.iterator(), o.right.iterator());
  }

  private static <T extends Comparable<? super T>> int
  compareLexicographic(Iterator<? extends T> left, Iterator<? extends T> right) {
    while (left.hasNext() && right.hasNext()) {
      int compare = left.next().compareTo(right.next());
      if (compare != 0) {
        return compare;
      }
    }
    if (left.hasNext()) {
      return 1;
    }
    if (right.hasNext()) {
      return -1;
    }
    return 0;
  }
}
