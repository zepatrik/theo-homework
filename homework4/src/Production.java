public class Production {
    final String left;
    final String right;

    Production(String left, String right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (right.isEmpty()) {
            return left + " \u2192 " + "\u03B5"; //print the greek letter epsilon for empty right side
        } else {
            return left + " \u2192 " + right;
        }
    }
}
