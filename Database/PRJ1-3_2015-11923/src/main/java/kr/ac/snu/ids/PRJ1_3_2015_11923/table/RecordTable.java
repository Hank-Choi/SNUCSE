package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.util.ArrayList;

public class RecordTable
{
    ArrayList < TableColumnTuple > columns;
    ArrayList <ArrayList< String >> records;

    public RecordTable(ArrayList < TableColumnTuple > columns, ArrayList < ArrayList < String > > records)
    {
        this.columns = columns;
        this.records = records;
    }

    public ArrayList < TableColumnTuple > getColumns()
    {
        return columns;
    }

    public ArrayList < ArrayList < String > > getRecords()
    {
        return records;
    }
}
