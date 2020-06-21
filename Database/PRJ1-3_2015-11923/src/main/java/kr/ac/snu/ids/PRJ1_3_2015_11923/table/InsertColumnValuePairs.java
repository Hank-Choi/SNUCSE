package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.io.Serializable;
import java.util.ArrayList;

import kr.ac.snu.ids.PRJ1_3_2015_11923.Token;

public class InsertColumnValuePairs implements Serializable
{
    private ArrayList< String > columnNames;
    private ArrayList <Token> values;

    public InsertColumnValuePairs(ArrayList < String > columnNames, ArrayList < Token > values)
    {
        this.setColumnNames(columnNames);
        this.setValues(values);
    }

	public ArrayList< String > getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList< String > columnNames) {
		this.columnNames = columnNames;
	}

	public ArrayList <Token> getValues() {
		return values;
	}

	public void setValues(ArrayList <Token> values) {
		this.values = values;
	}
}
