package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;

import kr.ac.snu.ids.PRJ1_3_2015_11923.Token;

public class Column implements Serializable
{
    String columnName;
    private String dataType;
    // 이 column이 foreign key 일 경우 참조하는 table.column
    Reference reference = null;
    private boolean nullable = true;
    //primary key
    boolean pk = false;

    public Column(String columnName, String dataType, Token notNull)
    {
        this.columnName = columnName;
        this.setDataType(dataType);
        this.setNullable(notNull == null);
    }

    public void setPk(boolean pk)
    {
        this.pk = pk;
    }

    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }

    public String getType()
    {
        return getDataType();
    }

    public boolean isPk()
    {
        return pk;
    }

    public void columnDesc()
    {
        String nullString = isNullable() ? "Y" : "N";
        String constraintKeyString = "";
        if (pk && reference != null)
        {
            constraintKeyString = "PRI/FOR";
        }
        else if (pk)
        {
            constraintKeyString = "PRI";
        }
        else if (reference != null)
        {
            constraintKeyString = "FOR";
        }
        System.out.printf("%-18s%-14s%-9s%-13s\n", columnName, getDataType(), nullString, constraintKeyString);
    }

    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

    public String getColumnName()
    {
        return columnName;
    }

	public boolean isNullable() {
		return nullable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
