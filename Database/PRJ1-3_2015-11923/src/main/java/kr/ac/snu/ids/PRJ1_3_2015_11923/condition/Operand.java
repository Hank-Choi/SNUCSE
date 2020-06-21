package kr.ac.snu.ids.PRJ1_3_2015_11923.condition;


import kr.ac.snu.ids.PRJ1_3_2015_11923.SimpleDBMSParser;
import kr.ac.snu.ids.PRJ1_3_2015_11923.table.TableColumnTuple;

import java.util.ArrayList;

//Operand의 value 혹은 column의 위치를 나타내는 클래스 
//value 로 초기화 혹은 tableName,columnName으로 초기화한다.
//column을 참조할 때 getDataType 혹은 getValue 를 처음 호출할 때 columnName과 tableName을 통해 record에서의 해당 column index를 저장,
//이후에 getDataType 혹은 getValue를 호출하면 index를 통해 value를 반환한다.
public class Operand {
    private String value=null;
    private String dataType=null;
    private int index=-1;
    private boolean isValue=false;
    private String columnName=null;
    private String tableName=null;
    private boolean initialized = false;

    public Operand(){
    }


    public Operand(String value, String dataType) {
        this.value = value;
        this.dataType=dataType;
        this.isValue=true;
    }

    public void setColumn(String columnName,String tableName) {
        this.columnName = columnName;
        this.tableName=tableName;
    }

    public String getDataType(ArrayList<TableColumnTuple> columns) throws Exception {
        if(isValue) {
            return dataType;
        }
        else if (!initialized) {
            this.index = SimpleDBMSParser.getIndexOfSpecificColumn(columns, columnName, tableName);
            if(index == -1){
                throw new Exception("Where clause try to reference non existing column");
            }
            else if(index == -2){
                throw new Exception("Where clause try to reference tables which are not specified");
            }
            String dataTypeTemp = columns.get(index).getDataType();
            if(dataTypeTemp.startsWith("char")){
                this.dataType="char";
            }
            else{
                this.dataType=dataTypeTemp;
            }
            this.initialized = true;
        }
        return dataType;
    }

    public String getValue(ArrayList<String> record, ArrayList<TableColumnTuple> columns) throws Exception {
        if(isValue) {
            return value;
        }
        else if(initialized){
            return record.get(index);
        }
        else{
            this.index = SimpleDBMSParser.getIndexOfSpecificColumn(columns, columnName, tableName);
            if(this.index == -1){
                throw new Exception("Where clause try to reference non existing column");
            }
            else if(index == -2){
                throw new Exception("Where clause try to reference tables which are not specified");
            }
            this.dataType=columns.get(this.index).getDataType();
            this.initialized=true;
            return record.get(this.index);
        }
    }

}