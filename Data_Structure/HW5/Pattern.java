package src;

public class Pattern implements Comparable<Pattern> {
    private final String patternString;
    private int line;
    private int row;

    Pattern(String patternString) {
        this.patternString = patternString;
    }

    Pattern(String patternString, int line, int row) {
        this.patternString = patternString;
        this.line = line;
        this.row = row;
    }

    public int getLine() {
        return line;
    }

    public int getRow() {
        return row;
    }

    public String getIndex() {
        return "(" + line + ", " + row + ")";
    }

    @Override
    public int compareTo(Pattern pattern) {
        return this.patternString.compareTo(pattern.patternString);
    }

    @Override
    public String toString() {
        return patternString;
    }
}
