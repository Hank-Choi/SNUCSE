package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.condition.BooleanExpressionList;

public class TableExpression {
    private final ArrayList<ReferedTableName> fromClause;
    private final BooleanExpressionList whereClause;

    public TableExpression(ArrayList<ReferedTableName> fromClause, BooleanExpressionList whereClause) {
        this.fromClause = fromClause;
        this.whereClause = whereClause;
    }

    public ArrayList<ReferedTableName> getFromClause() {
        return fromClause;
    }

    public BooleanExpressionList getWhereClause() {
        return whereClause;
    }
}
