import java.util.Objects;

public class State implements Comparable<State> {
  public final String name;

  public State(String name) {
    if (name == null || name.contains(";")) {
      throw new IllegalArgumentException("Invalid name " + name);
    }
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(State s) {
    return toString().compareTo(s.toString());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof State)) {
      return false;
    }
    State state = (State) o;
    return Objects.equals(name, state.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode() * 31;
  }
}
