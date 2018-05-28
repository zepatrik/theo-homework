import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Grammar {
    /**
     * Parses the grammar that is defined in the file f.
     * A file may look like this
     * Grammar
     * Nonterminals: A,B,C
     * Alphabet: a,b,c
     * Startsymbol: A
     * Productions:
     * A -> Aa
     * A -> AB
     * Needs to start with "Grammar"
     * Second line: "Nonterminals: " and then all nonterminals, which can only be chars, in a comma separated list
     * without spaces.
     * Third line: "Alphabet: " and then all terminals, see above.
     * Fourth line: "Startsymbol: " and then the startsymbol
     * Fifth line: "Productions:"
     * Then each production takes a line; left and right side are separated by " -> "; using multiple right sides with
     * "|" is currently not supported.
     */
    static Grammar parse(Scanner scanner) {
        //First line
        String first = scanner.nextLine();
        if (!first.equals("Grammar")) {
            throw new IllegalArgumentException("Parsed grammar does not start with 'Grammar'.");
        }

        //Second line; nonterminals
        String second = scanner.nextLine();
        if (!second.startsWith("Nonterminals: ")) {
            throw new IllegalArgumentException("Parsed grammar does not declare Nonterminals first.");
        }
        Set<Character> nonterminals = new HashSet<>();
        second = second.substring("Nonterminals: ".length());
        for (String nonterminal : second.split(",")) {
            if (nonterminal.length() == 1) {
                nonterminals.add(nonterminal.charAt(0));
            } else {
                throw new IllegalArgumentException(
                        "Nonterminals have to be input as a comma separated list without spaces. Nonterminals may only be chars.");
            }
        }

        //Third line; Alphabet
        String third = scanner.nextLine();
        if (!third.startsWith("Alphabet: ")) {
            throw new IllegalArgumentException("Parsed grammar does not declare Alphabet second.");
        }
        Set<Character> alphabet = new HashSet<>();
        third = third.substring("Alphabet: ".length());
        for (String terminal : third.split(",")) {
            if (terminal.length() == 1) {
                alphabet.add(terminal.charAt(0));
            } else {
                throw new IllegalArgumentException(
                        "Alphabet has to be input as a comma separated list without spaces. Terminals may only be chars.");
            }
        }

        //Fourth line; Startsymbol
        String fourth = scanner.nextLine();
        if (!fourth.startsWith("Startsymbol: ")) {
            throw new IllegalArgumentException("Parsed grammar does not declare start symbol third.");
        }
        fourth = fourth.substring("Startsymbol: ".length());
        char start;
        if (fourth.length() == 1) {
            start = fourth.charAt(0);
        } else {
            throw new IllegalArgumentException("Startsymbol must be a single char.");
        }

        //Fifth line; rules
        String fifth = scanner.nextLine();
        if (!fifth.equals("Productions:")) {
            throw new IllegalArgumentException(
                    "Parsed grammar does not contain the String 'Productions' in the fifth line.");
        }
        Set<Production> productions = new HashSet<>();
        String production;
        while (!(production = scanner.nextLine()).equals("END")) {
            if (!production.contains(" -> ")) {
                throw new IllegalArgumentException("Production " + production + " does not contain ' -> '");
            }
            String[] split = production.split(" -> ");
            String left = split[0];
            if (split.length == 1) { // "A -> "; empty production.
                productions.add(new Production(left, ""));
            } else {
                String[] right = split[1].split("\\|");
                for (String r : right) {
                    productions.add(new Production(left, r));
                }
                int j = split[1].lastIndexOf("|");
                if(split[1].lastIndexOf("|")==split[1].length()-1){
                    productions.add(new Production(left,""));
                }
            }
        }

        return new Grammar(alphabet, nonterminals, productions, start);
    }

    final Set<Character> alphabet;
    final Set<Character> nonTerminals;
    final Set<Production> productions;
    final char startingSymbol;

    public Grammar(Set<Character> alphabet, Set<Character> nonTerminals, Set<Production> productions,
                    char startingSymbol) {
        this.alphabet = Collections.unmodifiableSet(new HashSet<>(alphabet));
        this.nonTerminals = Collections.unmodifiableSet(new HashSet<>(nonTerminals));
        this.productions = Collections.unmodifiableSet(new HashSet<>(productions));
        this.startingSymbol = startingSymbol;
        checkValidGrammar();
    }

    @Override
    public String toString() {
        return "Grammar{" + "alphabet=" + alphabet + ", nonTerminals=" + nonTerminals + ", productions=" + productions
                + ", startingSymbol=" + startingSymbol + '}';
    }

    private void checkValidGrammar() {
        if (alphabet == null) {
            throw new IllegalArgumentException("Grammar constructor: alphabet cannot be null.");
        }
        if (nonTerminals == null) {
            throw new IllegalArgumentException("Grammar constructor: nonTerminals cannot be null.");
        }
        if (productions == null) {
            throw new IllegalArgumentException("Grammar constructor: productions cannot be null.");
        }
        for (Character t : alphabet) {
            if (nonTerminals.contains(t)) {
                throw new IllegalArgumentException("Terminals and nonterminals must be disjoint! " + t + " is part of both.");
            }
        }
        if (!nonTerminals.contains(startingSymbol)) {
            throw new IllegalArgumentException("Starting symbol " + startingSymbol + " must be part of the nonterminals");
        }
        for (Production p : productions) {
            //no need to check that p is not null, because productions is a valid set, and
            // hence only valid productions can be in it
            if (p.left.isEmpty()) {
                throw new IllegalArgumentException("Production left side must be set for production " + p);
            }

            //if right side has length 0, then it is epsilon, which is fine
            char[] letters = new char[p.left.length() + p.right.length()];
            p.left.getChars(0, p.left.length(), letters, 0);
            p.right.getChars(0, p.right.length(), letters, p.left.length());
            for (char c : letters) {
                if (!alphabet.contains(c) && !nonTerminals.contains(c)) {
                    throw new IllegalArgumentException(
                            "Letter " + c + " in production " + p + " is neither a terminal nor a nonterminal");
                }
            }
        }
    }
}
