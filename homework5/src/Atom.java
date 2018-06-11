public abstract class Atom implements Comparable<Atom> {
  public abstract boolean isTerminal();

  /**
   * Returns the letter of this atom if it is a {@link Terminal}.
   *
   * @throws IllegalStateException if this is not a terminal or epsilon.
   */
  public abstract char letter();

  /**
   * Returns the name of this atom if its a {@link NonTerminal}
   *
   * @throws IllegalStateException if this is not a non-terminal.
   */
  public abstract String name();

  @Override
  public int compareTo(Atom o) {
    return toString().compareTo(o.toString());
  }

  @Override
  public abstract String toString();
}
