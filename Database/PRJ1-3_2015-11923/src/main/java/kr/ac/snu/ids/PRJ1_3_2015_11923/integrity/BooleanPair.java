package kr.ac.snu.ids.PRJ1_3_2015_11923.integrity;

public class BooleanPair {
    private boolean allNullable;
    private boolean referenced;

    public BooleanPair(boolean allNullable, boolean referenced) {
        this.allNullable = allNullable;
        this.referenced = referenced;
    }

    public boolean isAllNullable() {
        return allNullable;
    }

    public boolean isReferenced() {
        return referenced;
    }
}
