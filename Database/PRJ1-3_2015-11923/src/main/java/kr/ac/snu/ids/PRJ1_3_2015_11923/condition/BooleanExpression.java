package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

//Boolean 값을 평가하는 interface
public interface BooleanExpression {

    public Boolean checkBooleanValue(ArrayList<String> record, ArrayList<TableColumnTuple> columnDetail) throws Exception;

}
