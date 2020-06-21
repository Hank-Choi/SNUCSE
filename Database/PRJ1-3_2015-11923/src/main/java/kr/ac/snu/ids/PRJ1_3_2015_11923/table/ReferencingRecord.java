package kr.ac.snu.ids.PRJ1_3_2015_11923.table;

import java.util.ArrayList;
import java.util.ListIterator;

import kr.ac.snu.ids.PRJ1_3_2015_11923.integrity.BooleanPair;

public class ReferencingRecord {
    ArrayList<Integer> referencedColumnIndexes;
    ArrayList<Integer> referencingColumnIndexes;
    ArrayList<Boolean> nullableList;
    ArrayList<ArrayList<String>> referencingTableRecords;

    public ReferencingRecord(
            ArrayList<Integer> referencedColumnIndexes,
            ArrayList<Integer> referencingColumnIndexes,
            ArrayList<Boolean> nullableList,
            ArrayList<ArrayList<String>> referencingTableRecords) {
        this.referencedColumnIndexes = referencedColumnIndexes;
        this.referencingColumnIndexes = referencingColumnIndexes;
        this.nullableList = nullableList;
        this.referencingTableRecords = referencingTableRecords;
    }

    //delete 시에 해당 record를 참조하는 record 모두 null로 변경
    public void setForeignKeysToNull(ArrayList<String> record) {
        for (ArrayList<String> referencingTableRecord : referencingTableRecords) {
            ListIterator<Integer> referencedColumnIndexIter = referencedColumnIndexes.listIterator();
            ListIterator<Integer> referencingColumnIndexIter = referencingColumnIndexes.listIterator();
            while (referencedColumnIndexIter.hasNext()) {
                int referencedIndex = referencedColumnIndexIter.next();
                int referencingIndex = referencingColumnIndexIter.next();
                if (record.get(referencedIndex).equals(referencingTableRecord.get(referencingIndex))) {
                    referencingTableRecord.remove(referencingIndex);
                    referencingTableRecord.add(referencingIndex,null);
                }
            }
        }
    }

    //delete 시에 해당 record를 참조하는 record 가 있는지 판단
    public BooleanPair checkReferencedValue(ArrayList<String> record) {
        BooleanPair result = new BooleanPair(false, false);
        for (ArrayList<String> referencingTableRecord : referencingTableRecords) {
            ListIterator<Integer> referencedColumnIndexIter = referencedColumnIndexes.listIterator();
            ListIterator<Integer> referencingColumnIndexIter = referencingColumnIndexes.listIterator();
            ListIterator<Boolean> nullableListIterator = nullableList.listIterator();
            while (referencedColumnIndexIter.hasNext()) {
                boolean nullable = nullableListIterator.next();
                if (record.get(referencedColumnIndexIter.next()).equals(referencingTableRecord.get(referencingColumnIndexIter.next()))) {
                    if (nullable) {
                        result = new BooleanPair(true, true);
                    } else {
                        return new BooleanPair(false, true);
                    }
                }
            }
        }
        return result;
    }
}
