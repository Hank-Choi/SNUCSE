package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;

//ForeignKey의 reference를 저장
public class Reference implements Serializable
{
    String tableName;
    String columnName;

    public Reference(String tableName, String columnName)
    {
        this.tableName = tableName;
        this.columnName = columnName;
    }
}
