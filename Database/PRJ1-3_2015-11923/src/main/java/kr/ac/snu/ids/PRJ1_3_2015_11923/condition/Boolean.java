package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;

// TRUE, FALSE, UNKNOWN을 저장할 수 있는 Boolean class이다. and or not 등의 비트연산을 UNKNOWN값을 고려하여 만들었다.
public class Boolean {
    static int TRUE = 1;
    static int FALSE = 0;
    static int UNKNOWN = -1;

    private int value = -1;

    public Boolean(int value) {
        this.value = value;
    }

    public boolean isTrue(){
        return value == 1;
    }

    public void and(Boolean operandBoolean){
        if(!(this.value == UNKNOWN && operandBoolean.value == UNKNOWN)) {
            this.value *= operandBoolean.value;
        }
    }

    public void or(Boolean operandBoolean){
        if(this.value==TRUE || operandBoolean.value==TRUE){
            this.value = TRUE;
        }
        else if(this.value==UNKNOWN || operandBoolean.value==UNKNOWN){
            this.value=UNKNOWN;
        }
        else{
            this.value=FALSE;
        }
    }

    public Boolean not(){
        if(this.value==TRUE){
            this.value=FALSE;
        }
        else if(this.value==FALSE){
            this.value=TRUE;
        }
        return this;
    }
}
