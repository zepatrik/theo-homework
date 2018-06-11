import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CYKTable {
  /*
   * Internal layout: table[r][c] -> V_ij
   * r
   * 3 14
   * 2 13 24
   * 1 12 23 34
   * 0 11 22 33 44
   *    0  1  2  3 c
   */
  private final Set[][] table;
  private final int size;

  public CYKTable(int size) {
    if (size == 0) {
      throw new IllegalArgumentException("Zero size");
    }
    this.size = size;
    this.table = new Set[size][];
    for (int rowIndex = 0; rowIndex < size; rowIndex++) {
      Set[] row = new Set[size - rowIndex];
      Arrays.setAll(row, k -> new HashSet());
      table[rowIndex] = row;
    }
  }

  private void checkIndex(int i, int j) {
    if (i <= 0 || size < i) {
      throw new IllegalArgumentException("Invalid index i " + i);
    }
    if (j < i || size < j) {
      throw new IllegalArgumentException("Invalid index j " + j);
    }
  }

  Set<NonTerminal> get(int i, int j) {
    checkIndex(i, j);
    Set<?> cell = table[j - i][i - 1];
    return (Set<NonTerminal>) cell;
  }

  void set(int i, int j, Set<NonTerminal> nonTerminals) {
    checkIndex(i, j);
    table[j - i][i - 1] = new HashSet(Objects.requireNonNull(nonTerminals));
  }

  void add(int i, int j, NonTerminal nonTerminal) {
    checkIndex(i, j);
    table[j - i][i - 1].add(nonTerminal);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(size * size);
    for (int rowIndex = size - 1; rowIndex >= 0; rowIndex--) {
      Set[] row = table[rowIndex];
      assert row.length == size - rowIndex;
      for (Set cell : row) {
        String rowString = String.join(",", Util.toString(new TreeSet<>(cell)));
        builder.append("{").append(rowString).append("}");
      }
      if (rowIndex > 0) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }
}
