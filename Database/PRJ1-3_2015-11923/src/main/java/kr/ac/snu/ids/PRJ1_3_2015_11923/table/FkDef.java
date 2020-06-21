package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;
import java.util.ArrayList;

//Create table 시에 정의한 ForeignKey Definition
public class FkDef implements Serializable
{
    private String foreignTableName;
    ArrayList< String > columnNames;
    ArrayList < String > foreignColumnNames;

    public FkDef(String foreignTableName, ArrayList < String > fkNames, ArrayList < String > foreignColumnNames)
    {
        this.columnNames = fkNames;
        this.setForeignTableName(foreignTableName);
        this.foreignColumnNames = foreignColumnNames;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public ArrayList<String> getForeignColumnNames() {
        return foreignColumnNames;
    }

    public String getForeignTableName() {
		return foreignTableName;
	}

	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}
}
