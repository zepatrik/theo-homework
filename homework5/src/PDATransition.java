import java.util.Objects;

public class PDATransition extends Transition {
  public final Character popSymbol;
  public final String pushSymbols;

  public PDATransition(State start, char label, Character popSymbol, State end,
      String pushSymbols) {
    super(start, end, label);
    this.popSymbol = popSymbol;
    this.pushSymbols = pushSymbols;
  }

  public Character getPopSymbol() {
    return popSymbol;
  }

  public String getPushSymbols() {
    return pushSymbols;
  }

  @Override
  public String toString() {
    String letter = label == Util.EPSILON ? "" : label + "";
    return start + ";" + letter + ";" + popSymbol + ";" + end + ";" + pushSymbols;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PDATransition that = (PDATransition) o;
    return Objects.equals(start, that.start)
        && Objects.equals(end, that.end)
        && Objects.equals(label, that.label)
        && Objects.equals(popSymbol, that.popSymbol)
        && Objects.equals(pushSymbols, that.pushSymbols);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, label, popSymbol, pushSymbols);
  }

  public int compareTo(Transition t){
    return this.toString().compareTo(t.toString());
  }
}
