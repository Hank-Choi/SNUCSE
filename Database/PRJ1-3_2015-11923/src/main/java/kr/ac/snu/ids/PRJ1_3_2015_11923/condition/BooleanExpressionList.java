package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

//and로 연결된 BooleanExpression들을 list로 묶고 or로 연결된 해당 리스트들을 또 list로 묶어 booleanTerms로 갖는다.
public class BooleanExpressionList implements BooleanExpression {
    ArrayList<ArrayList<BooleanFactor>> booleanTerms;

    public BooleanExpressionList(ArrayList<ArrayList<BooleanFactor>> booleanTerms) {
        this.booleanTerms = booleanTerms;
    }

    //각각의 booleanFactor들을 모두 and연산을 한 후 만들어진 Boolean값 list 들을 모두 or 연산처리한다.
    @Override
    public Boolean checkBooleanValue(ArrayList<String> record, ArrayList<TableColumnTuple> columnDetail) throws Exception {
        Boolean result = new Boolean(Boolean.FALSE);
        for (ArrayList<BooleanFactor> booleanFactors : booleanTerms) {
            Boolean booleanTermResult = new Boolean(Boolean.TRUE);
            for (BooleanFactor booleanFactor : booleanFactors) {
                booleanTermResult.and(booleanFactor.checkBooleanValue(record, columnDetail));
            }
            result.or(booleanTermResult);
        }
        return result;
    }

}
