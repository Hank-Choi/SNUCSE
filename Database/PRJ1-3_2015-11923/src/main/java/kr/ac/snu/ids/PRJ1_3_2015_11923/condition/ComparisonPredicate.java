package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

public class ComparisonPredicate extends Predicate {
    private Operand operand2;
    private String operator;

    public ComparisonPredicate(Operand operand1, Operand operand2, String operator) {
        super(operand1);
        this.operand2 = operand2;
        this.operator = operator;
    }


    @Override
    public Boolean checkBooleanValue(ArrayList<String> record, ArrayList<TableColumnTuple> columnDetail) throws Exception {
        Operand operand1 = getOperand1();
        if (operand1.getDataType(columnDetail).equals(operand2.getDataType(columnDetail))) {
            String operandValue1 = operand1.getValue(record, columnDetail);
            String operandValue2 = operand2.getValue(record, columnDetail);
            int compareValue;
            if (operandValue1 == null || operandValue2 == null) {
                return new Boolean(Boolean.UNKNOWN);
            }
            if ("int".equals(operand1.getDataType(columnDetail))) {
                Integer intOperand1 = Integer.parseInt(operandValue1);
                Integer intOperand2 = Integer.parseInt(operandValue2);
                compareValue = intOperand1.compareTo(intOperand2);
            } else {
                compareValue = operandValue1.compareTo(operandValue2);
            }
            boolean booleanValue=false;
            switch (operator) {
                case ">":
                    booleanValue = compareValue>0;
                    break;
                case "<":
                    booleanValue = compareValue<0;
                    break;
                case ">=":
                    booleanValue = compareValue>=0;
                    break;
                case "<=":
                    booleanValue = compareValue<=0;
                    break;
                case "!=":
                    booleanValue = compareValue!=0;
                    break;
                case "=":
                    booleanValue = compareValue==0;
                    break;
            }
            if(booleanValue){
                return new Boolean(Boolean.TRUE);
            }
            else{
                return new Boolean(Boolean.FALSE);
            }
        }
        else{
            throw new Exception("Where clause try to compare incomparable values");
        }
    }

}
