package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;

public class ReferedTableName implements Serializable
{
    private String tableName;
    String asName;

    public ReferedTableName(String tableName, String asName)
    {
        this.setTableName(tableName);
        this.asName = asName;
    }

	public String getTableName() {
		return tableName;
	}

    public String getAsName() {
        return asName;
    }

    public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}

