package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

public class BooleanFactor implements BooleanExpression {
    private BooleanExpression booleanExpression;
    private boolean isNot;

    public BooleanFactor(boolean isNot,BooleanExpression booleanExpression){
        this.booleanExpression=booleanExpression;
        this.isNot=isNot;
    }

    @Override
    public Boolean checkBooleanValue(ArrayList<String> record, ArrayList<TableColumnTuple> columnDetail) throws Exception {
        if(isNot) {
            return booleanExpression.checkBooleanValue(record, columnDetail).not();
        }
        else
            return booleanExpression.checkBooleanValue(record,columnDetail);
    }
}
