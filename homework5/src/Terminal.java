public final class Terminal extends Atom {
  private final char letter;

  public Terminal(char letter) {
    if (letter == ' ') {
      throw new IllegalArgumentException("Space not allowed as letter");
    }
    this.letter = letter;
  }

  public Terminal() {
    this.letter = Util.EPSILON;
  }

  @Override
  public boolean isTerminal() {
    return true;
  }

  public boolean isEpsilon() {
    return letter == Util.EPSILON;
  }

  @Override
  public char letter() {
    if (letter == Util.EPSILON) {
      throw new IllegalStateException("Epsilon");
    }
    return letter;
  }

  @Override
  public String name() {
    throw new IllegalStateException();
  }

  @Override
  public String toString() {
    return isEpsilon() ? "" : String.valueOf(letter);
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Terminal)) {
      return false;
    }
    Terminal terminal = (Terminal) o;
    return letter == terminal.letter;
  }

  @Override
  public int hashCode() {
    return Character.hashCode(letter) * 31;
  }
}
