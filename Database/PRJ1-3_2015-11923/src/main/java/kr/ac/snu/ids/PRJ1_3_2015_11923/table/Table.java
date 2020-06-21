package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import kr.ac.snu.ids.PRJ1_3_2015_11923.SimpleDBMSParser;
import kr.ac.snu.ids.PRJ1_3_2015_11923.Token;
import kr.ac.snu.ids.PRJ1_3_2015_11923.condition.BooleanExpressionList;
import kr.ac.snu.ids.PRJ1_3_2015_11923.integrity.BooleanPair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

public class Table implements Serializable {
    private String tableName;

    private ArrayList<Column> columns = new ArrayList();

    private ArrayList<String> primaryKeys = new ArrayList();

    private ArrayList<FkDef> foreignKeys = new ArrayList();

    // 이 table을 참조하는 tableName LIst
    private ArrayList<String> referencedBy = new ArrayList();

    private ArrayList<ArrayList<String>> records = new ArrayList();

    public Table(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public ArrayList<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public boolean checkDuplicatedColumn(String columnName) {
        for (Column column : columns) {
            if (column.getColumnName().equalsIgnoreCase(columnName))
                return true;
        }
        return false;
    }

    public ArrayList<String> getReferencedBy() {
        return referencedBy;
    }

    public boolean isRefered() {
        return referencedBy.size() > 0;
    }

    public ArrayList<FkDef> getForeignKeys() {
        return foreignKeys;
    }

    public int searchIndexOfColumn(String columnName) {
        int index = -1;
        int tempIndex = 0;
        for (Column column : columns) {
            if (column.columnName.equals(columnName)) {
                index = tempIndex;
            }
            tempIndex++;
        }
        return index;
    }

    public void addReferencedBy(String tableName) {
        this.referencedBy.add(tableName);
    }

    public void removeReferencedBy(String tableName) {
        this.referencedBy.remove(tableName);
    }

    public void setPkDef(ArrayList<String> columnList) {
        primaryKeys = columnList;
    }

    public void setFkDef(String tableName, ArrayList<String> columns, ArrayList<String> foreignColumns) {
        foreignKeys.add(new FkDef(tableName, columns, foreignColumns));
    }

    public ArrayList<ArrayList<String>> getRecords() {
        return records;
    }

    //create table 입력이 끝난 후 각 constraints 정의를 통해 column들 수정
    public void setConstraints() throws Exception {
        //set primary key
        for (String pk : this.primaryKeys) {
            Column pkColumn = searchColumn(pk);
            if (pkColumn == null) {
                throw new Exception("Create table has failed: '" + pk + "' does not exists in column definition");
            }
            pkColumn.setPk(true);
            pkColumn.setNullable(false);
        }
        //set foreign keys
        for (FkDef fkDef : this.foreignKeys) {
            if (fkDef.columnNames.size() != fkDef.foreignColumnNames.size()) {
                throw new Exception("Create table has failed: foreign key references wrong type");
            }
            //get table from db
            Table foreignTable = SimpleDBMSParser.getTableFromDB(fkDef.getForeignTableName());
            if (foreignTable == null) {
                throw new Exception("Create table has failed: foreign key references non existing table");
            }
            //reference column은 primary key를 모두 포함하여야 한다.
            if (!fkDef.foreignColumnNames.containsAll(foreignTable.primaryKeys)) {
                throw new Exception("Create table has failed: foreign key references non primary key column");
            }
            //각각의 foreignKey column에 reference 추가
            ListIterator<String> columnNameIter = fkDef.columnNames.listIterator();
            ListIterator<String> foreignColumnNameIter = fkDef.foreignColumnNames.listIterator();
            while (columnNameIter.hasNext()) {
                //각각의 Column에 대응되는 column의 tableName 과 columnName을 Column.reference에 넣는다.
                String tempColumnName = columnNameIter.next();
                String tempForeignColName = foreignColumnNameIter.next();
                Column thisCol = searchColumn(tempColumnName);
                Column foreCol = foreignTable.searchColumn(tempForeignColName);
                if (thisCol == null) {
                    throw new Exception("Create table has failed: '" + tempColumnName + "' does not exists in column definition");
                }
                if (foreCol == null) {
                    throw new Exception("Create table has failed: foreign key references non existing column");
                }
                if (!thisCol.getType().equals(foreCol.getType())) {
                    throw new Exception("Create table has failed: foreign key references wrong type");
                }
                if (!foreCol.isPk()) {
                    throw new Exception("Create table has failed: foreign key references non primary key column");
                }
                thisCol.setReference(new Reference(fkDef.getForeignTableName(), tempForeignColName));
            }
        }
    }

    public Column searchColumn(String columnName) {
        for (Column col : columns) {
            if (columnName.equalsIgnoreCase(col.getColumnName())) {
                return col;
            }
        }
        return null;
    }

    public void desc() {
        System.out.println("-------------------------------------------------");
        System.out.println("table_name [" + tableName + "]");
        System.out.println("column_name       type          null     key");
        for (Column column : columns) {
            column.columnDesc();
        }
        System.out.println("-------------------------------------------------");
    }

    public void addColumn(Column newColumn) {
        columns.add(newColumn);
    }

    public void addRecord(ArrayList<String> record) {
        this.records.add(record);
    }


    public void insertRecord(InsertColumnValuePairs insertColumnValuePairs) throws Exception {
        ArrayList<String> insertColumnNames = insertColumnValuePairs.getColumnNames();
        ArrayList<Token> values = insertColumnValuePairs.getValues();
        ArrayList<String> newRecord = new ArrayList();
        if (insertColumnNames.isEmpty()) {
            if (columns.size() == values.size()) {
                ListIterator<Column> colIter = columns.listIterator();
                ListIterator<Token> valIter = values.listIterator();
                int columnIndex = 0;
                while (colIter.hasNext()) {
                    Token newValue = valIter.next();
                    newRecord.add(constraintsValidationOfInsert(newValue, colIter.next(), columnIndex));
                    columnIndex++;
                }
            } else {
                throw new Exception("Insertion has failed: Types are not matched");
            }
        } else {
            ArrayList<String> columnNamesExist = new ArrayList();
            int columnIndex = 0;
            for (Column column : columns) {
                columnNamesExist.add(column.getColumnName());
                int valueIndex = -1, tempIndex = 0;
                for (String insertColumnName : insertColumnNames) {
                    if (insertColumnName.equals(column.getColumnName())) {
                        valueIndex = tempIndex;
                    }
                    tempIndex++;
                }
                if (valueIndex == -1) {
                    newRecord.add(constraintsValidationOfInsert(new Token(SimpleDBMSParser.NULL), column, columnIndex));
                } else {
                    Token newValue = values.get(valueIndex);
                    newRecord.add(constraintsValidationOfInsert(newValue, column, columnIndex));
                }
                columnIndex++;
            }
            for (String insertColumnName : insertColumnNames) {
                if (!columnNamesExist.contains(insertColumnName)) {
                    throw new Exception("Insertion has failed: '" + insertColumnName + "' does not exist");
                }
            }
        }
        addRecord(newRecord);
        SimpleDBMSParser.dropTable(tableName);
        SimpleDBMSParser.storeTableInDB(tableName, this);
    }

    //삽입 시에 dataType과 primary key integrity, foreign key integrity를 확인
    public String constraintsValidationOfInsert(Token value, Column column, int index) throws Exception {
        if (value.kind == SimpleDBMSParser.NULL) {
            if (!column.isNullable()) {
                throw new Exception("Insertion has failed: '" + column.getColumnName() + "' is not nullable");
            }
            return null;
        } else {
            String insertValue = null;
            int insertDatatype = value.kind;
            String columnDataTypeString = column.getDataType();
            switch (insertDatatype) {
                case SimpleDBMSParser.CHAR_STRING:
                    if (columnDataTypeString.startsWith("char")) {
                        int size = Math.min(Integer.parseInt(columnDataTypeString.replaceAll("[^0-9]", "")), value.image.length() - 2);
                        insertValue = value.image.substring(1, size + 1);
                    }
                    break;
                case SimpleDBMSParser.INT_VALUE:
                    if (columnDataTypeString.startsWith("int")) {
                        insertValue = value.image;
                    }
                    break;
                case SimpleDBMSParser.DATE_VALUE:
                    if (columnDataTypeString.startsWith("date")) {
                        insertValue = value.image;
                    }
                    break;
                default:
                    break;
            }
            if (insertValue == null) {
                throw new Exception("Insertion has failed: Types are not matched");
            }
            if (column.isPk()) {
                for (ArrayList<String> record : this.records) {
                    if (insertValue.equals(record.get(index))) {
                        throw new Exception("Insertion has failed: Primary key duplication");
                    }
                }
            }
            if (column.reference != null) {
                Table referenceTable = SimpleDBMSParser.getTableFromDB(column.reference.tableName);
                assert referenceTable != null;
                ArrayList<Column> referenceColumns = referenceTable.getColumns();
                int foreignKeyIndex = 0;
                for (Column referenceColumn : referenceColumns) {
                    if (referenceColumn.columnName.equals(column.reference.columnName)) {
                        break;
                    }
                    foreignKeyIndex++;
                }
                ArrayList<ArrayList<String>> referenceTableRecords = referenceTable.getRecords();
                for (ArrayList<String> referenceTableRecord : referenceTableRecords) {
                    if (insertValue.equals(referenceTableRecord.get(foreignKeyIndex))) {
                        return insertValue;
                    }
                }
                throw new Exception("Insertion has failed: Referential integrity violation");
            }
            return insertValue;
        }
    }

   //table 내에서 조건에 맞는 records를 삭제한다.
    // records를 whereClause.BooleanExpressionList.checkBooleanValue()를 통해 filtering 한 후
    // 이 테이블을 참조하는 column들을 모두 돌면서 column이 모두 nullable일 경우 삭제되는 data를 참조하는 record null로 변경
    // nullable하지 않을 경우 삭제 취소
    public void deleteRecordsRequest(BooleanExpressionList whereClause) throws Exception {
        ArrayList<TableColumnTuple> columnTupleArrayList = new ArrayList();
        ArrayList<Table> tableList = new ArrayList();
        ArrayList<ReferencingRecord> referencingRecords = new ArrayList();
        int deleteSuccessedCount = 0;
        int deletePassedCount = 0;
        for (Column column : columns) {
            columnTupleArrayList.add(new TableColumnTuple(column.getColumnName(), tableName, column.getDataType()));
        }
        for (String nameOfReferencingTable : referencedBy) {
            tableList.add(SimpleDBMSParser.getTableFromDB(nameOfReferencingTable));
        }

        for (Table referencingTable : tableList) {
            ArrayList<Integer> referencedColumnIndexes = new ArrayList();
            ArrayList<Integer> referencingColumnIndexes = new ArrayList();
            ArrayList<Boolean> nullableList = new ArrayList();
            for (FkDef fkDef : referencingTable.getForeignKeys()) {
                if (fkDef.getForeignTableName().equals(tableName)) {
                    ListIterator<String> referencingColumnNameIter = fkDef.getColumnNames().listIterator();
                    for (String s : fkDef.getForeignColumnNames()) {
                        referencedColumnIndexes.add(searchIndexOfColumn(s));
                        int referencingColumnIndex = referencingTable.searchIndexOfColumn(referencingColumnNameIter.next());
                        referencingColumnIndexes.add(referencingColumnIndex);
                        nullableList.add(referencingTable.columns.get(referencingColumnIndex).isNullable());
                    }
                }
            }
            referencingRecords.add(
                    new ReferencingRecord(
                            referencedColumnIndexes, referencingColumnIndexes, nullableList, referencingTable.records
                    ));
        }
        ListIterator<ArrayList<String>> recordIter = records.listIterator();
        if (whereClause != null) {
            while (recordIter.hasNext()) {
                ArrayList<String> eachRecord = recordIter.next();
                if (whereClause.checkBooleanValue(eachRecord, columnTupleArrayList).isTrue()) {
                    BooleanPair referenced = checkReferencedValue(eachRecord, referencingRecords);
                    if (referenced.isReferenced()) {
                        if (referenced.isAllNullable()) {
                            setForeignKeysToNull(eachRecord,referencingRecords);
                            recordIter.remove();
                            deleteSuccessedCount++;
                        }
                        else{
                            deletePassedCount++;
                        }
                    } else {
                        recordIter.remove();
                        deleteSuccessedCount++;
                    }
                }
            }
        } else {
            while (recordIter.hasNext()) {
                ArrayList<String> eachRecord = recordIter.next();
                BooleanPair referenced = checkReferencedValue(eachRecord, referencingRecords);
                if (referenced.isReferenced()) {
                    if (referenced.isAllNullable()) {
                        setForeignKeysToNull(eachRecord,referencingRecords);
                        recordIter.remove();
                        deleteSuccessedCount++;
                    }
                    else{
                        deletePassedCount++;
                    }
                } else {
                    recordIter.remove();
                    deleteSuccessedCount++;
                }
            }
        }
        for(Table table:tableList){
            SimpleDBMSParser.dropTable(table.tableName);
            SimpleDBMSParser.storeTableInDB(table.tableName,table);
        }
        SimpleDBMSParser.dropTable(this.tableName);
        SimpleDBMSParser.storeTableInDB(this.tableName,this);
        System.out.println(deleteSuccessedCount + " row(s) are deleted");
        if (deletePassedCount != 0) {
            System.out.println(deletePassedCount + " row(s) are not deleted due to referential integrity");
        }
    }

    public static BooleanPair checkReferencedValue(ArrayList<String> record, ArrayList<ReferencingRecord> referencingRecords) {
        BooleanPair result = new BooleanPair(false, false);
        for (ReferencingRecord referencingRecord : referencingRecords) {
            BooleanPair step = referencingRecord.checkReferencedValue(record);
            if (step.isReferenced()) {
                result = new BooleanPair(true, true);
                if (!step.isAllNullable()) {
                    return new BooleanPair(false, true);
                }
            }
        }
        return result;
    }

    public void setForeignKeysToNull(ArrayList<String> record, ArrayList<ReferencingRecord> referencingRecords) {
        for (ReferencingRecord referencingRecord : referencingRecords) {
            referencingRecord.setForeignKeysToNull(record);
        }
    }

}
