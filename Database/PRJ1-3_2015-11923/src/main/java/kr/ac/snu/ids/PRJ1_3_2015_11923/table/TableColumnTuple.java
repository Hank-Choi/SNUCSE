package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;

public class TableColumnTuple implements Serializable
{
    private String columnName;

    private String tableName;

    private String asName;

    private String dataType;


    public TableColumnTuple(String columnName, String tableName, String asName,String dataType)
    {
        this.columnName = columnName;
        this.tableName = tableName;
        this.asName = asName;
        this.dataType=dataType;
    }

    public TableColumnTuple(String columnName, String tableName,String dataType)
    {
        this.columnName = columnName;
        this.tableName = tableName;
        this.asName = columnName;
        this.dataType=dataType;
    }

    public String getAsName() {
        return asName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDataType() {
        return dataType;
    }
}
