package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

public abstract class Predicate implements BooleanExpression {
    private Operand operand1;

    public Operand getOperand1() {
        return operand1;
    }

    public void setOperand1(Operand operand1) {
        this.operand1 = operand1;
    }

    public Predicate(Operand operand1){
        this.operand1=operand1;
    }
}
