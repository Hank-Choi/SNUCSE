package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

public class NullPredicate extends Predicate {
    private boolean nullCheck;

    public NullPredicate(Operand operand1,boolean nullCheck) {
        super(operand1);
        this.nullCheck=nullCheck;
    }

    @Override
    public Boolean checkBooleanValue(ArrayList<String> record, ArrayList<TableColumnTuple> columnDetail) throws Exception {
        {
            Operand operand1 = getOperand1();
            String operandValue1 = operand1.getValue(record, columnDetail);
            if(operandValue1!=null ^ nullCheck){
                return new Boolean(Boolean.TRUE);
            }
            else{
                return new Boolean(Boolean.FALSE);
            }
        }
    }
}
